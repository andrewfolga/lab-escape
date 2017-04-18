package tide.labyrinth.domain;

/**
 * Created by andrzejfolga on 18/04/2017.
 */
public interface LabsRepository {

    String store(LabyrinthData labyrinthData);

    LabyrinthData get(String key) throws LabNotFoundException;

    char getValueFor(String key, int x, int y) throws LabNotFoundException;
}
