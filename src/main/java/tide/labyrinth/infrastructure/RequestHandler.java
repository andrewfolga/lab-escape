package tide.labyrinth.infrastructure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.springframework.stereotype.Component;
import tide.labyrinth.domain.LabEscape;
import tide.labyrinth.domain.NoEscapeException;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

@Component
public class RequestHandler implements HttpHandler {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String LAB_ESCAPE_PATH_REGEX = "^/labescape$";
    public static final String NO_ESCAPE_PATH = "No escape path!";
    public static final String INTERNAL_SERVER_ERROR = "There has been a problem with calculating the escape path!";

    @Resource
    private RequestReader requestReader;
    @Resource
    private ResponseWriter responseWriter;
    @Resource
    private LabEscape labEscape;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String path = httpExchange.getRequestURI().getPath();
        String response = "";
        HttpResponseCode responseCode = HttpResponseCode._200_OK;

        if (matchesURIPath(LAB_ESCAPE_PATH_REGEX, path)) {
            InputData inputData = requestReader.readInputData(new BufferedInputStream(httpExchange.getRequestBody()));
            try {
                char[][] labWithEscapePath = labEscape.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
                response = collectResponse(labWithEscapePath);
            } catch (NoEscapeException t) {
                response = NO_ESCAPE_PATH;
            } catch (Throwable t) {
                responseCode = HttpResponseCode._500_INTERNAL_SERVER_ERROR;
                response = INTERNAL_SERVER_ERROR;
            }
            responseWriter.write(httpExchange, responseCode, response);
        } else {
            responseWriter.write(httpExchange, HttpResponseCode._404_NOT_FOUND, response);
        }
    }

    private String collectResponse(char[][] result) {
        return Arrays.stream(result).map(e -> new String(e)).collect(joining(LINE_SEPARATOR));
    }

    private boolean matchesURIPath(String pathRegex, String path) {
        return getMatcher(pathRegex, path).find();
    }

    private Matcher getMatcher(String pathPattern, String path) {
        Pattern pp = Pattern.compile(pathPattern);
        return pp.matcher(path);
    }

}