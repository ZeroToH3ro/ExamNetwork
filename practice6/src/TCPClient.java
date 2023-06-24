import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the first positive integer (a): ");
            int a = Integer.parseInt(userInput.readLine());

            System.out.print("Enter the second positive integer (b): ");
            int b = Integer.parseInt(userInput.readLine());

            Socket socket = new Socket("localhost", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(a + "," + b);

            String response = in.readLine();
            System.out.println("Sum of " + a + " and " + b + ": " + response);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
