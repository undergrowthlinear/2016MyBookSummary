package com.undergrowth.buddy.bean;

import java.lang.annotation.Annotation;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-14:40
 */
public class RuntimeDefinitionImpl implements RuntimeDefinition {
    @Override
    public Class<? extends Annotation> annotationType() {
        return RuntimeDefinition.class;
    }
}