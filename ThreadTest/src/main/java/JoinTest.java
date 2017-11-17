/**
 * Created by bonismo@hotmail.com
 * 下午5:32 on 17/11/17.
 * <p>
 * Join 合并线程
 * 当子线程需要大量耗时运算，所以主线程必须等待子线程执行完成后再结束。
 */
public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        JoinThread t1 = new JoinThread("t1");
        JoinThread t2 = new JoinThread("t2");
        t1.start();
        t2.start();
        // 如果没有 join，合并线程，主方法可能在最初/中间执行完成
        // 如果此时主方法需要当前线程的数据时，是获取不到的。
        t1.join();
        t2.join();
        System.out.println("主线程开始执行");
    }
}

/**
 * t1>>10执行了0次
 * t1>>10执行了1次
 * t1>>10执行了2次
 * t1>>10执行了3次
 * t1>>10执行了4次
 * t2>>11执行了0次
 * t2>>11执行了1次
 * t2>>11执行了2次
 * t2>>11执行了3次
 * t2>>11执行了4次
 * <p>
 * t1和t2都执行完才继续主线程的执行，所谓合并，就是等待其它线程执行完，再执行当前线程，执行起来的效果就好像把其它线程合并到当前线程执行一样。
 */

/**
 * 去掉 join 后，主方法提前执行了
 t1>>10执行了0次
 t1>>10执行了1次
 t1>>10执行了2次
 t1>>10执行了3次
 主线程开始执行
 t2>>11执行了0次
 t1>>10执行了4次
 t2>>11执行了1次
 t2>>11执行了2次
 t2>>11执行了3次
 t2>>11执行了4次
 */


class JoinThread extends Thread {
    public JoinThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + ">>" + getId() + "执行了" + i + "次");
        }
    }
}
