package com.kerinb.IR_proj2_group14.DocumentFiles;

public class DocData {
    private String docId;
    private String docTitle;
    private String docAuthors;
    private String docBibliography;
    private String docContent;

    public DocData(){
        this.docId = "";
        this.docTitle = "";
        this.docAuthors = "";
        this.docBibliography = "";
        this.docContent = "";
    }

    String getDocAuthors() {
        return docAuthors;
    }

    String getDocBibliography() {
        return docBibliography;
    }

    String getDocId() {
        return docId;
    }

    String getDocTitle() {
        return docTitle;
    }

    String getDocContent() {
        return docContent;
    }

    void setDocId(String docId) {
        this.docId = docId;
    }

    void setDocAuthors(String docAuthors) {
        this.docAuthors = docAuthors;
    }

    void setDocBibliography(String docBibliography) {
        this.docBibliography = docBibliography;
    }

    void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    void setDocContent(String docContent) {
        this.docContent = docContent;
    }
}
