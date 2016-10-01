package es.usefulearnings.gui.view;

import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import javafx.scene.control.Label;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Yago on 01/10/2016.
 */
class YahooFieldNodeRetriever {
  private static YahooFieldNodeRetriever _instance = new YahooFieldNodeRetriever();

  static YahooFieldNodeRetriever getInstance() {
    return _instance ;
  }

  private YahooFieldNodeRetriever(){
  }

  Label getYahooFieldNumericLabel(YahooField yahooField){
    Label label = new Label();

    if(yahooField != null)
      label.setText(yahooField.getFmt());

    return label;
  }

  Label getYahooLongFormatLabel(YahooLongFormatField yahooLongFormatField){
    Label label = new Label();

    if(yahooLongFormatField != null)
      label.setText(yahooLongFormatField.getLongFmt());

    return label;
  }

  Label getYahooDateLabel (YahooField yahooDate){
    Label dateLabel = new Label();
    if (yahooDate != null)
      dateLabel.setText(yahooDate.getFmt());

    return dateLabel;
  }

  Label getYahooDateCollectionLabel(Collection<YahooField> collection){
    Label datesLabel = new Label();
    if (collection != null) {
      Iterator<YahooField> it = collection.iterator();
      while (it.hasNext()) {
        YahooField date = it.next();
        if (it.hasNext()) {
          datesLabel.setText(datesLabel.getText() + date.getFmt() + " - ");
        } else {
          datesLabel.setText(datesLabel.getText() + date.getFmt());
        }
      }
    }
    return datesLabel;
  }
}
