package main.java.yoochul.week03;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 텍스트 파일 I/O 예제
 */
class FileIO_Text {
    public static void main(String[] args) {
        String sourceFile = Paths.get("src", "main", "java", "yoochul", "week03", "sample.txt").toString();
        String destinationFile = Paths.get("src", "main", "java", "yoochul", "week03", "sample_copy.txt").toString();

        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("File copied successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
