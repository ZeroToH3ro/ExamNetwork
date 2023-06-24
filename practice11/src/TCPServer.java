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

                int n = Integer.parseInt(in.readLine());
                List<List<Integer>> permutations = generatePermutations(n);

                StringBuilder response = new StringBuilder();
                for (List<Integer> permutation : permutations) {
                    response.append(permutation.toString()).append("\n");
                }

                out.println(response.toString());

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private List<List<Integer>> generatePermutations(int n) {
            List<List<Integer>> permutations = new ArrayList<>();
            List<Integer> nums = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                nums.add(i);
            }
            backtrack(nums, new ArrayList<>(), permutations);
            return permutations;
        }

        private void backtrack(List<Integer> nums, List<Integer> current, List<List<Integer>> permutations) {
            if (current.size() == nums.size()) {
                permutations.add(new ArrayList<>(current));
                return;
            }
            for (int num : nums) {
                if (current.contains(num)) {
                    continue;
                }
                current.add(num);
                backtrack(nums, current, permutations);
                current.remove(current.size() - 1);
            }
        }
    }
}
