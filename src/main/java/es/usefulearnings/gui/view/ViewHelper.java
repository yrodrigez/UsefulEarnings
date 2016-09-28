package es.usefulearnings.gui.view;


import javafx.scene.Node;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Yago Rodríguez
 */
interface ViewHelper<E>  {
 <T> Node getViewFor(E entity) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
}
