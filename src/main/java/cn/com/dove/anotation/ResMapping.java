package cn.com.dove.anotation;

import cn.com.dove.framework.em.Action;
import cn.com.dove.framework.em.LoginState;
import cn.com.dove.framework.em.ReqType;
import cn.com.dove.utils.Constans;

import java.io.File;
import java.lang.annotation.*;

/**
 * Created by wangdequan on 2018/1/29.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResMapping {
   String url();

   Action action() default Action.POST;

   String comment() default "";

   LoginState loginState() default LoginState.ALLOW_NOT_LOGIN;

   boolean uploadFile() default false;

}

//ruby ./redis-trib.rb create --replicas 1 192.168.159.128:7000 192.168.159.128:7001 192.168.159.128:7002 192.168.159.130:7007 192.168.159.130:7008 192.168.159.130:7009