import menu.*;
import org.junit.Test;
import restaurant.Order;
import restaurant.OrderImpl;

import static org.junit.Assert.*;

public class OrderImplTest {
    private static final Menu menu = new BurgerMenu();

    @Test
    public void testGetItems(){
        // order
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        assertTrue(order.getItems().size() == 3);

        // Check if there is a plain hamburger in the inventory
        MenuItem burger = menu.getMenuItem("Hamburger");
        assertTrue(order.getItems().contains(burger));

        // Create a Hamburger with mods
        MenuItem modBurger = menu.getMenuItem("Hamburger");

        // Ingredients for the modBurger
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        modBurger.addExtraIngredient(ketchup);
        modBurger.removeIngredient(bacon);

        // Check if the modBurger has been added
        assertTrue(order.getItems().contains(modBurger));

        // Check if a Coke has been added.
        assertTrue(order.getItems().contains(menu.getMenuItem("Coke")));
    }

    @Test
    public void testAdd() {
        // order
        Order order = new OrderImpl("1 Hamburger");
        assertTrue(order.getItems().size() == 1);

        // Add a new order
        Order next = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Coke");
        order.add(next);
        assertTrue(order.getItems().size() == 3);

        // Create a Hamburger with mods
        MenuItem modBurger = menu.getMenuItem("Hamburger");

        // Ingredients for the modBurger
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        modBurger.addExtraIngredient(ketchup);
        modBurger.removeIngredient(bacon);

        // Check if the modBurger has been added
        assertTrue(order.getItems().contains(modBurger));

        // Check if a Coke has been added.
        assertTrue(order.getItems().contains(menu.getMenuItem("Coke")));
    }
    // TODO: The contains method compares using equals, not equalsWithExtras, so we cannot test if the removed item
    // is still in the list
    @Test
    public void testRemove() {
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger / +Patty, 1 Coke");
        Order burger = new OrderImpl("1 Hamburger / +Patty");
        order.remove(burger);
        // we are only removing 1 Hamburger
        assertTrue(order.getItems().size() == 2);

        // Create a Hamburger with mods
        MenuItem modBurger = menu.getMenuItem("Hamburger");
        MenuItem doubleBurger = menu.getMenuItem("Hamburger");
        MenuItem Coke = menu.getMenuItem("Coke");

        // Ingredients for the modBurger
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        modBurger.addExtraIngredient(ketchup);
        modBurger.removeIngredient(bacon);

        // Ingredients for the doubleBurger
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        doubleBurger.addExtraIngredient(patty);

        order.getItems();
        // Check if the modBurger is still in the order
        assertTrue(order.getItems().contains(modBurger));
//        assertFalse(order.getItems().contains(doubleBurger));
        assertTrue(order.getItems().contains(Coke));
    }

    @Test
    public void testAddThenRemove(){
        Order order = new OrderImpl("1 Hamburger");
        Order extra = new OrderImpl("1 Hotdog");
        Order burgerOrder = new OrderImpl("1 Hamburger");
        order.add(extra);
        assertTrue(order.getItems().size() == 2);
        MenuItem burger = menu.getMenuItem("Hamburger");
        MenuItem hotdog = menu.getMenuItem("Hotdog");
        order.remove(burgerOrder);
        assertTrue(order.getItems().size() == 1);
        assertFalse(order.getItems().contains(burger));
        assertTrue(order.getItems().contains(hotdog));

    }

    // TODO: this breaks when all tests are run but works when run individually. Why?
    @Test
    public void testGetId() {
       Order first = new OrderImpl("1 Hamburger") ;
       Order second = new OrderImpl("1 Hamburger") ;
       Order third = new OrderImpl("1 Hamburger") ;
       assertTrue(first.getId() == 1);
       assertTrue(second.getId() == 2);
       assertTrue(third.getId() == 3);
    }
    @Test
    public void testReturned() {
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        assertTrue(order.getItems().get(1).getPrice() == 7.99);
        MenuItem item = menu.getMenuItem("Hamburger");
        item.setComment("Undercooked.");
        order.returned(item);
        assertTrue(order.getItems().get(1).getPrice() == 0);
        assertTrue(order.getItems().get(1).getComment().equals("Undercooked."));
    }

    @Test
    public void testGetIngredients() {
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        assertTrue(order.getIngredients().contains(IngredientFactory.makeIngredient("BurgerBun")));
        assertTrue(order.getIngredients().size() == 15);
    }

    @Test
    public void testToString() {
        Order order = new OrderImpl("1 Hamburger / +Bacon -Cheese, 1 Hamburger / +Patty, 1 Coke");
        String expected = "1. Hamburger\nBacon\n2. Hamburger\nPatty\n3. Coke\n";
        assertTrue(expected.equals(order.toString()));
    }
}