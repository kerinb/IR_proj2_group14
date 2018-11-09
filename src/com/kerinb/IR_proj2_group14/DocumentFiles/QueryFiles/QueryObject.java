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

    public String getTitle() { return title; }

    public String getNarrative() { return narrative; }

    public String getQueryId() { return queryId; }

    public String getQueryNum() { return queryNum; }

    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }

    public void setNarrative(String narrative) { this.narrative = narrative; }

    public void setQueryId(String queryId) { this.queryId = queryId; }

    public void setQueryNum(String queryNum) { this.queryNum = queryNum; }

    public void setDescription(String description) { this.description = description; }
}
