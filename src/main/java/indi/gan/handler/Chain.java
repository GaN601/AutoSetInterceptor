package indi.gan.handler;

import cn.hutool.core.util.NumberUtil;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * @author GaN
 * @since 2022/7/29
 */
public class Chain<T> {
    private final TreeMap<Integer, List<Handler<T>>> handlers = new TreeMap<>();
    
    public Chain<T> addHandler(Handler<T> handler) {
        int score = Integer.MAX_VALUE;
        Class<? extends Handler> aClass = handler.getClass();
        Order annotation = aClass.getAnnotation(Order.class);
        if (annotation == null) {
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                annotation = anInterface.getAnnotation(Order.class);
                if (annotation != null) {
                    if (NumberUtil.compare(score, annotation.value()) >= 1) {
                        score = annotation.value();
                    }
                }
            }
        } else {
            score = annotation.value();
        }
        
        if (!handlers.containsKey(score)) {
            handlers.put(score, new ArrayList<>());
        }
        handlers.get(score).add(handler);
        return this;
    }
    
    public Chain<T> addHandler(Supplier<Handler<T>> handler) {
        Handler<T> apply = handler.get();
        return addHandler(apply);
    }
    
    
    /**
     * 允许处理成功的次数
     */
    private final Integer successCount;
    
    public Chain() {
        this(-1);
    }
    
    public Chain(final Integer successCount) {
        this.successCount = successCount;
    }
    
    /**
     * @param request 传入的参数会被所有处理器进行处理
     * @exception Exception 抛出异常表示中断当前链的处理
     */
    public Boolean process(T request) {
        int sum = 0;
        for (Integer integer : this.handlers.navigableKeySet()) {
            for (Handler<T> handler : this.handlers.get(integer)) {
                boolean flag = handler.handler(request);
                if (flag) {
                    sum++;
                }
                if (successCount != -1 && sum >= successCount) {
                    break;
                }
            }
        }
    
        return sum > 0;
    }
}
