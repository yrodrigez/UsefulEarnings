package es.usefulearnings.utils;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWorker;
import es.usefulearnings.entities.Entity;

import java.beans.IntrospectionException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Yago on 09/10/2016.
 */
public class CSVWriter {
  private String _filePath;
  private StringBuilder _csv;

  public CSVWriter(
    String filePath,
    Class<? extends  Entity> entity
  ) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
    _csv = new StringBuilder();
    setCSVHeaders(entity);
    _filePath = filePath;
  }

  public void writeLine(String line){
    _csv.append(line);
    _csv.append(System.lineSeparator());
  }

  public void save() throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(_filePath));
    bw.write(_csv.toString());
    bw.flush();
    bw.close();
  }

  private List<String> getHeader(
    Class<?> entity
  ) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    List<String> ret = new LinkedList<>();
    EntityParameterBeanWorker worker  = new EntityParameterBeanWorker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter)annotation;
        ParameterType type = parameter.parameterType();
        String header = parameter.name();
        switch (type){
          case INNER_CLASS:
            ret.addAll(getHeader(method.getReturnType()));
            break;

          case IGNORE:
            ret.add(header);
            break;

          case YAHOO_LONG_FORMAT_FIELD:
            ret.add(header);
            break;

          case RAW_NUMERIC:
            ret.add(header);
            break;

          case YAHOO_FIELD_NUMERIC:
            ret.add(header);
            break;

          default:
            break;
        }
      }
    );
    worker.walk(entity);
    return ret;
  }

  private void setCSVHeaders(
    Class<?> entity
  ) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
    List<String> headers = getHeader(entity.getClass());
    for (int i = 0; i < headers.size() ; i++){
      _csv.append(headers.get(i));
      if( i+1 == headers.size()){
        _csv.append(System.lineSeparator());
      } else {
        _csv.append(",");
      }
    }
  }
}
