package com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes;

public enum FinTimesTags {

    TEXT_START("<TEXT>"), TEXT_END("</TEXT>"), HEADLINE_START("<HEADLINE>"), HEADLINE_END("</HEADLINE>"), BYLINE_START(
            "<BYLINE>"), BYLINE_END("</BYLINE>"), DOC_NO_START("<DOCNO>"), DOC_NO_END("</DOCNO>"), DOC_ID_START(
                    "<DOCID>"), DOC_ID_END("</DOCID>"), DOC_START("<DOC>"), DOC_END("</DOC>");

    String tag;

    FinTimesTags(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
}
