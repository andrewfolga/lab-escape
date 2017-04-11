package tide.labyrinth.domain;

import java.util.Objects;

/**
 * Created by andrzejfolga on 09/04/2017.
 */
public class Coordinate {
    private final int currentX;
    private final int currentY;

    public Coordinate(int currentX, int currentY) {
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public int getX() {
        return currentX;
    }

    public int getY() {
        return currentY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return currentX == that.currentX &&
                currentY == that.currentY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentX, currentY);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "currentX=" + currentX +
                ", currentY=" + currentY +
                '}';
    }
}
