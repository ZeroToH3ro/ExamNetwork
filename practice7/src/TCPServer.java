import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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

                // Read matrix size
                String sizeData = in.readLine();
                String[] size = sizeData.split(",");
                int n = Integer.parseInt(size[0]); // Number of rows
                int m = Integer.parseInt(size[1]); // Number of columns

                // Read matrix elements
                int[][] matrix = new int[n][m];
                for (int i = 0; i < n; i++) {
                    String rowData = in.readLine();
                    String[] rowElements = rowData.split(",");
                    for (int j = 0; j < m; j++) {
                        matrix[i][j] = Integer.parseInt(rowElements[j]);
                    }
                }

                // Find the maximum element and its indices
                int maxElement = findMaxElement(matrix);
                int[] maxIndices = findIndices(matrix, maxElement);

                // Find and replace non-prime elements with 0
                int[][] modifiedMatrix = replaceNonPrimeElements(matrix);

                // Sort columns in ascending order
                sortColumns(modifiedMatrix);

                // Prepare the response
                StringBuilder response = new StringBuilder();
                response.append("Maximum element: ").append(maxElement).append("\n");
                response.append("Indices of maximum element: ").append(Arrays.toString(maxIndices)).append("\n");
                response.append("Prime elements: ").append(getPrimeElements(modifiedMatrix)).append("\n");
                response.append("Sorted matrix:\n").append(getMatrixString(modifiedMatrix));

                // Send the response to the client
                out.println(response.toString());

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int findMaxElement(int[][] matrix) {
            int maxElement = matrix[0][0];
            for (int[] row : matrix) {
                for (int element : row) {
                    if (element > maxElement) {
                        maxElement = element;
                    }
                }
            }
            return maxElement;
        }

        private int[] findIndices(int[][] matrix, int target) {
            int[] indices = new int[2];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] == target) {
                        indices[0] = i;
                        indices[1] = j;
                        break;
                    }
                }
            }
            return indices;
        }

        private int[][] replaceNonPrimeElements(int[][] matrix) {
            int[][] modifiedMatrix = new int[matrix.length][];
            for (int i = 0; i < matrix.length; i++) {
                modifiedMatrix[i] = new int[matrix[i].length];
                for (int j = 0; j < matrix[i].length; j++) {
                    modifiedMatrix[i][j] = isPrime(matrix[i][j]) ? matrix[i][j] : 0;
                }
            }
            return modifiedMatrix;
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

        private void sortColumns(int[][] matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                int[] column = new int[matrix.length];
                for (int i = 0; i < matrix.length; i++) {
                    column[i] = matrix[i][j];
                }
                Arrays.sort(column);
                for (int i = 0; i < matrix.length; i++) {
                    matrix[i][j] = column[i];
                }
            }
        }

        private List<Integer> getPrimeElements(int[][] matrix) {
            List<Integer> primeElements = new ArrayList<>();
            for (int[] row : matrix) {
                for (int element : row) {
                    if (isPrime(element)) {
                        primeElements.add(element);
                    }
                }
            }
            return primeElements;
        }

        private String getMatrixString(int[][] matrix) {
            StringBuilder matrixString = new StringBuilder();
            for (int[] row : matrix) {
                for (int element : row) {
                    matrixString.append(element).append("\t");
                }
                matrixString.append("\n");
            }
            return matrixString.toString();
        }
    }
}
