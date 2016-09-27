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
    Set<Company> listToIterate = new HashSet<>(_filteredEntities);
    for(Company company: listToIterate){
      applyFilter(company, Company.class, company);
    }
    
    for (Company company: _filteredEntities){
      //System.err.println("Resulting company: " + company +
      System.err.println("Company " + company.getSymbol() + " Sector: " + company.getProfile().getSector());
      System.err.println("Current price: " + company.getFinancialData().getCurrentPrice().getRaw());
      System.err.println("exDividend date: " + company.getCalendarEvents().getExDividendDate().getFmt());
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
                if (collection != null) {
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
                if (elementValue == null) {
                  removeCompany(company);
                  break;
                }
                if (pd.getReadMethod().invoke(elementValue) == null) {
                  removeCompany(company);                  break;
                } else {
                  RestrictionValue restrictionValue = _parameters.get(field);
                  String string = (String) pd.getReadMethod().invoke(elementValue);
                  String toEval = (String) restrictionValue.getValue();
                  if (!evaluateString(string, restrictionValue.getOperator(), toEval)) {
                    removeCompany(company);
                  }
                }
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case YAHOO_FIELD_NUMERIC:
              case YAHOO_LONG_FORMAT_FIELD:
                if (!_parameters.containsKey(field)) break;
                if (elementValue == null) {
                  removeCompany(company);
                  break;
                }
                if (pd.getReadMethod().invoke(elementValue) == null) {
                  removeCompany(company);
                  break;
                } else {
                  RestrictionValue restrictionValue = _parameters.get(field);
                  double number = ((YahooField) pd.getReadMethod().invoke(elementValue)).getRaw();
                  double toEval = ((double) restrictionValue.getValue());
                  if (!evaluateNumber(number, restrictionValue.getOperator(), toEval)) {
                    removeCompany(company);
                  }
                }
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case RAW_NUMERIC:
                if (!_parameters.containsKey(field)) break;
                if (elementValue == null) {
                  removeCompany(company);
                  break;
                }
                if (pd.getReadMethod().invoke(elementValue) == null) {
                  removeCompany(company);
                  break;
                } else {
                  RestrictionValue restrictionValue = _parameters.get(field);
                  double number = ((double) pd.getReadMethod().invoke(elementValue));
                  double toEval = (double) restrictionValue.getValue();
                  if (!evaluateNumber(number, restrictionValue.getOperator(), toEval)) {
                    removeCompany(company);
                  }
                }
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case YAHOO_FIELD_DATE:
                if (!_parameters.containsKey(field)) break;
                if (elementValue == null) {
                  removeCompany(company);
                  break;
                }
                if (pd.getReadMethod().invoke(elementValue) == null) {
                  removeCompany(company);
                  break;
                } else {
                  RestrictionValue restrictionValue = _parameters.get(field);
                  long timeStamp = new Double(((YahooField) pd.getReadMethod().invoke(elementValue)).getRaw()).longValue();
                  long stampToEval = (long) restrictionValue.getValue();
                  if (!evaluateTimeStamp(timeStamp, restrictionValue.getOperator(), stampToEval) || timeStamp == 0) {
                    removeCompany(company);
                  }
                }
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
              case YAHOO_FIELD_DATE_COLLECTION:
                if (!_parameters.containsKey(field)) break;
                if (elementValue == null) {
                  removeCompany(company);
                  break;
                }
                if (pd.getReadMethod().invoke(elementValue) == null) {
                  removeCompany(company);
                  break;
                } else {
                  Collection<YahooField> yahooFieldCollection = (Collection<YahooField>) pd.getReadMethod().invoke(elementValue);
                  if (yahooFieldCollection == null) {
                    removeCompany(company);
                    break;
                  }
                  RestrictionValue restrictionValue = _parameters.get(field);
                  boolean remove = true;
                  for (YahooField yahooField : yahooFieldCollection) {
                    long timeStamp = new Double(yahooField.getRaw()).longValue();
                    long stampToEval = (long) restrictionValue.getValue();
                    remove = remove && (!evaluateTimeStamp(timeStamp, restrictionValue.getOperator(), stampToEval) || timeStamp == 0);
                  }
                  if (remove) {
                    removeCompany(company);
                  }
                }
                break;
/*--------------------------------------------------------------------------------------------------------------------*/
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
