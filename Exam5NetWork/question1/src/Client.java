import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        InetAddress serverAddress;
        int serverPort = 5000;

        try {
            clientSocket = new DatagramSocket();

            serverAddress = InetAddress.getLocalHost();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the sequence of real numbers (comma-separated): ");
            String data = scanner.nextLine();

            byte[] sendData = data.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("\nResponse from server:\n" + response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
