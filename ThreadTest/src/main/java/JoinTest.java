import javax.sound.midi.Soundbank;

/**
 * Created by bonismo@hotmail.com
 * 下午5:32 on 17/11/17.
 */
public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        JoinThread t1 = new JoinThread("t1");
        JoinThread t2 = new JoinThread("t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("主线程开始执行");
    }
}

/**
 *
 t1>>10执行了0次
 t1>>10执行了1次
 t1>>10执行了2次
 t1>>10执行了3次
 t1>>10执行了4次
 t2>>11执行了0次
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
