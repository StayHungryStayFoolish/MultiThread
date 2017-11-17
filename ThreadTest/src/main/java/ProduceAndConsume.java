import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bonismo@hotmail.com
 * 下午4:26 on 17/11/17.
 * <p>
 * 假设一个盘子只能放一个鸡蛋，一次只能拿一个，一次只能放一个。
 */
public class ProduceAndConsume {

    public static void main(String[] args) {
        Plate plate = new Plate();
        for (int i = 0; i < 5; i++) {
            new Thread(new Plate.AddThread(plate)).start();
            new Thread(new Plate.GetThread(plate)).start();
        }
    }
}

/**
 *
 放入鸡蛋
 拿到鸡蛋
 放入鸡蛋
 拿到鸡蛋
 放入鸡蛋
 拿到鸡蛋
 放入鸡蛋
 拿到鸡蛋
 放入鸡蛋
 拿到鸡蛋
 */

// 放鸡蛋的盘子
class Plate{

    List eggs = new ArrayList();

    public synchronized Object getEgg() {
        // 建议使用 while 循环，因为 while 总会执行完再判断。if 则不会。
        while (eggs.size() == 0) {
            try {
                // 当没有鸡蛋时，线程开启等待，释放对象锁，进入对象等待池（wait pool），等待 notify()唤醒进入等锁池（lock pool）
                // 从而竞争锁，进入就绪状态
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Object egg = eggs.get(0);
        // 情况盘子
        eggs.clear();
        // 唤醒放鸡蛋的线程
        notify();
        System.out.println("拿到鸡蛋");
        return egg;
    }

    public synchronized void  putEgg(Object egg) {
        // 当盘子有一个鸡蛋时，线程等待
        while (eggs.size() > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        eggs.add(egg);
        notify();
        System.out.println("放入鸡蛋");
    }

    // 放入鸡蛋线程
    static class AddThread implements Runnable {

        // 盘子
        private Plate plate;
        // 鸡蛋
        private Object egg = new Object();

        public AddThread(Plate plate) {
            this.plate = plate;
        }

        @Override
        public void run() {
            plate.putEgg(egg);

        }
    }

    // 拿鸡蛋线程
    static class GetThread implements Runnable {

        private Plate plate;

        public GetThread(Plate plate) {
            this.plate = plate;
        }

        @Override
        public void run() {
            plate.getEgg();
        }
    }

}