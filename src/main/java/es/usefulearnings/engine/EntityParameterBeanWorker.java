package es.usefulearnings.engine;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.Company;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author yago.
 */
public class EntityParameterBeanWorker {
  private EntityParameterProcessor _processor;

  public EntityParameterBeanWorker(EntityParameterProcessor processor){
    _processor = processor;
  }

  public void walk(Class<?> classToWalk) throws IntrospectionException {
    for (Field field : classToWalk.getClass().getDeclaredFields()) {
      if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
          if (descriptor.getName().equals(field.getName())) {
            _processor.processParameter(field, field.getAnnotation(EntityParameter.class), descriptor.getReadMethod());
            break;
          }
        }
      }
    }
  }
}
