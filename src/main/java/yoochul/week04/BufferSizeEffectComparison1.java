package main.java.yoochul.week04;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * 파일을 A에서 B로 복사할때 버퍼 사이즈가 성능에 영향을 미치는지 테스트한다.
 * 버퍼 사이즈: 4K, 8K, 32K, 그리고 파일크기와 동일한 크기
 */
public class BufferSizeEffectComparison1 {
    private static final int[] BUFFER_SIZES = {4096, 8192, 32768}; // 4K, 8K, 32K
    private static final String FILE_PATH = Paths.get("src", "main", "java", "yoochul", "week04", "sample.txt").toString();
    private static final String COPY_PATH = Paths.get("src", "main", "java", "yoochul", "week04", "sample_copy.txt").toString();
    private static final int BUFFER_SIZE = 8192;
    private static final int TEST_RUNS = 5;
    private static final int FILE_SIZE = 100_000_000; // 100MB file

    public static void main(String[] args) throws IOException {
        generateTestFile(FILE_PATH, FILE_SIZE);

        warmUpCpu();

        // 4K
        long[] buffer4K = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH, BUFFER_SIZES[0]);
            long endTime = System.currentTimeMillis();
            buffer4K[i] = endTime - startTime;
        }
        System.out.println("4k 버퍼 시간: " + Arrays.toString(buffer4K));

        // 8K
        long[] buffer8K = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH, BUFFER_SIZES[1]);
            long endTime = System.currentTimeMillis();
            buffer8K[i] = endTime - startTime;
        }
        System.out.println("8k 버퍼 시간: " + Arrays.toString(buffer8K));

        // 32K
        long[] buffer32K = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH, BUFFER_SIZES[2]);
            long endTime = System.currentTimeMillis();
            buffer32K[i] = endTime - startTime;
        }
        System.out.println("32k 버퍼 시간: " + Arrays.toString(buffer32K));

        // 파일 통째로 버퍼에 넣기
        long[] bufferSameAsFileSize = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH, (int) Files.size(Paths.get(FILE_PATH)));
            long endTime = System.currentTimeMillis();
            bufferSameAsFileSize[i] = endTime - startTime;
        }
        System.out.println("파일 사이즈 100MB 버퍼 시간: " + Arrays.toString(bufferSameAsFileSize) + "\n");

        // 평균 시간 측정
        double avg4K = Arrays.stream(buffer4K).average().orElse(0);
        double avg8K = Arrays.stream(buffer8K).average().orElse(0);
        double avg32K = Arrays.stream(buffer32K).average().orElse(0);
        double avg100MB = Arrays.stream(bufferSameAsFileSize).average().orElse(0);
        System.out.printf("4K 평균 걸린 시간: %.2f ms\n", avg4K);
        System.out.printf("8K 평균 걸린 시간: %.2f ms\n", avg8K);
        System.out.printf("32K 평균 걸린 시간: %.2f ms\n", avg32K);
        System.out.printf("100MB 평균 걸린 시간: %.2f ms\n", avg100MB);
    }

    private static void warmUpCpu() {
        int sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;
        }
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

    private static void fileNIO(String srcPath, String destPath, int bufferSize) throws IOException {
        try (FileChannel srcChannel = FileChannel.open(Paths.get(srcPath), StandardOpenOption.READ);
             FileChannel destChannel = FileChannel.open(Paths.get(destPath), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
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