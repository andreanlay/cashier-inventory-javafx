package cashier.inventory.project.database;

public class cashierItem{
    private String name;
    private int quantity;
    private int price;
    private int stock;

    public cashierItem(String name, int quantity, int price, int stock) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
    
    public int getStock(){
        return stock;
    }
}
