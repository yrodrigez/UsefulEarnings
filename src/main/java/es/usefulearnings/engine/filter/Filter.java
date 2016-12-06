package es.usefulearnings.engine.filter;


import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yago.
 */
public abstract class Filter<E> implements Serializable {
  protected Map<Field, RestrictionValue> _parameters;
  protected Set<E> _allEntities;
  protected Set<E> _selected;
  protected Set<E> _toBeRemoved;

  private long _filteredDate;

  public Filter(Set<E> entities, Map<Field, RestrictionValue> parameters) {
    _allEntities = entities;
    _parameters = parameters;
    _selected = new LinkedHashSet<>();
    _toBeRemoved = new LinkedHashSet<>();

    _filteredDate = new Date().getTime() / 1000L;
  }


  protected abstract void filter() throws IllegalAccessException, IntrospectionException, InvocationTargetException;

  public Set<E> getSelected() {
    // this is going to be & and | !
    _selected.removeAll(_toBeRemoved);
    return _selected;
  }

  public long getFilteredDate() {
    return _filteredDate;
  }

  protected void select(E entity){
    _selected.add(entity);
  }

  protected void remove(E entity){
    _toBeRemoved.add(entity);
  }

  private boolean evaluateNumber (Number number, BasicOperator operator, Number toEval){
    switch (operator) {
      case EQ:
        return number.doubleValue() == toEval.doubleValue();
      case LT:
        return number.doubleValue() < toEval.doubleValue();
      case GT:
        return number.doubleValue() > toEval.doubleValue();

      default:
        return false;
    }
  }

  protected boolean evaluateDouble(double number, BasicOperator operator, double toEval) {
    return evaluateNumber(number, operator, toEval);
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
      return "Found companies at "
        + new SimpleDateFormat("hh:mm:ss").format(new Date(_filteredDate * 1000L)) +
        ": " + getSelected().size();
  }
}
