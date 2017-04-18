package tide.labyrinth.infrastructure;

import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tide.labyrinth.domain.DFSLabEscape;
import tide.labyrinth.domain.NoEscapeException;

import java.io.BufferedInputStream;
import java.net.URI;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestHandlerTest {

    @Mock
    RequestReader requestReader;
    @Mock
    DFSLabEscape labEscapeDFS;

    public static final String LABYRINTH = "OOO\nO O\nOOO";

    @InjectMocks
    private RequestHandler requestHandler;

    @Before
    public void setUp() throws Exception {
        requestHandler = new RequestHandler(requestReader, labEscapeDFS);
    }

    @Test
    public void shouldFindEscapeForLabyrinth() throws Exception {

        InputData inputData = new InputData(1,1, new char[][] {});
        when(requestReader.readInputData(any(BufferedInputStream.class))).thenReturn(inputData);
        when(labEscapeDFS.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY())).thenReturn(inputData.getLabyrinth());

        requestHandler.labescape(LABYRINTH);

        InOrder inOrder = inOrder(requestReader, labEscapeDFS);
        inOrder.verify(requestReader).readInputData(any(BufferedInputStream.class));
        inOrder.verify(labEscapeDFS).drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldFailToFindEscapeForImpossibleLabyrinth() throws Exception {

        InputData inputData = new InputData(1,1, new char[][] {});
        when(requestReader.readInputData(any(BufferedInputStream.class))).thenReturn(inputData);
        when(labEscapeDFS.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY())).thenThrow(NoEscapeException.class);

        requestHandler.labescape(LABYRINTH);

        InOrder inOrder = inOrder(requestReader, labEscapeDFS);
        inOrder.verify(requestReader).readInputData(any(BufferedInputStream.class));
        inOrder.verify(labEscapeDFS).drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldFailToFindEscapeForLabyrinthDueToInvalidStartingPointCoordinates() throws Exception {

        InputData inputData = new InputData(1,1, new char[][] {});
        when(requestReader.readInputData(any(BufferedInputStream.class))).thenReturn(inputData);
        when(labEscapeDFS.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY())).thenThrow(IllegalArgumentException.class);

        requestHandler.labescape(LABYRINTH);

        InOrder inOrder = inOrder(requestReader, labEscapeDFS);
        inOrder.verify(requestReader).readInputData(any(BufferedInputStream.class));
        inOrder.verify(labEscapeDFS).drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
        inOrder.verifyNoMoreInteractions();
    }
}