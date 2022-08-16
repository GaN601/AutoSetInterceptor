package indi.gan.annotation;

import indi.gan.enums.RedisType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * key = String.format(prefix,key)
 * <p>key() == 源对象对应字段值</p>
 * <p>在当前对象上加入{@link AutoSet}注解</p>
 * @author GaN
 * @since 2022/8/3
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SetRedisValue {
    /**
     * 获取当前对象的指定字段的属性值
     * @return 源对象值
     */
    String fieldValue() default "";
    
    /**
     * @return 赋值方式
     */
    RedisType type();
    
    /**
     * @return RedisKey前缀
     */
    String prefix();
    
    /**
     * @return 被注解对象的字段值, 填写字段名称
     */
    String[] args() default {};
}
