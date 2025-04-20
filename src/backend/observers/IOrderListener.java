package backend.observers;

import backend.models.Order;

public interface IOrderListener {
    void onOrderFilled(Order order, double filledQuantity, double fillPrice);
}
