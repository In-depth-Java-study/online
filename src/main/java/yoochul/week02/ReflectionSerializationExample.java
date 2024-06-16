package main.java.yoochul.week02;

// 실행 위해서 Jackson 라이브러리 필요

/*import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class ReflectionSerializationExample {
    public static void main(String[] args) throws Exception {
        // ObjectMapper 객체 생성
        ObjectMapper mapper = new ObjectMapper();

        // Person 객체 생성
        Person person = new Person("Bob", 25);

        // Java 객체를 Map으로 변환
        Map<String, Object> map = convertObjectToMap(person);

        // Map을 JSON 문자열로 직렬화
        String json = mapper.writeValueAsString(map);
        System.out.println("직렬화된 JSON 데이터: " + json);

        // JSON 문자열을 Map으로 역직렬화
        Map<String, Object> deserializedMap = mapper.readValue(json, HashMap.class);

        // Map을 다시 Java 객체로 변환
        Person deserializedPerson = convertMapToObject(deserializedMap, Person.class);
        System.out.println("역직렬화된 Person 객체: " + deserializedPerson.getName() + ", " + deserializedPerson.getAge());
    }

    // 객체를 Map으로 변환하는 메서드 (Reflection 사용)
    public static Map<String, Object> convertObjectToMap(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        Map<String, Object> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }
        return map;
    }

    // Map을 객체로 변환하는 메서드 (Reflection 사용)
    public static <T> T convertMapToObject(Map<String, Object> map, Class<T> clazz) throws Exception {
        T object = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(object, map.get(field.getName()));
            }
        }
        return object;
    }

    class Person {
        private String name;
        private int age;

        // 기본 생성자와 getter/setter 메서드들
        public Person() {}

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
}*/
