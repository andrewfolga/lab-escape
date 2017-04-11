package tide.labyrinth.domain;

/**
 * Created by andrzejfolga on 09/04/2017.
 */
public enum Direction {
    UP,DOWN,LEFT,RIGHT;

    public Direction turnRight() {
        switch(this) {
            case UP: return RIGHT;
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
            case LEFT: return UP;
        }
        return DOWN;
    }
}
