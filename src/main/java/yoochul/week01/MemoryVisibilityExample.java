package main.java.yoochul.week01;

/**
 * volatile은 가시성을 확보해준다.
 * WriterThread가 공유변수(counter)를 변경했을때 ReaderThread가 변경을 알기 위해서는 counter를 volatile로 만들어야 한다.
 */
class MemoryVisibilityExample {

    static class SharedData {
        /*volatile*/ int counter = 0; // volatile을 지정해줘야 ReaderThread가 공유변수의 변경을 알 수 있다.
    }

    static class ReaderThread extends Thread {
        private SharedData data;

        public ReaderThread(SharedData data) {
            this.data = data;
        }

        /**
         * 공유변수가 변경 되었다면 로컬변수 값도 변경시킨다. 공유변수가 WriterThread에 의해 10으로 증가될때 이 쓰레드는 종료되게 된다.
         * 하지만 공유변수 counter가 volatile이 아니라면 값의 바로 변경을 알지 못하게 된다.
         */
        public void run() {
            int localValue = data.counter;
            while (localValue < 10) {
                if (localValue != data.counter) {
                    localValue = data.counter;
                    System.out.println("1. ReaderThread: 업데이트된 counter 값: " + localValue);
                }
            }
        }
    }

    static class WriterThread extends Thread {
        private SharedData data;

        public WriterThread(SharedData data) {
            this.data = data;
        }

        /**
         * 공유 데이터인 SharedData의 counter를 10번 증가시킨다.
         * SharedData.counter가 volatile이어야지 증가했을때 로컬 캐시가 아닌 RAM에 바로 값을 변경시킨다.
         */
        public void run() {
            int localValue = data.counter;
            while (data.counter < 10) {
                System.out.println("2. WriterThread: counter 값 1씩 증가중 " + (localValue + 1));
                data.counter = ++localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SharedData data = new SharedData();
        new ReaderThread(data).start();
        new WriterThread(data).start();
    }
}
