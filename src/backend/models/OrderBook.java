package backend.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
//import java.util.TreeSet;

public class OrderBook {
    protected final TreeMap<Long, LimitLinkedList> buys;
    protected final TreeMap<Long, LimitLinkedList> sells;
    protected final Map<Long, Limit> limitMap;

    public OrderBook() {
        buys = new TreeMap<>(Collections.reverseOrder());
        sells = new TreeMap<>();
        limitMap = new HashMap<>();
    }

    public void AddOrder(Order order) {
        if (limitMap.containsKey(order.getId())) return;

        Limit newLimit = new Limit(order);
        long priceKey = newLimit.scalePrice();
        Map<Long, LimitLinkedList> book = order.isBuy() ? buys : sells;

        LimitLinkedList level = book.get(priceKey);
        if (level == null) {
            level = new LimitLinkedList(newLimit);
            book.put(priceKey, level);
        } else {
            level.insert(newLimit);
        }
        limitMap.put(order.getId(), newLimit);
    }

    public void CancelOrder(Order order) {
        if (!limitMap.containsKey(order.getId())) return;
        Limit limit = limitMap.get(order.getId());
        long priceKey = limit.scalePrice();
        Map<Long, LimitLinkedList> book = order.isBuy() ? buys : sells;
        LimitLinkedList level = book.get(priceKey);
        if(level.size()>1) {
            level.delete(limit);
        } else {
            book.remove(priceKey);
        }
        limitMap.remove(order.getId());
    }

    public void ModifyOrder(Order order, double newPrice, double newAmount) {
        CancelOrder(order);
        Order modifiedOrder = new Order(newPrice, newAmount,
                order.isBuy(), order.getAssetId(), order.getUserId());
        AddOrder(order);
    }

    public LimitLinkedList BestBuy() {
        return buys.firstEntry().getValue();
    }

    public LimitLinkedList BestSell() {
        return sells.firstEntry().getValue();
    }

    public double getSpread() {
        return 0;
    }
}
