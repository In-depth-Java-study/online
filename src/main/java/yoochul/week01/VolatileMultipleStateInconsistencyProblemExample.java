package main.java.yoochul.week01;

/**
 * volatile은 복수의 상태를 관리할 수 없다.
 * 공유 객체의 필드 a, b가 동일해야 하지만 일관되지 않은 문제가 생긴다.
 */
class VolatileMultipleStateInconsistencyProblemExample {
    static class SharedData {
        volatile int a = 0;
        volatile int b = 0;

        public void update(int newValue) {
            a = newValue;
            b = newValue;
        }

        public boolean isConsistent() {
            return a == b;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SharedData sharedData = new SharedData();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                sharedData.update(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                if (!sharedData.isConsistent()) {
                    System.out.println("Inconsistent state detected: a=" + sharedData.a + ", b=" + sharedData.b);
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
