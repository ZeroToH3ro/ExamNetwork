import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
                int n = Integer.parseInt(data);
                List<Integer> fibonacciPrimes = getFibonacciPrimes(n);

                StringBuilder result = new StringBuilder();
                for (int number : fibonacciPrimes) {
                    result.append(number).append(",");
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

        private List<Integer> getFibonacciPrimes(int n) {
            List<Integer> fibonacciPrimes = new ArrayList<>();

            int previous = 0;
            int current = 1;

            while (current < n) {
                if (isPrime(current)) {
                    fibonacciPrimes.add(current);
                }

                int next = previous + current;
                previous = current;
                current = next;
            }

            return fibonacciPrimes;
        }

        private boolean isPrime(int number) {
            if (number <= 1) {
                return false;
            }

            for (int i = 2; i * i <= number; i++) {
                if (number % i == 0) {
                    return false;
                }
            }

            return true;
        }
    }
}
