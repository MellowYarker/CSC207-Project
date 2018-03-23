package graveyard;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ResourceBundle;

public class TextInputBox implements Initializable{

    @FXML
    private JFXTextField orderStringBox;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    public void submitOrder(){
        String s = orderStringBox.getText();
        System.out.println("inside submit order: "+ s);
        Restaurant.getInstance().newEvent(s);
    }

    public void clearTextBox(){
        orderStringBox.clear();
    }




}
