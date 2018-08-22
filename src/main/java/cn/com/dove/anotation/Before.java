package cn.com.dove.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Created by wangdequan on 2018/1/29.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {

    String ctrlPath() default "";

    String ctrlMethod() default "";

    Class<?> clazz() default String.class;



}
