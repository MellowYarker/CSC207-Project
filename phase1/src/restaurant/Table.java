package restaurant;

public interface Table {
    void addOrder(Order o);

    void addOrderToBill(Order o);

    void removeFromBill(Order o);

    void printBill();

    int getId();

    //Order getOrder();
}

