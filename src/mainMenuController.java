package cashier.inventory.project;

import cashier.inventory.project.database.cashierItem;
import cashier.inventory.project.database.dbHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class mainMenuController {
    @FXML private Button logoutBtn;
    @FXML private TableView<cashierItem> itemList;
    @FXML private TableColumn <cashierItem, String> itemColumn;
    @FXML private TableColumn <cashierItem, Integer> quantityColumn;
    @FXML private TableColumn <cashierItem, Integer> subtotalColumn;
    @FXML private Label totalPriceLbl;
    @FXML private Label dateLbl;
    @FXML private Label timeLbl;
    @FXML private ChoiceBox<String> categoryCB;
    @FXML private FlowPane itemPane;

    private static ObservableList <cashierItem> menu;
    private dbHandler db;

    public void initialize(){
        db = new dbHandler();
        loadChoiceBox();
        initData();
        itemList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        menu = FXCollections.observableArrayList();
        categoryCB.getSelectionModel().selectedItemProperty().addListener((obser, oldData, newData) -> {
            loadChoiceBoxMenu();
        });
        clock();
    }

    private void initData(){
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }

    public void inventory(ActionEvent actionEvent) throws Exception{
        Window parent = logoutBtn.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/cashier.inventory.project/inventory.fxml"));
        stage.setTitle("Inventory");
        stage.setScene(new Scene(root));
        parent.hide();
        stage.setResizable(false);
        stage.show();
    }

    public void logout(ActionEvent actionEvent) throws Exception{
        Window parent = logoutBtn.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/cashier.inventory.project/start.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        parent.hide();
        stage.setResizable(false);
        stage.show();
    }

    public void checkout(ActionEvent actionEvent){
        Optional<String> res = Display.askInput("Checkout", "Amount of money :");
        int custMoney = Integer.parseInt(res.get());
        if(calculate() > custMoney){
            Display.Message("Checkout", "Not enough money!");
            return;
        }
        for(cashierItem item : menu){
            if(item.getQuantity() == item.getStock()){
                db.deleteItemByName(item.getName());
            }else{
                db.updateStock(item.getName(), item.getStock() - item.getQuantity());
            }
        }
        Display.Message("Checkout", "Change Money : Rp" + (custMoney- calculate())  + "\nRemember to the give receipt to customer.");
        itemList.getItems().clear();
    }

    public void delete(ActionEvent actionEvent){
        ObservableList<cashierItem> selectedItems = itemList.getSelectionModel().getSelectedItems();
        menu.removeAll(selectedItems);
        totalPriceLbl.setText("Rp" + calculate());
        itemList.getItems().clear();
        itemList.getItems().setAll(menu);
    }

    private void loadChoiceBox(){
        categoryCB.getItems().clear();
        ResultSet res = db.getCategory();
        if(res != null){
            try{
                while (res.next()) {
                    String category = res.getString("category");
                    categoryCB.getItems().add(category);
                }
            }catch(SQLException e){
                System.out.println("ERROR Getting category data!");
            }
        }
    }

    private void loadChoiceBoxMenu(){
        ObservableList <String> list = FXCollections.observableArrayList();
        String ctr = categoryCB.getValue();
        ResultSet data = db.getItemByCategory(ctr);
        if(data != null){
            try{
                while(data.next()){
                    String name = data.getString("item_name");
                    list.add(name);
                }
            }catch(SQLException e){
                //Do nothing
            }
        }
        itemPane.getChildren().clear();
        for(String name : list){
            Button btn = new Button(name);
            btn.setPrefSize(100, 50);
            itemPane.getChildren().addAll(btn);
            itemPane.setHgap(25);
            itemPane.setVgap(15);
            btn.setOnAction(e -> {
                String itemName = btn.getText();
                boolean found = false;
                for(cashierItem item : menu){
                    if(item.getName().equals(itemName) && item.getQuantity() + 1 > item.getStock()){
                        Display.Message("Information", "Out of Stock!");
                        return;
                    }
                }
                if(menu.size() != 0){
                    for(cashierItem item : menu){
                        if(item.getName().equals(itemName)){
                            item.setQuantity(item.getQuantity() + 1);
                            item.setPrice(getItem(itemName).getPrice() * item.getQuantity());
                            found = true;
                        }
                    }
                }
                if(!found){
                    menu.add(getItem(itemName));
                }
                totalPriceLbl.setText("Rp" + calculate());
                itemList.getItems().clear();
                itemList.getItems().addAll(menu);
            });
        }
    }

    private cashierItem getItem(String name){
        ResultSet res = db.getItemsByName(name);
        try{
            while(res.next()){
                return new cashierItem(name, 1, res.getInt("price"), res.getInt("quantity"));
            }
        }catch(SQLException e){
            //Do nothing
        }
        return null;
    }

    private int getStock(String name){
        ResultSet res = db.getItemsByName(name);
        try{
            return res.getInt("quantity");
        }catch(SQLException e){
            //Do nothing
        }
        return -1;
    }

    private int calculate(){
        int total = 0;
        for(cashierItem item : menu){
            total += item.getPrice();
        }
        return total;
    }

    private void clock(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e-> {
            dateLbl.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("E, MMM dd yyyy")));
            timeLbl.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " GMT+7");
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}