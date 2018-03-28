package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import menu.Ingredient;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class ManagerView extends Observable implements Initializable {
    @FXML StackPane rootStackPane;

    //Lists
    @FXML JFXListView<Label> placedList;
    @FXML JFXListView<Label> receivedList;
    @FXML JFXListView<Label> firedList;
    @FXML JFXListView<Label> readyList;
    @FXML JFXListView<Label> deliveredList;

    //Labels
    @FXML Label incomeLabel;
    @FXML Label tipLabel;

    //Buttons
    @FXML JFXButton shipmentButton;
    @FXML JFXButton emailButton;

    JFXDialog shipmentDialog;
    JFXDialog emailDialog;
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
        ShipmentPopup shipmentPopup = new ShipmentPopup();
        shipmentDialog = shipmentPopup.loadShipmentDetailsPopup();
        EmailPopup emailPopup = new EmailPopup();
        emailDialog = emailPopup.emailPopup;
        shipmentButton.setOnAction(e -> shipmentPopup.showPopup());
        emailButton.setOnAction(e -> emailPopup.showPopup());
    }

    private void loadOrdersLists(){
        loadOrderList(Restaurant.getInstance().getPlacedOrders(), placedList);
        loadOrderList(Restaurant.getInstance().getReceivedOrders(), receivedList);
        loadOrderList(Restaurant.getInstance().getCookingOrders(), firedList);
        loadOrderList(Restaurant.getInstance().getReadyOrders(), readyList);
        loadOrderList(Restaurant.getInstance().getDeliveredOrders(), deliveredList);
    }

    private void loadOrderList(List<Order> orderList, JFXListView<Label> listView){
        ObservableList<Label> listViewItems = FXCollections.observableArrayList();
        for(Order o: orderList){
            listViewItems.add(generateOrderHeader(o));
            for(MenuItem i: o.getItems()){
                listViewItems.add(generateItemListEntry(i));
            }
        }
        if (listViewItems.isEmpty()) listViewItems.add(new Label("No orders found."));
        listView.setItems(listViewItems);
    }

    private Label generateOrderHeader(Order o){
        Label header = new Label("Order " + o.getId());
        header.setBackground(Backgrounds.LIGHT_GREY_BACKGROUND);
        return header;
    }

    private Label generateItemListEntry(MenuItem i){
        StringBuilder labeltext = new StringBuilder(i.getQuantity() + " " + i.getName());
        if(!i.getExtraIngredients().isEmpty()){
            labeltext.append(" with extra ");
            for(Ingredient extra : i.getExtraIngredients()){
                labeltext.append(extra.getName()).append(", ");
            }
            labeltext.delete(labeltext.length()-2, labeltext.length());
        }
        if(!i.getRemovedIngredients().isEmpty()){
            labeltext.append(" with no ");
            for(Ingredient extra : i.getRemovedIngredients()){
                labeltext.append(extra.getName()).append(", ");
            }
            labeltext.delete(labeltext.length()-2, labeltext.length());
        }
        Label entry = new Label(labeltext.toString());
        return entry;
    }

    private void loadIncomeLabels(){
        incomeLabel.setText("Today's income: $" + String.format("%.2f", Restaurant.getInstance().getDailyIncomeTotal()));
        tipLabel.setText("Today's tip total: $" + String.format("%.2f", Restaurant.getInstance().getTipTotal()));
    }

    void refresh(){
        loadOrdersLists();
        loadIncomeLabels();
    }

    private class ShipmentPopup{
        JFXDialog shipmentPopup;
        private Map<Ingredient, Integer> receivedItems = new HashMap<>();

        private ShipmentPopup(){
           shipmentPopup = loadShipmentDetailsPopup();
        }
        private void showPopup(){
            shipmentPopup.show();
        }
        private JFXDialog loadShipmentDetailsPopup(){
            //HEADER
            JFXDialogLayout content = new JFXDialogLayout();
            Label shipmentDialogHeader = new Label("Receive a shipment");
            content.setHeading(shipmentDialogHeader);

            //BUTTONS
            JFXButton cancelButton = new JFXButton("Cancel");
            JFXButton okButton = new JFXButton("OK");
            content.setActions(cancelButton, okButton);

            //BODY
            JFXListView<HBox> shipmentItems = buildList();
            content.setBody(shipmentItems);

            JFXDialog popup = new JFXDialog(rootStackPane, content, JFXDialog.DialogTransition.CENTER);
            cancelButton.setOnAction(e -> shipmentPopup.close());
            okButton.setOnAction(e -> {
                receiveShipment();
                setChanged();
                notifyObservers();
                shipmentPopup.close();
            });
            return popup;
        }
        private JFXListView<HBox> buildList(){
            ObservableList<HBox> listItems = FXCollections.observableArrayList();
            for(Ingredient i: Restaurant.getInstance().getMenu().getAllIngredients()){
                listItems.add(buildListEntry(i));
            }
            JFXListView<HBox> listView = new JFXListView<>();
            listView.setItems(listItems);
            return listView;
        }
        private HBox buildListEntry(Ingredient ingredient) {
            HBox entryBox = new HBox();
            Region filler = new Region();
            HBox.setHgrow(filler, Priority.ALWAYS);
            TextField quantityField = new TextField("0");
            quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d+")) { //if the input digit is not a digit
                    quantityField.setText(newValue.replaceAll("\\D+", ""));
                } else { // if it is a digit
                    receivedItems.put(ingredient, Integer.valueOf(newValue));
                }
            });
            Label ingredientNameLabel = new Label(ingredient.getName());

            entryBox.getChildren().addAll(quantityField, filler, ingredientNameLabel);
            return entryBox;
        }
        private void receiveShipment() {
            String shipmentEventString = buildShipmentEventString();
            Restaurant.getInstance().newEvent(shipmentEventString);
        }
        private String buildShipmentEventString(){
            StringBuilder sb = new StringBuilder("receivedShipment | ");
            for (Map.Entry<Ingredient, Integer> entry : receivedItems.entrySet()){
                if(entry.getValue() > 0){
                    sb.append(entry.getValue()).append(" ");
                    sb.append(entry.getKey().getName()).append(", ");
                }
            }
            sb.delete(sb.length()-2, sb.length()); //delete trailing comma
            return sb.toString();
        }
    }
    private class EmailPopup{
        JFXDialog emailPopup;
        TextArea emailTextArea = new TextArea();
        String emailText;

        private EmailPopup(){
            emailPopup = loadEmailPopup();
        }

        private void showPopup(){
            updateTextArea();
            emailPopup.show();
        }

        private void updateTextArea(){
            try(BufferedReader br = new BufferedReader(new FileReader("requests.txt"))) {
                StringBuilder sb = new StringBuilder("No restocking necessary!");
                String line = br.readLine();
                if (line != null && line.length() != 0){
                    sb.delete(0, sb.length());
                    sb.append("Dear suppliers, \nPlease be advised that:");
                    do{
                        sb.append("\n"+line);
                        line = br.readLine();
                    } while(br.ready());
                }
                emailTextArea.setText(sb.toString());
                //emailText = sb.toString();
            } catch (IOException ex){
                RestaurantLogger.log(Level.WARNING, ex.toString());
            }
        }

        private JFXDialog loadEmailPopup(){
            //HEADER
            JFXDialogLayout content = new JFXDialogLayout();
            Label emailDialogHeader = new Label("Email to suppliers");
            content.setHeading(emailDialogHeader);

            //Button
            JFXButton okButton = new JFXButton("OK");
            content.setActions(okButton);

            //BODY
            content.setBody(emailTextArea);


            JFXDialog popup = new JFXDialog(rootStackPane, content, JFXDialog.DialogTransition.CENTER, true);
            okButton.setOnAction(e -> popup.close());
            return popup;
        }
    }
}
