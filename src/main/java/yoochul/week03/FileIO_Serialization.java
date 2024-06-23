package main.java.yoochul.week03;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;

/**
 * 직렬화 I/O 예제
 */
class FileIO_Serialization {
    // /Users/yoochulkim/IdeaProjects/YOOCHUL-GITHUB/online/src/main/java/yoochul/week03
//    private static final String serializedObjectFilePath = Paths.get("src", "main", "java", "yoochul", "week03", "person.ser").toString();
    private static final String serializedObjectFilePath = Paths.get("Users", "yoochulkim", "IdeaProjects", "YOOCHUL-GITHUB", "online", "src", "main", "java", "yoochul", "week03", "person.ser").toString();
    public static void main(String[] args) {
        Person person = new Person("Alice", 30);

        // 객체를 파일에 직렬화
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serializedObjectFilePath))) {
            oos.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일에서 객체를 역직렬화
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serializedObjectFilePath))) {
            Person deserializedPerson = (Person) ois.readObject();
            System.out.println("Name: " + deserializedPerson.name);
            System.out.println("Age: " + deserializedPerson.age);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class Person implements Serializable {
        private static final long serialVersionUID = 1L;
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
