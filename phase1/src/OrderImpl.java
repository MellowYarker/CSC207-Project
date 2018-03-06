import java.util.ArrayList;
import java.util.List;


public class OrderImpl implements Order {
    private List<MenuItem> orderItems;
    private boolean isReceivedByCook, isReadyForPickup, isDelivered;

    OrderImpl(List<MenuItem> orderItems){ this.orderItems = orderItems; }

    @Override
    public List<MenuItem> getItems() {
        return orderItems;
    }

    @Override
    public void receivedByCook() {
        isReceivedByCook = true;
    }

    @Override
    public void readyForPickup() {
        isReadyForPickup = true;
    }

    @Override
    public void delivered() {
        isDelivered = true;
    }

    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredientList= new ArrayList<>();
        for (MenuItem item : orderItems){ ingredientList.addAll(item.getIngredients()); }
        return ingredientList;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(MenuItem item: orderItems){ sb.append(i++).append(". ").append(item.getName()).append("\n"); }
        return sb.toString();
    }
}
