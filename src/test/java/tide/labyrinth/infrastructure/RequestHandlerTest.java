package tide.labyrinth.infrastructure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tide.labyrinth.domain.LabManager;
import tide.labyrinth.domain.LabNotFoundException;
import tide.labyrinth.domain.NoEscapeException;
import tide.labyrinth.domain.LabyrinthData;
import tide.labyrinth.infrastructure.messaging.RequestHandler;
import tide.labyrinth.infrastructure.messaging.RequestReader;

import java.io.BufferedInputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestHandlerTest {

    @Mock
    RequestReader requestReader;
    @Mock
    LabManager labManager;

    @InjectMocks
    private RequestHandler requestHandler;

    private static final int NUMBER_OF_WALLS = 3;
    private static final int NUMBER_OF_EMPTY_SPACES = 5;
    private static final String LAB_KEY ="LAB_KEY";

    @Before
    public void setUp() throws Exception {
        requestHandler = new RequestHandler(requestReader, labManager);
    }

    @Test
    public void shouldFindEscapeForLabyrinth() throws Exception {

        LabyrinthData labyrinthData = new LabyrinthData(1,1, NUMBER_OF_WALLS, NUMBER_OF_EMPTY_SPACES, new char[][] { {'O','O','O'},{'O',' ','O'},{'O',' ','O'}});
        when(labManager.drawPathForEscape(LAB_KEY)).thenReturn(labyrinthData.getLabyrinth());

        requestHandler.labEscape(LAB_KEY);

        verify(labManager).drawPathForEscape(LAB_KEY);
        verifyNoMoreInteractions(labManager);
    }

    @Test
    public void shouldFailToFindEscapeForImpossibleLabyrinth() throws Exception {

        when(labManager.drawPathForEscape(LAB_KEY)).thenThrow(NoEscapeException.class);

        ResponseEntity responseEntity = requestHandler.labEscape(LAB_KEY);

        assertThat(responseEntity.getStatusCode(), is(OK));
        verify(labManager).drawPathForEscape(LAB_KEY);
        verifyNoMoreInteractions(labManager);
    }

    @Test
    public void shouldFailToFindEscapeForLabyrinthDueToNonExistentLabyrinth() throws Exception {

        when(labManager.drawPathForEscape(LAB_KEY)).thenThrow(LabNotFoundException.class);

        ResponseEntity responseEntity = requestHandler.labEscape(LAB_KEY);

        assertThat(responseEntity.getStatusCode(), is(INTERNAL_SERVER_ERROR));
        verify(labManager).drawPathForEscape(LAB_KEY);
        verifyNoMoreInteractions(labManager);
    }
}