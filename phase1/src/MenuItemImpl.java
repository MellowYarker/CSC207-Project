import java.util.ArrayList;
import java.util.List;

class MenuItemImpl implements MenuItem {
    private final String name;
    private double price;
    private List<Ingredient> ingredients;
    private int quantity;
    private List<FoodMod> mods;
    private double modPrice = 0.0;

    public MenuItemImpl(final String name, final int quantity) {

        this.name = name;
        this.quantity = quantity;
        //TODO: get the ingredients from the menu
        //TODO: get the prices from the menu
    }

    public MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients) {
        this(name, price, ingredients, 1);
    }

    public MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients, final int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = ingredients;
        mods = new ArrayList<>();
    }

    //MenuItem fromString();
    public static MenuItem fromRestaurantMenu(final MenuItem restaurantItem, final int quantity) {
        return new MenuItemImpl(restaurantItem.getName(), restaurantItem.getPrice(), restaurantItem.getIngredients(), quantity);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getModPrice() {
        return modPrice;
    }

    @Override
    public List<FoodMod> getMods() {
        return mods;
    }

    @Override
    public void increaseModPrice(final FoodMod mod) {
        modPrice += mod.getPrice();
    }

    @Override
    public void decreaseModPrice(final FoodMod mod) {
        modPrice -= mod.getPrice();
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String printIngredients() {
        // print the ingredients of this MenuItem
        final StringBuilder result = new StringBuilder("");
        for (final Ingredient ingredient : ingredients) {
            result.append("| " + ingredient.getName() + " ");
        }
        return result.toString();
    }

    @Override
    public boolean equals(final MenuItem item) {
        return name.equals(item.getName());
    }
}
