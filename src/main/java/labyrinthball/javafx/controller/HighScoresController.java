package labyrinthball.javafx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import labyrinthball.results.Result;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import labyrinthball.results.ResultRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HighScoresController {
    private Result result;

    public void setResult(Result score){
        this.result = score;
    }

    ResultRepository repository = new ResultRepository();

    private int getTop = 10;

    @FXML
    private TableView<Result> tableView;

    @FXML
    private TableColumn<Result, String> name;

    @FXML
    private TableColumn<Result, Integer> moves;

    @FXML
    private TableColumn<Result, String> dateTime;

    @FXML
    private TableColumn<Result, String> stopWatch;


    @FXML
    private void initialize() throws IOException{
        log.debug("Populating HighScores table");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        moves.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));
        dateTime.setCellValueFactory(
                celldata -> new ReadOnlyStringWrapper(celldata.getValue().getEndTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)))
        );
        stopWatch.setCellValueFactory(new PropertyValueFactory<>("timer"));
        repository.loadFromFile(new File("results.json"));
        List<Result> highScores = repository.findHighScores(getTop);
        ObservableList<Result> observableList = FXCollections.observableArrayList();
        observableList.addAll(highScores);
        tableView.setItems(observableList);
    }

    @FXML
    public void restartGame(javafx.event.ActionEvent actionEvent) throws IOException {
        log.debug("Restarting application...");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        }
    }

