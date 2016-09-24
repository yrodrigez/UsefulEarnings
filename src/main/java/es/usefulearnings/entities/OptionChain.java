package es.usefulearnings.entities;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.option.OptionLink;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Yago on 13/09/2016.
 */
public class OptionChain implements  Serializable, Entity {

  @EntityParameter(name = "Expiration Date", parameterType = ParameterType.RAW_DATE)
  private long mExpirationDate;

  @EntityParameter(name= "", parameterType = ParameterType.IGNORE)
  private Company company;

  @EntityParameter(name = "Calls", parameterType = ParameterType.OPTION_LINK_COLLECTION)
  private ArrayList<OptionLink> mCalls;
  @EntityParameter(name = "Puts", parameterType = ParameterType.OPTION_LINK_COLLECTION)
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

  @Override
  public boolean isEmpty() {
    return !(mCalls.isEmpty() || mPuts.isEmpty());
  }
}

