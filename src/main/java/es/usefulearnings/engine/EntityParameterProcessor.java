package es.usefulearnings.engine;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author yago.
 */
public interface EntityParameterProcessor {
  void processParameter(Field field, Annotation annotation, Method method, int position)
    throws InvocationTargetException, IllegalAccessException, IntrospectionException, InstantiationException;
}
