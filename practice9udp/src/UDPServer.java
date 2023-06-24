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

                String[] words = inputString.split("\\s+");
                String longestWord = findLongestWord(words);
                int position = findWordPosition(inputString, longestWord);

                String response = "Longest word: " + longestWord + ", Position: " + position;

                byte[] responseBuffer = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findLongestWord(String[] words) {
        String longestWord = "";

        for (String word : words) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }

        return longestWord;
    }

    public static int findWordPosition(String inputString, String word) {
        int position = inputString.indexOf(word);
        return position + 1; // Add 1 to get 1-based index
    }
}
