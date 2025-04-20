package backend.models;

public class Order {
    private static long counter = 0;
    private double price;
    private double quantity;
    private final boolean isBuy;
    private final int assetId;
    private final int userId;
    private final long id;

    public Order(double price, double quantity, boolean isBuy, int assetId, int userId) {
        this.price = price;
        this.quantity = quantity;
        this.isBuy = isBuy;
        this.assetId = assetId;
        this.userId = userId;
        this.id = counter++;
    }

    public Order(Order order) {
        this.price = order.price;
        this.quantity = order.quantity;
        this.isBuy = order.isBuy;
        this.assetId = order.assetId;
        this.userId = order.userId;
        this.id = order.id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public int getAssetId() {
        return assetId;
    }

    public int getUserId() {
        return userId;
    }

    public long getId() {
        return id;
    }

    public boolean isMarketOrder() {
        return price < 0;
    }
}
