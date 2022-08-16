package indi.gan.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Component;

/**
 * 根据参数将指定BitSet位置结果设置到字段上
 * @author GaN
 * @since 2022/8/15
 */
@Component
@Slf4j
public class RedisBitSetBoolHandler extends RedisHandler {
    
    @Override
    public boolean handler(final SetRedisValueDTO target) throws Exception {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.IS_SET_BIT) {
            RBitSet bitSet = redissonClient.getBitSet(getKey(target));
            Long bit = null;
            if (ObjectUtil.isNotEmpty(annotation.args())) {
                Object property = BeanUtil.getProperty(target.getSource(), annotation.args()[0]);
                try {
                    bit = Long.valueOf(String.valueOf(property));
                } catch (NumberFormatException e) {
                    log.error("redisBitSetBoolHandler转long错误: {}", property);
                }
            }
            
            if (bit != null) {
                return setFieldValue(target, bitSet.get(bit));
            }
        }
        return false;
    }
}
