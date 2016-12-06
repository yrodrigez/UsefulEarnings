package es.usefulearnings.engine.filter;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yago.
 */
public class CompanyFilter extends Filter<Company> {

  public CompanyFilter(Set<Company> companies, Map<Field, RestrictionValue> parameters) {
    super(companies, parameters);
  }


  @Override
  public void filter() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    Set<Company> listToIterate = new HashSet<>(_allEntities);
    for (Company company : listToIterate) {
      applyFilter(company, Company.class, company);
    }
  }

  @SuppressWarnings("unchecked")
  private <E> void applyFilter(
    Company company,
    Class<?> elementType,
    E elementValue
  ) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    for (Field field : elementType.getDeclaredFields()) {
      if (field.getAnnotation(EntityParameter.class) != null) {
        ParameterType parameterType = field.getAnnotation(EntityParameter.class).parameterType();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(elementType).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {

            switch (parameterType) {
/*--------------------------------------------------------------------------------------------------------------------*/
              case INNER_CLASS:
                if (elementValue == null) {
                  applyFilter(company, pd.getPropertyType(), null);
                  break;
                } else {
                  applyFilter(company, pd.getPropertyType(), pd.getReadMethod().invoke(elementValue));
                  break;
                }
/*--------------------------------------------------------------------------------------------------------------------*/
              case INNER_CLASS_COLLECTION:
                Collection<E> collection;
                if (elementValue == null) collection = null;
                else collection = ((Collection<E>) pd.getReadMethod().invoke(elementValue));
                Class<?> newElementValue = (Class<?>) ((ParameterizedType) pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
                if (collection != null && collection.size() > 0) {
                  for (E innerElement : collection) {
                    applyFilter(company, newElementValue, innerElement);
                  }
                } else {
                  applyFilter(company, newElementValue, null);
                }
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case URL:
              case RAW_STRING:
                if (!_parameters.containsKey(field)) break;
                if (elementValue != null && pd.getReadMethod().invoke(elementValue) != null) {
                  String string = (String) pd.getReadMethod().invoke(elementValue);
                  if (string != null && !string.equals("")) {
                    RestrictionValue restrictionValue = _parameters.get(field);
                    String toEval = (String) restrictionValue.getValue();
                    if (evaluateString(string, restrictionValue.getOperator(), toEval)) {
                      select(company);
                      break;
                    }
                  }
                }
                remove(company);
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case YAHOO_FIELD_NUMERIC:
              case YAHOO_LONG_FORMAT_FIELD:
                if (!_parameters.containsKey(field)) break;
                if (elementValue != null && pd.getReadMethod().invoke(elementValue) != null) {
                  double number = ((YahooField) pd.getReadMethod().invoke(elementValue)).getRaw();
                  if (number != 0.0) {
                    RestrictionValue restrictionValue = _parameters.get(field);
                    double toEval = ((double) restrictionValue.getValue());
                    if (evaluateDouble(number, restrictionValue.getOperator(), toEval)) {
                      select(company);
                      break;
                    }
                  }
                }
                remove(company);
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case RAW_NUMERIC:
                if (!_parameters.containsKey(field)) break;
                if (elementValue != null && pd.getReadMethod().invoke(elementValue) != null) {
                  Double number = ((Number) (pd.getReadMethod().invoke(elementValue))).doubleValue();
                  if (number != null) {
                    RestrictionValue restrictionValue = _parameters.get(field);
                    Double toEval = (Double) restrictionValue.getValue();
                    if (evaluateDouble(number, restrictionValue.getOperator(), toEval)) {
                      select(company);
                      break;
                    }
                  }
                }
                remove(company);
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case YAHOO_FIELD_DATE:
                if (!_parameters.containsKey(field)) break;
                if (elementValue != null && pd.getReadMethod().invoke(elementValue) != null) {
                  Long timeStamp = ((Number) (((YahooField) pd.getReadMethod().invoke(elementValue)).getRaw())).longValue();
                  if (timeStamp != null) {
                    RestrictionValue restrictionValue = _parameters.get(field);
                    long stampToEval = (long) restrictionValue.getValue();
                    if (evaluateTimeStamp(timeStamp, restrictionValue.getOperator(), stampToEval)) {
                      select(company);
                      break;
                    }
                  }
                }
                remove(company);
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case YAHOO_FIELD_DATE_COLLECTION:
                if (!_parameters.containsKey(field)) break;
                if (elementValue != null && pd.getReadMethod().invoke(elementValue) != null) {
                  Collection<YahooField> yahooFieldCollection = (Collection<YahooField>) pd.getReadMethod().invoke(elementValue);
                  if (yahooFieldCollection != null && !yahooFieldCollection.isEmpty()) {
                    RestrictionValue restrictionValue = _parameters.get(field);
                    boolean remove = true;
                    for (YahooField yahooField : yahooFieldCollection) {
                      long timeStamp = ((Number) yahooField.getRaw()).longValue();
                      long stampToEval = ((Number) restrictionValue.getValue()).longValue();
                      if(timeStamp == 0) continue;
                      remove = remove && (!evaluateTimeStamp(timeStamp, restrictionValue.getOperator(), stampToEval));
                    }
                    if (remove) {
                      remove(company);
                      break;
                    } else {
                      select(company);
                      break;
                    }
                  }
                }
                remove(company);
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case HISTORICAL_DATA:
              case RAW_DATE_COLLECTION: //TODO THIS!
              case RAW_DATE: //TODO THIS!
              case IGNORE:
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              default:
                throw new IllegalArgumentException("Wrong Argument -> " + parameterType.name());
            } // switch parameterType
          } // if pd.getName().equals(field.getName())
        } // for PropertyDescriptor
      } // if field.getAnnotation(EntityParameter.class) != null
    } // for Field field : elementType.getDeclaredFields()
  }
}
