package indi.gan.handler.impl.field;


import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import indi.gan.handler.RedisHandler;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Component;

/**
 * @author GaN
 * @since 2022/8/3
 */
@Component
public class RedisBitSetHandler extends RedisHandler {
    
    @Override
    public boolean handler(final SetRedisValueDTO target) {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.BITSET) {
            RBitSet bitSet = redissonClient.getBitSet(getKey(target));
            
            return setFieldValue(target, bitSet.cardinality());
        }
        return false;
    }
}
