package com.kerinb.IR_proj2_group14.DocumentFiles.QueryFiles;

public class QueryObject {

    private String queryNum;
    private String queryId;
    private String title;
    private String description;
    private String narrative;

    QueryObject(){
        this.queryNum = "";
        this.queryId = "";
        this.title = "";
        this.narrative = "";
        this.description = "";
    }

    String getTitle() { return title; }

    String getNarrative() { return narrative; }

    String getQueryId() { return queryId; }

    String getQueryNum() { return queryNum; }

    public String getDescription() { return description; }

    void setTitle(String title) { this.title = title; }

    void setNarrative(String narrative) { this.narrative = narrative; }

    void setQueryId(String queryId) { this.queryId = queryId; }

    void setQueryNum(String queryNum) { this.queryNum = queryNum; }

    void setDescription(String description) { this.description = description; }
}
