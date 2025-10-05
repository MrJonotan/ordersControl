package orderscontrol.model;

public class OrderStatus {
    private int id;
    private String statusName;

    public OrderStatus (int id, String statusName){
        this.id = id;
        this.statusName = statusName;
    }

    public OrderStatus (String statusName){
        this.statusName = statusName;
    }

    public int getId() { return id; }

    public String getStatusName() { return statusName; }

    public void setId(int id) { this.id = id; }

    public void setStatusName(String statusName) { this.statusName = statusName; }

    @Override
    public String toString() {
        return "| " + statusName + "|";
    }
}
