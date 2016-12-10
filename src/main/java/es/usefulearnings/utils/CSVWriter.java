package es.usefulearnings.utils;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWalker;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;

import java.beans.IntrospectionException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Yago on 09/10/2016.
 */
public class CSVWriter <E> {
  private String _filePath;
  private StringBuilder _csv;
  private final String separator = ";";

  public CSVWriter (
    String filePath,
    ArrayList<E> entities
  ) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
    _csv = new StringBuilder();
    _filePath = filePath;

    List<String> headers = getHeader(entities.get(0));
    writeLine(headers);
    for (E entity : entities){
      List<String> line = lineCreator(entity);
      writeLine(line);
    }
  }

  @Override
  public String toString() {
    return _csv.toString();
  }

  private void writeLine(List<String> line){
    for (int i = 0; i < line.size() ; i++){
      String value = line.get(i);
      if(value != null)
        _csv.append(value.replaceAll(",", ""));
      else
        _csv.append("");
      if( i+1 != line.size()){
        _csv.append(separator);
      }
    }
    _csv.append(System.lineSeparator());
  }

  public void save() throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(_filePath + ".csv"));
    bw.write(_csv.toString());
    bw.flush();
    bw.close();
  }


  private <E> List<String> lineCreator(
    E entity
  ) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    List<String> line = new LinkedList<>();

    EntityParameterBeanWalker worker = new EntityParameterBeanWalker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter)annotation;
        ParameterType type = parameter.parameterType();
        switch (type){
          case INNER_CLASS:
            Object o = method.invoke(entity);
            if (o != null)
              line.addAll(lineCreator(o));
            break;

          case YAHOO_FIELD_DATE:
            YahooField fieldDate = (YahooField)method.invoke(entity);
            if (fieldDate != null)
              line.add(fieldDate.getFmt());
            else
              line.add("");
            break;

          case IGNORE:
            String ignore = (String)method.invoke(entity);
            if (ignore != null)
              line.add(ignore);
            else
              line.add("");
            break;

          case RAW_STRING:
            String rawString = (String)method.invoke(entity);
            if(rawString != null)
              line.add(rawString);
            break;

          case RAW_DOUBLE:
            Object o1 = method.invoke(entity);
            if(o1 != null) {
              Number number = (Number) o1;
              line.add(Double.toString(number.doubleValue()));
            } else {
              line.add("");
            }
            break;

          case RAW_LONG:
            Object rawLong = method.invoke(entity);
            if(rawLong != null) {
              Number number = (Number) rawLong;
              line.add(Long.toString(number.longValue()));
            } else {
              line.add("");
            }
          break;

          case UNIX_TIME_STAMP:
            Object unixTimeStamp = method.invoke(entity);
            if(unixTimeStamp != null) {
              Number number = (Number) unixTimeStamp;
              line.add(new SimpleDateFormat("dd/MM/yyyy").format(new Date(number.longValue() * 1000L)));
            } else {
              line.add("");
            }
            break;

          case YAHOO_LONG_FORMAT_FIELD:
            YahooLongFormatField yahooLongFormatField = (YahooLongFormatField)method.invoke(entity);
            if(yahooLongFormatField != null)
              line.add(yahooLongFormatField.getLongFmt());
            else
              line.add("");
            break;

          case URL:
            String url = (String)method.invoke(entity);
            if(url != null)
              line.add(url);
            else
              line.add("");
            break;


          case YAHOO_FIELD_NUMERIC:
            YahooField yahooField = (YahooField)method.invoke(entity);
            if(yahooField != null)
              line.add(yahooField.getFmt());
            else
              line.add("");
            break;


          case YAHOO_FIELD_DATE_COLLECTION:
            String dates = "";
            ArrayList<YahooField> fieldCollection = (ArrayList<YahooField>)method.invoke(entity);
            if(fieldCollection != null && !fieldCollection.isEmpty()) {
              dates += fieldCollection.remove(0).getFmt();
              if (!fieldCollection.isEmpty()){
                dates += " - " + fieldCollection.remove(0).getFmt();
              }
            }
            line.add(dates);
            break;
        }

      }
    );
    worker.walk(entity.getClass());
    return line;
  }

  private <E> List<String> getHeader(
    E entity
  ) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    List<String> ret = new LinkedList<>();
    EntityParameterBeanWalker worker  = new EntityParameterBeanWalker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter)annotation;
        ParameterType type = parameter.parameterType();
        String header = parameter.name();
        switch (type){
          case INNER_CLASS:
            ret.addAll(getHeader(method.invoke(entity)));
            break;

          case RAW_STRING:
          case YAHOO_FIELD_NUMERIC:
          case RAW_DOUBLE:
          case YAHOO_LONG_FORMAT_FIELD:
          case URL:
          case IGNORE:
          case YAHOO_FIELD_DATE:
          case YAHOO_FIELD_DATE_COLLECTION:
          case RAW_DATE:
          case RAW_DATE_COLLECTION:
          case RAW_BOOLEAN:
          case UNIX_TIME_STAMP:
          case RAW_LONG:
            ret.add(header);
            break;


          default:
            break;
        }
      }
    );
    worker.walk(entity.getClass());
    return ret;
  }

}
