package labyrinthball.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.TextField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LaunchController {
    @FXML
    private TextField player;

    private FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    public void startGame(javafx.event.ActionEvent actionEvent) throws IOException {
        if(player.getText().isEmpty()){
            log.warn("A username was not provided!");
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Missing Username!");
            alert.setContentText("Please enter a username to proceed!");
            alert.showAndWait();
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayer(player.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            log.info("Username is {}", player.getText());
        }
        }
    }

