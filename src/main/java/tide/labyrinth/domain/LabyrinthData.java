package tide.labyrinth.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Created by andrzejfolga on 10/04/2017.
 */
public class LabyrinthData {

    private final int startPosX;
    private final int startPosY;
    private final int numberOfWalls;
    private final int numberOfEmptySpaces;
    private final char[][] labyrinth;

    @JsonCreator
    public LabyrinthData(int startPosX, int startPosY, int numberOfWalls, int numberOfEmptySpaces, char[][] labyrinth) {
        isTrue(startPosX <= labyrinth.length - 1 && startPosY <= labyrinth[0].length - 1,
                "Coordinates should be within the following rangesx:0-%d and y:0-%d", labyrinth.length-1, labyrinth[0].length-1);
        this.numberOfWalls = numberOfWalls;
        this.numberOfEmptySpaces = numberOfEmptySpaces;
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

    public int getNumberOfWalls() {
        return numberOfWalls;
    }

    public int getNumberOfEmptySpaces() {
        return numberOfEmptySpaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabyrinthData that = (LabyrinthData) o;
        return startPosX == that.startPosX &&
                startPosY == that.startPosY &&
                numberOfWalls == that.numberOfWalls &&
                numberOfEmptySpaces == that.numberOfEmptySpaces &&
                Arrays.deepEquals(labyrinth, that.labyrinth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosX, startPosY, numberOfWalls, numberOfEmptySpaces, labyrinth);
    }

    @Override
    public String toString() {
        return "LabyrinthData{" +
                "startPosX=" + startPosX +
                ", startPosY=" + startPosY +
                ", numberOfWalls=" + numberOfWalls +
                ", numberOfEmptySpaces=" + numberOfEmptySpaces +
                ", labyrinth=" + Arrays.toString(labyrinth) +
                '}';
    }
}
