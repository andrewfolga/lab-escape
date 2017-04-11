package tide.labyrinth.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by andrzejfolga on 09/04/2017.
 */
public class LabEscapeTest {

    private LabEscape labEscape = new LabEscape();

    @Test
    public void shouldHandleStandardLabyrinth() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O','O','O'},
                {'O',' ',' ',' ','O'},
                {'O',' ','O',' ','O'},
                {'O','O','O',' ','O'},
                {' ',' ',' ',' ','O'},
                {'O','O','O','O','O'}};

        char[][] escapePath = labEscape.drawPathForEscape(labyrinth, 2, 1);

        char[][] correctEscapePath = new char[][]{
                {'O','O','O','O','O'},
                {'O','•','•','•','O'},
                {'O','•','O','•','O'},
                {'O','O','O','•','O'},
                {'•','•','•','•','O'},
                {'O','O','O','O','O'}};

        assertThat(escapePath, is(equalTo(correctEscapePath)));
    }
    @Test
    public void shouldHandleOneNodeLabyrinth() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O'},
                {'O',' ','O'}};

        char[][] escapePath = labEscape.drawPathForEscape(labyrinth, 1, 1);

        char[][] correctEscapePath = new char[][]{
                {'O','O','O'},
                {'O','•','O'}};

        assertThat(escapePath, is(equalTo(correctEscapePath)));
    }

    @Test
    public void shouldHandleSparseLabyrinth() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O','O','O'},
                {'O',' ',' ',' ','O'},
                {'O',' ',' ',' ',' '},
                {'O',' ',' ',' ','O'},
                {'O',' ',' ',' ','O'},
                {'O','O','O','O','O'}};

        char[][] escapePath = labEscape.drawPathForEscape(labyrinth, 4, 1);

        char[][] correctEscapePath = new char[][]{
                {'O','O','O','O','O'},
                {'O','•','•','•','O'},
                {'O','•',' ','•','•'},
                {'O','•',' ',' ','O'},
                {'O','•',' ',' ','O'},
                {'O','O','O','O','O'}};

        assertThat(escapePath, is(equalTo(correctEscapePath)));
    }

    @Test
    public void shouldHandleLabyrinthWithOneDiagonal() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O','O','O','O'},
                {'O',' ',' ',' ',' ','O'},
                {'O',' ',' ','O',' ','O'},
                {' ',' ','O',' ',' ','O'},
                {'O','O',' ',' ',' ','O'},
                {'O','O','O','O','O','O'}};

        char[][] escapePath = labEscape.drawPathForEscape(labyrinth, 4, 2);

        char[][] correctEscapePath = new char[][]{
                {'O','O','O','O','O','O'},
                {'O','•','•','•','•','O'},
                {'O','•',' ','O','•','O'},
                {'•','•','O',' ','•','O'},
                {'O','O','•','•','•','O'},
                {'O','O','O','O','O','O'}};

        assertThat(escapePath, is(equalTo(correctEscapePath)));
    }

    @Test(expected = NoEscapeException.class)
    public void shouldFailForNoEscapeLabyrinth() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O','O','O'},
                {'O',' ',' ',' ','O'},
                {'O',' ','O',' ','O'},
                {'O','O','O',' ','O'},
                {'O',' ',' ',' ','O'},
                {'O','O','O','O','O'}};

        labEscape.drawPathForEscape(labyrinth, 2, 1);
    }

    @Test
    public void shouldHandleExitBehindStartDirection() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O','O','O'},
                {'O',' ',' ',' ','O'},
                {'O',' ','O',' ','O'},
                {'O',' ','O',' ','O'},
                {'O',' ','O',' ','O'},
                {'O',' ','O','O','O'}};

        char[][] escapePath = labEscape.drawPathForEscape(labyrinth, 4, 1);

        char[][] correctEscapePath = new char[][]{
                {'O','O','O','O','O'},
                {'O',' ',' ',' ','O'},
                {'O',' ','O',' ','O'},
                {'O',' ','O',' ','O'},
                {'O','•','O',' ','O'},
                {'O','•','O','O','O'}};

        assertThat(escapePath, is(equalTo(correctEscapePath)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailFastIfStartingPointOnTheWall() throws Exception {

        char[][] labyrinth = new char[][]{
                {'O','O','O','O','O'},
                {'O',' ',' ',' ','O'},
                {'O',' ','O',' ','O'},
                {'O',' ','O',' ','O'},
                {'O','O','O',' ','O'},
                {'O',' ','O','O','O'}};

        labEscape.drawPathForEscape(labyrinth, 4, 1);
    }

}
