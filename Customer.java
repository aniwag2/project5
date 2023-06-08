import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Customer extends Thread{
    ArrayList<Item> arrayList = new ArrayList<>();
    private String email;

    private static Object obj = new Object();

    public Customer(ArrayList<Item> arrayList, String email) {
        this.arrayList = arrayList;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public ArrayList<Item> customerOutput() throws IOException {
        ArrayList<Item> serverSendCustomer = new ArrayList<>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(getEmail(), true));
        writer.flush();
        Scanner scanner = new Scanner(System.in);
        Scanner fileScanner = new Scanner(new File("ProductList.txt"));
        fileScanner.useDelimiter(",");
        BufferedReader reader = new BufferedReader(new FileReader("ProductList.txt"));
        while (fileScanner.hasNext()) {
            Item item = new Item(0, null, null, null, 0, 0.00, null);
            fileScanner.useDelimiter(",");
            item.setId(Integer.parseInt(fileScanner.next()));
            item.setName(fileScanner.next());
            item.setStore(fileScanner.next());
            item.setDescription(fileScanner.next());
            item.setQuantity(Integer.parseInt(fileScanner.next()));
            item.setPrice(Double.parseDouble(fileScanner.next()));
            String string = fileScanner.next();
            fileScanner.useDelimiter("\r");
            item.setSellerEmail(string);
            arrayList.add(item);
        }

        String string = "";
        for (Item item : arrayList
        ) {
            string = string + item.fancyToString();
        }
        while (true) {
            String[] buttons = {"Search for a product", "Sort the marketplace", "Purchase an item",
                    "View purchase history", "Log out"};
            int option = JOptionPane.showOptionDialog(null,
                    "Below is a list of products for sale \n" + string, "Customer interface",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
            if (option == 0) {
                String search = JOptionPane.showInputDialog("Enter a search term");
                String line;
                string = "";

                while (reader.ready()) {
                    line = reader.readLine();
                    if (line.contains(search)) {
                        string = string + line;
                    }
                    JOptionPane.showMessageDialog(null, "All products containing search term "
                            + search + " have been printed below\n" + string);

                }

            } else if (option == 1) {
                while (true) {
                    try {
                        String[] sort = {"Sort by price", "Sort by quantity"};
                        int options = JOptionPane.showOptionDialog(null, "Choose an option",
                                "Customer interface", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, sort, sort[0]);
                        reader = new BufferedReader(new FileReader("ProductList.txt"));
                        if (options == 0) {
                            Collections.sort(arrayList);
                            string = "";
                            for (Item item : arrayList
                            ) {
                                string = string + item.toString() + "\n";
                            }


                            break;

                        } else if (options == 1) {
                            Collections.sort(arrayList, Item::compareQuantity);
                            string = "";
                            for (Item item : arrayList
                            ) {
                                string = string + item.toString() + "\n";
                            }

                            break;

                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Input!");

                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid Input!");
                    }
                }
            } else if (option == 2) {
                int index = Integer.parseInt(JOptionPane.showInputDialog("Enter index of item to purchase \n" +
                        string));


                for (Item item : arrayList)
                    if (item.getId() == index) {
                        String[] yn = {"Yes", "No"};
                        int options = JOptionPane.showOptionDialog(null, "You have selected: "
                                + item.fancyToString() + " Is that correct?", "Customer interface",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yn, yn[0]);
                        while (true) {
                            try {
                                synchronized (obj) {
                                    if (options == 0) {
                                        int quantity = Integer.parseInt(JOptionPane.showInputDialog(null,
                                                "How many would you like to purchase?"));
                                        JOptionPane.showMessageDialog(null,
                                                "Purchase confirmed!\nYou have purchased " + quantity + " "
                                                        + item.getName());
                                        item.setQuantity(item.getQuantity() - quantity);
                                        Item tempItem = new Item(item.getId(), item.getName(), item.getStore(),
                                                item.getDescription(), item.getQuantity(),
                                                item.getPrice(), item.getSellerEmail());
                                        tempItem.setQuantity(quantity);
                                        writer.write(tempItem.toString());
                                        writer.newLine();
                                        writer.flush();
                                        BufferedReader bfr = new BufferedReader(new FileReader(String.
                                                valueOf(item.getId())));
                                        ArrayList<String> stringArrayList = new ArrayList<>();

                                        while (bfr.ready()) {
                                            stringArrayList.add(bfr.readLine());
                                        }
                                        BufferedWriter bfw = new BufferedWriter(new
                                                FileWriter(String.valueOf(item.getId())));
                                        String revenue = stringArrayList.get(1);
                                        Double doubleRevenue = Double.parseDouble(revenue.substring(8));
                                        doubleRevenue += quantity * item.getPrice();
                                        revenue = revenue.substring(0, revenue.indexOf(":") - 1);
                                        revenue = revenue + String.valueOf(doubleRevenue);
                                        stringArrayList.set(2, revenue);
                                        stringArrayList.add("Customers:");
                                        stringArrayList.add(this.getEmail() + ",");
                                        for (int i = 0; i < stringArrayList.size(); i++) {
                                            bfw.write(stringArrayList.get(i));
                                            if (!(i == 1)) {
                                                bfw.write(stringArrayList.get(i));
                                                bfw.newLine();
                                                bfw.flush();
                                            }
                                        }
                                        scanner.nextLine();
                                        break;

                                    }
                                }
                                if (scanner.nextInt() == 1) {
                                    JOptionPane.showMessageDialog(null, "Cancelling purchase");
                                    break;
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid Input!");
                                }
                            } catch (InputMismatchException e) {
                                JOptionPane.showMessageDialog(null, "Invalid Input!");
                            }
                            scanner.nextLine();
                        }
                    }

            } else if (option == 3) {
                try {
                    reader = new BufferedReader(new FileReader(getEmail()));
                    if (reader.ready()) {
                        string = "";
                        while (reader.ready()) {
                            string = string + reader.readLine();
                        }
                        JOptionPane.showMessageDialog(null, string);
                    } else {

                        JOptionPane.showMessageDialog(null, "No purchase history found");
                    }
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "No purchase history found");
                }
            } else if (option == 4) {
                JOptionPane.showMessageDialog(null, "Logging Out!");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Input!");
            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            serverSendCustomer.add(arrayList.get(i));
        }
        return serverSendCustomer;
    }

}
