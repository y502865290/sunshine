package neu.homework.sunshine.medical.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import neu.homework.sunshine.common.domain.ElasticsearchIndex;
import neu.homework.sunshine.common.domain.RabbitExchange;
import neu.homework.sunshine.common.domain.RabbitQueue;
import neu.homework.sunshine.common.domain.ServiceResult;
import neu.homework.sunshine.common.util.JsonUtil;
import neu.homework.sunshine.medical.domain.MmsSickness;
import neu.homework.sunshine.medical.util.ElasticsearchUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SicknessIndexService {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitQueue.DIRECT_UPDATE_SICKNESS),
            exchange = @Exchange(name = RabbitExchange.SICKNESS_DIRECT,type = ExchangeTypes.DIRECT),
            key = {"add","update"}
    ))
    public void updateToEs(String message){
        MmsSickness mmsSickness = JsonUtil.toClass(message, MmsSickness.class);
        ElasticsearchClient esClient = ElasticsearchUtil.getElasticsearchClient();
        IndexRequest<MmsSickness> request = IndexRequest.of(i -> i
                .index(ElasticsearchIndex.SICKNESS.getName())
                .id(mmsSickness.getId().toString())
                .document(mmsSickness));
        IndexResponse response = null;
        try {
            response = esClient.index(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            ElasticsearchUtil.close();
        }
    }

    public ServiceResult searchInES(String keyword){
        ElasticsearchClient esClient = ElasticsearchUtil.getElasticsearchClient();
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        highlightFieldMap.put("name",HighlightField.of(f -> f
                .preTags("<em style='color=red'>")
                .postTags("</em>")
                .numberOfFragments(10000)));
        highlightFieldMap.put("detail",HighlightField.of(f -> f
                .preTags("<em style='color=red'>")
                .postTags("</em>")
                .numberOfFragments(10000)));
        highlightFieldMap.put("symptom",HighlightField.of(f -> f
                .preTags("<em style='color=red'>")
                .postTags("</em>")
                .numberOfFragments(10000)));
        SearchResponse<MmsSickness> res = null;
        try {
            res = esClient.search(s -> s
                    .index(ElasticsearchIndex.SICKNESS.getName())
                    .query(q -> q
                            .multiMatch(m -> m
                                    .query(keyword)
                                    .fields(List.of("name","detail","symptom"))))
                    .highlight(h -> h
                            .fields(highlightFieldMap)), MmsSickness.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Hit<MmsSickness>> data = res.hits().hits();
        List<MmsSickness> result = new ArrayList<>();
        for(Hit<MmsSickness> hitItem : data){
            MmsSickness m = hitItem.source();
            Map<String,List<String>> highlight = hitItem.highlight();
            if(highlight.containsKey("detail")){
                if(highlight.get("detail").size() > 1){
                    throw new RuntimeException("fragment数量大于1,numberOfFragments参数过小。");
                }else{
                    m.setDetail(highlight.get("detail").get(0));
                }
            }
            if(highlight.containsKey("name")){
                if(highlight.get("name").size() > 1){
                    throw new RuntimeException("fragment数量大于1,numberOfFragments参数过小。");
                }else{
                    m.setDetail(highlight.get("name").get(0));
                }
            }
            if(highlight.containsKey("symptom")){
                if(highlight.get("symptom").size() > 1){
                    throw new RuntimeException("fragment数量大于1,numberOfFragments参数过小。");
                }else{
                    m.setDetail(highlight.get("symptom").get(0));
                }
            }
            result.add(m);
        }
        return ServiceResult.ok().setData(result);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitQueue.DIRECT_DELETE_SICKNESS),
            exchange = @Exchange(name = RabbitExchange.SICKNESS_DIRECT,type = ExchangeTypes.DIRECT),
            key = {"delete"}
    ))
    public void deleteToEs(String message){
        Long id = Long.valueOf(message);
        ElasticsearchClient esClient = ElasticsearchUtil.getElasticsearchClient();
        DeleteRequest deleteRequest = DeleteRequest.of(d -> d
                .index(ElasticsearchIndex.SICKNESS.getName())
                .id(id.toString()));
        try {
            DeleteResponse response = esClient.delete(deleteRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            ElasticsearchUtil.close();
        }
    }

    public ServiceResult addAll(List<MmsSickness> all){


        ElasticsearchClient esClient = ElasticsearchUtil.getElasticsearchClient();

        DeleteByQueryRequest deleteByQueryRequest = DeleteByQueryRequest.of(d -> d
                .index(ElasticsearchIndex.SICKNESS.getName())
                .query(q -> q
                        .matchAll(m -> m
                                .boost(1.0F))));
        try {
            DeleteByQueryResponse deleteByQueryResponse = esClient.deleteByQuery(deleteByQueryRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            ElasticsearchUtil.close();
        }
        esClient = ElasticsearchUtil.getElasticsearchClient();
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (MmsSickness item : all) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(ElasticsearchIndex.SICKNESS.getName())
                            .id(item.getId().toString())
                            .document(item)
                    )
            );
        }
        BulkResponse result = null;

        try {
            result = esClient.bulk(br.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            ElasticsearchUtil.close();
        }

        if (result.errors()) {
            throw new RuntimeException();
        }
        return ServiceResult.ok().setMessage("添加成功");
    }
}
