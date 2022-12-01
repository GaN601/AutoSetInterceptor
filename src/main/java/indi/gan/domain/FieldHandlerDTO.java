package indi.gan.domain;

import indi.gan.handler.BusinessUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author GaN
 * @since 2022/8/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldHandlerDTO {
    /**
     * 当前对象
     */
    private Object source;
    /**
     * 被注解标记的字段
     */
    private Field field;
    /**
     * 被代理的方法
     */
    private Method method;
    private BusinessUtil webUtil;
}
