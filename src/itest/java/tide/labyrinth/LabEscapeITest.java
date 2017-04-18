package tide.labyrinth;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import tide.labyrinth.infrastructure.RequestHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {RequestHandler.class})
public class LabEscapeITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCopeUnderLoad() throws Exception {
        InputStream inputDataStream = Files.newInputStream(FileSystems.getDefault().getPath("data", "large.txt"));
        String stringInput= IOUtils.toString(inputDataStream);

        IntStream.range(0, 1000).parallel().forEach(e -> {
            try {

                ResponseEntity<String> response = restTemplate.postForEntity("/labescape", stringInput, String.class);

                BufferedReader rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getBody().getBytes())));
                rd.lines().limit(1).forEach(l -> assertThat(l, not(equalTo(RequestHandler.NO_ESCAPE_PATH))));
            } catch (Exception ex) {
                System.out.println(ex);
                fail();
            }
        });
    }


    @Test
    public void shouldReturnInternalServerErrorIfIncorrectInput() throws Exception {
        String stringInput = "test input";

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("/labescape", stringInput, String.class);

        Assert.assertThat(stringResponseEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }


    @Test
    public void shouldFailToFindHandlerForIncorrectURL() throws Exception {
        String stringInput = "test input";

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("/doesNotExist", stringInput, String.class);

        Assert.assertThat(stringResponseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

}
