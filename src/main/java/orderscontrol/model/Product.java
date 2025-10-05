package orderscontrol.model;

public class Product {
    private int id;
    private final String description;
    private final float price;
    private int quantity;
    private String category;


    // Конструктор для создания/обновления записи в БД из кода
    public Product(int id, String description, float price, int quantity, String category){
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Конструктор для создания экземпляра класса из запроса
    public Product(String description, float price, int quantity, String category){
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "| " + this.id + " | " + this.description + " | " + this.price + " | " + this.quantity + " | " + this.category + " |";
    }
}
