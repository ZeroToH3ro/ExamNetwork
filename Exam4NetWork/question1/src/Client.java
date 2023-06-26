import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    private static final int BUFFER_SIZE = 1024;
    private static final String END_MESSAGE = "END";

    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            int serverPort = 5000;

            DatagramSocket socket = new DatagramSocket();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập xâu ký tự: ");
            String inputString = scanner.nextLine();

            byte[] requestData = inputString.getBytes();
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length,
                    serverAddress, serverPort);
            socket.send(requestPacket);

            while (!inputString.equalsIgnoreCase(END_MESSAGE)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket responsePacket = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(responsePacket);

                String responseData = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println(responseData);

                System.out.print("Nhập xâu ký tự (hoặc 'END' để kết thúc): ");
                inputString = scanner.nextLine();

                requestData = inputString.getBytes();
                requestPacket.setData(requestData);
                socket.send(requestPacket);
            }

            System.out.println("Client stopped.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
