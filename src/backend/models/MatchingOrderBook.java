package backend.models;

import backend.observers.IOrderListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import java.util.TreeMap;

public class MatchingOrderBook extends OrderBook {
    private final List<IOrderListener> listeners = new ArrayList<>();

    public void addListener(IOrderListener listener) {
        listeners.add(listener);
    }

    public void notifyOrderFilled(Order order, double filledQuantity, double fillPrice) {
        for (IOrderListener listener : listeners) {
            listener.onOrderFilled(order, filledQuantity, fillPrice);
        }
    }

    public MatchingOrderBook() {
        super();
    }

    public void HandleOrder(Order order) {
        if(order.isMarketOrder()) {
            MatchMarketOrder(order);
        } else {
            AddOrder(order);
        }
    }

    public void MatchMarketOrder(Order order) {
        double quantityInitial = order.getQuantity();
        double quantityToFill = quantityInitial;
        double totalValueFilled = 0;
        boolean isBuy = order.isBuy();

        Map<Long, LimitLinkedList> bookSide = isBuy ? sells : buys;

        while (quantityToFill > 0 && !bookSide.isEmpty()) {
            Map.Entry<Long, LimitLinkedList> bestEntry = bookSide.entrySet().iterator().next();
            LimitLinkedList limitList = bestEntry.getValue();
            Limit current = limitList.getHead();

            while (current != null && quantityToFill > 0) {
                Order limitOrder = current.getOrder();
                double availableQty = limitOrder.getQuantity();

                double matchedQty = Math.min(availableQty, quantityToFill);

                System.out.println("Matched " + matchedQty + " units at price " + limitOrder.getPrice());

                limitOrder.setQuantity(availableQty - matchedQty);
                quantityToFill -= matchedQty;
                totalValueFilled += matchedQty * limitOrder.getPrice();
                notifyOrderFilled(limitOrder, matchedQty, limitOrder.getPrice());

                if (limitOrder.getQuantity() == 0) {
                    Limit toDelete = current;
                    current = current.next;
                    limitList.delete(toDelete);
                } else {
                    current = current.next;
                }
            }

            if (limitList.getHead() == null) {
                bookSide.remove(bestEntry.getKey());
            }
        }
        double avgFillPrice = totalValueFilled / (quantityInitial - quantityToFill);
//        if (quantityToFill > 0) {
//            System.out.println("Market order not fully filled. Remaining quantity: " + quantityToFill);
//        } else {
//            System.out.println("Market order fully filled.");
//        }
        notifyOrderFilled(order, quantityInitial - quantityToFill, avgFillPrice);
    }

}
