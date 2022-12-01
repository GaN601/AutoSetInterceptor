package indi.gan.dto;

import indi.gan.handler.BusinessUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author GaN
 * @since 2022/9/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AspectDTO {
    private Method aopMethod;
    /**
     * 要设置值的对象
     */
    private Object ret;
    private BusinessUtil webUtil;
}
