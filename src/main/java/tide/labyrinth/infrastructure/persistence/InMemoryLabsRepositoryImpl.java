package tide.labyrinth.infrastructure.persistence;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;
import tide.labyrinth.domain.LabNotFoundException;
import tide.labyrinth.domain.LabsRepository;
import tide.labyrinth.domain.LabyrinthData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.String.format;

/**
 * Created by andrzejfolga on 18/04/2017.
 */
@Component
public class InMemoryLabsRepositoryImpl implements LabsRepository {

    private static final String KEY = "%s%s%s%s";
    public static final String NOT_FOUND = "There is no such labyrinth with key %s!";

    private ConcurrentMap<String, LabyrinthData> labs = new ConcurrentHashMap<>();

    @Override
    public String store(LabyrinthData labyrinthData) {
        Validate.notNull(labyrinthData);
        String key = buildKey(labyrinthData);
        labs.put(key, labyrinthData);
        return key;
    }

    @Override
    public LabyrinthData get(String key) throws LabNotFoundException {
        LabyrinthData labyrinthData = labs.get(key);
        if (labyrinthData == null) throw new LabNotFoundException(format(NOT_FOUND, key));
        return labyrinthData;
    }

    @Override
    public char getValueFor(String key, int x, int y) throws LabNotFoundException {
        LabyrinthData labyrinthData = get(key);
        return labyrinthData.getLabyrinth()[x][y];
    }

    private String buildKey(LabyrinthData labyrinthData) {
        return format(KEY,
                labyrinthData.getStartPosX(),
                labyrinthData.getStartPosY(),
                labyrinthData.getLabyrinth().length,
                labyrinthData.getLabyrinth()[0].length);
    }
}
