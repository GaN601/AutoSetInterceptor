package indi.gan.handler.impl.obj;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import indi.gan.annotation.AutoSet;
import indi.gan.domain.FieldHandlerDTO;
import indi.gan.dto.AspectDTO;
import indi.gan.handler.BusinessUtil;
import indi.gan.handler.Chain;
import indi.gan.handler.FieldHandler;
import indi.gan.handler.ObjectHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 对象字段处理器, 如果想要对返回的对象中的字段进行处理, 应该实现{@link FieldHandler}
 *
 * @author GaN
 * @date 2022/11/18
 */
@Component
@Slf4j
public class ObjectFieldHandler implements ObjectHandler {
    
    private final Chain<FieldHandlerDTO> fieldChain = new Chain<>();
    private final Chain<AspectDTO> objectChain = new Chain<>();
    
    
    @PostConstruct
    public void init() {
        SpringUtil.getBeansOfType(FieldHandler.class).values().forEach(fieldChain::addHandler);
        SpringUtil.getBeansOfType(ObjectHandler.class).values().forEach(objectChain::addHandler);
    }
    
    @Override
    public boolean handler(final AspectDTO target) {
        dataHandler(target);
        return true;
    }
    
    private void dataHandler(AspectDTO aspectDTO) {
        Object data = aspectDTO.getRet();
        if (data == null) {
            return;
        }
        boolean b = BusinessUtil.ifIterableThenRun(data, (o) -> dataHandler(
                new AspectDTO(aspectDTO.getAopMethod(), o, aspectDTO.getWebUtil())));
        if (b) {return;}
        
        CompletionService<Boolean> async = ThreadUtil.newCompletionService();
        setObjectValue(aspectDTO, async);
        try {
            while (true) {
                Future<Boolean> poll = async.poll(1000, TimeUnit.MICROSECONDS);
                if (poll == null) {
                    break;
                }
                log.debug("异步设置结果: {}", poll.get());
            }
        } catch (Exception e) {
            log.error("CompletionService获取异步执行结果收到异常", e);
        }
        
    }
    
    private void setObjectValue(final AspectDTO aspectDTO, final CompletionService<Boolean> async) {
        Object data = aspectDTO.getRet();
        Class<?> aClass = data.getClass();
        AutoSet annotation = aClass.getAnnotation(AutoSet.class);
        if (annotation != null) {
            FieldHandlerDTO request = new FieldHandlerDTO();
            request.setMethod(aspectDTO.getAopMethod());
            request.setWebUtil(aspectDTO.getWebUtil());
            request.setSource(data);
            setFieldValue(async, request);
        }
    }
    
    private void setFieldValue(final CompletionService<Boolean> async, final FieldHandlerDTO request) {
        try {
            // 获取源对象, 比如NftProduct
            Object data = request.getSource();
            Class<?> aClass = data.getClass();
            log.debug("设置源对象值: {}", data);
            for (Field field : ReflectUtil.getFields(aClass)) {
                log.debug("设置{}源对象字段: {}", data.getClass().getName(), field.getName());
                FieldHandlerDTO dto = new FieldHandlerDTO();
                BeanUtil.copyProperties(request, dto);
                // 装配字段对象
                dto.setField(field);
                // 如果字段包含AutoSet注解则递归
                if (field.getAnnotation(AutoSet.class) != null) {
                    async.submit(() -> {
                        trySetFieldObjectValue(field, dto);
                        return true;
                    });
                } else {
                    fieldChain.process(dto);
                }
            }
        } catch (Exception e) {
            log.error("自动设置值失败", e);
        }
    }
    
    private void trySetFieldObjectValue(Field field, FieldHandlerDTO fieldHandlerDTO) {
        try {
            Object data = fieldHandlerDTO.getSource();
            log.debug("尝试设置对象值: {}", data.getClass().getName());
            field.setAccessible(true);
            // 获取字段上的对象
            Object fieldObj = field.get(data);
            if (fieldObj == null) {return;}
            
            BusinessUtil webUtil = fieldHandlerDTO.getWebUtil();
            BusinessUtil.iterableRun(fieldObj, (item) ->
            {
                try {
                    objectChain.process(new AspectDTO(fieldHandlerDTO.getMethod(), item, webUtil));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            log.error("自动设置值失败", e);
        }
    }
}
