package cashier.inventory.project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class startController{
    @FXML private TextField username;

    @FXML private PasswordField password;

    @FXML private Button login;

    @FXML private Label time;

    public void verifyLogin(ActionEvent actionEvent) throws Exception{
        Window parent = login.getScene().getWindow();
        if(username.getText().isEmpty() || password.getText().isEmpty()){
            Display.ErrorMessage("Error!", "Please fill all fields!");
        }else if(!username.getText().equals("admin") || !password.getText().equals("root")){
            Display.ErrorMessage("Error!", "Invalid login!");
        }else{
            Display.Message("Login Information", "Welcome, admin.");
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/cashier.inventory.project/menu.fxml"));
            stage.setTitle("Cashier");
            stage.setScene(new Scene(root));
            parent.hide();
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML public void initialize(){
        clock();
    }

    private void clock(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e-> {
            time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}