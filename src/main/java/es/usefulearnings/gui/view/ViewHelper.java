package es.usefulearnings.gui.view;


import javafx.scene.Node;
import javafx.stage.Window;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Yago Rodríguez
 */
interface ViewHelper<E>  {
 Node getViewFor(Object entity) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException;
 FilterView getFilterView() throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException;
 void showEntityOnWindow(Window window, E entity);
}
