package com.kerinb.IR_proj2_group14.QueryFiles;

public class QueryData {
    private String docId;
    private String docContent;

    public QueryData(){
        this.docId = "";
        this.docContent = "";
    }

    public String getQueryId() {
        return docId;
    }

    public String getQueryContent() {
        return docContent;
    }

    void setQueryId(String docId) {
        this.docId = docId;
    }

    void setQueryContent(String docContent) {
        this.docContent = docContent;
    }
}
