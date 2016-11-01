package mir2.crawl;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class Crawler {
    public static final String HOME_URL = "http://www.mir2wiki.cn";
    public static final String ROOT_DIR = "C:/mir2info/";

    public static void main(String[] args) throws IOException {

    }

    public String getContent(String url) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        URL url1 = new URL(url);
        URI uri = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(), null);
        CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
            System.out.println("error");
            return null;
        }
        String content = EntityUtils.toString(response.getEntity());
        return content;
    }
}
