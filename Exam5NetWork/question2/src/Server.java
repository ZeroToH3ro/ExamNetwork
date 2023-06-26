import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        try {
            int serverPort = 8000;
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is running. Waiting for connection...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Receive the file path from client
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String filePath = inFromClient.readLine();
            System.out.println("Received request from client for file: " + filePath);

            // Read the file content
            List<Integer> array = readArrayFromFile(filePath);

            // Process the array
            List<Integer> primeNumbers = findPrimeNumbers(array);
            int secondLargest = findSecondLargest(array);
            replaceSecondLargest(array, secondLargest, 100);

            // Prepare the response
            StringBuilder response = new StringBuilder();
            response.append("Prime numbers: ").append(primeNumbers.toString()).append("\n");
            response.append("Array after replacing second largest element: ").append(array.toString());

            // Send the response to client
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            outToClient.println(response.toString());

            // Close the connections
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
            serverSocket.close();

            System.out.println("Server has sent the response to client and closed the connection.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> readArrayFromFile(String filePath) throws IOException {
        List<Integer> array = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = fileReader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                int number = Integer.parseInt(tokenizer.nextToken());
                array.add(number);
            }
        }

        fileReader.close();
        return array;
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

    private static List<Integer> findPrimeNumbers(List<Integer> array) {
        List<Integer> primeNumbers = new ArrayList<>();

        for (int number : array) {
            if (isPrime(number)) {
                primeNumbers.add(number);
            }
        }

        return primeNumbers;
    }

    private static int findSecondLargest(List<Integer> array) {
        int max = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;

        for (int number : array) {
            if (number > max) {
                secondLargest = max;
                max = number;
            } else if (number > secondLargest && number != max) {
                secondLargest = number;
            }
        }

        return secondLargest;
    }

    private static void replaceSecondLargest(List<Integer> array, int oldValue, int newValue) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == oldValue) {
                array.set(i, newValue);
            }
        }
    }
}
