package es.usefulearnings.entities;

import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;
import es.usefulearnings.entities.option.OptionLink;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Yago on 13/09/2016.
 */
public class OptionChain implements  Serializable {

  @ObservableField(name = "Expiration Date", fieldType = FieldType.DATE)
  private long mExpirationDate;

  @ObservableField(name= "", fieldType = FieldType.IGNORE)
  private Company company;

  @ObservableField(name = "Calls", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<OptionLink> mCalls;
  @ObservableField(name = "Calls", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<OptionLink> mPuts;

  public OptionChain(
    long expirationDate,
    ArrayList<OptionLink> calls,
    ArrayList<OptionLink> puts
  ) {
    mExpirationDate = expirationDate;
    mCalls = calls;
    mPuts = puts;
  }

  public long getmExpirationDate() {
    return mExpirationDate;
  }

  public void setmExpirationDate(long mExpirationDate) {
    this.mExpirationDate = mExpirationDate;
  }

  public ArrayList<OptionLink> getmCalls() {
    return mCalls;
  }

  public void setmCalls(ArrayList<OptionLink> mCalls) {
    this.mCalls = mCalls;
  }

  public ArrayList<OptionLink> getmPuts() {
    return mPuts;
  }

  public void setmPuts(ArrayList<OptionLink> mPuts) {
    this.mPuts = mPuts;
  }
}

