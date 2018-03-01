import java.util.Queue;

class Restaurant {
    private Table[] tables;
    private Inventory inventory = new InventoryImpl();
    private int numOfTables;
    private EventManager eventManager;

    Restaurant(int numOfTables) {
        this.numOfTables = numOfTables;
        this.tables = new Table[numOfTables];
        for (int i = 0; i < numOfTables; i++) {
            tables[i] = new Table(i+1); //add all the tables to the restaurant.
        }
        eventManager = new EventManager();
        handleEvents(eventManager);
    }

    private void printBill(int tableId) {
        tables[tableId - 1].printBill();
    }

    private void addOrderToBill(int tableId, String[] order) {
        Table table = tables[tableId - 1];
        for (String item : order) {
            table.addToBil(item);
        }
    }

    private void addToInventory(Ingredient ingredient, int amount){
        inventory.addToInventory(ingredient, amount);
    }

    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();
        while (!events.isEmpty()) {
            Event e = events.remove();
            if (e.getType().equals("order")) {
                addOrderToBill(e.getTableId(), e.getOrder()); //TODO: this should be put after the item has been delivered, but for now we are just testing.
            } else if (e.getType().equals("bill")) {
                printBill(e.getTableId());
            } else if (e.getType().equals("cookSeen")) {
                System.out.println("Cook has seen your order.");
            } else if (e.getType().equals("cookReady")) {
                System.out.println("Order of blabla is ready for pick up.");
            } else if (e.getType().equals("serverDelivered")) {
                System.out.println("Order of blabla has been delivered");
            } else if (e.getType().equals("serverReturned")) {
                System.out.println("We got a live one here! Order of blabla has been sent back to the kitchen!");
            }
        }
    }
}
