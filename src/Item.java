package cashier.inventory.project.database;

public class Item{
    private String id;
    private String name;
    private String category;
    private int price;
    private int quantity;

    public Item(String id, String name, String category, int quantity, int price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public String getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getCategory(){
        return category;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getPrice(){
        return price;
    }
}