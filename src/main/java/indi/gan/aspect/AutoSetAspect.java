package indi.gan.aspect;

import cn.hutool.extra.spring.SpringUtil;
import indi.gan.dto.AspectDTO;
import indi.gan.handler.BusinessUtil;
import indi.gan.handler.Chain;
import indi.gan.handler.ObjectHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author GaN
 * @date 2022/12/2
 */
@Aspect
@Slf4j
@Order
@Component
public class AutoSetAspect {
    private final Chain<AspectDTO> objectChain = new Chain<>();
    @Resource
    private BusinessUtil webUtil;
    
    @PostConstruct
    public void init() {
        SpringUtil.getBeansOfType(ObjectHandler.class).values().forEach(objectChain::addHandler);
    }
    
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void point() {}
    
    @AfterReturning(value = "point() || @target(indi.gan.annotation.AutoSet)", returning = "ret")
    public void autoSet(JoinPoint joinPoint, Object ret) {
        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            return;
        }
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            BusinessUtil.iterableRun(ret, item -> {
                AspectDTO aspectDTO = new AspectDTO(signature.getMethod(), item, webUtil);
                objectChain.process(aspectDTO);
            });
        } catch (Exception e) {
            log.error("自动设置值失败", e);
        }
    }
}
