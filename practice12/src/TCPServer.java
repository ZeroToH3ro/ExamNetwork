import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server started, listening on port 8888...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from: " + clientSocket.getInetAddress().getHostAddress());

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String sourcePath = in.readLine();
                String destinationPath = in.readLine();

                copyFiles(sourcePath, destinationPath);

                out.println("File/Folder copied successfully.");

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void copyFiles(String sourcePath, String destinationPath) throws IOException {
            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);

            if (sourceFile.isDirectory()) {
                copyDirectory(sourceFile, destinationFile);
            } else {
                copyFile(sourceFile, destinationFile);
            }
        }

        private void copyDirectory(File sourceDir, File destDir) throws IOException {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File[] files = sourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    File destFile = new File(destDir, file.getName());
                    if (file.isDirectory()) {
                        copyDirectory(file, destFile);
                    } else {
                        copyFile(file, destFile);
                    }
                }
            }
        }

        private void copyFile(File sourceFile, File destFile) throws IOException {
            try (InputStream in = new FileInputStream(sourceFile);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
}
