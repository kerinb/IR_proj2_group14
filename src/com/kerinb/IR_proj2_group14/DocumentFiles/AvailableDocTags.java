package com.kerinb.IR_proj2_group14.DocumentFiles;

import java.util.Arrays;
import java.util.List;

public enum AvailableDocTags {
    ID(".I"), TITLE(".T"), AUTHORS(".A"), BIBLIOG(".B"), TEXT_BODY(".W");

    String tag;

    AvailableDocTags(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public List<String> getListOFAvailableDocTags() {
        String[] allAvailableDocTags = {String.valueOf(ID), String.valueOf(TITLE), String.valueOf(AUTHORS), String.valueOf(BIBLIOG), String.valueOf(TEXT_BODY)};
        return Arrays.asList(allAvailableDocTags);
    }
}
