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

                int n = Integer.parseInt(new String(receivePacket.getData(), 0, receivePacket.getLength()));

                String permutations = getPermutations(n);

                byte[] responseBuffer = permutations.getBytes();

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPermutations(int n) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i + 1;
        }

        StringBuilder result = new StringBuilder();
        generatePermutations(nums, 0, result);
        return result.toString();
    }

    private static void generatePermutations(int[] nums, int index, StringBuilder result) {
        if (index == nums.length) {
            for (int num : nums) {
                result.append(num).append(" ");
            }
            result.append("\n");
        } else {
            for (int i = index; i < nums.length; i++) {
                swap(nums, index, i);
                generatePermutations(nums, index + 1, result);
                swap(nums, index, i);
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
