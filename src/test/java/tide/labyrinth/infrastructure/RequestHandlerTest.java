package tide.labyrinth.infrastructure;

import java.lang.IllegalArgumentException;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tide.labyrinth.domain.LabEscape;
import tide.labyrinth.domain.NoEscapeException;

import java.io.BufferedInputStream;
import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static tide.labyrinth.infrastructure.RequestHandler.INTERNAL_SERVER_ERROR;
import static tide.labyrinth.infrastructure.RequestHandler.NO_ESCAPE_PATH;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestHandlerTest {

    @Mock
    HttpExchange httpExchange;
    @Mock
    RequestReader requestReader;
    @Mock
    ResponseWriter responseWriter;
    @Mock
    LabEscape labEscape;

    @InjectMocks
    private RequestHandler requestHandler;

    @Test
    public void shouldFindEscapeForLabyrinth() throws Exception {

        URI requestURI = new URI("/labescape");
        InputData inputData = new InputData(1,1, new char[][] {});
        String labWithEscapePath = new String();
        when(httpExchange.getRequestURI()).thenReturn(requestURI);
        when(requestReader.readInputData(any(BufferedInputStream.class))).thenReturn(inputData);
        when(labEscape.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY())).thenReturn(inputData.getLabyrinth());

        requestHandler.handle(httpExchange);

        InOrder inOrder = inOrder(requestReader, labEscape, responseWriter);
        inOrder.verify(requestReader).readInputData(any(BufferedInputStream.class));
        inOrder.verify(labEscape).drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
        inOrder.verify(responseWriter).write(httpExchange, HttpResponseCode._200_OK, labWithEscapePath);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldFailToFindEscapeForImpossibleLabyrinth() throws Exception {

        URI requestURI = new URI("/labescape");
        InputData inputData = new InputData(1,1, new char[][] {});
        when(httpExchange.getRequestURI()).thenReturn(requestURI);
        when(requestReader.readInputData(any(BufferedInputStream.class))).thenReturn(inputData);
        when(labEscape.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY())).thenThrow(NoEscapeException.class);

        requestHandler.handle(httpExchange);

        InOrder inOrder = inOrder(requestReader, labEscape, responseWriter);
        inOrder.verify(requestReader).readInputData(any(BufferedInputStream.class));
        inOrder.verify(labEscape).drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
        inOrder.verify(responseWriter).write(httpExchange, HttpResponseCode._200_OK, NO_ESCAPE_PATH);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldFailToFindEscapeForLabyrinthDueToInvalidStartingPointCoordinates() throws Exception {

        URI requestURI = new URI("/labescape");
        InputData inputData = new InputData(1,1, new char[][] {});
        when(httpExchange.getRequestURI()).thenReturn(requestURI);
        when(requestReader.readInputData(any(BufferedInputStream.class))).thenReturn(inputData);
        when(labEscape.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY())).thenThrow(IllegalArgumentException.class);

        requestHandler.handle(httpExchange);

        InOrder inOrder = inOrder(requestReader, labEscape, responseWriter);
        inOrder.verify(requestReader).readInputData(any(BufferedInputStream.class));
        inOrder.verify(labEscape).drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
        inOrder.verify(responseWriter).write(httpExchange, HttpResponseCode._500_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldFailToFindHandlerForEscapeLabyrinth() throws Exception {
        URI requestURI = new URI("/labescapethatdoesnotexist");
        when(httpExchange.getRequestURI()).thenReturn(requestURI);

        requestHandler.handle(httpExchange);

        InOrder inOrder = inOrder(responseWriter);
        inOrder.verify(responseWriter).write(httpExchange, HttpResponseCode._404_NOT_FOUND, "");
        inOrder.verifyNoMoreInteractions();
    }
}