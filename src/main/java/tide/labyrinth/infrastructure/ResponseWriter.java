package tide.labyrinth.infrastructure;

import com.sun.net.httpserver.HttpExchange;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class ResponseWriter {

    public void write(HttpExchange httpExchange, HttpResponseCode httpResponseCode, String response) throws IOException {
        httpExchange.sendResponseHeaders(httpResponseCode.getHttpCode(), response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
