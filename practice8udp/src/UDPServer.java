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

                String normalizedString = normalizeString(inputString);

                byte[] responseBuffer = normalizedString.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String normalizeString(String input) {
        String[] words = input.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            String normalizedWord = word.toLowerCase();
            normalizedWord = Character.toUpperCase(normalizedWord.charAt(0)) + normalizedWord.substring(1);
            result.append(normalizedWord).append(" ");
        }

        return result.toString().trim();
    }
}
