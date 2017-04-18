package tide.labyrinth.infrastructure.messaging;

import org.springframework.stereotype.Component;
import tide.labyrinth.domain.LabyrinthData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
@Component
public class RequestReader {

    public LabyrinthData readInputData(InputStream inputStream) throws IOException {
        int startX = 0;
        int startY = 0;
        int numberOfWalls = 0;
        int numberOfEmptySpaces = 0;
        char[][] labyrinth = null;
        List<char[]> labyrinthInput = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            String[] coordinates = buffer.readLine().split(" ");
            startX = Integer.valueOf(coordinates[0]);
            startY = Integer.valueOf(coordinates[1]);
            while ((line = buffer.readLine()) != null) {
                numberOfEmptySpaces += line.chars().mapToObj(i -> (char) i).filter(c -> c == ' ').count();
                numberOfWalls += line.chars().mapToObj(i -> (char) i).filter(c -> c == 'O').count();
                labyrinthInput.add(line.toCharArray());
            }
        }
        labyrinth = new char[labyrinthInput.size()][];

        for (int i = 0; i < labyrinthInput.size(); i++) {
            labyrinth[i] = labyrinthInput.get(i);
        }
        return new LabyrinthData(startX, startY, numberOfWalls, numberOfEmptySpaces, labyrinth);
    }
}
