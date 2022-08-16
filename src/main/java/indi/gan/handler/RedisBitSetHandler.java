package indi.gan.handler;

import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Component;

/**
 * 将对应的BitSet所有为1的数量赋值到字段上
 * @author GaN
 * @since 2022/8/3
 */
@Component
public class RedisBitSetHandler extends RedisHandler{
    
    @Override
    public boolean handler(final SetRedisValueDTO target) throws Exception {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.BITSET) {
            RBitSet bitSet = redissonClient.getBitSet(getKey(target));
            
            return setFieldValue(target, bitSet.cardinality());
        }
        return false;
    }
}
