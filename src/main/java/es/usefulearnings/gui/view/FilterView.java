package es.usefulearnings.gui.view;

import es.usefulearnings.engine.filter.RestrictionValue;
import javafx.scene.Node;

import java.util.Map;

/**
 * @author yago.
 */
public class FilterView {

  private Map<String, RestrictionValue> _filterParams;
  private Node _view;

  public FilterView(Class<?> anyClass){

  }

  public Node getView(){
    return _view;
  }

  public Map<String, RestrictionValue> getFilterParams() {
    return _filterParams;
  }
}
