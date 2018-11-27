package com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes;

class FinTimesObject {

    private String docNo;
    private String docId;
    private String headline;
    private String byLine;
    private String text;
    private String date;

    FinTimesObject(){
        this.docNo = "";
        this.docId = "";
        this.headline = "";
        this.byLine = "";
        this.text = "";
        this.date = "";
    }

    String getDocNo() { return docNo; }
    void setDocNo(String docNo) { this.docNo = docNo; }

    String getDocId() { return docId; }
    void setDocId(String docId) { this.docId = docId; }

    String getHeadline() { return headline; }
    void setHeadline(String headline) { this.headline = headline; }

    String getByLine() { return byLine; }
    void setByLine(String byLine) { this.byLine = byLine; }

    String getText() { return text; }
    void setText(String text) { this.text = text; }

    String getDate() { return date; }
    void setDate(String date) { this.date = date; }
}