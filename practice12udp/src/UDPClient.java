import java.io.*;
import java.net.*;

public class UDPClient {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 8888;

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the source file/folder path: ");
            String sourcePath = userInput.readLine();

            byte[] requestBuffer = sourcePath.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(requestBuffer, requestBuffer.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            byte[] receiveBuffer = new byte[BUFFER_SIZE];

            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

            if (response.startsWith("Error")) {
                System.out.println("Server response: " + response);
            } else {
                File destinationFile = new File("destination/" + sourcePath);

                FileOutputStream fos = new FileOutputStream(destinationFile);

                while (true) {
                    clientSocket.receive(receivePacket);

                    byte[] data = receivePacket.getData();
                    int bytesRead = receivePacket.getLength();

                    if (bytesRead < BUFFER_SIZE) {
                        fos.write(data, 0, bytesRead);
                        break;
                    }

                    fos.write(data, 0, bytesRead);
                }

                fos.close();

                System.out.println("File/folder copied successfully.");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
