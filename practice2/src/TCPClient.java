import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {
    public static void main(String[] args) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter an integer number: ");
            int number = Integer.parseInt(userInput.readLine());

            Socket socket = new Socket("localhost", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(number);

            String response = in.readLine();
            List<Integer> primeFactors = parsePrimeFactors(response);

            System.out.print("Prime factors: ");
            for (int factor : primeFactors) {
                System.out.print(factor + " x ");
            }
            System.out.println("\b\b ");

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> parsePrimeFactors(String response) {
        response = response.replace("[", "").replace("]", "");
        String[] factorsArray = response.split(", ");
        List<Integer> primeFactors = new ArrayList<>();
        for (String factorStr : factorsArray) {
            primeFactors.add(Integer.parseInt(factorStr));
        }
        return primeFactors;
    }
}
