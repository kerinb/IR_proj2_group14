package com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes;

import java.util.Arrays;
import java.util.List;

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

    public List<String> getListOFAvailableDocTags() {
        String[] allAvailableDocTags = {String.valueOf(TEXT_START), String.valueOf(TEXT_END), String.valueOf(
                HEADLINE_START), String.valueOf(HEADLINE_END), String.valueOf(BYLINE_START), String.valueOf(BYLINE_END),
                String.valueOf(DOC_NO_START), String.valueOf(DOC_NO_END), String.valueOf(DOC_ID_START), String.valueOf(
                        DOC_ID_END), String.valueOf(DOC_START), String.valueOf(DOC_END)};
        return Arrays.asList(allAvailableDocTags);
    }
}
