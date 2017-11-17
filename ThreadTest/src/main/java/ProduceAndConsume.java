import java.util.ArrayList;
import java.util.List;

/**
 * Created by bonismo@hotmail.com
 * 下午4:26 on 17/11/17.
 */
public class ProduceAndConsume {
}

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

    public
}