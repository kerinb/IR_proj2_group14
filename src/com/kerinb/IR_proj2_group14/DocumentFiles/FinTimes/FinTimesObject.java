package com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes;

public class FinTimesObject {
    private String docNo;
    private String headline;
    private String byLine;
    private String text;

    public FinTimesObject(){
        this.docNo = "";
        this.headline = "";
        this.byLine = "";
        this.text = "";
    }

    public String getDocNo() { return docNo; }
    public void setDocNo(String docNo) { this.docNo = docNo; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getByLine() { return docNo; }
    public void setByLine(String byLine) { this.byLine = byLine; }

    public String getText() { return headline; }
    public void setText(String text) { this.text = text; }
}
