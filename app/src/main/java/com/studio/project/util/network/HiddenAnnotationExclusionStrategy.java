package com.studio.project.util.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Andrew on 17.02.2020
 */
public class HiddenAnnotationExclusionStrategy implements ExclusionStrategy
{
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(IgnoreField.class) != null;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(IgnoreField.class) != null;
    }
}
