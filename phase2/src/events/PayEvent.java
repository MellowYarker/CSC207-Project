package events;

import restaurant.Bill;
import restaurant.Table;

public class PayEvent implements Event {
    private Table table;
    private int seatNumber;
    private double paymentAmount;
    PayEvent(Table table, int seatNumber, String paymentAmount){
        this.table = table;
        this.seatNumber = seatNumber;
        this.paymentAmount = Double.parseDouble(paymentAmount); //TODO: test this
    }

    @Override
    public EventType getType() {
        return EventType.PAY;
    }

    @Override
    public void doEvent() {
        Bill bill = table.getSeat(seatNumber).getBill();
        System.out.println("Bill subtotal: " + bill.getSubtotal());
        System.out.println("Bill unpaid amount: " + bill.getUnpaidAmount());
        System.out.println("Bill paid amount :" + bill.getPaidAmount());
        System.out.println("Tip amount: " + bill.getTipAmount());
        System.out.println("Amount to be paid: " + paymentAmount);
        System.out.println("BILL PAID");
        table.getSeat(seatNumber).getBill().pay(paymentAmount);
        System.out.println("Bill subtotal: " + bill.getSubtotal());
        System.out.println("Bill unpaid amount: " + bill.getUnpaidAmount());
        System.out.println("Bill paid amount :" + bill.getPaidAmount());
        System.out.println("Tip amount: " + bill.getTipAmount());
        System.out.println("Amount to be paid: " + paymentAmount);
        //all these souts are for testing

        //TODO: @Achilles please log the payment info here.
        String logString = "Payment of $" + paymentAmount + " received from Table " + table.getId() + " seat " + seatNumber;
    }
}
