package com.example.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义表属性名称
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAttrAnnotation {

    String name();

    /**
     * 是否是主键
     *
     * @return
     */
    boolean isKey() default false;

    /**
     * 是否插入此值,设置为自增字段不插入此值
     *
     * @return
     */
    boolean isInsert() default true;
}
