import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the number of rows (n): ");
            int n = Integer.parseInt(userInput.readLine());

            System.out.print("Enter the number of columns (m): ");
            int m = Integer.parseInt(userInput.readLine());

            int[][] matrix = new int[n][m];
            for (int i = 0; i < n; i++) {
                System.out.print("Enter elements for row " + (i + 1) + " (comma-separated): ");
                String[] elements = userInput.readLine().split(",");
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(elements[j]);
                }
            }

            Socket socket = new Socket("localhost", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(n + "," + m);

            for (int[] row : matrix) {
                StringBuilder rowData = new StringBuilder();
                for (int element : row) {
                    rowData.append(element).append(",");
                }
                out.println(rowData.substring(0, rowData.length() - 1));
            }

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
