package indi.gan.handler.impl.field;


import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import indi.gan.handler.RedisHandler;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Component;

/**
 * @author GaN
 * @since 2022/8/3
 */
@Component
public class RedisStringHandler extends RedisHandler {
    
    @Override
    public boolean handler(final SetRedisValueDTO target) {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.STRING) {
            RBucket<Object> bucket = redissonClient.getBucket(getKey(target));
            return setFieldValue(target, bucket.get());
        }
        return false;
    }
}
