package es.yahoousefulearnings.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Yago Rodríguez on 9/09/16.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Entity {
  String name();
}
