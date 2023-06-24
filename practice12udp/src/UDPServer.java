import java.io.*;
import java.net.*;

public class UDPServer {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(8888);
            System.out.println("Server started, listening on port 8888...");

            byte[] receiveBuffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String sourcePath = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Source path received: " + sourcePath);

                File sourceFile = new File(sourcePath);

                if (sourceFile.exists()) {
                    if (sourceFile.isFile()) {
                        sendFile(sourceFile, receivePacket.getAddress(), receivePacket.getPort());
                    } else if (sourceFile.isDirectory()) {
                        sendDirectory(sourceFile, receivePacket.getAddress(), receivePacket.getPort());
                    }
                } else {
                    System.out.println("Source file/folder does not exist.");
                    sendErrorMessage(receivePacket.getAddress(), receivePacket.getPort());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(File file, InetAddress clientAddress, int clientPort) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        DatagramSocket socket = new DatagramSocket();

        byte[] sendBuffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = bis.read(sendBuffer)) != -1) {
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, bytesRead, clientAddress, clientPort);
            socket.send(sendPacket);
            sendBuffer = new byte[BUFFER_SIZE];
        }

        bis.close();
        socket.close();

        System.out.println("File sent successfully.");
    }

    private static void sendDirectory(File directory, InetAddress clientAddress, int clientPort) throws IOException {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                sendFile(file, clientAddress, clientPort);
            } else if (file.isDirectory()) {
                sendDirectory(file, clientAddress, clientPort);
            }
        }
    }

    private static void sendErrorMessage(InetAddress clientAddress, int clientPort) throws IOException {
        String errorMessage = "File/folder does not exist.";
        byte[] errorBuffer = errorMessage.getBytes();

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket errorPacket = new DatagramPacket(errorBuffer, errorBuffer.length, clientAddress, clientPort);
        socket.send(errorPacket);
        socket.close();
    }
}
