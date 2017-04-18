package tide.labyrinth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import tide.labyrinth.infrastructure.messaging.RequestHandler;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
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
    public void shouldBuildANewLabyrinthAndReturnKey() throws Exception {
        String stringInput = "1 1\nOOO\nO O\nOOO";

        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);

        assertThat(labEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(labEntity.getBody(), is(equalTo("1133")));
    }

    @Test
    public void shouldReturnInternalServerErrorIfIncorrectInput() throws Exception {
        String stringInput = "testInput";

        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);

        assertThat(labEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void shouldFailToBuildANewLabyrinthWithIncorrectStartCoordinates() throws Exception {
        String stringInput = "3 3\nOOO\nO O\nOOO";

        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);

        assertThat(labEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void shouldGetLabyrinthWithStats() throws Exception {
        String stringInput = "1 1\nOOO\nO O\nO O";
        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);
        String insertedKey = labEntity.getBody();

        ResponseEntity<String> getValueEntity = restTemplate.getForEntity("/labs/"+insertedKey, String.class);

        assertThat(getValueEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(getValueEntity.getBody(), containsString("numberOfEmptySpaces=2"));
        assertThat(getValueEntity.getBody(), containsString("numberOfWalls=7"));
    }

    @Test
    public void shouldGetAValueForCoordinates() throws Exception {
        String stringInput = "1 1\nOOO\nO O\nO O";
        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);
        String insertedKey = labEntity.getBody();

        ResponseEntity<Character> getValueEntity = restTemplate.getForEntity("/labs/"+insertedKey+"/coordX/1/coordY/2", Character.class);
        assertThat(getValueEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(getValueEntity.getBody(), is(equalTo('O')));
        getValueEntity = restTemplate.getForEntity("/labs/"+insertedKey+"/coordX/2/coordY/1", Character.class);
        assertThat(getValueEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(getValueEntity.getBody(), is(equalTo(' ')));
    }

    @Test
    public void shouldReturnInternalServerErrorIfLabDoesNotExist() throws Exception {
        String stringInput = "1 1\nOOO\nO O\nO O";
        restTemplate.postForEntity("/labs", stringInput, String.class);

        ResponseEntity<Character> getValueEntity = restTemplate.getForEntity("/labs/1132/coordX/1/coordY/2", Character.class);

        assertThat(getValueEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldFailToFindHandlerForIncorrectURL() throws Exception {
        String stringInput = "test input";

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("/doesNotExist", stringInput, String.class);

        assertThat(stringResponseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldEscapeExistingLabyrinth() throws Exception {
        String stringInput = "1 1\nOOO\nO O\nO O";
        ResponseEntity<String> labEntity = restTemplate.postForEntity("/labs", stringInput, String.class);

        ResponseEntity<String> escapeEntity = restTemplate.postForEntity("/labs/"+labEntity.getBody()+"/escape", stringInput, String.class);
        assertThat(escapeEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(escapeEntity.getBody(), is(equalTo("OOO\nO•O\nO•O")));
    }

}
