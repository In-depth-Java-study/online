package main.java.yoochul.week04;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Arrays;

public class DirectIndirectPerformanceComparison {
    private final static String SRC_FILE = Paths.get("src", "main", "java", "yoochul", "week04", "sample.txt").toString(); // 1GB 파일
    private final static String DEST_FILE_DIRECT = Paths.get("src", "main", "java", "yoochul", "week04", "sample_direct.txt").toString();
    private final static String DEST_FILE_INDIRECT = Paths.get("src", "main", "java", "yoochul", "week04", "sample_indirect.txt").toString();
    private static final int BUFFER_SIZE = 1024 * 1024; // 1MB
    private static final int FILE_SIZE = 1024 * 1024 * 1024; // 100MB

    public static void main(String[] args) throws IOException {
        generateTestFile(SRC_FILE, FILE_SIZE);

        // Direct Buffer Benchmark
        long directTime = benchmarkFileCopy(SRC_FILE, DEST_FILE_DIRECT, true);
        System.out.println("Direct: " + directTime + " ms");

        // Indirect Buffer Benchmark
        long indirectTime = benchmarkFileCopy(SRC_FILE, DEST_FILE_INDIRECT, false);
        System.out.println("Indirect: " + indirectTime + " ms");
    }

    private static void generateTestFile(String filePath, long size) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            char[] buffer = new char[BUFFER_SIZE];
            Arrays.fill(buffer, 'A');
            long written = 0;
            while (written < size) {
                int toWrite = (int) Math.min(BUFFER_SIZE, size - written);
                writer.write(buffer, 0, toWrite);
                written += toWrite;
            }
        }
    }

    private static long benchmarkFileCopy(String sourceFile, String destFile, boolean useDirectBuffer) throws IOException {
        long startTime = System.currentTimeMillis();

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile);
             FileChannel inputChannel = fis.getChannel();
             FileChannel outputChannel = fos.getChannel()) {

            ByteBuffer buffer = useDirectBuffer ? ByteBuffer.allocateDirect(BUFFER_SIZE) : ByteBuffer.allocate(BUFFER_SIZE);

            while (inputChannel.read(buffer) != -1) {
                buffer.flip();
                outputChannel.write(buffer);
                buffer.clear();
            }
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
