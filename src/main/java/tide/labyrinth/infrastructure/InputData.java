package tide.labyrinth.infrastructure;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
public class InputData {

    private final int startPosX;
    private final int startPosY;
    private final char[][] labyrinth;

    public InputData(int startPosX, int startPosY, char[][] labyrinth) {
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.labyrinth = labyrinth;
    }

    public int getStartPosX() {
        return startPosX;
    }

    public int getStartPosY() {
        return startPosY;
    }

    public char[][] getLabyrinth() {
        return labyrinth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputData inputData = (InputData) o;

        return startPosX == inputData.startPosX &&
                startPosY == inputData.startPosY &&
                Arrays.deepEquals(labyrinth, inputData.labyrinth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosX, startPosY, labyrinth);
    }

    @Override
    public String toString() {
        return "InputData{" +
                "startPosX=" + startPosX +
                ", startPosY=" + startPosY +
                ", labyrinth=" + Arrays.toString(labyrinth) +
                '}';
    }
}
