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

                String reversedName = reverseName(inputString);

                byte[] responseBuffer = reversedName.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String reverseName(String fullName) {
        String[] nameParts = fullName.split("\\s+");
        StringBuilder reversedName = new StringBuilder();

        if (nameParts.length > 1) {
            for (int i = nameParts.length - 1; i >= 0; i--) {
                reversedName.append(nameParts[i]).append(" ");
            }
            reversedName.deleteCharAt(reversedName.length() - 1);
        } else {
            reversedName.append(fullName);
        }

        return reversedName.toString();
    }
}
