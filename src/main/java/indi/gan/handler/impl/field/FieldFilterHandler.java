package indi.gan.handler.impl.field;


import indi.gan.annotation.ModelView;
import indi.gan.domain.FieldHandlerDTO;
import indi.gan.handler.FieldHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理{@link ModelView}注解
 *
 * @author GaN
 * @since 2022/9/26
 */
@Slf4j
@Component
@Order(50000)
public class FieldFilterHandler extends FieldHandler {
    /**
     * 如果不满足条件则返回false或抛出异常中断处理
     *
     * @param target 传递对象
     * @return 处理成功则返回true
     */
    @Override
    public boolean handler(final FieldHandlerDTO target) {
        ModelView fieldAnnotation = target.getField().getAnnotation(ModelView.class);
        if (fieldAnnotation == null) {return false;}
        
        ModelView methodAnnotation = target.getMethod().getAnnotation(ModelView.class);
        Set<Class<?>> collect;
        if (methodAnnotation == null) {
            collect = new HashSet<>();
        } else {
            collect = Arrays.stream(methodAnnotation.groups()).collect(Collectors.toSet());
        }
        
        // 方法注解分组是否包含字段注解分组
        boolean flag = true;
        if (!collect.isEmpty()) {
            for (Class<?> group : fieldAnnotation.groups()) {
                if (collect.contains(group)) {
                    flag = false;
                    break;
                }
            }
        }
        
        if (flag) {
            Field field = target.getField();
            field.setAccessible(true);
            try {
                field.set(target.getSource(),null);
            } catch (IllegalAccessException e) {
                log.error("过滤{}字段值失败",field.getName(), e);
            }
        }
    
        return true;
    }
}
