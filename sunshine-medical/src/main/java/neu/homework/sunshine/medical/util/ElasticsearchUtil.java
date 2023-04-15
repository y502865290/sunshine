package neu.homework.sunshine.medical.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticsearchUtil {
    private static String url;

    private static Integer port;

    private static RestClient httpClient;

    private static ElasticsearchTransport transport;

    private static ElasticsearchClient esClient;

    private static RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.url}")
    private String urlProp;

    @Value("${elasticsearch.port}")
    private Integer portProp;

    @PostConstruct
    public void init(){
        url = this.urlProp;
        port = this.portProp;
    }

    public static RestHighLevelClient getHighLevelClient(){
        createRestClient();
        if(restHighLevelClient == null){
            restHighLevelClient = new RestHighLevelClientBuilder(httpClient)
                    .setApiCompatibilityMode(true)
                    .build();
        }
        return restHighLevelClient;
    }

    private static void createRestClient(){
        if(httpClient == null || !httpClient.isRunning()){
            httpClient = RestClient.builder(new HttpHost(url,port)).build();
        }
    }

    private static void createTransport(){
        createRestClient();
        if(transport == null){
            transport = new RestClientTransport(
                    httpClient,
                    new JacksonJsonpMapper()
            );
        }
    }

    public static ElasticsearchClient getElasticsearchClient(){
        createTransport();
        if(esClient == null){
            esClient = new ElasticsearchClient(transport);
        }
        return esClient;
    }

    public static void close() {
        try{
            if(transport != null){
                transport.close();
                transport = null;
            }
            if(restHighLevelClient != null){
                restHighLevelClient.close();
                restHighLevelClient = null;
            }
            if(httpClient != null){
                httpClient.close();
                httpClient = null;
            }
            esClient = null;
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
