package es.usefulearnings.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({
  ElementType.ANNOTATION_TYPE,
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.PARAMETER,
  ElementType.LOCAL_VARIABLE
})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EntityParameter {
  String name();
  ParameterType parameterType();
  Class<? extends AllowedValuesRetriever> allowedValues() default DefaultAllowedValuesRetriever.class;
  boolean isMaster() default false;
}



