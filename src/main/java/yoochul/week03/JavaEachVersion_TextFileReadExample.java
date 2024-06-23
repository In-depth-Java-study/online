package main.java.yoochul.week03;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 자바 버전별 Text 파일 읽어오는 변화
 *
 * - FileInputStream + DataInputStream - Java 1.0
 * - BufferedReader와 FileReader - Java 1.1 - 6
 * - Scanner – Java 5
 * - Files.readAllBytes() – Java 7
 * - Files.lines() – Java 8
 * - Files.readString() – Java 11
 */
class JavaEachVersion_TextFileReadExample {
    private static final String textFilePath = Paths.get("src", "main", "java", "yoochul", "week03", "sample.txt").toString();

    public static void main(String[] args) {
        try {
            printFile1(textFilePath);
            lineSeperator();
            printFile2(textFilePath);
            lineSeperator();
            printFile3(textFilePath);
            lineSeperator();
            printFile4(textFilePath);
            lineSeperator();
            printFile5(textFilePath);
            lineSeperator();
            printFile6(textFilePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void lineSeperator() {
        System.out.println("\n------------------------------------------------");
    }

    /**
     * Java 1.0 - FileInputStream과 DataInputStream 조합
     */
    private static void printFile1(String filePath) throws IOException {
        System.out.println("Java 1.0 - FileInputStream과 DataInputStream 조합");
        FileInputStream fis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(filePath);
            dis = new DataInputStream(fis);

            String line;
            while ((line = dis.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) dis.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Java 1.1 - 6 - FileReader와 BufferedReader 조합
     */
    private static void printFile2(String filePath) throws IOException {
        System.out.println("Java 1.1 - 6 - FileReader와 BufferedReader 조합");
        try (FileReader fr = new FileReader(filePath);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java 5 - Scanner
     *
     * 다양한 입력 소스를 처리하려고 나옴
     */
    private static void printFile3(String filePath) throws IOException {
        System.out.println("Java 5 - Scanner");
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java 7 - Files.readAllBytes()
     *
     * 다양한 입력 소스를 처리하려고 나옴
     */
    private static void printFile4(String filePath) throws IOException {
        System.out.println("Java 7 - Files.readAllBytes()");
        try {
            byte[] bytes = Files.readAllBytes(Path.of(filePath));
            String content = new String(bytes);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java 8 - Files.lines()
     */
    private static void printFile5(String filePath) throws IOException {
        try {
            Files.lines(Path.of(filePath)).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java 11 - Files.readString()
     */
    private static void printFile6(String filePath) throws IOException {
        System.out.println("Java 11 - Files.readString()");
        try {
            String content = Files.readString(Path.of(filePath));
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
