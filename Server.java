import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws IOException {
        try {
            BufferedReader reader = null;
            PrintWriter writer = null;
            ServerSocket serverSocket = new ServerSocket(6934);
            Socket socket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            String email = "";
            String password = "";
            String userType = "";
            String check = "";
            ArrayList<String> sellerValues = new ArrayList<>();
            ArrayList<String> customerValues = new ArrayList<>();
            email = reader.readLine();
            password = reader.readLine();
            userType = reader.readLine();
            reader.close();
            writer.close();
            if (userType.equals("Seller")) {
                check = reader.readLine();
                while(check != null) {
                    sellerValues.add(check);
                    check = reader.readLine();
                }
            } else if (userType.equals("Customer")) {
                check = reader.readLine();
                while(check != null) {
                    customerValues.add(check);
                    check = reader.readLine();
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}
