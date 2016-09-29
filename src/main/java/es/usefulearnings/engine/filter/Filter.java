package es.usefulearnings.engine.filter;


import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author Yago.
 */
public abstract class Filter<E> implements Serializable {
  protected Map<Field, RestrictionValue> _parameters;
  protected Set<E> _filteredEntities;
  private long _filteredDate;


  public Filter(Set<E> entities, Map<Field, RestrictionValue> parameters) {
    _filteredEntities = entities;
    _parameters = parameters;

    _filteredDate = new Date().getTime() / 1000L;
  }

  protected abstract void filter() throws IllegalAccessException, IntrospectionException, InvocationTargetException;

  public Set<E> getEntities() {
    return _filteredEntities;
  }

  protected void removeCompany(E entity) {
    _filteredEntities.remove(entity);
  }

  protected boolean evaluateNumber(double number, BasicOperator operator, double toEval) {
    switch (operator) {
      case EQ:
        return number == toEval;
      case LT:
        return number < toEval;
      case GT:
        return number > toEval;

      default:
        return false;
    }
  }

  protected boolean evaluateTimeStamp(long timeStamp, BasicOperator operator, long stampToEval) {
    switch (operator) {
      case EQ:
        return timeStamp == stampToEval;
      case LT:
        return timeStamp < stampToEval;
      case GT:
        return timeStamp > stampToEval;

      default:
        return false;
    }
  }

  protected boolean evaluateString(String string, BasicOperator operator, String toEval) {
    switch (operator) {
      case EQ:
        return string.equals(toEval);
      case GT:
        return string.compareToIgnoreCase(toEval) > 0;
      case LT:
        return string.compareToIgnoreCase(toEval) < 0;

      default:
        return false;
    }
  }

  @Override
  public String toString(){
    return "Filtered at: "
      + new SimpleDateFormat("hh:mm:ss").format(new Date(_filteredDate * 1000L))
      + " companies found: " + _filteredEntities.size();
  }
}
