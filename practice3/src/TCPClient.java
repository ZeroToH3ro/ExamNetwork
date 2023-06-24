import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter a sequence of real numbers (comma-separated): ");
            String sequence = userInput.readLine();

            Socket socket = new Socket("localhost", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(sequence);

            String response = in.readLine();
            System.out.println("Elements appearing exactly once: " + response);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
