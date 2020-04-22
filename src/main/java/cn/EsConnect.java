package cn;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 喻浩
 * @create 2020-04-17-12:04
 */
public class EsConnect {

    private RestHighLevelClient client;
    public EsConnect() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        //集群节点
//                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9200, "http")));
        this.client = client;
    }

    public void shutdown(){
        if(client!=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void test() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("my-application");
        request.mapping(
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"message\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        if (!createIndexResponse.isAcknowledged()){
            throw new RuntimeException("创建索引失败");
        }
    }

    public static void main(String[] args) throws IOException {
        EsConnect connect = new EsConnect();
        connect.test();
        connect.shutdown();
    }
}
