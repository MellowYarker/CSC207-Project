package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class CookOrderView extends Observable implements Initializable {
    @FXML private JFXMasonryPane orderMasonryPane;
    @FXML private JFXButton receivedButton;
    @FXML private JFXButton firedButton;
    @FXML private JFXButton readyButton;

    private List<OrderBox> orderBoxList = new ArrayList<>();
    private VBox selectedOrderBox;
    private Background storedBoxBackground;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receivedButton.setOnAction(e -> receiveOrder());
        firedButton.setOnAction(e -> fireOrder());
        readyButton.setOnAction(e -> readyOrder());
        refresh();
    }

    private class OrderBox {
        private VBox box;
        private Order order;

        OrderBox(Order order, String flag){
            this.order = order;
            box = new VBox(10);
            box.setOnMouseClicked(e -> selectBox(box));
            Text orderHeader = new Text("Order: " + order.getId() + "\nTable: " + order.getTableId());

            box.getChildren().add(orderHeader);
            for (MenuItem item : order.getItems()){
                box.getChildren().add(makeItemText(item));
            }

            switch (flag) {
                case "placed":
                    box.setBackground(Backgrounds.GREY_BACKGROUND);
                    break;
                case "received":
                    box.setBackground(Backgrounds.RED_BACKGROUND);
                    break;
                case "cooking":
                    box.setBackground(Backgrounds.YELLOW_BACKGROUND);
                    break;
                case "ready":
                    box.setBackground(Backgrounds.GREEN_BACKGROUND);
                    break;
                default:
                    box.setBackground(Backgrounds.GREY_BACKGROUND);
                    break;
            }
        }
        private Text makeItemText(MenuItem item) {
            Text itemLabel = new Text(item.getQuantity() + " " + item.getName());

            return itemLabel;
        }
        Order getOrder(){ return order; }
        VBox getVBox() {
            return box; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderBox orderBox = (OrderBox) o;
            return Objects.equals(box, orderBox.box) ||
                    Objects.equals(order, orderBox.order);
        }

        @Override
        public int hashCode() {
            return Objects.hash(box, order);
        }
    }

    void refresh() {
        orderMasonryPane.getChildren().clear();
        orderBoxList.clear();

        List<VBox> orderList = new ArrayList<>();
        for (Order placedOrder : Restaurant.getInstance().getPlacedOrders()) {
            orderList.add(makeOrderBox(placedOrder, "placed"));
        }
        for (Order receivedOrder : Restaurant.getInstance().getReceivedOrders()) {
            orderList.add(makeOrderBox(receivedOrder, "received"));
        }
        for (Order cookingOrder : Restaurant.getInstance().getCookingOrders()) {
           orderList.add(makeOrderBox(cookingOrder, "cooking"));
        }
        for (Order readyOrder : Restaurant.getInstance().getReadyOrders()) {
            orderList.add(makeOrderBox(readyOrder, "ready"));
        }
        //In order for the vboxes' size to be calculated, they need to be put into a scene and then called applyCss and layout.
        //if not, the boxes will not be rendered because there height and width will be null.
        Group group = new Group();
        Scene scene = new Scene(group);
        group.getChildren().addAll(orderList);
        group.applyCss();
        group.layout();
        for(Node n : group.getChildren()){
            VBox v = (VBox) n;
            v.setMinWidth(v.getWidth() + v.getSpacing());
            VBox.setVgrow(v, Priority.NEVER);
            v.setPadding(new Insets(10,10,10,10));
            v.autosize();
        }
        orderMasonryPane.getChildren().addAll(group.getChildren());
    }

    private VBox makeOrderBox(Order o, String flag) {
        OrderBox orderBox = new OrderBox(o, flag);
        orderBoxList.add(orderBox);
        return orderBox.getVBox();
    }

    private void selectBox(VBox orderBox){
        if(orderBox.getBackground().equals(Backgrounds.SELECTED_BACKGROUND)){ //click the selected box
            deselectBox(orderBox);
        } else if (selectedOrderBox == null){ //click an unselected box, make sure you can only click one at a time
            storedBoxBackground = orderBox.getBackground();
            orderBox.setBackground(Backgrounds.SELECTED_BACKGROUND);
            selectedOrderBox = orderBox;
        }
    }

    private void deselectBox(VBox orderBox){
        selectedOrderBox = null;
        orderBox.setBackground(storedBoxBackground);
    }

    private void receiveOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                Restaurant.getInstance().newEvent("update | received | "+orderId);
                deselectBox(orderBox.getVBox());
                sleep();
                setChanged();
                notifyObservers();
                break;
            }
        }
    }

    private void fireOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                if(!Restaurant.getInstance().getPlacedOrders().isEmpty()){
                    mustAcknowledgeOrderWarning(orderId, "cook");
                    deselectBox(orderBox.getVBox());
                    break;
                } else{
                    Restaurant.getInstance().newEvent("update | fired | "+orderId);
                    deselectBox(orderBox.getVBox());
                    sleep();
                    setChanged();
                    notifyObservers();
                    break;
                }
            }
        }
    }

    private void readyOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                if(!Restaurant.getInstance().getPlacedOrders().isEmpty()){
                    mustAcknowledgeOrderWarning(orderId, "finish");
                    deselectBox(orderBox.getVBox());
                    break;

                } else {
                    Restaurant.getInstance().newEvent("update | ready | " + orderId);
                    deselectBox(orderBox.getVBox());
                    sleep();
                    setChanged();
                    notifyObservers();
                    break;
                }
            }
        }
    }

    private void mustAcknowledgeOrderWarning(int orderId, String verb){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("You must acknowledge all placed orders before " + verb + "ing order " + orderId +".");
        alert.showAndWait();
    }

    private void sleep(){
        try{
            Thread.sleep(300);
        }catch(InterruptedException ex){
            RestaurantLogger.log(Level.SEVERE, ex.toString());
        }
    }
}
