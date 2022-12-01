package indi.gan.handler.impl.field;

import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.FieldHandlerDTO;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.handler.Chain;
import indi.gan.handler.FieldHandler;
import indi.gan.handler.RedisHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import java.lang.reflect.Field;
import java.util.Map;

/**
 * 如果以后要添加其他注解, 可以实现FieldHandler
 *
 * @author GaN
 * @since 2022/8/3
 */
@Component
public class FieldRedisHandler extends FieldHandler {
    private static final Chain<SetRedisValueDTO> REDIS_CHAIN = new Chain<>();
    private ApplicationContext applicationContext;
    
    @Autowired
    public void setApplicationContext(
            final ApplicationContext applicationContext) {this.applicationContext = applicationContext;}
    
    @PostConstruct
    public void init() {
        Map<String, RedisHandler> beansOfType = applicationContext.getBeansOfType(RedisHandler.class);
        beansOfType.forEach((key,value)-> REDIS_CHAIN.addHandler(value));
    }
    
    @Override
    public boolean handler(final FieldHandlerDTO target) {
        Field field = target.getField();
        SetRedisValue annotation = field.getAnnotation(SetRedisValue.class);
        if (annotation != null) {
            field.setAccessible(true);
            return REDIS_CHAIN.process(new SetRedisValueDTO(annotation,
                    target.getSource(),
                    field,
                    target.getWebUtil().getUserId()));
        }
        return false;
    }
}
