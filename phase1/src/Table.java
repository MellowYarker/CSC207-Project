import java.util.ArrayList;
import java.util.List;

public class Table {
	private final int id;
	private final List<MenuItem> bill = new ArrayList<>();
	private Order order;

	Table(int id) {
		this.id = id;
	}

	void addOrderToTable(Order o) {
		order = o;
	}

	void addToBill(Order o) {
		bill.addAll(o.getItems());
	}

	void printBill() {
		System.out.println("BILL FOR TABLE #" + id);
		for (MenuItem item : bill) {
			System.out.println(item.getQuantity() + " " + item.getName() + ": $" + item.getPrice());
		}
		System.out.println("Total: $" + getBillPrice() + "\n");
	}

	private double getBillPrice() {
		double ret = 0.00;
		for (MenuItem item : bill) {
			ret += item.getPrice() * item.getQuantity();
			ret += item.getModPrice();
		}
		return ret;
	}

	protected Order getOrder() {
		return order;
	}
}
