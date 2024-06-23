package main.java.yoochul.week03;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 네트워크 I/O - 텍스트 예제
 *
 * 크롬에서 http://localhost:8080/?name=John 로 요청
 */
class NetworkIO_Text {
    public static void main(String[] args) {
        final int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    System.out.println("Client connected");

                    // HTTP 요청의 첫 줄을 읽음
                    String line = in.readLine(); // GET /?name=John HTTP/1.1
                    if (line == null || !line.startsWith("GET")) {
                        // 유효하지 않은 요청 처리
                        System.out.println("유효하지 않은 요청");
                        continue;
                    }

                    // 이름 파라미터 추출
                    String name = "World"; // 기본 이름
                    String requestLine = line.split(" ")[1];
                    if (requestLine.contains("?name=")) {
                        name = requestLine.split("=")[1];
                    }

                    // HTTP 응답 작성
                    String httpResponse = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "\r\n" +
                            "<html>" +
                            "<body>" +
                            "<h1>Hello, " + name + "!</h1>" +
                            "</body>" +
                            "</html>";

                    out.write(httpResponse);
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
