import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bonismo@hotmail.com
 * 下午11:37 on 17/11/16.
 */
public class ThreadTest {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool();
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
