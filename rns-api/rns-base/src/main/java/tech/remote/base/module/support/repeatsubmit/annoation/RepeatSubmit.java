package tech.remote.base.module.support.repeatsubmit.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 需要防止重复提交 的注解<br>
 * 单位：毫秒
 *
 * @Author 远游工作室: 胡克
 * @Date 2020-11-25 20:56:58
 *
 *  
 * @Copyright  <a href="http://www.remotenomad.tech">远游工作室</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatSubmit {

    /**
     * 重复提交间隔时间/毫秒
     *
     * @return
     */
    int value() default 300;

    /**
     * 最长间隔30s
     */
    int MAX_INTERVAL = 30000;
}
