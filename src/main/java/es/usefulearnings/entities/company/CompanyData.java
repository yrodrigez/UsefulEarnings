package es.usefulearnings.entities.company;

import java.io.Serializable;

/**
 * @author Yago Rodríguez
 */
public class CompanyData implements Serializable {

  private boolean isSet = false;

  public void set(){
    this.isSet = true;
  }

  public boolean isSet() {
    return isSet;
  }

}
