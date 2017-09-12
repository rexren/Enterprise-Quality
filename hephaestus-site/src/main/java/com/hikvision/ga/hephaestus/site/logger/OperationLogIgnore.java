package com.hikvision.ga.hephaestus.site.logger;

import java.lang.annotation.*;

/**
 * 定义OperationLogIgnore注解，类和方法上都没有配置ignore的注解，则记录日志
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogIgnore {
}
