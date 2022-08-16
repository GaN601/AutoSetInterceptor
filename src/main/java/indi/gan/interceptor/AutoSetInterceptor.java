package indi.gan.interceptor;

import cn.hutool.core.util.ArrayUtil;
import indi.gan.annotation.AutoSet;
import indi.gan.handler.Chain;
import indi.gan.handler.FieldHandler;
import indi.gan.domain.FieldHandlerDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author GaN
 * @since 2022/8/3
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),})
@Slf4j
@Component
public class AutoSetInterceptor implements Interceptor {
    private ApplicationContext applicationContext;
    
    @Autowired
    public void setApplicationContext(
            final ApplicationContext applicationContext) {this.applicationContext = applicationContext;}
    
    private final Chain<FieldHandlerDTO> fieldChain = new Chain<>();
    
    @PostConstruct
    public void init(){
        Map<String, FieldHandler> beansOfType = applicationContext.getBeansOfType(FieldHandler.class);
        beansOfType.forEach((key,value) -> fieldChain.addHandler(value));
    }
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result != null) {
            // 处理单个bean
            return this.desensitization(result);
        }
        
        return result;
    }
    
    @SneakyThrows
    private Object desensitization(Object obj) {
        if (obj instanceof Iterable) {
            ((Iterable<?>) obj).forEach(this::desensitization);
            return obj;
        }
        
        Class cls = obj.getClass();
        if (cls.getAnnotation(AutoSet.class) == null) {
            return obj;
        }
        
        Field[] objFields = cls.getDeclaredFields();
        if (ArrayUtil.isEmpty(objFields)) {
            return obj;
        }
        
        for (Field field : objFields) {
            fieldChain.process(new FieldHandlerDTO(obj, field));
        }
        
        return obj;
    }
    
    @Override
    public Object plugin(Object target) {
        // Spring bean 方式配置时，如果没有配置属性就不会执行下面的 setProperties 方法
        // 就不会初始化，因此考虑在这个方法中做一次判断和初始化
        return Plugin.wrap(target, this);
    }
}
