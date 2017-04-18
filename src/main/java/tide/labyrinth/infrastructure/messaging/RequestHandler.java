package tide.labyrinth.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tide.labyrinth.domain.LabManager;
import tide.labyrinth.domain.LabNotFoundException;
import tide.labyrinth.domain.LabyrinthData;
import tide.labyrinth.domain.NoEscapeException;

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
    private final LabManager labManager;

    private final static Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    @Autowired
    public RequestHandler(RequestReader requestReader, LabManager labManager) {
        this.requestReader = requestReader;
        this.labManager = labManager;
    }

    @RequestMapping("/labs/{key}/escape")
    @ResponseBody
    public ResponseEntity labEscape(@PathVariable("key") String key) throws IOException {

        String response = "";

        try {
            char[][] labWithEscapePath = labManager.drawPathForEscape(key);
            response = collectResponse(labWithEscapePath);
        } catch (NoEscapeException t) {
            return ResponseEntity.ok(NO_ESCAPE_PATH);
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/labs")
    @ResponseBody
    public ResponseEntity labCreate(@RequestBody String body) throws IOException {

        String labKey = "";

        LabyrinthData labyrinthData = requestReader.readInputData(new ByteArrayInputStream(body.getBytes()));
        try {
            labKey = labManager.createLabyrinth(labyrinthData);
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(labKey);
    }

    @RequestMapping("/labs/{key}")
    @ResponseBody
    public ResponseEntity labGet(@PathVariable("key") String key) throws IOException {

        LabyrinthData labData = null;
        try {
            labData = labManager.getLabyrinth(key);
        } catch (LabNotFoundException lnfe) {
            LOG.warn("Labyrinth does not exist", lnfe);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(labData.toString());
    }

    @RequestMapping("/labs/{key}/coordX/{x}/coordY/{y}")
    @ResponseBody
    public ResponseEntity labFindValueFor(@PathVariable("key") String key, @PathVariable("x") String x, @PathVariable("y") String y) throws IOException {

        char labValueForXY = ' ';
        try {
            labValueForXY = labManager.getValueFor(key, Integer.valueOf(x), Integer.valueOf(y));
        } catch (LabNotFoundException lnfe) {
            LOG.warn("Labyrinth key=%s does not exist", key, lnfe);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(labValueForXY);
    }

    private String collectResponse(char[][] result) {
        return Arrays.stream(result).map(e -> new String(e)).collect(joining(LINE_SEPARATOR));
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RequestHandler.class, args);
    }
}