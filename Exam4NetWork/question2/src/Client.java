import java.io.*;
import java.net.*;

public class Client {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 5000;

            clientSocket = new DatagramSocket();

            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter file name: ");
            String fileName = userInputReader.readLine();

            byte[] sendData = fileName.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);
            System.out.println("Request sent to server.");

            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("\nServer Response:\n" + serverResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
