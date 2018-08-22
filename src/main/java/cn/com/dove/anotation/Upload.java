package cn.com.dove.anotation;

import cn.com.dove.framework.em.ReqType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangdequan on 2018/1/29.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Upload {

   String filePath();

   String fileName() default "";
}

//ruby ./redis-trib.rb create --replicas 1 192.168.159.128:7000 192.168.159.128:7001 192.168.159.128:7002 192.168.159.130:7007 192.168.159.130:7008 192.168.159.130:7009