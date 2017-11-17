import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Created by bonismo@hotmail.com
 * 下午3:27 on 17/11/17.
 * <p>
 * 线程安全单例模式
 */
public class LazyInitHolderSingleton {

    private LazyInitHolderSingleton() {
    }

    private static class SingletonHolder{
        private static final LazyInitHolderSingleton INSTANCE = new LazyInitHolderSingleton();
    }

    public static LazyInitHolderSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
