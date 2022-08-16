package indi.gan.annotation;

import indi.gan.handler.Handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在对象上添加AutoSet注解启用该功能, 但是FieldHandler也没处理这个注解, 暂未实现
 * @author GaN
 * @since 2022/8/2
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoSetValue {
    /**
     * 获取当前对象的指定字段的属性值
     * @return 源对象值
     */
    String fieldValue() default "";
    
    Class<? extends Handler<?>> handler();
    
    String prefix() default "";
    
    /**
     * @return 当前对象的字段值, 填写字段名称
     */
    String[] args() default {};
}
