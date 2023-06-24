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
                String longestWord = findLongestWord(input);
                int wordIndex = findWordIndex(input, longestWord);

                String response = "Longest word: " + longestWord + ", Index: " + wordIndex;
                out.println(response);

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String findLongestWord(String input) {
            String[] words = input.trim().split("\\s+");
            String longestWord = "";
            for (String word : words) {
                if (word.length() > longestWord.length()) {
                    longestWord = word;
                }
            }
            return longestWord;
        }

        private int findWordIndex(String input, String word) {
            String[] words = input.trim().split("\\s+");
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals(word)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
