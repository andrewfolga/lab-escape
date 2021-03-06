package tide.labyrinth.infrastructure;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tide.labyrinth.domain.LabyrinthData;
import tide.labyrinth.infrastructure.messaging.RequestReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestReaderTest {

    public static final char[][] LABYRINTH = {{'O', 'O', 'O'}, {'O', ' ', 'O'}, {'O', 'O', 'O'}};
    public static final String INPUT_DATA_STRING = "1 1\nOOO\nO O\nOOO";

    private RequestReader requestReader = new RequestReader();

    @Test
    public void shouldReadInputDataSuccessfully() throws Exception {

        InputStream inputStream = new ByteArrayInputStream(String.valueOf(INPUT_DATA_STRING).getBytes());

        LabyrinthData labyrinthData = requestReader.readInputData(inputStream);

        Assert.assertThat(labyrinthData, is(equalTo(new LabyrinthData(1, 1, 8, 1, LABYRINTH))));
    }
}