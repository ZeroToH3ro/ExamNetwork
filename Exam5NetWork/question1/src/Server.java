import java.net.*;
import java.util.*;

public class Server {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        DatagramSocket serverSocket = null;

        try {
            int serverPort = 5000;
            serverSocket = new DatagramSocket(serverPort);

            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            System.out.println("Server is running. Waiting for data...");

            serverSocket.receive(receivePacket);
            String data = new String(receivePacket.getData(), 0, receivePacket.getLength());

            List<Double> numberList = parseNumberList(data);
            if (numberList != null) {
                List<Double> duplicateElements = findDuplicateElements(numberList);
                double sumOfPrimes = calculateSumOfPrimes(numberList);

                String response = generateResponse(duplicateElements, sumOfPrimes);
                sendResponseToClient(response, receivePacket.getAddress(), receivePacket.getPort());
            } else {
                sendResponseToClient("Invalid data format.", receivePacket.getAddress(), receivePacket.getPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    private static List<Double> parseNumberList(String data) {
        List<Double> numberList = new ArrayList<>();
        String[] tokens = data.split(",");

        try {
            for (String token : tokens) {
                double number = Double.parseDouble(token);
                numberList.add(number);
            }
            return numberList;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Double> findDuplicateElements(List<Double> numberList) {
        List<Double> duplicateElements = new ArrayList<>();
        Map<Double, Integer> elementCount = new HashMap<>();

        for (Double number : numberList) {
            int count = elementCount.getOrDefault(number, 0);
            elementCount.put(number, count + 1);
        }

        for (Map.Entry<Double, Integer> entry : elementCount.entrySet()) {
            if (entry.getValue() == 2) {
                duplicateElements.add(entry.getKey());
            }
        }

        return duplicateElements;
    }

    private static double calculateSumOfPrimes(List<Double> numberList) {
        double sum = 0;

        for (Double number : numberList) {
            if (isPrime(number.intValue())) {
                sum += number;
            }
        }

        return roundToOneDecimal(sum);
    }

    private static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    private static double roundToOneDecimal(double value) {
        return Math.round(value * 10) / 10.0;
    }

    private static String generateResponse(List<Double> duplicateElements, double sumOfPrimes) {
        StringBuilder response = new StringBuilder();

        if (!duplicateElements.isEmpty()) {
            response.append("Duplicate elements in the list: ");
            for (Double element : duplicateElements) {
                response.append(element).append(" ");
            }
            response.append("\n");
        } else {
            response.append("No duplicate elements found in the list.\n");
        }

        response.append("Sum of prime numbers: ").append(sumOfPrimes);

        return response.toString();
    }

    private static void sendResponseToClient(String response, InetAddress address, int port) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        byte[] sendData = response.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(sendPacket);
        socket.close();
    }
}
