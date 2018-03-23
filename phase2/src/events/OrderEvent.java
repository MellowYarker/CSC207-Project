package events;

import restaurant.Order;
import restaurant.OrderImpl;
import restaurant.Restaurant;
import restaurant.Table;


class OrderEvent extends BaseEvent implements Event {
    private final Order order;
    private final Table table;

    OrderEvent(Table table, String orderString) {
        this.table = table;
        order = new OrderImpl(orderString);
        order.setTableId(table.getId());
    }

    @Override
    public EventType getType() {
        return EventType.ORDER;
    }

    @Override
    public void doEvent() {
        Restaurant.getInstance().addPlacedOrder(order);
        notifyObservers();
    }

    Order getOrder() {
        return order;
    }
}

