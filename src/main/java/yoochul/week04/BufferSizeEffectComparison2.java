package main.java.yoochul.week04;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class BufferSizeEffectComparison2 {
    private static final int BUFFER_SIZES = 32768; // 4K, 8K, 32K
    private static final String FILE_PATH = Paths.get("src", "main", "java", "yoochul", "week04", "sample.txt").toString();
    private static final String COPY_PATH = Paths.get("src", "main", "java", "yoochul", "week04", "sample_copy.txt").toString();
    private static final int BUFFER_SIZE = 8192;
    private static final int TEST_RUNS = 5;
    private static final int FILE_SIZE = 100_000_000; // 100MB file

    public static void main(String[] args) throws IOException {
        generateTestFile(FILE_PATH, FILE_SIZE);

        // 32K
        long[] buffer32K = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH, BUFFER_SIZES);
            long endTime = System.currentTimeMillis();
            buffer32K[i] = endTime - startTime;
        }
        System.out.println("32k 버퍼 시간: " + Arrays.toString(buffer32K));
        deleteFile(COPY_PATH);

        // 파일 통째로 버퍼에 넣기 1
        long[] bufferSameAsFileSize1 = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO(FILE_PATH, COPY_PATH, (int) Files.size(Paths.get(FILE_PATH)));
            long endTime = System.currentTimeMillis();
            bufferSameAsFileSize1[i] = endTime - startTime;
        }
        System.out.println("파일 사이즈 100MB 버퍼 시간: " + Arrays.toString(bufferSameAsFileSize1));
        deleteFile(COPY_PATH);

        // 파일 통째로 버퍼에 넣기 2 - MappedByteBuffer
        long[] bufferSameAsFileSize2 = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO_MappedByteBuffer(FILE_PATH, COPY_PATH);
            long endTime = System.currentTimeMillis();
            bufferSameAsFileSize2[i] = endTime - startTime;
        }
        System.out.println("파일 사이즈 100MB 버퍼 시간(MappedByteBuffer): " + Arrays.toString(bufferSameAsFileSize2));
        deleteFile(COPY_PATH);

        // 파일 통째로 버퍼에 넣기 3 - MappedByteBuffer, RandomAccessFile
        long[] bufferSameAsFileSize3 = new long[TEST_RUNS];
        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();
            fileNIO_MappedByteBuffer_RandomAccessFile(FILE_PATH, COPY_PATH);
            long endTime = System.currentTimeMillis();
            bufferSameAsFileSize3[i] = endTime - startTime;
        }
        System.out.println("파일 사이즈 100MB 버퍼 시간(MappedByteBuffer, RandomAccessFile): " + Arrays.toString(bufferSameAsFileSize3) + "\n");
        deleteFile(COPY_PATH);

        // 평균 시간 측정
        double avg32K = Arrays.stream(buffer32K).average().orElse(0);
        double avg100MB = Arrays.stream(bufferSameAsFileSize1).average().orElse(0);
        double avgRandom1 = Arrays.stream(bufferSameAsFileSize2).average().orElse(0);
        double avgRandom2 = Arrays.stream(bufferSameAsFileSize3).average().orElse(0);
        System.out.printf("32K 평균 걸린 시간: %.2f ms\n", avg32K);
        System.out.printf("100MB 평균 걸린 시간: %.2f ms\n", avg100MB);
        System.out.printf("100MB 평균 걸린 시간 (MappedByteBuffer): %.2f ms\n", avgRandom1);
        System.out.printf("100MB 평균 걸린 시간 (MappedByteBuffer, RandomAccessFile): %.2f ms\n", avgRandom2);
    }

    private static void deleteFile(String filePath) {
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("Failed to delete the file: " + e.getMessage());
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

    private static void fileNIO_MappedByteBuffer(String srcPath, String destPath) throws IOException {
        try (FileChannel sourceChannel = FileChannel.open(Paths.get(srcPath), StandardOpenOption.READ);
             FileChannel destChannel = FileChannel.open(Paths.get(destPath), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            // 파일 크기 가져오기
            long fileSize = sourceChannel.size();

            // 파일을 메모리에 매핑
            MappedByteBuffer buffer = sourceChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            // 메모리 매핑된 파일 내용을 대상 파일에 씀
            destChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fileNIO_MappedByteBuffer_RandomAccessFile(String srcPath, String destPath) throws IOException {
        try (RandomAccessFile sourceFile = new RandomAccessFile(FILE_PATH, "r");
             RandomAccessFile destFile = new RandomAccessFile(COPY_PATH, "rw");
             FileChannel sourceChannel = sourceFile.getChannel();
             FileChannel destChannel = destFile.getChannel()) {

            long size = sourceChannel.size();
            MappedByteBuffer buffer = sourceChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            destChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}