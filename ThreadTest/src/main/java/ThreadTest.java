import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by bonismo@hotmail.com
 * 下午11:37 on 17/11/16.
 *
 * 使用 ExecutorService、Callable、Future 实现有返回结果的多线程。
 */
public class ThreadTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(" --- 程序开始运行 ---");
        Date date1 = new Date();
        int taskSize = 5;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        // 创建有返回值的任务
        List<Future> list = new ArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            Callable callable = new MyCallable(i + "");
            Future future = pool.submit(callable);
            System.out.println(">>>" + future.get().toString());
            list.add(future);
        }
        pool.shutdown();

        // 获取所有并发运行结果
        for (Future future : list) {
            System.out.println(">>>" + future.get().toString());
        }

        Date date2 = new Date();
        System.out.println(" ---- 程序结束运行 ---- ");
        System.out.println(" ---- 程序运行时间 ---- ");
        System.out.println("【" + (date2.getTime() - date1.getTime()) + "毫秒 】");
    }
}

class MyCallable implements Callable<Object> {

    private String taskNumber;

    public MyCallable(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public Object call() throws Exception {
        System.out.println(">>>" + taskNumber + "任务启动");
        Date dateTmp1 = new Date();
        Thread.sleep(1000);
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        System.out.println(">>>" + taskNumber + "任务终止");
        return taskNumber + "任务返回运行结果，当前任务时间 【" + time + "毫秒】";
    }
}


/**
 --- 程序开始运行 ---
 >>>0任务启动
 >>>0任务终止
 >>>0任务返回运行结果，当前任务时间 【1002毫秒】
 >>>1任务启动
 >>>1任务终止
 >>>1任务返回运行结果，当前任务时间 【1005毫秒】
 >>>2任务启动
 >>>2任务终止
 >>>2任务返回运行结果，当前任务时间 【1001毫秒】
 >>>3任务启动
 >>>3任务终止
 >>>3任务返回运行结果，当前任务时间 【1004毫秒】
 >>>4任务启动
 >>>4任务终止
 >>>4任务返回运行结果，当前任务时间 【1005毫秒】
 >>>0任务返回运行结果，当前任务时间 【1002毫秒】
 >>>1任务返回运行结果，当前任务时间 【1005毫秒】
 >>>2任务返回运行结果，当前任务时间 【1001毫秒】
 >>>3任务返回运行结果，当前任务时间 【1004毫秒】
 >>>4任务返回运行结果，当前任务时间 【1005毫秒】
 ---- 程序结束运行 ----
 ---- 程序运行时间 ----
 【5033毫秒 】

 */