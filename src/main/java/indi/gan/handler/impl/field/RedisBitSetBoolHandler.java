package indi.gan.handler.impl.field;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import indi.gan.handler.RedisHandler;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Component;

/**
 * @author GaN
 * @since 2022/8/15
 */
@Component
@Slf4j
public class RedisBitSetBoolHandler extends RedisHandler {
    
    @Override
    public boolean handler(final SetRedisValueDTO target) {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.IS_SET_BIT) {
            RBitSet bitSet = redissonClient.getBitSet(getKey(target));
            Long userId = null;
            if (ObjectUtil.isNotEmpty(annotation.args())) {
                Object property = BeanUtil.getProperty(target.getSource(), annotation.args()[0]);
                try {
                    userId = Long.valueOf(String.valueOf(property));
                } catch (NumberFormatException e) {
                    log.error("redisBitSetBoolHandler转long错误: {}", property);
                }
            }
            
            if (userId == null) {
                userId = target.getUserId();
            }
            
            if (userId != null) {
                return setFieldValue(target, bitSet.get(userId));
            }
        }
        return false;
    }
}
