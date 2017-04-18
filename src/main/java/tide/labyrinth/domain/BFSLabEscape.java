package tide.labyrinth.domain;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by andrzejfolga on 11/04/2017.
 */
@Component
public class BFSLabEscape {

    public char[][] drawPathForEscape(char[][] labyrinth, int startX, int startY) throws NoEscapeException {
        Validate.isTrue(labyrinth[startX][startY]==' ');

        PathFinder pathFinder = new PathFinder(labyrinth, startX, startY, 1, 0, 99, 159);

        return pathFinder.tryEscape();
    }


    static class PathFinder {

        private char[][] maze;
        private int startX;
        private int startY;
        private int endX;
        private int endY;
        private int mazeHeight;
        private int mazeWidth;

        public PathFinder(char[][] maze, int startX, int startY, int endX, int endY, int mazeHeight, int mazeWidth) {
            this.maze = maze;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.mazeHeight = mazeHeight;
            this.mazeWidth = mazeWidth;
        }

        public char[][] tryEscape() throws NoEscapeException {
            breadthFirstSearch();
            return maze;
        }

        private boolean isStartPos(int x, int y) {
            return (x == startX && y == startY);
        }

        private boolean isEndPos(int x, int y) {
            return (x == endX && y == endY);
        }

        // http://en.wikipedia.org/wiki/Breadth-first_search
        private void breadthFirstSearch() throws NoEscapeException {
            HashSet<Tuple> discovered = new HashSet<Tuple>();
            Hashtable<Tuple, Tuple> nodeDiscovered = new Hashtable<Tuple, Tuple>();
            Queue<Tuple> queue = new LinkedList<Tuple>();
            Tuple start = new Tuple(startX, startY);
            Tuple end = new Tuple(endX, endY);
            queue.add(start);
            discovered.add(start);
            while (!queue.isEmpty() && !discovered.contains(end)) {
                Tuple next = queue.poll();
                for (Tuple neighbour : pointNeighbours(next.x, next.y)) {
                    if (!discovered.contains(neighbour)) {
                        queue.add(neighbour);
                        discovered.add(neighbour);
                        nodeDiscovered.put(neighbour, next);
                    }
                }
            }
            try {
                Tuple next = nodeDiscovered.get(end);
                while (true) {
                    maze[next.x][next.y] = '•';
                    if (nodeDiscovered.contains(next) && nodeDiscovered.get(next) != start) {
                        next = nodeDiscovered.get(next);
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("No Path Found - Maze Impossible");
                throw new NoEscapeException();
            }
        }

        private ArrayList<Tuple> pointNeighbours(int x, int y) {
            ArrayList<Tuple> neighbours = new ArrayList<Tuple>();
            if (y != 0 && maze[x][y - 1] != 'O') {
                neighbours.add(new Tuple(x, y - 1));
            }
            if (x != 0 && maze[x - 1][y] != 'O') {
                neighbours.add(new Tuple(x - 1, y));
            }
            if (y != mazeHeight - 1 && maze[x][y + 1] != 'O') {
                neighbours.add(new Tuple(x, y + 1));
            }
            if (x != mazeWidth - 1 && maze[x + 1][y] != 'O') {
                neighbours.add(new Tuple(x + 1, y));
            }
            return neighbours;
        }

        class Tuple {
            public final int x;
            public final int y;

            public Tuple(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof Tuple)) {
                    return false;
                }
                Tuple that = (Tuple) other;
                return (this.x == that.x && this.y == that.y);
            }

            @Override
            public int hashCode() {
                // http://en.wikipedia.org/wiki/Pairing_function#Cantor_pairing_function
                return ((x + y) * (x + y + 1) / 2) + y;
            }
        }
    }
}
