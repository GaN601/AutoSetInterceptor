package indi.gan.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author GaN
 * @since 2022/7/29
 */
public class Chain<T> {
    private final List<Handler<T>> handlers = new ArrayList<>();
    
    public Chain<T> addHandler(Handler<T> handler) {
        this.handlers.add(handler);
        return this;
    }
    
    public Chain<T> addHandler(Supplier<Handler<T>> handler) {
        Handler<T> apply = handler.get();
        
            return addHandler(apply);
    }
    
    
    /**
     * 允许处理成功的次数
     */
    private Integer successCount;
    
    public Chain() {
        this(-1);
    }
    public Chain(final Integer successCount) {
        this.successCount = successCount;
    }
    
    /**
     * @param request 传入的参数会被所有处理器进行处理
     * @return
     */
    public Boolean process(T request) throws Exception {
        int sum = 0;
        for (Handler<T> handler : this.handlers) {
            boolean flag = handler.handler(request);
            if (flag) {
                sum++;
            }
    
            if (successCount != -1 && sum >= successCount) {
                break;
            }
        }
        
        return sum > 0;
    }
}
