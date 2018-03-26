package events;

import menu.Ingredient;
import menu.IngredientFactory;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ShipmentEvent implements Event {
    private static final int QUANTITY_ADDRESS = 0;
    private static final int NAME_ADDRESS = 1;
    private final Map<Ingredient, Integer> shipment = new HashMap<>();

    ShipmentEvent(String shipment) {
        String[] receivedItems = shipment.split(", ");
        for (String ingredient : receivedItems) {
            String[] ingredientWithQuantity = ingredient.split("\\s");
            int quantity = Integer.parseInt(ingredientWithQuantity[ShipmentEvent.QUANTITY_ADDRESS]);
            Ingredient i = IngredientFactory.makeIngredient(ingredientWithQuantity[ShipmentEvent.NAME_ADDRESS]);
            this.shipment.put(i, quantity);
        }
    }

    @Override
    public EventType getType() {
        return EventType.RECEIVEDSHIPMENT;
    }

    @Override
    public void doEvent() {
        Restaurant.getInstance().addToInventory(shipment);
        String s = "";
        for (Map.Entry<Ingredient, Integer> entry : shipment.entrySet()){
            s += entry.getValue().toString() + " " + entry.getKey().getName() + ", ";
        }
        RestaurantLogger.log(Level.INFO, "Shipement of ingredients recieved" + s + " were added to inventory." );
    }
}
