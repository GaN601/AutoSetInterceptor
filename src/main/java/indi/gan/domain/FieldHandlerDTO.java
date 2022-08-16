package indi.gan.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author GaN
 * @since 2022/8/3
 */
@Data
@AllArgsConstructor
public class FieldHandlerDTO {
    private Object source;
    private Field field;
}
