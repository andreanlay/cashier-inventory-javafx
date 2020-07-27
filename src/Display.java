package cashier.inventory.project;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;

import java.util.Optional;

public class Display {
    public static void Message(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<ButtonType> ConfirmMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    public static void ErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static Optional<String> askInput(String title, String message){
        TextInputDialog input = new TextInputDialog();
        input.setHeaderText(null);
        input.setTitle(title);
        input.setContentText(message);
        return input.showAndWait();
    }
}
