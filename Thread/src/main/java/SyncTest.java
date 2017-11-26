/**
 * Created by bonismo@hotmail.com
 * 下午9:12 on 17/11/26.
 */
public class SyncTest implements Runnable {

    int b = 100;

    synchronized void methodOne() throws InterruptedException {
        b = 1000;
        Thread.sleep(1000);
        System.out.println("b = " + b);
    }

    synchronized void methodTwo() throws InterruptedException {
        Thread.sleep(500);
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

    public static void main(String[] args) throws InterruptedException {
        SyncTest syncTest = new SyncTest();
        Thread thread = new Thread(syncTest);
        thread.start();
        syncTest.methodTwo();
        System.out.println("Main Thread b = " + syncTest.b);
    }
}
