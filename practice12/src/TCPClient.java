import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter source path: ");
            String sourcePath = userInput.readLine();

            System.out.print("Enter destination path: ");
            String destinationPath = userInput.readLine();

            Socket socket = new Socket("localhost", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(sourcePath);
            out.println(destinationPath);

            String response = in.readLine();
            System.out.println(response);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
