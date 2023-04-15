package neu.homework.sunshine.medical;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import neu.homework.sunshine.common.domain.ElasticsearchIndex;
import neu.homework.sunshine.medical.domain.MmsSickness;
import neu.homework.sunshine.medical.util.ElasticsearchUtil;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @org.junit.Test
    public void testES() throws IOException {
        ElasticsearchClient esClient = ElasticsearchUtil.getElasticsearchClient();
        Map<String,HighlightField> highlightFieldMap = new HashMap<>();
        highlightFieldMap.put("name",HighlightField.of(h->h.preTags("<em>").postTags("</em>")));
        highlightFieldMap.put("symptom",HighlightField.of(h->h.preTags("<em>").postTags("</em>")));
        highlightFieldMap.put("detail",HighlightField.of(h->h.preTags("<em>").postTags("</em>")));
        SearchResponse<MmsSickness> res = esClient.search(s -> s
                        .index(ElasticsearchIndex.SICKNESS.getName())
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query("皮肤病")
                                        .fields(List.of("name","symptom","detail"))))
                        .highlight(h -> h.fields(highlightFieldMap))
        , MmsSickness.class);
        List<Hit<MmsSickness>> list = res.hits().hits();
        for(Hit<MmsSickness> item : list){
            System.out.println(item.source());
            System.out.println(item.highlight());
        }
        ElasticsearchUtil.close();
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @org.junit.Test
    public void testRabbitMq(){
        rabbitTemplate.convertAndSend("simple","hello");
    }
}