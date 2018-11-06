package com.kerinb.IR_proj2_group14.DocumentFiles.QueryFiles;

public enum QueryTags {

    TOP_START("<top>"), TOP_END("</top>"), QUERY_NUMBER("<num> Number:"), QUERY_TITLE("<title>"), QUERY_DESCRIPTION(
            "<desc> Description:"), QUERY_NARRATIVE("<narr> Narrative:");

    String tag;

    QueryTags(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
}