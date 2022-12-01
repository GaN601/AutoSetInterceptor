package indi.gan.handler.impl.field;

import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import indi.gan.enums.RedisType;
import indi.gan.handler.RedisHandler;
import org.redisson.api.RList;
import org.springframework.stereotype.Component;

/**
 * @author GaN
 * @since 2022/10/9
 */
@Component
public class RedisListLengthHandler extends RedisHandler {

    @Override
    public boolean handler(final SetRedisValueDTO target)  {
        SetRedisValue annotation = target.getAnnotation();
        if (annotation.type() == RedisType.LIST_LENGTH) {
            String key = getKey(target);
            RList<Object> list = redissonClient.getList(key);
            return setFieldValue(target, list.size());
        }
        return false;
    }
}
