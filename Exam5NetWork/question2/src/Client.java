import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            String serverIP = "localhost";
            int serverPort = 8000;

            Socket socket = new Socket(serverIP, serverPort);
            System.out.println("Connected to server.");

            // Get the file path from user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine();

            // Send the file path to server
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
            outToServer.println(filePath);

            // Receive and display the response from server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = inFromServer.readLine()) != null) {
                System.out.println(response);
            }

            // Close the connections
            outToServer.close();
            inFromServer.close();
            socket.close();

            System.out.println("Connection closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
