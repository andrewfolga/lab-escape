package tide.labyrinth.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by andrzejfolga on 18/04/2017.
 */
@Component
public class LabManager {

    private final LabsRepository labsRepository;
    private final DFSLabEscape dfsLabEscape;

    @Autowired
    public LabManager(LabsRepository labsRepository, DFSLabEscape dfsLabEscape) {
        this.labsRepository = labsRepository;
        this.dfsLabEscape = dfsLabEscape;
    }

    public char[][] drawPathForEscape(String key) throws NoEscapeException, LabNotFoundException {
        LabyrinthData labyrinthData = labsRepository.get(key);
        return dfsLabEscape.drawPathForEscape(labyrinthData.getLabyrinth(), labyrinthData.getStartPosX(), labyrinthData.getStartPosY());
    }

    public String createLabyrinth(LabyrinthData labyrinthData) {
        return labsRepository.store(labyrinthData);
    }

    public LabyrinthData getLabyrinth(String key) throws LabNotFoundException {
        return labsRepository.get(key);
    }

    public char getValueFor(String key, int x, int y) throws LabNotFoundException {
        return labsRepository.getValueFor(key, x, y);
    }
}
