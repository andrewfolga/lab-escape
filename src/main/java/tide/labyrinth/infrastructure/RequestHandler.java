package tide.labyrinth.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tide.labyrinth.domain.DFSLabEscape;
import tide.labyrinth.domain.NoEscapeException;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Controller
@EnableAutoConfiguration
@ComponentScan({"tide.labyrinth"})
public class RequestHandler {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NO_ESCAPE_PATH = "No escape path!";
    public static final String INTERNAL_SERVER_ERROR = "There has been a problem with calculating the escape path!";

    private final RequestReader requestReader;
    private final DFSLabEscape labEscape;

    @Autowired
    public RequestHandler(RequestReader requestReader, DFSLabEscape labEscape) {
        this.requestReader = requestReader;
        this.labEscape = labEscape;
    }

    @RequestMapping("/labescape")
    @ResponseBody
    public ResponseEntity labescape(@RequestBody String body) throws IOException {

        String response = "";

        InputData inputData = requestReader.readInputData(new ByteArrayInputStream(body.getBytes()));
        try {
            char[][] labWithEscapePath = labEscape.drawPathForEscape(inputData.getLabyrinth(), inputData.getStartPosX(), inputData.getStartPosY());
            response = collectResponse(labWithEscapePath);
        } catch (NoEscapeException t) {
            return ResponseEntity.ok(NO_ESCAPE_PATH);
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    private String collectResponse(char[][] result) {
        return Arrays.stream(result).map(e -> new String(e)).collect(joining(LINE_SEPARATOR));
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RequestHandler.class, args);
    }
}