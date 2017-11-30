/**
 * Created by bonismo@hotmail.com
 * 上午1:33 on 17/12/1.
 */
public class SyncDifferentTest {

    public static void main(String[] args) {
        final DualSync dualSync = new DualSync();
        new Thread() {
            public void run() {
                dualSync.a();
            }
        }.start();
        dualSync.b();
    }
}

/**
 a() 方法级别锁
 b() 对象级别锁
 a() 方法级别锁
 b() 对象级别锁
 a() 方法级别锁
 b() 对象级别锁
 a() 方法级别锁
 b() 对象级别锁
 a() 方法级别锁
 b() 对象级别锁

 结果并不是 a() 运行5次，让线程给 b()再运行5次。
 因为方法 a() 并不需要获取 b() 的对象锁。所有是并列运行。

 想要正确输出结果：
 1.更改 b() 为方法级别的锁
 或者
 2.更改 a() 为对象级别的锁
 */

class DualSync{
    private Object syncObject = new Object();

    public synchronized void a() {
        for (int i = 0; i < 5; i++) {
            System.out.println("a() 方法级别锁");
            Thread.yield();
        }
    }

    public synchronized void b() {
        synchronized (syncObject) {
            for (int i = 0; i < 5; i++) {
                System.out.println("b() 对象级别锁");
                 Thread.yield();
            }
        }
    }
}