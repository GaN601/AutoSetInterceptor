package indi.gan.handler;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author GaN
 * @date 2022/12/2
 */
public interface BusinessUtil {
    Long getUserId();
    
    
    /**
     * 对传入的data判断是否为可迭代对象, 如果是则对其迭代并执行f方法
     *
     * @param data 可能的可迭代对象, 不会对map的key进行迭代
     * @param f    如果可迭代, 则执行f,f的参数是可迭代对象内的元素.
     * @return 如果发生了迭代, 则返回true
     */
    static boolean ifIterableThenRun(Object data, Consumer<Object> f) {
        if (data instanceof Iterable) {
            ((Iterable<?>) data).forEach(f);
            return true;
        }
        
        if (data instanceof Map) {
            ((Map<?, ?>) data).values().forEach(f);
            return true;
        }
        return false;
    }
    
    /**
     * 如果data是可迭代对象, 则迭代调用f, f的参数是data中的元素.
     * <p>如果data不可迭代, 则直接调用f, f的参数是data</p>
     *
     * @param data 可能的可迭代对象, 不会对map的key进行迭代
     * @param f    要执行的方法
     * @return 如果发生了迭代, 则返回true. 如果没有发生迭代, 则会返回false, f方法还是会执行
     */
    static boolean iterableRun(Object data, Consumer<Object> f) {
        boolean b = ifIterableThenRun(data, f);
        if (b) {
            return true;
        } else {
            f.accept(data);
        }
        
        return false;
    }
}
