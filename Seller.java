import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Seller extends Thread{
    /*
    Creates the ArrayList that will output the product info that the Seller
    will see and the product info that will be stored in a file for later use.
     */
    private ArrayList<Item> products;
    private String sellerEmail;

    private static Object obj = new Object();

    public Seller(ArrayList<Item> products, String sellerEmail) {
        this.products = products;
        this.sellerEmail = sellerEmail;
    }

    //This method takes the values that the seller inputs and adds it to the ArrayList.
    //This ArrayList will be outputted line by line to create the view of the Marketplace.
    public void addProduct(int id, String name, String store, String description, int quantity, double price) throws IOException {
        boolean productExists = false;
        for (int i = 0; i < products.size(); i++) {
            if (id == products.get(i).getId()) {
                productExists = true;
            }
        }
        if (productExists) {
            JOptionPane.showMessageDialog(null, "Product Already Exists!", "Add Product", JOptionPane.ERROR_MESSAGE);
        } else {
            Item temp = new Item(id, name, store, description, quantity, price, sellerEmail);
            File item = new File(Integer.toString(id));
            if (item.exists()) {
                JOptionPane.showMessageDialog(null, "Product wit that ID already exists!", "Add Product", JOptionPane.ERROR_MESSAGE);

            } else if (item.createNewFile()) {
                synchronized (obj) {
                    products.add(temp);
                    JOptionPane.showMessageDialog(null, "Product Successfully Added!", "Add Product", JOptionPane.ERROR_MESSAGE);

                    BufferedWriter bfw = new BufferedWriter(new FileWriter(Integer.toString(id)));
                    bfw.write(store);
                    bfw.newLine();
                    bfw.write("Revenue:0");
                    bfw.newLine();
                    bfw.write("Customers:");
                    bfw.close();
                }
            }


        }
    }

    //This method allows the previously added products to be edited.
    public void editProduct(int id, String name, String description, int quantity, double price) {
        int index = 0;
        boolean productExists = false;
        for (int i = 0; i < products.size(); i++) {
            if (id == products.get(i).getId()) {
                index = i;
                productExists = true;
            }
        }
        if (productExists) {
            synchronized (obj) {
                products.get(index).setName(name);
                products.get(index).setDescription(description);
                products.get(index).setQuantity(quantity);
                products.get(index).setPrice(price);
                JOptionPane.showMessageDialog(null, "Product Successfully Updated!", "Edit Product", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Product Doesn't Exist!", "Edit Product", JOptionPane.ERROR_MESSAGE);

        }
    }

    //This method allows the seller to delete previously added products.
    public void deleteProduct(int id) {
        int index = 0;
        boolean exists = false;
        for (int i = 0; i < products.size(); i++) {
            if (id == products.get(i).getId()) {
                index = i;
                exists = true;
            }
        }
        if (exists) {
            synchronized (obj) {
                products.remove(index);
                JOptionPane.showMessageDialog(null, "Product successfully removed!", "Delete Product", JOptionPane.PLAIN_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Product Doesn't Exist!", "Delete Product", JOptionPane.ERROR_MESSAGE);
        }

    }

    //This method takes the values from a file containing the info of the customers that the seller needs to see.
    public void viewCustomerInfo(int id) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(Integer.toString(id)));
        bfr.readLine();
        bfr.readLine();
        JOptionPane.showMessageDialog(null, bfr.readLine(), "View Customer Info", JOptionPane.INFORMATION_MESSAGE);
        bfr.close();
    }

    public void viewStoreRevenue(ArrayList<Item> products, String store) throws IOException {
        double revenue = 0;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getStore().equals(store)) {
                BufferedReader bfr = new BufferedReader(new FileReader(Integer.toString(products.get(i).getId())));
                bfr.readLine();
                String line = bfr.readLine();
                double rev = Double.parseDouble(line.substring(line.indexOf(":") + 1));
                revenue += rev;
                bfr.close();
            }
        }
        JOptionPane.showMessageDialog(null, "Revenue: "+ revenue,
                "View Store Revenue", JOptionPane.INFORMATION_MESSAGE);

    }

    public void importProduct(String filename) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(filename));
        String line = bfr.readLine();
        while (line != null) {
            String temp = line;
            int id = Integer.parseInt(temp.substring(temp.indexOf(":") + 1, temp.indexOf("|")));
            temp = temp.substring(temp.indexOf("|") + 1);
            String name = temp.substring(temp.indexOf(":") + 1, temp.indexOf("|"));
            temp = temp.substring(temp.indexOf("|") + 1);
            String store = temp.substring(temp.indexOf(":") + 1, temp.indexOf("|"));
            temp = temp.substring(temp.indexOf("|") + 1);
            double price = Double.parseDouble(temp.substring(temp.indexOf(":") + 1, temp.indexOf("|")));
            temp = temp.substring(temp.indexOf("|") + 1);
            String description = temp.substring(temp.indexOf(":") + 1, temp.indexOf("|"));
            temp = temp.substring(temp.indexOf("|") + 1);
            int quantity = Integer.parseInt(temp.substring(temp.indexOf(":") + 1));
            products.add(new Item(id, name, store, description, quantity, price, sellerEmail));
            line = bfr.readLine();
        }
        bfr.close();
    }

    public void exportProduct(String filename) throws IOException {
        BufferedWriter bfw = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < products.size(); i++) {
            bfw.write(String.valueOf(products.get(i)));
            bfw.newLine();
        }
        bfw.close();
    }

    public ArrayList<Item> sellerOutput() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String productName = "";
        String store = "";
        double price = 0.0;
        String description = "";
        int quantity = 0;
        int choice = 0;
        int productId = 0;
        ArrayList<Item> products = new ArrayList<>();
        ArrayList<Item> serverSend = new ArrayList<>();
        Seller seller = new Seller(products, sellerEmail);
        JOptionPane.showMessageDialog(null, "Welcome Seller!",
                "Seller", JOptionPane.INFORMATION_MESSAGE);
        String action = "";
        do {
            String productList = "Products:\n";
            for (int i = 0; i < seller.products.size(); i++) {
                productList += seller.products.get(i) + "\n";
            }
            JOptionPane.showMessageDialog(null, productList, "Seller",
                    JOptionPane.INFORMATION_MESSAGE);

            String[] options = {"Add Product", "Edit Product", "Delete Product", "Check Customer Info",
                    "View Store Revenues", "Import Products", "Export Products", "Log Off"};
            action = (String) JOptionPane.showInputDialog(null, "What would you like to do?",
                    "Seller",
                    JOptionPane.PLAIN_MESSAGE, null, options, null);

            if (action.equals("Add Product")) {
                productName = JOptionPane.showInputDialog(null, "What is the product name?",
                        "Add Product", JOptionPane.QUESTION_MESSAGE);
                productId = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the product ID?", "Add Product", JOptionPane.QUESTION_MESSAGE));
                store = JOptionPane.showInputDialog(null, "What is the store name?",
                        "Add Product", JOptionPane.QUESTION_MESSAGE);
                description = JOptionPane.showInputDialog(null,
                        "What is the product description?", "Add Product", JOptionPane.QUESTION_MESSAGE);
                quantity = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the quantity?", "Add Product", JOptionPane.QUESTION_MESSAGE));
                price = Double.parseDouble(JOptionPane.showInputDialog(null,
                        "What is the price?", "Add Product", JOptionPane.QUESTION_MESSAGE));
                seller.addProduct(productId, productName, store, description, quantity, price);
                JOptionPane.showMessageDialog(null, "Item Successfully Added!",
                        "Add Product", JOptionPane.INFORMATION_MESSAGE);
            } else if (action.equals("Edit Product")) {
                choice = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the ID of the product you would like to edit?", "Edit Product",
                        JOptionPane.QUESTION_MESSAGE));
                productName = JOptionPane.showInputDialog(null,
                        "What is the new name of the product?", "Edit Product",
                        JOptionPane.QUESTION_MESSAGE);
                description = JOptionPane.showInputDialog(null,
                        "What is the new description of the product?", "Edit Product",
                        JOptionPane.QUESTION_MESSAGE);
                quantity = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the new quantity of the product?", "Edit Product",
                        JOptionPane.QUESTION_MESSAGE));
                price = Double.parseDouble(JOptionPane.showInputDialog(null,
                        "What is the new price of the product?", "Edit Product",
                        JOptionPane.QUESTION_MESSAGE));
                seller.editProduct(choice, productName, description, quantity, price);
            } else if (action.equals("Delete Product")) {
                choice = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the ID of the product to delete?", "Delete Product",
                        JOptionPane.QUESTION_MESSAGE));
                seller.deleteProduct(choice);
            } else if (action.equals("Check Customer Info")) {
                choice = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the ID of the product you would like to view customer info for?",
                        "View Customer Info", JOptionPane.QUESTION_MESSAGE));
                seller.viewCustomerInfo(choice);
            } else if (action.equals("View Store Revenues")) {
                String wantedStore = JOptionPane.showInputDialog(null,
                        "What is the store you want to check the revenue of?", "View Store Revenue",
                        JOptionPane.QUESTION_MESSAGE);
                seller.viewStoreRevenue(products, wantedStore);
            } else if (action.equals("Import Products")) {
                String file = JOptionPane.showInputDialog(null,
                        "What is the name of the file you would like to import?", "Import File",
                        JOptionPane.QUESTION_MESSAGE);
                seller.importProduct(file);
            } else if (action.equals("Export Products")) {
                String file = JOptionPane.showInputDialog(null,
                        "What is name of the file you would like to export?", "Export File",
                        JOptionPane.QUESTION_MESSAGE);
                seller.exportProduct(file);
            } else if (action.equals("Log Off")) {
                JOptionPane.showMessageDialog(null, "Goodbye!", "Log Off",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } while (!action.equals("Log Off"));

        BufferedWriter bfw = new BufferedWriter(new FileWriter("ProductList.txt"));
        for (int i = 0; i < seller.products.size(); i++) {
            bfw.write(String.valueOf(seller.products.get(i)));
            bfw.newLine();
            serverSend.add(seller.products.get(i));
        }
        bfw.close();
        return serverSend;
    }
}
