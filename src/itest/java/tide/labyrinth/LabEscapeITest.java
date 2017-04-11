package tide.labyrinth;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.net.httpserver.HttpServerImpl;
import tide.labyrinth.domain.PathFinder;
import tide.labyrinth.infrastructure.InputData;
import tide.labyrinth.infrastructure.RequestHandler;
import tide.labyrinth.infrastructure.RequestReader;

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

        IntStream.range(0, 100).forEach(e -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                System.out.println("Iteration " + e);
                HttpPost httpPost = new HttpPost("http://0.0.0.0:8081/labescape");
                httpPost.setEntity(new ByteArrayEntity(byteInput, ContentType.TEXT_PLAIN));
//                Thread.sleep(10);
                CloseableHttpResponse response = client.execute(httpPost);
                System.out.println("Response " + e);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                rd.lines().limit(1).forEach(l -> System.out.println(Thread.currentThread().getName() + l));
//                rd.lines().limit(1).forEach(l -> assertThat(l, is(not(RequestHandler.NO_ESCAPE_PATH))));
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
