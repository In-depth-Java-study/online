package main.java.yoochul.week01;

/**
 * volatile은 원자성을 보장하지 않는다.
 * A와 B의 계좌 총합이 3000이어야 하지만 transfer (A 계좌에서 감소 후 B 계좌 증가) 중에 다른 쓰레드가 중간에 총합을 확인하면 3000이 되지 않는다.
 */
class VolatileAtomicityProblemExample {
    static class BankAccount {
        volatile int balance = 0;

        public BankAccount(int initialBalance) {
            this.balance = initialBalance;
        }
    }

    static class Bank {
        BankAccount accountA = new BankAccount(1000);
        BankAccount accountB = new BankAccount(2000);

        public void transferFromAToB(int amount) {
            accountA.balance -= amount;
            accountB.balance += amount;
        }

        public boolean isTotalBalanceCorrect() {
            return accountA.balance + accountB.balance == 3000;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                bank.transferFromAToB(1);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                if (!bank.isTotalBalanceCorrect()) {
                    System.out.println("비상: 은행 총 잔고가 3000이 아닙니다. 금고에 구멍이 난거 같습니다.");
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
