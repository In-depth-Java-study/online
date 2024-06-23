package main.java.yoochul.week03;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Base64;

class NetworkIO_ByteToText_MistakeExample {
    public static void main(String[] args) {
        final int port = 8080;
        final String imagePath = Paths.get("src", "main", "java", "yoochul", "week03", "beer.jpg").toString();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    System.out.println("Client connected");

                    // HTTP 요청의 첫 줄을 읽음
                    String line = in.readLine();
                    if (line == null || !line.startsWith("GET")) {
                        // 유효하지 않은 요청 처리
                        continue;
                    }

                    // 요청 헤더를 건너뜀
                    while (!(line = in.readLine()).isEmpty()) {
                        // 빈 줄이 나올 때까지 읽기
                    }

                    // 이미지 파일 읽기
                    File file = new File(imagePath);
                    byte[] imageBytes = new byte[(int) file.length()];
                    try (FileInputStream fis = new FileInputStream(file)) {
                        fis.read(imageBytes);
                    }

                    // Base64 인코딩
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    System.out.println(base64Image);

                    // HTTP 응답 작성
                    String httpResponseHeader = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "\r\n";
                    String httpResponseBody = "<html>" +
                            "<body>" +
                            "<h1>Base64 Encoded Image</h1>" +
                            "<img src=\"data:image/jpeg;base64," + base64Image + "\" />" +
                            "</body>" +
                            "</html>";

                    // 응답 전송
                    out.write(httpResponseHeader);
                    out.write(httpResponseBody);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
