/**
 * Created by bonismo@hotmail.com
 * 下午5:15 on 17/11/17.
 * <p>
 * 休眠线程
 */
public class SleepTest {
    public static void main(String[] args) {
        // 创建共享对象
        Service service = new Service();
        // 创建线程
        SleepThread t1 = new SleepThread("t1", service);
        SleepThread t2 = new SleepThread("t2", service);
        // 启动线程
        t1.start();
        t2.start();
    }
}

/**
 * t1准备计算
 * t1感觉累了，开始睡觉
 * t1睡醒了，开始计算
 * t1计算完成
 * t2准备计算
 * t2感觉累了，开始睡觉
 * t2睡醒了，开始计算
 * t2计算完成
 */

class SleepThread extends Thread {
    private Service service;

    public SleepThread(String name, Service service) {
        super(name);
        this.service = service;
    }

    @Override
    public void run() {
        service.calc();
    }
}

class Service {
    public synchronized void calc() {
        System.out.println(Thread.currentThread().getName() + "准备计算");
        System.out.println(Thread.currentThread().getName() + "感觉累了，开始睡觉");
        try {
            // 睡1秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
        System.out.println(Thread.currentThread().getName() + "睡醒了，开始计算");
        System.out.println(Thread.currentThread().getName() + "计算完成");
    }
}
