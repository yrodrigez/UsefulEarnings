package es.usefulearnings.gui.view;


import es.usefulearnings.engine.filter.RestrictionValue;
import javafx.scene.Node;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Yago Rodríguez
 */
interface ViewHelper<E>  {
 <T> Node getViewFor(E entity) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
 Node getFilterView(Map<Field, RestrictionValue> filter) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
}
