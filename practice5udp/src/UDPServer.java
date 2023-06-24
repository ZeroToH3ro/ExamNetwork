import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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
                int n = Integer.parseInt(inputString);

                List<Integer> fibonacciNumbers = generateFibonacciNumbers(n);
                List<Integer> primeNumbers = findPrimeNumbers(fibonacciNumbers);

                String response = "Prime Fibonacci numbers less than " + n + ": " + primeNumbers.toString();
                byte[] responseBuffer = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> generateFibonacciNumbers(int n) {
        List<Integer> fibonacciNumbers = new ArrayList<>();

        int a = 0;
        int b = 1;

        while (b < n) {
            fibonacciNumbers.add(b);
            int temp = a + b;
            a = b;
            b = temp;
        }

        return fibonacciNumbers;
    }

    private static List<Integer> findPrimeNumbers(List<Integer> numbers) {
        List<Integer> primeNumbers = new ArrayList<>();

        for (int number : numbers) {
            if (isPrime(number)) {
                primeNumbers.add(number);
            }
        }

        return primeNumbers;
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
}
