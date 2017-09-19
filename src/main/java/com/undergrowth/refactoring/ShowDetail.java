package com.undergrowth.refactoring;

public class ShowDetail {

  private String header;
  private String tail;
  private StringBuilder body;
  private ShowType showType;

  public ShowDetail(ShowType showType) {
    super();
    this.showType = showType;
  }

  public ShowDetail() {
    this(ShowType.TXT);
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public void setTail(String tail) {
    this.tail = tail;
  }

  public void setBody(StringBuilder body) {
    this.body = body;
  }

  @Override
  public String toString() {
    return "ShowDetail [header=" + header + ", body=" + body.toString() + ", tail=" + tail + "]";
  }

  public ShowType getShowType() {
    return showType;
  }

  public void setShowType(ShowType showType) {
    this.showType = showType;
  }

}
