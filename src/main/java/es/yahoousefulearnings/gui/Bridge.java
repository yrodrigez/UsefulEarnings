package es.yahoousefulearnings.gui;


public class Bridge {

  private Bridge bridge;

  private Bridge(){

  }

  public Bridge getInstance(){
    if(bridge != null)
      return this.bridge;
    else
      return new Bridge();
  }
}
