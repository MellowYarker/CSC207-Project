import java.util.List;

class MenuItemImpl implements MenuItem {
    private String name;
    private double price;
    private List<Ingredient> ingredients;
    private int quantity;

    public MenuItemImpl(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
        //get the ingredients from the menu
        //get the prices from the menu
    }

    public MenuItemImpl(String name, double price, List<Ingredient> ingredients){ //TODO: add mods support here.
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public String getName(){return name;}

    public double getPrice(){return price;}

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
