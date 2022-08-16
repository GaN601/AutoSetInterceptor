package indi.gan.handler;


import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.FieldHandlerDTO;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.interceptor.AutoSetInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 如果以后要添加其他字段注解, 可以在这里增加FieldHandler, 然后在{@link AutoSetInterceptor}中的处理链中添加字段处理器
 * @author GaN
 * @since 2022/8/3
 */
@Component
public class FieldRedisHandler extends FieldHandler{
    private static final Chain<SetRedisValueDTO> REDIS_CHAIN = new Chain<>();
    private ApplicationContext applicationContext;
    
    @Autowired
    public void setApplicationContext(
            final ApplicationContext applicationContext) {this.applicationContext = applicationContext;}
    
    @PostConstruct
    public void init(){
        Map<String, RedisHandler> beansOfType = applicationContext.getBeansOfType(RedisHandler.class);
        beansOfType.forEach((key,value)-> REDIS_CHAIN.addHandler(value));
    }
    
    @Override
    public boolean handler(final FieldHandlerDTO target) throws Exception {
        Field field = target.getField();
        SetRedisValue annotation = field.getAnnotation(SetRedisValue.class);
        if (annotation != null) {
            field.setAccessible(true);
            return REDIS_CHAIN.process(new SetRedisValueDTO(annotation, target.getSource(), field));
        }
        return false;
    }
}
