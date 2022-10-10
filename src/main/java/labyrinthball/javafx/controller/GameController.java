package labyrinthball.javafx.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import labyrinthball.results.Result;
import labyrinthball.results.ResultRepository;
import labyrinthball.stopwatch.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import labyrinthball.state.*;

@Slf4j
public class GameController {
    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Button endButton;

    @FXML
    private Label timeLabel;

    private final ImageView pieceView = new ImageView();

    private final ImageView goalView = new ImageView();

    private LabyrinthState state;

    private String player;

    private IntegerProperty numberOfMoves = new SimpleIntegerProperty();

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private Stopwatch stopwatch = new Stopwatch();

    public void setPlayer(String name){
        this.player = name;
    }

    @FXML
    private void initialize() {
        createBindings();
        loadImage();
        populateGrid();
        resetGame();
        registerListeners();
    }

    private void createBindings() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
        timeLabel.textProperty().bind(stopwatch.hhmmssProperty());
    }

    @FXML
    private void resetGame() {
        log.debug("Setting up a new game");
        state = new LabyrinthState();
        numberOfMoves.set(0);
        handleStartResetTime();
        gameOver.set(state.isGoal());
        clearGrid();
        showStateOnGrid();
        endButton.setText("Give up!");
        endButton.setPrefWidth(90);
    }

    @FXML
    private void quitGame(javafx.event.ActionEvent actionEvent) throws IOException {
        if(!gameOver.get()){
            log.debug("Player {} has given up", player);
            storeResult();
        }
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    private void registerListeners() {
        gameOver.addListener(this::handleGameOver);
    }

    public void handleStartResetTime(){
        switch (stopwatch.getStatus()) {
            case STOPPED -> {
                stopwatch.start();
            }
            case RUNNING -> {
                stopwatch.stop();
                stopwatch.reset();
                stopwatch.start();
            }
            case  PAUSED-> {
                stopwatch.reset();
                stopwatch.start();
            }
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (Node) event.getSource();
        var row = GridPane.getRowIndex(source);
        var col = GridPane.getColumnIndex(source);
        log.debug("Click on square {},{}", row, col);
        var direction = getDirectionFromClickPosition(row, col);
        direction.ifPresentOrElse(this::performMove,
                () -> log.warn("Click does not correspond with any direction"));
    }

    private void performMove(Direction direction) {
        while (state.canMove(direction)) {
            log.info("Move: {}", direction);
            var oldState = state.clone();
            state.move(direction);
            log.trace("New state: {}", state);
            updateStateOnGrid(oldState, state);
            numberOfMoves.set(numberOfMoves.get() + 1);
            if (state.isGoal()) {
                gameOver.set(true);
            }
        }
    }

    private void storeResult(){
        log.debug("Storing game results for player {}", player);
        var repository = new ResultRepository();

        var file = new File("results.json");
        try {
            repository.loadFromFile(file);
        } catch (FileNotFoundException e) {
            log.warn("File {} was not found, creating one!", file);
        } catch (IOException e){
            log.warn("Corrupted / error reading date");
        }
        repository.add(generateResult());
        try {
            repository.saveToFile(file);
        } catch (IOException e){
            log.warn("Corrupted / error reading date");
        }
    }

    private Result generateResult(){
        return Result.builder()
                .name(player)
                .numberOfMoves(numberOfMoves.intValue())
                .endTime(ZonedDateTime.now())
                .timer(stopwatch.hhmmssProperty().getValue())
                .solved(gameOver.get())
                .build();
    }

    private void handleGameOver(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            stopwatch.stop();
            log.debug("Player {} has successfully finished this game!", player);
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Over");
            alert.setContentText("Congratulations, you have solved the puzzle!");
            alert.showAndWait();
            endButton.setText("See Standings!");
            endButton.setPrefWidth(130);
            storeResult();
        }
    }

    private void populateGrid() {
        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var col = 0; col < grid.getColumnCount(); col++) {
                var square = new StackPane();
                square.getStyleClass().add("square");
                if((row==0&&col==0)||(row==0&&col==3)||(row==2&&col==2)|| (row==2&&col==5)||
                        (row==3&&col==4)||(row==5&&col==1)|| (row==6&&col==3)||(row==6&&col==5)){
                    square.getStyleClass().add("rightWall");
                } else if ((row==0&&col==2)||(row==0&&col==6)||(row==2&&col==1)||(row==3&&col==6) ||
                        (row==4&&col==0) || (row==4&&col==8)||(row==4&&col==4)) {
                    square.getStyleClass().add("bottomWall");
                } else if ((row==3&&col==3) || (row==5&&col==2)) {
                    square.getStyleClass().add("rightBottomWall");
                }
                square.setOnMouseClicked(this::handleMouseClick);
                grid.add(square, col, row);
            }
        }
    }

    private void clearGrid() {
        for (var row = 0; row < 6; row++) {
            for (var col = 0; col < 6; col++) {
                getGridNodeAtPosition(grid, row, col)
                        .ifPresent(node -> ((StackPane) node).getChildren().clear());
            }
        }
    }

    private void showStateOnGrid() {
        var pos = state.getPosition(4);
        getGridNodeAtPosition(grid, pos)
                .ifPresent(node -> ((StackPane) node).getChildren().add(pieceView));
        var goalPos = state.getPosition(15);
        getGridNodeAtPosition(grid, goalPos)
                .ifPresent(node -> ((StackPane) node).getChildren().add(goalView));
    }

    private void updateStateOnGrid(LabyrinthState oldState, LabyrinthState newState) {
        var oldPos = oldState.getPosition(4);
        var newPos = newState.getPosition(4);
        if (!newPos.equals(oldPos)) {
            log.trace("Blue ball has been moved from {} to {}", oldPos, newPos);
            movePieceOnGrid(oldPos, newPos);
        }
    }

    private void movePieceOnGrid(Position from, Position to){
        getGridNodeAtPosition(grid, from)
                .ifPresent(node -> ((StackPane) node).getChildren().remove(pieceView));
        getGridNodeAtPosition(grid, to)
                .ifPresent(node -> ((StackPane) node).getChildren().add(pieceView));
    }

    private void loadImage(){
        Image blueBall = new Image("/images/blue.png");
        log.debug("Loading image resource {}", blueBall);
        Image goal = new Image("/images/goal.png");
        log.debug("Loading image resource {}", goal);
        pieceView.setImage(blueBall);
        goalView.setImage(goal);
    }

    private Optional<Direction> getDirectionFromClickPosition(int row, int col) {
        var blockPos = state.getPosition(LabyrinthState.BLUE_BALL);
        Direction direction = null;
        try {
            direction = Direction.of(row - blockPos.row(), col - blockPos.col());
        } catch (IllegalArgumentException e) {
        }
        return Optional.ofNullable(direction);
    }

    private static Optional<Node> getGridNodeAtPosition(GridPane gridPane, Position pos) {
        return getGridNodeAtPosition(gridPane, pos.row(), pos.col());
    }

    private static Optional<Node> getGridNodeAtPosition(GridPane gridPane, int row, int col) {
        return gridPane.getChildren().stream()
                .filter(child -> GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col)
                .findFirst();
    }
}
