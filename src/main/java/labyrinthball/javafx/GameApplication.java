package labyrinthball.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GameApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        log.info("Executing the application...");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        stage.setTitle("Labyrinth Ball Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
