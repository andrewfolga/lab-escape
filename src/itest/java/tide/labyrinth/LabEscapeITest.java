package tide.labyrinth;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.net.httpserver.HttpServerImpl;
import tide.labyrinth.infrastructure.RequestHandler;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.IntStream;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/labyrinth-system-appContext.xml"})
public class LabEscapeITest {

    @Resource
    private HttpServerImpl httpServer;

    @Before
    public void setUp() throws Exception {
        httpServer.setExecutor(newCachedThreadPool());
        httpServer.start();
    }

    @Test
    public void shouldCopeUnderLoad() throws Exception {

        InputStream inputDataStream = Files.newInputStream(FileSystems.getDefault().getPath("data", "large.txt"));
        byte[] byteInput= IOUtils.toByteArray(inputDataStream);

        IntStream.range(0, 1000).parallel().forEach(e -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://0.0.0.0:8081/labescape");
                httpPost.setEntity(new ByteArrayEntity(byteInput, ContentType.TEXT_PLAIN));

                CloseableHttpResponse response = client.execute(httpPost);

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                rd.lines().limit(1).forEach(l -> assertThat(l, not(equalTo(RequestHandler.NO_ESCAPE_PATH))));
            } catch (IOException ex) {
                System.out.println(ex);
                fail();
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        httpServer.stop(0);
    }
}
