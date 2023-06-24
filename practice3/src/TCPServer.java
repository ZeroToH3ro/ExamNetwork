import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

                String data = in.readLine();
                String[] numbers = data.split(",");
                Map<String, Integer> occurrenceMap = new HashMap<>();

                for (String number : numbers) {
                    occurrenceMap.put(number, occurrenceMap.getOrDefault(number, 0) + 1);
                }

                StringBuilder result = new StringBuilder();
                for (Map.Entry<String, Integer> entry : occurrenceMap.entrySet()) {
                    if (entry.getValue() == 1) {
                        result.append(entry.getKey()).append(",");
                    }
                }

                if (result.length() > 0) {
                    result.deleteCharAt(result.length() - 1); // Remove the trailing comma
                }

                out.println(result.toString());

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
