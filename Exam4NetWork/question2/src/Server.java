import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(5000);
            System.out.println("Server is running. Waiting for data...");

            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            serverSocket.receive(receivePacket);
            String fileName = new String(receivePacket.getData()).trim();
            System.out.println("Received request from client for file: " + fileName);

            List<Double> studentScores = readStudentScoresFromFile(fileName);
            if (studentScores.isEmpty()) {
                System.out.println("File is empty or does not exist.");
                return;
            }

            double classAverage = calculateClassAverage(studentScores);

            List<String> highScoringStudents = findHighScoringStudents(studentScores, classAverage);

            StringBuilder response = new StringBuilder();
            response.append("Class Average: ").append(classAverage).append("\n");
            response.append("High Scoring Students:\n");
            for (String student : highScoringStudents) {
                response.append(student).append("\n");
            }

            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            byte[] sendData = response.toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);

            System.out.println("Response sent to client.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    private static List<Double> readStudentScoresFromFile(String fileName) {
        List<Double> studentScores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                double score = Double.parseDouble(line);
                studentScores.add(score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentScores;
    }

    private static double calculateClassAverage(List<Double> studentScores) {
        double sum = 0;
        for (double score : studentScores) {
            sum += score;
        }
        return sum / studentScores.size();
    }

    private static List<String> findHighScoringStudents(List<Double> studentScores, double classAverage) {
        List<String> highScoringStudents = new ArrayList<>();
        for (int i = 0; i < studentScores.size(); i++) {
            double score = studentScores.get(i);
            if (score >= classAverage) {
                String studentName = "Student " + (i + 1) + ": " + score;
                highScoringStudents.add(studentName);
            }
        }
        return highScoringStudents;
    }
}
