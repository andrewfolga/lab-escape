package tide.labyrinth;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import tide.labyrinth.infrastructure.messaging.RequestHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.IntStream;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {RequestHandler.class})
public class LabEscapeStressTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCopeUnderLoad() throws Exception {
        InputStream inputDataStream = Files.newInputStream(FileSystems.getDefault().getPath("data", "large.txt"));
        String stringInput= IOUtils.toString(inputDataStream);
        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);

        IntStream.range(0, 1000).parallel().forEach(e -> {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity("/lab/"+labEntity.getBody()+"/escape", stringInput, String.class);

                BufferedReader rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getBody().getBytes())));
                rd.lines().limit(1).forEach(l -> assertThat(l, not(equalTo(RequestHandler.NO_ESCAPE_PATH))));
            } catch (Exception ex) {
                System.out.println(ex);
                fail();
            }
        });
    }

}
