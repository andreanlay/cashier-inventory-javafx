package cashier.inventory.project;

import cashier.inventory.project.database.dbHandler;
import cashier.inventory.project.database.Item;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class viewItemController {
    private dbHandler db;

    @FXML private TableColumn<Item, String> idCol;
    @FXML private TableColumn<Item, String> nameCol;
    @FXML private TableColumn<Item, String> categoryCol;
    @FXML private TableColumn<Item, Integer> quantityCol;
    @FXML private TableColumn<Item, Integer> priceCol;
    @FXML private TableView<Item> itemTable;

    public void initialize(){
        Display.Message("Reminder", "Press DELETE on keyboard to delete item!");
        db = new dbHandler();
        itemTable.setOnKeyReleased(e -> {
            KeyCode key = e.getCode();
            if(key == KeyCode.DELETE){
                Item item = itemTable.getSelectionModel().getSelectedItem();
                try{
                    db.deleteItem(item.getID());
                    getData();
                }catch(SQLException ex) {
                   //Do Nothing
                }
            }
        });
        prepareData();
        getData();
    }

    public void prepareData() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }

    public void getData() {
        ObservableList<Item> list = FXCollections.observableArrayList();
        ResultSet results = db.getItem();
        if(results != null){
            try{
                while(results.next()){
                    String id = results.getString("item_id");
                    String name = results.getString("item_name");
                    String category = results.getString("category");
                    int quantity = results.getInt("quantity");
                    int price = results.getInt("price");
                    list.add(new Item(id, name, category, quantity, price));
                }
            }catch(SQLException ex){
                System.out.println("ERROR loading data!");
            }
        }
        itemTable.getItems().setAll(list);
    }
}