package es.usefulearnings.engine;

/**
 * @author yago.
 */
public class RestrictionValue<E> {
  private BasicOperator _operator;
  private E _value;

  public RestrictionValue(E value, BasicOperator operator){
    _value = value;
    _operator = operator;
  }

  public BasicOperator getOperator() {
    return _operator;
  }

  public void set_operator(BasicOperator _operator) {
    this._operator = _operator;
  }

  public E getValue() {
    return _value;
  }

  public void set_value(E _value) {
    this._value = _value;
  }
}
