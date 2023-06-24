import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                String dataString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] dataArray = dataString.split(",");

                List<Double> numberList = new ArrayList<>();
                for (String numberString : dataArray) {
                    double number = Double.parseDouble(numberString);
                    numberList.add(number);
                }

                List<Double> uniqueElements = getUniqueElements(numberList);

                String response = "Unique elements: " + uniqueElements.toString();
                byte[] responseBuffer = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Double> getUniqueElements(List<Double> numberList) {
        Map<Double, Integer> countMap = new HashMap<>();

        for (double number : numberList) {
            countMap.put(number, countMap.getOrDefault(number, 0) + 1);
        }

        List<Double> uniqueElements = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                uniqueElements.add(entry.getKey());
            }
        }

        return uniqueElements;
    }
}
