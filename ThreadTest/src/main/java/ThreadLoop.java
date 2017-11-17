/**
 * Created by bonismo@hotmail.com
 * 下午4:42 on 17/11/17.
 * <p>
 * 子线程循环10次，主线程循环100次，如此循环100次。
 */
public class ThreadLoop {
    public static void main(String[] args) {
        Business business = new Business();
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadExecute(business, "sub");
            }
        }).start();
        threadExecute(business, "main");
    }

    /**
     * 线程处理
     *
     * @param business
     * @param threadType
     */
    public static void threadExecute(Business business, String threadType) {
        for (int i = 0; i < 3; i++) {
            try {
                if ("main".equals(threadType)) {
                    business.main(i);
                } else {
                    business.sub(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Business {

    private boolean aBoolean = true;

    /**
     * 主线程
     *
     * @param loop
     * @throws InterruptedException
     */
    public synchronized void main(int loop) throws InterruptedException {
        while (aBoolean) {
            this.wait();
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("main thread seq of " + i + ", loop of " + loop);
        }
        aBoolean = true;
        this.notify();
    }

    /**
     * 子线程
     *
     * @param loop
     * @throws InterruptedException
     */
    public synchronized void sub(int loop) throws InterruptedException {
        while (!aBoolean) {
            this.wait();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("sub thread seq of " + i + ", loop of " + loop);
        }
        aBoolean = false;
        this.notify();
    }
}
