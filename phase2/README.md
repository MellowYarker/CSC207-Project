* Restaurant Manager
** A management system for a restaurant

*** Setup

1. Clone this repo
2. In Intellij, set src as "sources root" and tests as "test sources root"
   * The SDK is java 1.8, project language 8 - Lambdas, type annotations etc.

   * Output should be in phase2/out, so create a folder called out.
3. Open any java file in the test folder, find a @Test tag, click it, then press alt (option on mac) + enter.
   * Add junit 4.0
   * All tests should run now.
4. Open the project structure window -> libraries, and search for `com.jfoenix:jfoenix:8.0.1`

****** Running the project

In src/main/Main.java run the main() method.

*** How to use it

On start up you should see this window
![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/startup.png "Startup Window")

      On the left are menu items, by clicking show ingredients you can see the ingredients in each item, price shows the price of the item.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/menu.png "Menu")

      On the right is the bill for the table, on the bill you can change the number of seats (8+ adds a gratuity of 15%)

      You can easily change table views by clicking *pick a table*

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/bill.png "Bill")

      To add an order to a seat (*seat 0 represents shared items between the whole table*), click on any menu item, for multiple items, hold shift as you click.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/addPt1.png "Order")

      Once you've selected all the items, click the orange **+** on the bottom right. Here you can select the quantity as well as add or remove ingredients.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/addPt2.png "Order")


      Next click the **Cook** tab on the top. Here, the cook would push an order through the queue, so click the order, then click Order received, continue this for the other 2 buttons.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/addPt3.png "Order")

      Once the order is ready, go back to the **Server** tab, you'll notice the Order is highlighted in green below the menu. You may click the order to deliver it. Notice the Order is now at the seat.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/addPt4.png "Order")

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/addPt5.png "Order")

      An order can be returned, to do this click on the **order** you would like to return, a window will pop-up and require you give the quantity as well as the reason why it is being returned.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/return.png "Returning")

      To pay, simply click on a seat and press the green **pay** button to pay for an individual seat. Alternatively, a table could join a cheque, in which case all bills will be added to *seat 0*. In this scenario, simply click the **Join All Cheques** button, then click the **pay** button.

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/join.png "Joining Cheques")

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/payPt1.png "Paying")

      ![alt text](https://github.com/MellowYarker/CSC207-Project/tree/master/phase2/images/payPt2.png "Paying")
