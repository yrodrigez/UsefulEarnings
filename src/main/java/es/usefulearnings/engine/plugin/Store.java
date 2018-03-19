package es.usefulearnings.engine.plugin;

public class Store {

    private static Store _instance;
    
    public static Store getInstance(){
        if(_instance == null){
            _instance = new Store();
        }

        return _instance;
    }


    public Object get(String route) {
        return null;
    }
}
