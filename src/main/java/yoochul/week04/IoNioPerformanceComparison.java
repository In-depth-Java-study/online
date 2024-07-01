package main.java.yoochul.week04;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class IoNioPerformanceComparison {
    private static final String FILE_PATH = Paths.get("src", "main", "java", "yoochul", "week04", "sample.txt").toString();
    private static final String COPY_PATH = Paths.get("src", "main", "java", "yoochul", "week04", "sample_copy.txt").toString();
    private static final int FILE_SIZE = 100_000_000; // 100MB file
    private static final int BUFFER_SIZE = 8192;
    private static final int TEST_RUNS = 3;

    public static void main(String[] args) throws IOException {
        generateTestFile(FILE_PATH, FILE_SIZE);

        // File I/O 측정
        long[] ioTimes = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileIO(FILE_PATH, COPY_PATH);
            long endTime = System.currentTimeMillis();
            ioTimes[i] = endTime - startTime;
        }
        System.out.println("File I/O 시간: " + Arrays.toString(ioTimes));

        // File NIO 측정
        long[] nioTimes = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH);
            long endTime = System.currentTimeMillis();
            nioTimes[i] = endTime - startTime;
        }
        System.out.println("File NIO 시간: " + Arrays.toString(nioTimes));

        // 평균 시간 측정
        double avgLocalTime = Arrays.stream(ioTimes).average().orElse(0);
        double avgNioTime = Arrays.stream(nioTimes).average().orElse(0);
        System.out.printf("File I/O 평균 걸린 시간: %.2f ms\n", avgLocalTime);
        System.out.printf("File NIO  평균 걸린 시간: %.2f ms\n", avgNioTime);

        // 누가 몇 퍼센트 빠른지 표기
        if (avgLocalTime != 0) {
            double percentageDifference = ((avgLocalTime - avgNioTime) / avgLocalTime) * 100;
            if (percentageDifference > 0) {
                System.out.printf("File NIO가 I/O 보다 %.2f%% 빠릅니다. \n", percentageDifference);
            } else {
                System.out.printf("File I/O가 NIO 보다 %.2f%%  \n", -percentageDifference);
            }
        } else {
            System.out.println("NIO와 IO 걸린시간은 동일합니다.");
        }

        System.out.println();
    }

    /**
     * 테스트할 파일을 생성한다.
     *
     * @param filePath 파일 생성할 위치
     * @param size 생성할 파일 사이즈
     */
    private static void generateTestFile(String filePath, int size) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            char[] buffer = new char[BUFFER_SIZE];
            Arrays.fill(buffer, 'A');
            int written = 0;
            while (written < size) {
                writer.write(buffer, 0, Math.min(BUFFER_SIZE, size - written));
                written += BUFFER_SIZE;
            }
        }
        System.out.println("Test file created.");
    }

    /**
     * IO를 이용해 파일 복붙
     *
     * @param srcPath 원본 파일 위치
     * @param destPath 복사할 파일 위치
     */
    private static void fileIO(String srcPath, String destPath) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcPath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destPath))) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * NIO를 이용해 파일 복붙
     *
     * @param srcPath 원본 파일 위치
     * @param destPath 복사할 파일 위치
     */
    private static void fileNIO(String srcPath, String destPath) throws IOException {
        try (FileChannel srcChannel = FileChannel.open(Paths.get(srcPath), StandardOpenOption.READ);
             FileChannel destChannel = FileChannel.open(Paths.get(destPath), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            while (srcChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    destChannel.write(buffer);
                }
                buffer.clear();
            }
        }
    }
}
