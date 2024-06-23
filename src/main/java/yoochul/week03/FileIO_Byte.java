package main.java.yoochul.week03;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 바이너리 파일 I/O 예제
 */
class FileIO_Byte {
    public static void main(String[] args) {
        String sourceFile = Paths.get("src", "main", "java", "yoochul", "week03", "beer.jpg").toString();
        String destFile = Paths.get("src", "main", "java", "yoochul", "week03", "beer_copy.jpg").toString();

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
