import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws IOException {
        File loginList = new File("loginList.txt");
        BufferedReader reader = null;
        PrintWriter writer = null;

        String host;
        do {
            host = JOptionPane.showInputDialog(null, "Enter host",
                    "", JOptionPane.QUESTION_MESSAGE);
            if ((host.isEmpty())) {
                JOptionPane.showMessageDialog(null, "Host cannot be empty!",
                        "", JOptionPane.ERROR_MESSAGE);
            }
        } while ((host.isEmpty()));


        String port;
        do {
            port = JOptionPane.showInputDialog(null, "Enter port number",
                    "", JOptionPane.QUESTION_MESSAGE);
            if ((port.isEmpty())) {
                JOptionPane.showMessageDialog(null, "Port number cannot be empty!",
                        "", JOptionPane.ERROR_MESSAGE);
            }
        } while ((port.isEmpty()));


        Socket socket = null;
        try {
            socket = new Socket(host, Integer.parseInt(port));

            JOptionPane.showMessageDialog(null, "Connection to server successful",
                    "", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection to server failed...", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        JOptionPane.showMessageDialog(null, "Welcome to the Marketplace!");
        String[] buttons = {"Login", "Register"};
        int option = JOptionPane.showOptionDialog(null, "Log in or register a new account?", "Welcome", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
        if (option == 0) {
            boolean loginFail;
            boolean noLogins = false;
            String email = "";
            String password = "";
            String userType = "";
            do {
                loginFail = true;
                BufferedReader bfr = new BufferedReader(new FileReader("loginList.txt"));
                String line = bfr.readLine();
                if (line == null) {
                    JOptionPane.showMessageDialog(null, "There are no registered emails yet!", "Error!", JOptionPane.ERROR_MESSAGE);
                    loginFail = false;
                    noLogins = true;
                } else {
                    email = JOptionPane.showInputDialog(null, "Enter your Email", "Login", JOptionPane.PLAIN_MESSAGE);
                    password = JOptionPane.showInputDialog(null, "Enter your Password", "Login", JOptionPane.PLAIN_MESSAGE);
                    while (line != null) {
                        if (line.substring(0, line.indexOf("%")).equals(email)) {
                            if (line.substring(line.indexOf("%") + 1, line.indexOf("&")).equals(password)) {
                                JOptionPane.showMessageDialog(null, "Access Granted, Welcome " + email, "Login", JOptionPane.PLAIN_MESSAGE);
                                if (line.substring(line.indexOf("&") + 1).equals("Seller")) {
                                    userType = "Seller";
                                } else if (line.substring(line.indexOf("&") + 1).equals("Customer")) {
                                    userType = "Customer";
                                }
                                loginFail = false;
                            }
                        }
                        line = bfr.readLine();
                    }
                    if (loginFail) {
                        JOptionPane.showMessageDialog(null, "Incorrect Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } while (loginFail);
            writer.println(email);
            writer.flush();
            writer.println(password);
            writer.flush();
            writer.println(userType);
            writer.flush();
            if (!noLogins) {
                if (userType.equals("Seller")) {
                    BufferedReader bfr = new BufferedReader(new FileReader(email));
                    ArrayList<String> lines = new ArrayList<>();
                    String line = bfr.readLine();
                    while (line != null) {
                        lines.add(line);
                        line = bfr.readLine();
                    }
                    bfr.close();
                    ArrayList<Item> products = new ArrayList<>();
                    if (lines.size() > 0) {
                        for (int i = 0; i < lines.size(); i++) {
                            String temp = lines.get(i);
                            int id = Integer.parseInt(temp.substring(temp.indexOf(":") + 1, temp.indexOf(",")));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String name = temp.substring(temp.indexOf(":") + 1, temp.indexOf(","));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String store = temp.substring(temp.indexOf(":") + 1, temp.indexOf(","));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String description = temp.substring(temp.indexOf(":") + 1, temp.indexOf(","));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            int quantity = Integer.parseInt(temp.substring(temp.indexOf(":") + 1, temp.indexOf(",")));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            double price = Double.parseDouble(temp.substring(temp.indexOf(":") + 1, temp.indexOf(",")));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String sellerEmail = temp.substring(temp.indexOf(":") + 1);
                            products.add(new Item(id, name, store, description, quantity, price, sellerEmail));
                        }
                    }
                    Seller seller = new Seller(products, email);
                    ArrayList<Item> serverSend = seller.sellerOutput();
                    for (int i = 0; i < serverSend.size(); i++) {
                        writer.println(serverSend.get(i));
                        writer.flush();
                    }
                } else if (userType.equals("Customer")) {
                    BufferedReader bfr = new BufferedReader(new FileReader(email));
                    ArrayList<String> lines = new ArrayList<>();
                    String line = bfr.readLine();

                    while (line != null) {
                        lines.add(line);
                        line = bfr.readLine();
                    }
                    bfr.close();
                    ArrayList<Item> products = new ArrayList<>();
                    if (lines.size() > 0) {
                        for (int i = 0; i < lines.size(); i++) {
                            String temp = lines.get(i);
                            int id = Integer.parseInt(temp.substring(temp.indexOf(":") + 1, temp.indexOf(",")));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String name = temp.substring(temp.indexOf(":") + 1, temp.indexOf(","));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String store = temp.substring(temp.indexOf(":") + 1, temp.indexOf(","));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String description = temp.substring(temp.indexOf(":") + 1, temp.indexOf(","));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            int quantity = Integer.parseInt(temp.substring(temp.indexOf(":") + 1, temp.indexOf(",")));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            double price = Double.parseDouble(temp.substring(temp.indexOf(":") + 1, temp.indexOf(",")));
                            temp = temp.substring(temp.indexOf(",") + 1);
                            String sellerEmail = temp.substring(temp.indexOf(":") + 1);
                            products.add(new Item(id, name, store, description, quantity, price, email));
                        }
                    }
                    Customer customer = new Customer(products, email);
                    customer.customerOutput();
                    ArrayList<Item> serverSendCustomer = customer.customerOutput();
                    for (int i = 0; i < serverSendCustomer.size(); i++) {
                        writer.println(serverSendCustomer.get(i));
                        writer.flush();
                    }
                }
            }
        } else if (option == 1) {
            boolean emailTaken = false;
            String email;
            do {
                emailTaken = false;
                email = JOptionPane.showInputDialog(null, "Enter your email", "Register", JOptionPane.PLAIN_MESSAGE);
                BufferedReader bfr1 = new BufferedReader(new FileReader("loginList.txt"));
                String line = bfr1.readLine();
                while (line != null) {
                    if (line.substring(0, line.indexOf("%")).equals(email)) {
                        System.out.println("Email is Taken!");
                        emailTaken = true;
                    }
                    line = bfr1.readLine();
                }
            } while (emailTaken);
            writer.println(email);
            writer.flush();
            String password = JOptionPane.showInputDialog(null, "Enter your password", "Register", JOptionPane.PLAIN_MESSAGE);
            writer.println(password);
            writer.flush();
            String userType = "";
            boolean menuLoop = true;
            do {
                String[] buttons2 = {"Seller", "Customer"};
                int type = JOptionPane.showOptionDialog(null, "Are you a seller or a customer?", "Register", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons2, buttons2[0]);
                if (type == 0) {
                    userType = "Seller";
                    File seller = new File(email);
                    if (seller.createNewFile()) {
                        JOptionPane.showMessageDialog(null, "Account created: " + seller.getName(), "Register", JOptionPane.INFORMATION_MESSAGE);
                    }
                    menuLoop = false;
                } else if (type == 1) {
                    userType = "Customer";
                    File customer = new File(email);
                    if (customer.createNewFile()) {
                        JOptionPane.showMessageDialog(null, "Account created: " + customer.getName(), "Register", JOptionPane.INFORMATION_MESSAGE);

                    }
                    menuLoop = false;
                } else {
                    return;
                }
            } while (menuLoop);
            writer.println(userType);
            writer.flush();
            BufferedReader bfr2 = new BufferedReader(new FileReader("loginList.txt"));
            ArrayList<String> lines = new ArrayList<>();
            String line = bfr2.readLine();

            while (line != null) {
                lines.add(line);
                line = bfr2.readLine();
            }
            bfr2.close();

            BufferedWriter bfw = new BufferedWriter(new FileWriter("loginList.txt"));
            if (lines.size() == 0) {
                bfw.write(email + "%" + password + "&" + userType);
                bfw.newLine();
                bfw.close();
            } else {
                for (String s : lines) {
                    bfw.write(s);
                    bfw.newLine();
                }
                bfw.write(email + "%" + password + "&" + userType);
                bfw.newLine();
                bfw.close();
            }

        }
        reader.close();
        writer.close();
    }
}
