package main.java.yoochul.week02;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionBasicExample {
    public static void main(String[] args) {
        try {
            // 클래스 로딩
            Class<?> clazz = Class.forName("main.java.yoochul.week02.Person");

            // 필드 접근
            Field field = clazz.getDeclaredField("name");
            field.setAccessible(true);
            Object object = clazz.newInstance();
            field.set(object, "김유철");
            Object fieldValue = field.get(object);
            System.out.println("필드 값: " + fieldValue);

            // 메서드 호출
            Method method1 = clazz.getDeclaredMethod("introduceMySelf");
            method1.setAccessible(true);
            Object returnValue1 = method1.invoke(object);
            System.out.println("자기소개: " + returnValue1);

            Method method2 = clazz.getDeclaredMethod("helloTo", String.class);
            method2.setAccessible(true);
            String friendName = "철수";
            Object returnValue2 = method2.invoke(object, friendName);
            System.out.println("친구한테 인사: " + returnValue2);

            // 생성자를 사용하여 객체 생성
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            Person newInstance = (Person) constructor.newInstance("김유철철철");
            System.out.println("이름: " + newInstance.getName());
            System.out.println("새로운 인스턴스: " + newInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Person {
    private String name;

    public Person() {
        this.name = "홍길동";
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String introduceMySelf() {
        return "Hello, I'm " + name;
    }

    public String helloTo(String friendName) {
        return "Hello, " + friendName;
    }
}
