package indi.gan.handler;

/**
 * @author GaN
 * @since 2022/7/29
 */
public interface Handler<T> {
    
    /**
     * 如果不满足条件则返回false或抛出异常中断处理
     * @param target
     * @throws Exception 抛出异常表示中断处理
     * @return 处理成功则返回true
     */
    boolean handler(T target) throws Exception;
    
    default void throwException(String message) throws Exception{
        throw new Exception(message);
    }
}
