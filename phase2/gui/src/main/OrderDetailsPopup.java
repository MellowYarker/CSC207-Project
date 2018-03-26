package main;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import menu.Ingredient;
import restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

class OrderDetailsPopup {
    //private static final Background SELECTED_BACKGROUND = new Background(new BackgroundFill(Color.web("#29B6F6"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background RED_BACKGROUND = new Background(new BackgroundFill(Color.web("#EF5350"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background GREEN_BACKGROUND = new Background(new BackgroundFill(Color.web("#9CCC65"), CornerRadii.EMPTY, Insets.EMPTY));
    private StackPane parent;
    private int tableId;
    private int seatId;
    private List<menu.MenuItem> orderItems = new ArrayList<>();
    private menu.MenuItem currentlySelectedItem;

    //there is a cuurrent selected item. that is the one with the loaded  mod lists.
    //set the items quantity every time you change the dropdown menu

    //every time an ingredient is selected in the left list, add it to the item
    //every time an ingredient is selected in the right list, remove it from the item

    OrderDetailsPopup(StackPane parent, List<JFXButton> selectedItemButtons, int tableId, int seatId){
        this.parent = parent;
        this.tableId = tableId;
        this.seatId = seatId;
        loadAddDialog(selectedItemButtons);
        for(JFXButton itemButton : selectedItemButtons){
            menu.MenuItem item = Restaurant.getInstance().getMenu().getMenuItem(itemButton.getText());
            orderItems.add(item);
           // allSelectedExtras.add(item.g);
        }
    }

    private void changeItemQuantity(MenuButton dropdownMenu, int quantity, String itemName){
        dropdownMenu.setText(String.valueOf(quantity));
        menu.MenuItem updateItem = Restaurant.getInstance().getMenu().getMenuItem(itemName);
        orderItems.get(orderItems.indexOf(updateItem)).setQuantity(quantity);
    }

    private HBox makeItemHBox(JFXButton button, JFXListView<Hyperlink> extras, JFXListView<Hyperlink> removed){
        HBox itemBox = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        MenuButton quantitySelector = new MenuButton("Select Quantity");
        for (int i = 1; i < 11; i++) {
            final int j = i;
            MenuItem quantity = new MenuItem(String.valueOf(j));
            quantity.setOnAction(e -> {
                changeItemQuantity(quantitySelector, j, button.getText());
                loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed);
            }); //will need more here
            quantitySelector.getItems().add(quantity);
        }
        itemBox.getChildren().addAll(quantitySelector, filler, new Label(button.getText()));

        itemBox.setOnMouseClicked(e -> loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed));
        return itemBox;
    }
    private void selectEvent(Hyperlink clickedHyperlink, boolean isExtra){
        JFXListCell<Hyperlink> selectedCell = (JFXListCell<Hyperlink>) clickedHyperlink.getParent();
        if(isExtra) {
            if (selectedCell.getBackground().equals(GREEN_BACKGROUND)) {
                selectedCell.setBackground(Background.EMPTY);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            } else {
                selectedCell.setBackground(GREEN_BACKGROUND);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            }
        } else {
            if (selectedCell.getBackground().equals(RED_BACKGROUND)){
                selectedCell.setBackground(Background.EMPTY);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            } else {
                selectedCell.setBackground(RED_BACKGROUND);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            }
        }
    }
    private void loadModLists(menu.MenuItem item, JFXListView<Hyperlink> extras, JFXListView<Hyperlink> removed){
        currentlySelectedItem = item;

        List<Ingredient> allIngredients = Restaurant.getInstance().getMenu().getAllIngredients();
        allIngredients.removeAll(item.getIngredients());
        ObservableList<Hyperlink> addableIngredients = FXCollections.observableArrayList();
        for(Ingredient i : allIngredients){
            Hyperlink addIngredient = new Hyperlink(i.getName());
            addIngredient.setOnMousePressed(e -> selectEvent(addIngredient, true));
            addableIngredients.add(addIngredient);
        }
        extras.setItems(addableIngredients);

        ObservableList<Hyperlink> removeableIngredients = FXCollections.observableArrayList();
        for (Ingredient i : item.getIngredients()){
            Hyperlink removeIngredient = new Hyperlink(i.getName());
            removeIngredient.setOnMousePressed(e -> selectEvent(removeIngredient, false));
            removeableIngredients.add(removeIngredient);
        }
        removed.setPrefHeight(extras.getHeight());
        removed.setItems(removeableIngredients);
    }
    private void loadAddDialog(List<JFXButton> selectedItemButtons){
        //HEADER
        JFXDialogLayout content = new JFXDialogLayout();
        Label orderDetailsDialogHeader = new Label("Order Details");

        //PARENT AND LIST COMPONENTS
        VBox dialogRoot = new VBox(5);
        dialogRoot.setMinHeight(700);
        JFXListView<Hyperlink> extraIngredientsListView = new JFXListView<>();
        extraIngredientsListView.setMinHeight(300);
        JFXListView<Hyperlink> removedIngredientsListView = new JFXListView<>();
        removedIngredientsListView.setMinHeight(300);

        //LIST OF SELECTED ITEMS AND THEIR QUANTITIES
        VBox top = new VBox();
        JFXListView<HBox> orderItemListView = new JFXListView<>();
        for (JFXButton button : selectedItemButtons){
            orderItemListView.getItems().add(makeItemHBox(button, extraIngredientsListView, removedIngredientsListView));
        }
        top.getChildren().add(orderItemListView);

        //MOD SELECTION
        HBox bottom = new HBox();
        bottom.setMinSize(367, 355);
        //EXTRAS
        VBox left = new VBox();
        left.setMinWidth(185);
        left.getChildren().add(extraIngredientsListView);

        //INGREDIENTS TO REMOVE
        VBox right = new VBox();
        right.setMinWidth(185);
        right.getChildren().add(removedIngredientsListView);

        //JOIN ALL COMPONENTS
        bottom.getChildren().addAll(left, right);
        dialogRoot.getChildren().addAll(top, bottom);
        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton confirmButton = new JFXButton("Confirm Order");
        content.setHeading(orderDetailsDialogHeader);
        content.setBody(dialogRoot);
        content.setActions(cancelButton, confirmButton);

        //BUILD THE DIALOG AND SET THE ACTIONS
        JFXDialog dialog = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
        cancelButton.setOnAction(e -> dialog.close());
        confirmButton.setOnAction(e -> {
            //newOrder
            Restaurant.getInstance().newEvent(buildOrderString());
            dialog.close();
        });
        dialog.show();
    }

    private String buildOrderString(){
        StringBuilder sb = new StringBuilder("order | " + tableId + " > " + seatId + " | ");
        for(menu.MenuItem item : orderItems){
            sb.append(item.getQuantity() + " " + item.getName());
            if(!item.getExtraIngredients().isEmpty() || !item.getRemovedIngredients().isEmpty()){
                sb.append(" / ");
                if(!item.getExtraIngredients().isEmpty()){
                    for(Ingredient extra : item.getExtraIngredients()){
                        sb.append("+" + extra.getName() + " "); //check that trailing space
                    }
                    sb.deleteCharAt(sb.lastIndexOf(" "));
                }
                if(!item.getRemovedIngredients().isEmpty()){
                    for(Ingredient remove : item.getRemovedIngredients()){
                        sb.append("-" + remove.getName() + " "); //check that trailing space
                    }
                    sb.deleteCharAt(sb.lastIndexOf(" "));
                }
            }
            sb.append(", ");
        }
        sb.delete(sb.length()-2, sb.length());
        String ret = sb.toString();
        System.out.println(ret);
        return ret;
    }
}
