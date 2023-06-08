package project5;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Marketplace {
    public static void main(String[] args) throws IOException {
        try {
            Scanner scan = new Scanner(System.in);
            File loginList = new File("loginList.txt");

            System.out.println("Welcome to the project5.Marketplace");
            System.out.println("1. Log in to existing account");
            System.out.println("2. Register a new account");
            String welcomeOption;
            do {
                welcomeOption = scan.nextLine();
                if (Integer.parseInt(welcomeOption) == 1) {
                    boolean loginFail;
                    boolean noLogins = false;
                    String email = "";
                    String password;
                    String userType = "";
                    do {
                        loginFail = true;
                        BufferedReader bfr = new BufferedReader(new FileReader("loginList.txt"));
                        String line = bfr.readLine();
                        if (line == null) {
                            System.out.println("There are no emails registered!");
                            loginFail = false;
                            noLogins = true;
                        } else {
                            System.out.println("Enter your email");
                            email = scan.nextLine();
                            System.out.println("Enter your password");
                            password = scan.nextLine();

                            while (line != null) {
                                if (line.substring(0, line.indexOf("%")).equals(email)) {
                                    if (line.substring(line.indexOf("%") + 1, line.indexOf("&")).equals(password)) {
                                        System.out.println("Access Granted");
                                        System.out.println("Welcome " + email);
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
                                System.out.println("Incorrect email or password!");
                            }
                        }
                    } while (loginFail);
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
                            seller.sellerOutput();
                            // methods and shit
                            // convert arraylist of items back into arraylist of strings
                            //write new arraylist of strings back into file

                            //copied from login
//                                BufferedWriter bfw = new BufferedWriter(new FileWriter(email));
//                                if (lines.size() == 0) {
//                                    bfw.write(email + "%" + password + "&" + userType);
//                                    bfw.newLine();
//                                    bfw.close();
//                                } else {
//                                    for (int i = 0; i < lines.size(); i++) {
//                                        bfw.write(lines.get(i));
//                                        bfw.newLine();
//                                    }
//                                    bfw.write(email + "%" + password + "&" + userType);
//                                    bfw.newLine();
//                                    bfw.close();
//                                }


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
                        }
                    }
                } else if (Integer.parseInt(welcomeOption) == 2) {
                    boolean emailTaken = false;
                    String email;
                    do {
                        emailTaken = false;
                        System.out.println("Enter an email");
                        email = scan.nextLine();
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
                    System.out.println("Enter a password");
                    String password = scan.nextLine();
                    String userType;
                    boolean menuLoop = true;
                    do {
                        System.out.println("Are you a seller or a customer?");
                        System.out.println("1. Seller");
                        System.out.println("2. Customer");
                        userType = scan.nextLine();
                        if (userType.equals("1")) {
                            userType = "Seller";
                            File seller = new File(email);
                            if (seller.createNewFile()) {
                                System.out.println("Account created: " + seller.getName());
                            }
                            menuLoop = false;
                        } else if (userType.equals("2")) {
                            userType = "Customer";
                            File customer = new File(email);
                            if (customer.createNewFile()) {
                                System.out.println("Account created: " + customer.getName());
                            }
                            menuLoop = false;
                        } else {
                            System.out.println("Please enter a valid response!");
                        }
                    } while (menuLoop);

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
                        for (int i = 0; i < lines.size(); i++) {
                            bfw.write(lines.get(i));
                            bfw.newLine();
                        }
                        bfw.write(email + "%" + password + "&" + userType);
                        bfw.newLine();
                        bfw.close();
                    }

                } else {
                    System.out.println("Please enter a valid response!");
                }
            } while (Integer.parseInt(welcomeOption) != 1 && Integer.parseInt(welcomeOption) != 2);

        } catch (Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}