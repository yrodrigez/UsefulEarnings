package es.usefulearnings.gui.view;


import javafx.scene.Node;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Yago Rodríguez
 */
interface ViewHelper<E>  {
 Node getView(E entity) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException;
 void showOnWindow(E entity) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
