package es.usefulearnings.gui.view;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author yago.
 */
interface FilterableView {
  FilterView getFilterableView() throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException;
}
