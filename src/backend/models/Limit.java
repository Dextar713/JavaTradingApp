package backend.models;

public class Limit implements Comparable<Limit> {
    private final Order order;
    public Limit next;
    public Limit prev;

    public Order getOrder() {
        return order;
    }

    public Limit(Order order) {
        this.order = new Order(order);
        this.next = this.prev = null;
    }

    public long scalePrice() {
        return Math.round(order.getPrice() * 1000_000);
    }

    @Override
    public int compareTo(Limit limit) {
        if(scalePrice() == limit.scalePrice()) {
            return 0;
        }
        return order.getPrice() > limit.order.getPrice() ? 1 : -1;
    }
}
