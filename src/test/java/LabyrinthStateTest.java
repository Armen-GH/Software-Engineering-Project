import labyrinthball.state.Direction;
import labyrinthball.state.LabyrinthState;
import labyrinthball.state.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LabyrinthStateTest {
    LabyrinthState state1 = new LabyrinthState();

    LabyrinthState state2 = new LabyrinthState(
            new Position(0,0), // Right Wall
            new Position(0,2), // Bottom Wall
            new Position(0,3), // Right Wall
            new Position(0,6), // Bottom Wall
            new Position(5,2), // Blue Ball position
            new Position(2,1), // Bottom Wall
            new Position(2,2), // Right Wall
            new Position(2,5), // Right Wall
            new Position(3,3), // Right Wall
            new Position(3,3), // Bottom Wall
            new Position(3,4), // Right Wall
            new Position(3,6), // Bottom Wall
            new Position(4,0), // Bottom Wall
            new Position(4,4), // Bottom Wall
            new Position(5,1), // Right Wall
            new Position(5,2), // White Circle Position
            new Position(5,2), // Right Wall
            new Position(5,2), // Bottom Wall
            new Position(6,3), // Right Wall
            new Position(6,5) // Right Wall
    );

    LabyrinthState state3 = new LabyrinthState(
            new Position(0,0), // Right Wall
            new Position(0,2), // Bottom Wall
            new Position(0,3), // Right Wall
            new Position(0,6), // Bottom Wall
            new Position(6,0), // Blue Ball position
            new Position(2,1), // Bottom Wall
            new Position(2,2), // Right Wall
            new Position(2,5), // Right Wall
            new Position(3,3), // Right Wall
            new Position(3,3), // Bottom Wall
            new Position(3,4), // Right Wall
            new Position(3,6), // Bottom Wall
            new Position(4,0), // Bottom Wall
            new Position(4,4), // Bottom Wall
            new Position(5,1), // Right Wall
            new Position(5,2), // White Circle Position
            new Position(5,2), // Right Wall
            new Position(5,2), // Bottom Wall
            new Position(6,3), // Right Wall
            new Position(6,5) // Right Wall
    );

    LabyrinthState state4 = new LabyrinthState(
            new Position(0,0), // Right Wall
            new Position(0,2), // Bottom Wall
            new Position(0,3), // Right Wall
            new Position(0,6), // Bottom Wall
            new Position(0,6), // Blue Ball position
            new Position(2,1), // Bottom Wall
            new Position(2,2), // Right Wall
            new Position(2,5), // Right Wall
            new Position(3,3), // Right Wall
            new Position(3,3), // Bottom Wall
            new Position(3,4), // Right Wall
            new Position(3,6), // Bottom Wall
            new Position(4,0), // Bottom Wall
            new Position(4,4), // Bottom Wall
            new Position(5,1), // Right Wall
            new Position(5,2), // White Circle Position
            new Position(5,2), // Right Wall
            new Position(5,2), // Bottom Wall
            new Position(6,3), // Right Wall
            new Position(6,5) // Right Wall
    );

    LabyrinthState state5 = new LabyrinthState(
            new Position(0,0), // Right Wall
            new Position(0,2), // Bottom Wall
            new Position(0,3), // Right Wall
            new Position(0,6), // Bottom Wall
            new Position(1,2), // Blue Ball position
            new Position(2,1), // Bottom Wall
            new Position(2,2), // Right Wall
            new Position(2,5), // Right Wall
            new Position(3,3), // Right Wall
            new Position(3,3), // Bottom Wall
            new Position(3,4), // Right Wall
            new Position(3,6), // Bottom Wall
            new Position(4,0), // Bottom Wall
            new Position(4,4), // Bottom Wall
            new Position(5,1), // Right Wall
            new Position(5,2), // White Circle Position
            new Position(5,2), // Right Wall
            new Position(5,2), // Bottom Wall
            new Position(6,3), // Right Wall
            new Position(6,5) // Right Wall
    );

    @Test
    void isGoal(){
        assertFalse(state1.isGoal());
        assertTrue(state2.isGoal());
        assertFalse(state3.isGoal());
    }

    @Test
    void canMove_state1(){
        assertTrue(state1.canMove(Direction.UP));
        assertTrue(state1.canMove(Direction.RIGHT));
        assertTrue(state1.canMove(Direction.DOWN));
        assertTrue(state1.canMove(Direction.LEFT));
    }

    @Test
    void canMove_state2(){
        assertTrue(state2.canMove(Direction.UP));
        assertFalse(state2.canMove(Direction.RIGHT));
        assertFalse(state2.canMove(Direction.DOWN));
        assertFalse(state2.canMove(Direction.LEFT));
    }

    @Test
    void canMove_State3(){
        assertTrue(state3.canMove(Direction.UP));
        assertTrue(state3.canMove(Direction.RIGHT));
        assertFalse(state3.canMove(Direction.DOWN));
        assertFalse(state3.canMove(Direction.LEFT));
    }

    @Test
    void canMove_State4(){
        assertFalse(state4.canMove(Direction.UP));
        assertFalse(state4.canMove(Direction.RIGHT));
        assertFalse(state4.canMove(Direction.DOWN));
        assertTrue(state4.canMove(Direction.LEFT));
    }

    @Test
    void canMove_State5(){
        assertFalse(state5.canMove(Direction.UP));
        assertTrue(state5.canMove(Direction.RIGHT));
        assertTrue(state5.canMove(Direction.DOWN));
        assertTrue(state5.canMove(Direction.LEFT));
    }

    @Test
    void move_state1_up(){
        var copy = state1.clone();
        state1.move(Direction.UP);
        assertEquals(copy.getPosition(4).getUp(),state1.getPosition(4));
    }

    @Test
    void move_state1_right(){
        var copy = state1.clone();
        state1.move(Direction.RIGHT);
        assertEquals(copy.getPosition(4).getRight(),state1.getPosition(4));
    }

    @Test
    void move_state1_down(){
        var copy = state1.clone();
        state1.move(Direction.DOWN);
        assertEquals(copy.getPosition(4).getDown(),state1.getPosition(4));
    }

    @Test
    void move_state1_left(){
        var copy = state1.clone();
        state1.move(Direction.LEFT);
        assertEquals(copy.getPosition(4).getLeft(),state1.getPosition(4));
    }

    @Test
    void move_state2_up(){
        var copy = state2.clone();
        state2.move(Direction.UP);
        assertEquals(copy.getPosition(4).getUp(),state2.getPosition(4));
    }

    @Test
    void move_state3_up(){
        var copy = state3.clone();
        state3.move(Direction.UP);
        assertEquals(copy.getPosition(4).getUp(),state3.getPosition(4));
    }

    @Test
    void move_state3_right(){
        var copy = state3.clone();
        state3.move(Direction.RIGHT);
        assertEquals(copy.getPosition(4).getRight(),state3.getPosition(4));
    }

    @Test
    void testEquals(){
        assertTrue(state1.equals(state1));

        var clone = state3.clone();
        clone.move(Direction.UP);
        assertFalse(clone.equals(state3));

        assertFalse(state1.equals(state2));
        assertFalse(state1.equals(null));
    }

    @Test
    void testHashCode() {
        assertTrue(state1.hashCode() == state1.hashCode());
        assertTrue(state1.hashCode() == state1.clone().hashCode());
    }

    @Test
    void testClone() {
        var clone = state1.clone();
        assertTrue(clone.equals(state1));
        assertNotSame(clone, state1);
    }

    @Test
    void testTooString(){
        assertEquals("[(0,0),(0,2),(0,3),(0,6),(1,4),(2,1),(2,2),(2,5),(3,3),(3,3)," +
                "(3,4),(3,6),(4,0),(4,4),(5,1),(5,2),(5,2),(5,2),(6,3),(6,5)]", state1.toString());
        assertEquals("[(0,0),(0,2),(0,3),(0,6),(5,2),(2,1),(2,2),(2,5),(3,3),(3,3)," +
                "(3,4),(3,6),(4,0),(4,4),(5,1),(5,2),(5,2),(5,2),(6,3),(6,5)]", state2.toString());
        assertEquals("[(0,0),(0,2),(0,3),(0,6),(6,0),(2,1),(2,2),(2,5),(3,3),(3,3)," +
                "(3,4),(3,6),(4,0),(4,4),(5,1),(5,2),(5,2),(5,2),(6,3),(6,5)]", state3.toString());
    }
}
