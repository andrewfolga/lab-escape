package tide.labyrinth.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by andrzejfolga on 09/04/2017.
 */
public class PathFinder {

    final static Logger LOG = LoggerFactory.getLogger(PathFinder.class);
    private final static String LOG_CONTEXT_MSG = "Context: currentDirection=%s, rightRotationCount=%s, walked=%s, visited=%s";

    public static final char WALL = 'O';
    public static final char FREE = ' ';
    public static final char PATH = '•';

    private static final Direction STARTING_DIRECTION = Direction.UP;
    private final Stack<Coordinate> walked = new Stack<>();
    private final List<Coordinate> visited = new ArrayList<>();

    private Direction currentDirection;
    private int rightRotationCount;
    private char[][] labyrinth;

    public PathFinder(char[][] labyrinth, int startX, int startY) {
        this.labyrinth = labyrinth;
        Coordinate startPosition = new Coordinate(startX, startY);
        this.walked.push(startPosition);
        this.visited.add(startPosition);
        this.currentDirection = STARTING_DIRECTION;
        this.rightRotationCount = 0;
    }

    public char[][] tryEscape() throws NoEscapeException {
        try {
            while (true) {
                if (hasTriedAllPaths()) {
                    throw new NoEscapeException();
                } else {
                    if (hasReachedExit()) {
                        return applyEscapePath();
                    } else if (hasTurnedAround()) {
                        stepBack();
                    } else if (isLeftWallOrPath() && canMoveForward()) {
                        moveForward();
                    } else {
                        turnRight();
                    }
                }
            }
        } catch (Throwable t) {
            LOG.error(String.format(LOG_CONTEXT_MSG, currentDirection, rightRotationCount, walked, visited) + t);
            throw t;
        }
    }

    private char[][] applyEscapePath() {
        walked.stream().forEach(e -> labyrinth[e.getX()][e.getY()]=PATH);
        return labyrinth;
    }

    private boolean hasTriedAllPaths() {
        if (walked.empty()) return true;
        else return false;
    }

    private void stepBack() {
        rightRotationCount = 0;
        walked.pop();
    }

    private boolean hasReachedExit() {
        Coordinate curCoord = walked.peek();
        int x = curCoord.getX();
        int y = curCoord.getY();
        switch (currentDirection) {
            case UP:
                if (x == 0 && labyrinth[x][y] == FREE)
                    return true;
                break;
            case RIGHT:
                if (y == labyrinth[0].length - 1 && labyrinth[x][y] == FREE)
                    return true;
                break;
            case DOWN:
                if (x == labyrinth.length - 1 && labyrinth[x][y] == FREE)
                    return true;
                break;
            case LEFT:
                if (y == 0 && labyrinth[x][y] == FREE)
                    return true;
                break;
        }
        return false;
    }

    private boolean hasTurnedAround() {
        if (rightRotationCount > 3) return true;
        else return false;
    }

    private boolean canMoveForward() {
        Coordinate curCoord = walked.peek();
        int curX = curCoord.getX();
        int curY = curCoord.getY();
        Coordinate nextCoordinate = null;
        switch (currentDirection) {
            case UP:
                nextCoordinate = new Coordinate(curX - 1, curY);
                if (curX >= 1 && labyrinth[curX - 1][curY] == FREE && !visited.contains(nextCoordinate))
                    return true;
                break;
            case RIGHT:
                nextCoordinate = new Coordinate(curX, curY + 1);
                if (curY <= labyrinth[0].length - 1 && labyrinth[curX][curY + 1] == FREE && !visited.contains(nextCoordinate))
                    return true;
                break;
            case DOWN:
                nextCoordinate = new Coordinate(curX + 1, curY);
                if (curX <= labyrinth.length - 1 && labyrinth[curX + 1][curY] == FREE && !visited.contains(nextCoordinate))
                    return true;
                break;
            case LEFT:
                nextCoordinate = new Coordinate(curX, curY - 1);
                if (curY >= 1 && labyrinth[curX][curY - 1] == FREE && !visited.contains(nextCoordinate))
                    return true;
                break;
        }
        return false;
    }

    private void turnRight() {
        rightRotationCount++;
        switch (currentDirection) {
            case UP:
                currentDirection = Direction.RIGHT;
                break;
            case RIGHT:
                currentDirection = Direction.DOWN;
                break;
            case DOWN:
                currentDirection = Direction.LEFT;
                break;
            case LEFT:
                currentDirection = Direction.UP;
                break;
        }
    }

    private void moveForward() {
        Coordinate curCoord = walked.peek();
        int curX = curCoord.getX();
        int curY = curCoord.getY();
        rightRotationCount = 0;
        Coordinate coordinate = null;
        switch (currentDirection) {
            case UP:
                coordinate = new Coordinate(curX - 1, curY);
                walked.push(coordinate);
                visited.add(coordinate);
                return;
            case RIGHT:
                coordinate = new Coordinate(curX, curY + 1);
                walked.push(coordinate);
                visited.add(coordinate);
                return;
            case DOWN:
                coordinate = new Coordinate(curX + 1, curY);
                walked.push(coordinate);
                visited.add(coordinate);
                return;
            case LEFT:
                coordinate = new Coordinate(curX, curY - 1);
                walked.push(coordinate);
                visited.add(coordinate);
                return;
        }
    }

    private boolean isLeftWallOrPath() throws NoEscapeException {
        Coordinate curCoord = walked.peek();
        int curX = curCoord.getX();
        int curY = curCoord.getY();
        switch (currentDirection) {
            case UP:
                if (labyrinth[curX - 1][curY - 1] == WALL || labyrinth[curX - 1][curY - 1] == FREE)
                    return true;
                break;
            case RIGHT:
                if (labyrinth[curX - 1][curY + 1] == WALL || labyrinth[curX - 1][curY + 1] == FREE)
                    return true;
                break;
            case DOWN:
                if (labyrinth[curX + 1][curY + 1] == WALL || labyrinth[curX + 1][curY + 1] == FREE)
                    return true;
                break;
            case LEFT:
                if (labyrinth[curX + 1][curY - 1] == WALL || labyrinth[curX + 1][curY - 1] == FREE)
                    return true;
                break;
        }
        return false;
    }

}
