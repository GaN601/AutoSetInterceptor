package indi.gan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 过滤字段用的, 当方法上groups包含字段上的groups的元素时, 字段会显示内容, 否则被修改为null
 * @author GaN
 * @since 2022/9/26
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelView {
    Class<?>[] groups();
}
