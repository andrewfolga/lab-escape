package tide.labyrinth.infrastructure;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@Component
public class RequestReader {

    public InputData readInputData(InputStream inputStream) throws IOException {
        int startX = 0;
        int startY = 0;
        char[][] labyrinth = null;
        List<char[]> labyrinthInput = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            String[] coordinates = buffer.readLine().split(" ");
            startX = Integer.valueOf(coordinates[0]);
            startY = Integer.valueOf(coordinates[1]);
            while ((line = buffer.readLine()) != null) {
                labyrinthInput.add(line.toCharArray());
            }
        }
        labyrinth = new char[labyrinthInput.size()][];

        for (int i = 0; i < labyrinthInput.size(); i++) {
            labyrinth[i] = labyrinthInput.get(i);
        }
        return new InputData(startX, startY, labyrinth);
    }
}
