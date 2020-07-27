package cashier.inventory.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import cashier.inventory.project.database.dbHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class inventoryController{
    private dbHandler db;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private ChoiceBox<String> categoryCB;

    public void initialize(){
        db = new dbHandler();
        categoryCB.getItems().clear();
        loadChoiceBox();
        priceField.setOnKeyTyped(e-> {
            if(!e.getCharacter().matches("[0-9]")){
                priceField.deletePreviousChar();
            }
        });
        quantityField.setOnKeyTyped(e-> {
            if(!e.getCharacter().matches("[0-9]")){
                quantityField.deletePreviousChar();
            }
        });
    }

    public void viewInventory(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/cashier.inventory.project/viewItem.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Inventory");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(idField.getScene().getWindow());
        stage.show();
    }

    public void addItem(ActionEvent actionEvent){
        String id = idField.getText();
        String name = nameField.getText();
        String category = categoryCB.getValue();
        String quantity = quantityField.getText();
        String price = priceField.getText();
        if(id.isEmpty() || name.isEmpty() || quantity.isEmpty() || price.isEmpty() || category.isEmpty()){
            Display.ErrorMessage("Error!", "Please fill all fields");
            return;
        }
        Optional<ButtonType> res = Display.ConfirmMessage("Add Item", "Are you sure ?");
        if(res.get() == ButtonType.OK){
            int value = Integer.parseInt(price);
            int qty = Integer.parseInt(quantity);
            boolean flag = db.addItem(id, name, category, qty, value);
            clearAll();
            if(flag){
                Display.Message("Success", "Successfully added item!");
            }else{
                Display.Message("Failed", "Failed to add item.");
            }
        }else{
            Display.Message("Fail", "Add item canceled.");
        }
    }

    public void exit(ActionEvent actionEvent) throws Exception{
        Window parent = idField.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/cashier.inventory.project/menu.fxml"));
        stage.setTitle("Cashier");
        stage.setScene(new Scene(root));
        parent.hide();
        stage.setResizable(false);
        stage.show();
    }

    public void addCategory(ActionEvent actionEvent){
        Optional<String> categoryName = Display.askInput("Category Name", "Please input desired category name:");
        if(db.addCategory(categoryName.get())){
            Display.Message("Success", "Successfully added " + categoryName.get() + " as new category!");
            loadChoiceBox();
        }else{
            Display.ErrorMessage("Fail", "Fail to add category");
        }
    }

    private void loadChoiceBox(){
        categoryCB.getItems().clear();
        ResultSet res = db.getCategory();
        if(res != null){
            try {
                while (res.next()) {
                    String category = res.getString("category");
                    categoryCB.getItems().add(category);
                }
            }catch(SQLException e){
                System.out.println("ERROR Getting category data!");
            }
        }
    }

    private void clearAll(){
        idField.clear();
        nameField.clear();
        categoryCB.setValue(null);
        quantityField.clear();
        priceField.clear();
    }
}
