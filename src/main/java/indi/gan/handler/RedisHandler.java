package indi.gan.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.gan.annotation.SetRedisValue;
import indi.gan.domain.SetRedisValueDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * Redis处理器
 * @author GaN
 * @since 2022/8/3
 */
@Slf4j
public abstract class RedisHandler implements Handler<SetRedisValueDTO> {
    private ObjectMapper objectMapper;
    
    @Autowired
    public void setObjectMapper(final ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;}
    
    
    protected RedissonClient redissonClient;
    
    @Autowired
    public void setRedissonClient(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
    
    protected String getKey(SetRedisValueDTO target) {
        Object source = target.getSource();
        SetRedisValue annotation = target.getAnnotation();
        
        if (StrUtil.isBlank(annotation.fieldValue())) {
            return annotation.prefix();
        }
        
        Object property = BeanUtil.getProperty(source, annotation.fieldValue());
        return String.format(annotation.prefix(), property);
    }
    
    /**
     * 如果value == null则什么也不做
     *
     * @param target 目标
     * @param value  要设置的值
     * @return
     */
    protected boolean setFieldValue(SetRedisValueDTO target, Object value) {
        Field targetField = target.getTargetField();
        try {
            Object o = objectMapper.readValue(objectMapper.writeValueAsString(value),targetField.getType());
            if (o == null) {
                return false;
            }
            targetField.set(target.getSource(), o);
            return true;
        } catch (Exception e) {
            log.error("{} 赋值失败",targetField.getName(), e);
        }
        return false;
    }
}
