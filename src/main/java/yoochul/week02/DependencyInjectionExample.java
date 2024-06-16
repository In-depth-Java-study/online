package main.java.yoochul.week02;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

class DependencyInjectionExample {
    public static void main(String[] args) {
        try {
            // 클래스 로딩
            Class<?> serviceClass = Class.forName("main.java.yoochul.week02.DependencyInjectionExample$Service");
            Class<?> controllerClass = Class.forName("main.java.yoochul.week02.DependencyInjectionExample$Controller");

            // 인스턴스 생성
            Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

            // 필드에 @Autowired 애노테이션이 있는지 확인하고 주입
            for (Field field : controllerClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(controllerInstance, serviceInstance);
                }
            }

            // 의존성 주입 잘 되었는지 체크
            controllerClass.getMethod("doSomething").invoke(controllerInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Autowired {
    }

    static class Service {
        public void doSomething() {
            System.out.println("서비스 doSomething...");
        }
    }

    static class Controller {
        @Autowired
        private Service service;

        public void doSomething() {
            service.doSomething();
            System.out.println("컨트롤러 doSomething...");
        }
    }
}
