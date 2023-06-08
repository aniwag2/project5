# project5

CS18000 Project 5

## Running and Compiling

To run the program, run the Server Class and then run the Client Class. Follow the instructions that the Grapgic User Interface gives you by creating a new account, saying if you're a seller or customer, then rerunning the program to log in. After logging in, follow all instructions given.

## Submissions

Alec Bargen submitted the Report through Brightspace.

Anitej Waghray submitted the Vocareum workspace.

Anitej Waghray submitted the Presentation through Brightspace.

## Class Descriptions

### Seller.java

The Seller class contains the methods that allow the seller to add, edit, and delete products. This is done using an ArrayList that contains all the inputted values from the seller and then the ArrayList is written into a text file to be used by the Customer class so that they can see what products can be bought in the Marketplace class. The seller also has the option to import a csv file which contains the information for products that can be added to the ArrayList. The seller can also export any products they created/edited into a separate csv file.

### Customer.java

The Customer class allows a customer to interact with a list of products created by users of the seller class. This is done by reading in a file called "ProductList.txt" which contains string representations of every item in every store created by seller. These Item objects are read into an arraylist and can be searched through, sorted and purchased based on user input. A complete list of customer transactions is generated here as well as total revenue made by each product.

### Server.java

While in Project 4 we had a Marketplace Class that handled all front end interaction and some of the back end that wasn't in Seller and Customer. For the Server class, the Marketplace class and some of Seller and Customer were refactored to be used in a Server Socket, where all information is stored in the Server.

### Client.java

This is the Client side of the Server Socket where all front end code from Marketplace, Seller, and Customer appear and any information inputted (through the Graphical User Interface) is sent to the Server.
