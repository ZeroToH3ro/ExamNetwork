import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 8888;

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the number of rows: ");
            int n = Integer.parseInt(userInput.readLine());

            System.out.print("Enter the number of columns: ");
            int m = Integer.parseInt(userInput.readLine());

            int[][] matrix = new int[n][m];

            for (int i = 0; i < n; i++) {
                System.out.print("Enter the elements of row " + (i + 1) + ": ");
                String rowInput = userInput.readLine();
                String[] rowValues = rowInput.trim().split("\\s+");
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(rowValues[j]);
                }
            }

            String request = n + ";" + m;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    request += ";" + matrix[i][j];
                }
            }

            byte[] requestBuffer = request.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(requestBuffer, requestBuffer.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("\nServer response:\n" + response);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
