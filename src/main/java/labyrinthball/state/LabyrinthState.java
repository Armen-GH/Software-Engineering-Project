package labyrinthball.state;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.StringJoiner;


/**
 * Represents the state of the game.
 */
@Slf4j
public class LabyrinthState implements Cloneable{
    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 7;

    /**
     * An array containing all Indices of right inner walls.
     */
    public static final int[] RIGHT_WALL = new int[]{0, 2, 6, 7, 8, 10, 14, 16, 18, 19};

    /**
     * Index of a bottom inner wall.
     */
    public static final int[] BOTTOM_WALL = new int[]{1, 3, 5, 9, 11, 12, 13, 17};

    /**
     * Index of the blue ball.
     */
    public static final int BLUE_BALL = 4;

    /**
     * Index of the white circle.
     */
    public static final int goal = 15;

    private Position[] positions;

    /**
     * Creates a {@code LabyrinthState} object that corresponds to the original
     * initial state of the labyrinth.
     */
    public LabyrinthState() {
        this(new Position(0,0), // Right Wall
                new Position(0,2), // Bottom Wall
                new Position(0,3), // Right Wall
                new Position(0,6), // Bottom Wall
                new Position(1,4), // Blue Ball position
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
    }

    /**
     * Creates a {@code LabyrinthState} object initializing the positions of
     * the blue ball, white circle and right / bottom inner walls with
     * the positions specified. The constructor expects an array of
     * 20 {@code Position} objects or 19 {@code Position} objects.
     *
     * @param Board the initial positions of the pieces
     */
    public LabyrinthState(Position... Board) {
        checkPositions(Board);
        this.positions = deepClone(Board);
    }

    private void checkPositions(Position[] positions) {
        if (positions.length != 20) {
            throw new IllegalArgumentException();
        }
        for (var position : positions) {
            if (!isOnBoard(position)) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * {@return a copy of the position of the piece specified}
     *
     * @param n the number of a piece
     */
    public Position getPosition(int n) {
        return positions[n].clone();
    }

    /**
     * {@return whether the puzzle is solved}
     */
    public boolean isGoal() {
        return haveEqualPositions(goal);
    }

    /**
     * {@return whether the blue ball can be moved to the direction specified}
     *
     * @param direction a direction in which the blue ball is intended to move to
     */
    public boolean canMove(Direction direction) {
        return switch (direction) {
            case UP -> canMoveUp();
            case RIGHT -> canMoveRight();
            case DOWN -> canMoveDown();
            case LEFT -> canMoveLeft();
        };
    }

    private boolean canMoveUp() {
        return positions[BLUE_BALL].row() > 0 && !isWall(Direction.UP, BOTTOM_WALL);
    }

    private boolean canMoveRight() {
        if (positions[BLUE_BALL].col() == BOARD_SIZE - 1) {
            return false;
        }
        return !isWall(Direction.RIGHT,RIGHT_WALL);
    }

    private boolean canMoveDown() {
        if (positions[BLUE_BALL].row() == BOARD_SIZE - 1) {
            return false;
        }
        return !isWall(Direction.DOWN,BOTTOM_WALL);
    }

    private boolean canMoveLeft() {
        return positions[BLUE_BALL].col() > 0 && !isWall(Direction.LEFT, RIGHT_WALL);
    }

    /**
     * Moves the blue ball to the direction specified.
     *
     * @param direction the direction to which the blue ball is moved
     */
    public void move(Direction direction) {
        switch (direction) {
            case UP -> moveUp();
            case RIGHT -> moveRight();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
        }
    }

    private void moveUp() {
        positions[BLUE_BALL].setUp();
    }

    private void moveRight() {
        positions[BLUE_BALL].setRight();
    }

    private void moveDown() {
        positions[BLUE_BALL].setDown();
    }

    private void moveLeft() {
        positions[BLUE_BALL].setLeft();
    }

    private boolean haveEqualPositions(int j) {
        return positions[LabyrinthState.BLUE_BALL].equals(positions[j]);
    }

    private boolean isOnBoard(Position position) {
        return position.row() >= 0 && position.row() < BOARD_SIZE &&
                position.col() >= 0 && position.col() < BOARD_SIZE;
    }

    private boolean isWall(Direction direction, int[] walls){
        for (var wall : walls) {
            if ((direction == Direction.RIGHT || direction == Direction.DOWN) && haveEqualPositions(wall)) {
                    return true;
            }
            if (direction == Direction.LEFT && positions[BLUE_BALL].getLeft().equals(positions[wall])) {
                    return true;
            }
            if (direction == Direction.UP && positions[BLUE_BALL].getUp().equals(positions[wall])) {
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof LabyrinthState ps) && Arrays.equals(positions, ps.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }

    @Override
    public LabyrinthState clone() {
        LabyrinthState copy;
        try {
            copy = (LabyrinthState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.positions = deepClone(positions);
        return copy;
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(",", "[", "]");
        for (var position : positions) {
            sj.add(position.toString());
        }
        return sj.toString();
    }

    private static Position[] deepClone(Position[] a) {
        Position[] copy = a.clone();
        for (var i = 0; i < a.length; i++) {
            copy[i] = a[i].clone();
        }
        return copy;
    }
}
