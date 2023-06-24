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

            System.out.print("Enter a number n: ");
            int n = Integer.parseInt(userInput.readLine());

            byte[] requestBuffer = String.valueOf(n).getBytes();

            DatagramPacket sendPacket = new DatagramPacket(requestBuffer, requestBuffer.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            String permutations = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("\nPermutations:\n" + permutations);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
