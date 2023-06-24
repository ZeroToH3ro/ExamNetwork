import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(8888);
            System.out.println("Server started, listening on port 8888...");

            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                String inputString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] inputs = inputString.split(";");

                int n = Integer.parseInt(inputs[0].trim());
                int m = Integer.parseInt(inputs[1].trim());
                int[][] matrix = new int[n][m];

                int maxElement = Integer.MIN_VALUE;
                String primeElements = "";

                for (int i = 0; i < n; i++) {
                    String[] rowValues = inputs[i + 2].trim().split(",");
                    for (int j = 0; j < m; j++) {
                        matrix[i][j] = Integer.parseInt(rowValues[j].trim());
                        if (matrix[i][j] > maxElement) {
                            maxElement = matrix[i][j];
                        }
                        if (isPrime(matrix[i][j])) {
                            primeElements += matrix[i][j] + " ";
                        } else {
                            primeElements += "0 ";
                        }
                    }
                    primeElements += "\n";
                }

                String response = "Max element: " + maxElement + "\nPrime elements:\n" + primeElements;
                byte[] responseBuffer = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
