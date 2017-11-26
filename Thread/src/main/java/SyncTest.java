/**
 * Created by bonismo@hotmail.com
 * 下午9:12 on 17/11/26.
 */
public class SyncTest implements Runnable {

    int b = 100;

    public static void main(String[] args) throws InterruptedException {
        SyncTest syncTest = new SyncTest();
        Thread thread = new Thread(syncTest); // 1
        thread.start(); // 2
        syncTest.methodTwo(); // 3
        System.out.println("Main Thread b = " + syncTest.b); // 4
    }

    synchronized void methodOne() throws InterruptedException {
        b = 1000;
        Thread.sleep(1000); // 6
        System.out.println("b = " + b);
    }

    synchronized void methodTwo() throws InterruptedException {
        Thread.sleep(500); // 5
        b = 2000;
    }

    @Override
    public void run() {
        try {
            methodOne();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
