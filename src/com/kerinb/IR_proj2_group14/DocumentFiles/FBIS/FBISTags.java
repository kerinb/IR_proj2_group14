package com.kerinb.IR_proj2_group14.DocumentFiles.FBIS;

public enum FBISTags {
	HEADER("<HEADER>"), TEXT("<TEXT>"), ABS("<ABS>"), AU("<AU>"),
    DATE1("<DATE1>"), DOC("<DOC>"), DOCNO("<DOCNO>"), H1("<H1>"),
    H2("<H2>"), H3("<H3>"), H4("<H4>"), H5("<H5>"),
    H6("<H6>"), H7("<H7>"), H8("<H8>"), HT("<HT>"),
    TR("<TR>"), TXT5("<TXT5>"), TI("<TI>");	//title 
	
	String tag; 
	
	FBISTags(String tag) {
		this.tag = tag; 
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
