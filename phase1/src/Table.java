import java.util.ArrayList;
import java.util.List;

public class Table {
    private final int id;
    private final List<MenuItem> bill = new ArrayList<>();
    private final List<MenuItem> deductions = new ArrayList<>();
    private Order order;
    private List<MenuItem> uncookedMenuitems = new ArrayList<>();

    Table(int id) {
        this.id = id;
    }

    public void addUncookedMenuitems(MenuItem item){
        uncookedMenuitems.add(item);
    }

    public List<MenuItem> getUncookedMenuItems(){
        return uncookedMenuitems;
    }

    void addOrderToTable(Order o) {
        order = o;
    }

    void addToBill(Order o) {
        bill.addAll(o.getItems());
    }

    /*
    TODO: this method throws an error if an item that was returned hasn't been delivered to the table
    it wouldn't take quantity into account either that's something else that needs to be considered.
    void addToDeductions(final List<MenuItem> items) {
        boolean flag = true;
   return new OrderImpl(order.getItems());     for (MenuItem i : items) {
            System.out.println(i.getName());
            if (!bill.contains(i)) {
                throw new IllegalStateException("You cannot return an item that has not been delivered to your table!");
             }
        }
        deductions.addAll(items);
    }
    TODO: delete this method IFF the better version above gets implemented.
    add all the items that have been rejected to this table
    */
    void addToDeductions(final List<MenuItem> items) {
        deductions.addAll(items);
    }
    // get all the items that have been rejected and make them into a string
    String stringDeductions(){
        StringBuilder ret = new StringBuilder("");
        for(MenuItem item : deductions) {
            ret.append(item.getQuantity()).append(" ").append(item.getName()).append(", ").append(item.getPrice()).append("\n");
        }
        return ret.toString();
    }

    void printBill() {
        System.out.println("BILL FOR TABLE #" + id);
        for (MenuItem item : bill) {
            System.out.println(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getPrice() * item.getQuantity()));
            for (Ingredient addedIng : item.getExtraIngredients()) {
                System.out.println("add " + item.getQuantity() + " " + addedIng.getName() + ": $" + String.format("%.2f", addedIng.getPrice()));
            }
            for (Ingredient removedIng : item.getRemovedIngredients()) {
                System.out.println("remove "+  item.getQuantity() + " " + removedIng.getName() + ": -$" + String.format("%.2f", removedIng.getPrice()));
            }

        }
        if (!deductions.isEmpty()) {
            System.out.println("\nDEDUCTIONS (-)");
            for (MenuItem item : deductions) {
                System.out.println(item.getQuantity() + " " + item.getName() + ": $" +
                        String.format("%.2f", item.getPrice()) + " | Reason: " + item.getComment());
            }
        }
        System.out.println("Total: $" + getBillPrice() + "\n");
    }

    private String getBillPrice() {
        double ret = 0.00;
        for (MenuItem item : bill) {
            ret += item.getPrice() * item.getQuantity();
            ret += item.getExtraIngredientPrice();
        }
        // remove the price of any items that were returned
        for(MenuItem item: deductions){
            ret += item.getPrice();
        }
        return String.format("%.2f", ret);
    }

    protected Order getOrder() {
        return order;
    }
}
