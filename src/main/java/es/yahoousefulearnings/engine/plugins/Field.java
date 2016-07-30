package es.yahoousefulearnings.engine.plugins;

public class Field {

  private String nombre;
  private Object value;
  private FieldType type;

  public Field(
    final String nombre,
    final FieldType type,
    final Object value
  ) {
    this.setNombre(nombre);
    this.setType(type);
    this.setValue(value);
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public FieldType getType() {
    return type;
  }

  public void setType(FieldType type) {
    this.type = type;
  }
}
