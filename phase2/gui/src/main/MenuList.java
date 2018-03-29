package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Background;
import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuList implements Initializable {
    private final List<JFXButton> selectedItems = new ArrayList<>();
    private boolean toggleShowIngredients;
    private boolean toggleShowPrice;
    @FXML private JFXListView<JFXButton> menuListView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addMenuLabels();
    }

    ObservableList<JFXButton> getSelectedItems() {
        return menuListView.getSelectionModel().getSelectedItems();
    }

    void clearSelected(){
        /*
        List<JFXButton> buttonsToRemove = new ArrayList<>();
        for(JFXButton selectedButton : selectedItems){
            JFXListCell<JFXButton> selectedCell = (JFXListCell<JFXButton>) selectedButton.getParent();
            selectedCell.setBackground(Background.EMPTY);
            buttonsToRemove.add(selectedButton);
        }
        selectedItems.removeAll(buttonsToRemove);
        addMenuLabels();
        */
        menuListView.getSelectionModel().clearSelection();
    }

    private void addMenuLabels() {
        ObservableList<JFXButton> listItems = FXCollections.observableArrayList();
        menuListView.setItems(listItems);
        Menu menu = Restaurant.getInstance().getMenu();
        for (MenuItem i : menu.getMenu()) {
            JFXButton itemButton = new JFXButton(i.getName());
            if(toggleShowPrice) itemButton.setText(i.getName()+"       $"+(String.format("%.2f", i.getPrice())));
            //itemButton.setOnAction(e -> selectItems(itemButton));
            listItems.add(itemButton);

            if(toggleShowIngredients){
                for (Ingredient ingredient : i.getIngredients()) {
                    JFXButton ingredientButton = new JFXButton("     > " + ingredient.getName());
                    if(toggleShowPrice) ingredientButton.setText("     > " + ingredient.getName()+"       $"+(String.format("%.2f", i.getPrice())));
                    listItems.add(ingredientButton);
                }
            }
        }
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        menuListView.setOnMouseClicked(e-> {
            ObservableList<JFXButton> selectedItems = menuListView.getSelectionModel().getSelectedItems();
            for(JFXButton button: selectedItems){
                selectItems(button);
            }
        });
    }

    @FXML private void toggleShowIngredients() {
        toggleShowIngredients = !toggleShowIngredients;
        addMenuLabels();
    }

    @FXML private void toggleShowPrice(){
        toggleShowPrice = !toggleShowPrice;
        addMenuLabels();
    }

    private void selectItems(JFXButton itemButton) {
        try{
            JFXListCell<JFXButton> selectedCell = (JFXListCell<JFXButton>) itemButton.getParent();
            if(selectedItems.contains(itemButton) && selectedCell.getBackground().equals(Backgrounds.SELECTED_BACKGROUND)){
                selectedCell.setBackground(Background.EMPTY);
            } else {
                selectedCell.setBackground(Backgrounds.SELECTED_BACKGROUND);
            }
        } catch (NullPointerException ex){} //this will catch NPEs that are made when you deselect an item
    }
}
