package es.usefulearnings.engine;

import es.usefulearnings.annotation.EntityParameter;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author yago.
 */
public class EntityParameterBeanWalker {
  private EntityParameterProcessor _processor;

  public EntityParameterBeanWalker(EntityParameterProcessor processor){
    _processor = processor;
  }

  public void walk(Class<?> classToWalk)
    throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    for (Field field : classToWalk.getDeclaredFields()) {
      if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(classToWalk).getPropertyDescriptors();
        for (int i = 0 ; i < descriptors.length; i++) {
          if (descriptors[i].getName().equals(field.getName())) {
            _processor.processParameter(field, field.getDeclaredAnnotation(EntityParameter.class), descriptors[i].getReadMethod(), i);
            break;
          }
        }
      }
    }
  }
}
