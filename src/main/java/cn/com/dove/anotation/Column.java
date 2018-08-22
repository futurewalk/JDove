package cn.com.dove.anotation;

import cn.com.dove.framework.em.PKST;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    public  String colName() default "";

    public boolean pK() default false;

    public PKST pkst() default PKST.DEFAULT_VALUE;

    public boolean notNull() default false;

    public String length() default "";

    public String type() default "";

    public String comment() default "";
}