import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server started, listening on port 8888...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from: " + clientSocket.getInetAddress().getHostAddress());

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String input = in.readLine();
                String transformedString = transformName(input);

                out.println(transformedString);

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String transformName(String input) {
            String[] nameParts = input.trim().split("\\s+");
            if (nameParts.length >= 3) {
                String firstName = nameParts[nameParts.length - 1];
                String lastName = nameParts[0];
                StringBuilder middleName = new StringBuilder();
                for (int i = 1; i < nameParts.length - 1; i++) {
                    middleName.append(nameParts[i]).append(" ");
                }
                return firstName + " " + lastName + " " + middleName.toString().trim();
            }
            return input;
        }
    }
}
