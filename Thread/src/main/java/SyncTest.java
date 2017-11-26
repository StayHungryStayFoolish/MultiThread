/**
 * Created by bonismo@hotmail.com
 * 下午9:12 on 17/11/26.
 *
 * 实现线程的同步
 * 工作原理：
 * 对需要同步的代码加锁，使得每次只能有一个线程可以进入同步块（一种悲观策略），从而保证线程安全。
 *
 * synchronized 用法：
 * 1、指定加锁对象：对给定对象加锁，进入同步代码前需要获得给定对象的锁
 * 2、直接作用域实例方法：相当于对当前实例加锁，进入同步代码前要获得当前实例的锁  （该测试使用，对当前实例加锁）
 * 3、直接所用于静态方法：相当于对当前类加锁，进入同步代码前需要获得当前类的锁
 *
 * Thread.sleep()
 * 使当前线程暂停执行，进入阻塞状态，让所 CPU 给其他线程执行，但是不会释放对象锁。
 * 即如果有  synchronized 同步块，其他线程仍然不能访问共享数据。
 *
 * 关键：
 * 第 3 步骤执行完成，子线程 methodOne 先执行还是 methodTwo 需要看谁先获取到锁
 */
public class SyncTest implements Runnable {

    int b = 100;

    /**
     * methodTwo 先执行，methodOne 再执行，所以 b 在方法1中最后再次赋值，b = 1000 固定输出
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SyncTest syncTest = new SyncTest();
        Thread thread = new Thread(syncTest); // 1 线程新建状态
        thread.start(); // 2 启动，进入就绪状态，并不一定执行，需要 CPU 调度
        System.out.println("实例调用");
        syncTest.methodTwo(); // 3 获得锁，start 需要调用 native 方法，并且在完成之后准备就绪，但并不一定在 CPU 上执行，有没有真正执行取决于 CPU 调度，之后才会调用 run 方法
        System.out.println("Main Thread b = " + syncTest.b); // 4
    }

    synchronized void methodOne() throws InterruptedException {
        b = 1000;
        Thread.sleep(500); // 6
        System.out.println("进入方法 1");
        System.out.println("b = " + b);
    }

    synchronized void methodTwo() throws InterruptedException {
        Thread.sleep(250); // 5
        b = 2000;
        System.out.println("进入方法 2");
    }

    @Override
    public void run() {
        System.out.println("进入 run 方法");
        try {
            methodOne(); // 3 步骤完成后，调用 run 方法，进入 methodOne() 方法
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/*

实例调用
进入 run 方法
进入方法 2
Main Thread b = 1000
进入方法 1
b = 1000

*/

// 或者

/*

进入 run 方法
实例调用
进入方法 1
b = 1000
进入方法 2
Main Thread b = 2000

*/