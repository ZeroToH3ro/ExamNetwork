import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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

                String numberString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int number = Integer.parseInt(numberString);

                List<Integer> factors = primeFactors(number);

                String response = "Prime factors: " + factors.toString();
                byte[] responseBuffer = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> primeFactors(int number) {
        List<Integer> factors = new ArrayList<>();

        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.add(i);
                number /= i;
            }
        }

        return factors;
    }
}
