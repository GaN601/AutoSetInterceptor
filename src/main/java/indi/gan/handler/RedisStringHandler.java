package indi.gan.handler;


import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import org.redisson.api.RBucket;

/**
 * @author GaN
 * @since 2022/8/3
 */
public class RedisStringHandler extends RedisHandler{
    
    @Override
    public boolean handler(final SetRedisValueDTO target) throws Exception {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.STRING) {
            RBucket<Object> bucket = redissonClient.getBucket(getKey(target));
            return setFieldValue(target, bucket.get());
        }
        return false;
    }
}
