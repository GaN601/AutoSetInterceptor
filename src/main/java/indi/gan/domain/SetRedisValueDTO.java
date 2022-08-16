package indi.gan.domain;

import indi.gan.annotation.SetRedisValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author GaN
 * @since 2022/8/3
 */
@Data
@AllArgsConstructor
public class SetRedisValueDTO {
    private SetRedisValue annotation;
    private Object source;
    private Field targetField;
}
