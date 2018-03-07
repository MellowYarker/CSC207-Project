import java.util.ArrayList;
import java.util.List;

class Event {
    private final EventType type;
    private final int tableId;
    private Order order;

    Event(EventType type, int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    Event(EventType type, int tableId, String order) {
        this.type = type;
        this.tableId = tableId;
        orderConstructorHelper(order);
    }

    private void orderConstructorHelper(String strings) {
        List<MenuItem> orderItems = new ArrayList<>();
        for (String item : strings.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItemSplitString = item.split("\\s", 2);
            int quantity = Integer.parseInt(orderItemSplitString[0]);
            String name = orderItemSplitString[1];
            orderItems.add(new MenuItemImpl(name, quantity));
        }
        order = new OrderImpl(orderItems);
    }

    EventType getType() {
        return type;
    }

    Order getOrder() {
        return order;
    }

    int getTableId() {
        return tableId;
    }
}
