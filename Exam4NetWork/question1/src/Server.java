import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static final String END_MESSAGE = "END";

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(5000);

            byte[] buffer = new byte[BUFFER_SIZE];

            System.out.println("Server started. Listening on port 5000...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(packet);

                String receivedData = new String(packet.getData(), 0, packet.getLength());

                if (receivedData.equalsIgnoreCase(END_MESSAGE)) {
                    break;
                }

                String[] words = receivedData.split("\\s+");

                int wordCount = words.length;
                Map<String, Integer> wordFrequency = getWordFrequency(words);

                String response = "Xâu vừa nhập có " + wordCount + " từ.\n";
                for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                    String word = entry.getKey();
                    int frequency = entry.getValue();
                    response += word + " số lần xuất hiện " + frequency + ".\n";
                }

                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);

                buffer = new byte[BUFFER_SIZE]; // Reset the buffer
            }

            System.out.println("Server stopped.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> getWordFrequency(String[] words) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String word : words) {
            if (frequencyMap.containsKey(word)) {
                int count = frequencyMap.get(word);
                frequencyMap.put(word, count + 1);
            } else {
                frequencyMap.put(word, 1);
            }
        }

        return frequencyMap;
    }
}
