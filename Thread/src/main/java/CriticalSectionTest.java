import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bonismo@hotmail.com
 * 下午10:25 on 17/11/30.
 * <p>
 * 临界区测试
 */
public class CriticalSectionTest {

    public static void main(String[] args) {
        PairManage manage1 = new PairManager1();
        PairManage manage2 = new PairManager2();

        testMethod(manage1,manage2);
    }

    /**
     运行测试结果：

     方法锁级别：
     pm1 :Pair Pair ==>x:515, y:515checkCounter = 355
     对象锁级别（直接对临界区进行控制）：
     pm2 :Pair Pair ==>x:511, y:511checkCounter = 26053172

     方法锁（同步整个方法），一个线程在改方法独占当前方法资源，无论这个方法中是否有线程安全。效率低
     对象锁（锁临界区），只在需要线程安全的地方加锁。效率高。
     检查数量 checkCounter 并不代表效率

     */

    public static void testMethod(PairManage manage1, PairManage manage2) {
        // 建立线程池
        ExecutorService service = Executors.newCachedThreadPool();

        // 自增操作临界区
        PairManipulator pm1 = new PairManipulator(manage1);
        PairManipulator pm2 = new PairManipulator(manage2);

        // 检查临界区
        PairChecker checker1 = new PairChecker(manage1);
        PairChecker checker2 = new PairChecker(manage2);

        // 线程处理
        service.execute(pm1);
        service.execute(pm2);
        service.execute(checker1);
        service.execute(checker2);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("pm1 :"+pm1+ "\npm2 :"+pm2);
        System.exit(0);
    }
}

/**
 * I、
 * 定义一个坐标类
 * x,y 每次自增1
 * 如果 x != y,抛出自定义运行时异常
 */
class Pair {

    private int x;
    private int y;

    public Pair() {
        this(0, 0);
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // X 自增
    public void incrementX() {
        x++;
    }

    // Y 自增
    public void incrementY() {
        y++;
    }

    @Override
    public String toString() {
        return "Pair ==>" +
                "x:" + x +
                ", y:" + y;
    }

    // 检查问题
    public void checkState() {
        if (x != y) {
            System.err.println("x != y");
            throw new PairValuesNotEqualException();
        }
    }

    // 自定义运行时异常，当 x!=y 时，抛出
    public class PairValuesNotEqualException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public PairValuesNotEqualException() {
            super("Pair values not equal : "+ Pair.this);
        }
    }
}

/**
 * II、
 * 对 Pair 类管理
 * java.util.concurrent.atomic 包内的类，
 * 提供了原子操作的类，在 Java 语言中,i++,++i,并不是原子操作。
 * 在使用中，如果需要保证原子操作，需要使用 synchronized 关键字。
 * 而 Atomic 的类通过线程安全操作保证了原子性。
 *
 * AtomicXXX 类是硬件提供原子操作指令实现的，在非激烈竞争情况下，开销更小，速度更快。
 * 源码：
 * Unsafe 是 Java 提供获得对象内存地址访问的类。作用是在更新操作时，提供"比较并替换"的作用。
 * valueOffset 用来记录 value 域本身在 内存的偏移地址，该记录为了更新操作在内存中找到 value 的位置
 *
 * 优点：
 * 最大好处就是避免多线程的优先级导致和死锁情况发生，提升在高并发处理下的性能。
 *
 */
abstract class PairManage {
    // check x != y 次数
    AtomicInteger  checkCounter = new AtomicInteger(0);
    protected Pair pair = new Pair();
    // 调用 Collections.synchronizedList(); 方法，是 List 变成线程安全的
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());

    // 定义自增长 临界区
    public abstract void increment();

    public synchronized Pair getPair() {
        return new Pair(pair.getX(), pair.getY());
    }

    protected void store(Pair pair) {
        // 安全线程添加元素
        storage.add(pair);
        try {
            TimeUnit.MICROSECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class PairManager1 extends PairManage {

    // 方法级别锁
    @Override
    public synchronized void increment() {
        pair.incrementX();
        pair.incrementY();
        store(getPair());
    }
}

class PairManager2 extends PairManage {

    // 对象界别锁，锁定的是临界区
    @Override
    public void increment() {
        Pair temp;
        synchronized (this) {
            pair.incrementX();
            pair.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}

/**
 * 任务类1，不断使用 PairManager 对 Pair 进行自增操作
 */
class PairManipulator implements Runnable {

    private PairManage manage;

    public PairManipulator(PairManage manage) {
        this.manage = manage;
    }

    @Override
    public void run() {
        while (true) {
            manage.increment();
        }
    }

    @Override
    public String toString() {
        return "Pair " + manage.getPair() + "checkCounter = " + manage.checkCounter.get();
    }
}

/**
 * 检查 x == y 的状态
 */
class PairChecker implements Runnable {

    private PairManage manage;

    public PairChecker(PairManage manage) {
        this.manage = manage;
    }

    @Override
    public void run() {
        while (true) {
            manage.checkCounter.incrementAndGet();
            manage.getPair().checkState();
        }
    }
}