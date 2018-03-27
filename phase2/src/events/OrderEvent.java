package events;

import menu.MenuItem;
import restaurant.*;

import java.util.logging.Level;

class OrderEvent implements Event {
    private final Order order;
    private final Table table;

    OrderEvent(Table table, int seat, String orderString) {
        this.table = table;
        order = OrderFactory.makeOrder(orderString, table.getId(), seat);
    }

    @Override
    public EventType getType() {
        return EventType.ORDER;
    }

    @Override
    public void doEvent() {
        Restaurant.getInstance().addPlacedOrder(order);

        StringBuilder s = new StringBuilder();
        s.append("Order number ");
        s.append(Integer.toString(order.getId()));
        s.append(": Table ");
        s.append(Integer.toString(order.getTableId()));
        s.append(" ordered ");
        for (MenuItem i: order.getItems()){
            if (order.getItems().indexOf(i) == (order.getItems().size() - 1) && order.getItems().size() > 1){
                s.append("and ");
                s.append(Integer.toString(i.getQuantity()));
                s.append(" ");
                s.append(i.getName());
                s.append("(s).");
            } else {
                s.append(Integer.toString(i.getQuantity()));
                s.append(" ");
                s.append(i.getName());
                if (order.getItems().indexOf(i) == (order.getItems().size() - 1)) {
                    s.append("(s).");
                } else {s.append("(s), ");}
            }
        }
        RestaurantLogger.log(Level.INFO, "" + s);
    }

    Order getOrder() {
        return order;
    }
}

