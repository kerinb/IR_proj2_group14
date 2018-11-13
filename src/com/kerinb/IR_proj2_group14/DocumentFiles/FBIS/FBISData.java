package com.kerinb.IR_proj2_group14.DocumentFiles.FBIS;

public class FBISData {

	public String docNum;
	public String ht; 
	public String header;
	public String date;
	public String ti;
	public String text; 
	public String abs; 
	public String au; 
	public String h1;
	public String h2;
	public String h3;
	public String h4;
	public String h5;
	public String h6;
	public String h7;
	public String h8;
	public String tr; 
	public String txt5;
	
	public FBISData() {
		super(); 
	}
	
	public FBISData(String docNum, String ht) {
		this.docNum = docNum; 
		this.ht = ht; 
	}

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getHt() {
		return ht;
	}

	public void setHt(String ht) {
		this.ht = ht;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTi() {
		return ti;
	}

	public void setTi(String ti) {
		this.ti = ti;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public String getAu() {
		return au;
	}

	public void setAu(String au) {
		this.au = au;
	}

	public String getH1() {
		return h1;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	public String getH2() {
		return h2;
	}

	public void setH2(String h2) {
		this.h2 = h2;
	}

	public String getH3() {
		return h3;
	}

	public void setH3(String h3) {
		this.h3 = h3;
	}

	public String getH4() {
		return h4;
	}

	public void setH4(String h4) {
		this.h4 = h4;
	}

	public String getH5() {
		return h5;
	}

	public void setH5(String h5) {
		this.h5 = h5;
	}

	public String getH6() {
		return h6;
	}

	public void setH6(String h6) {
		this.h6 = h6;
	}

	public String getH7() {
		return h7;
	}

	public void setH7(String h7) {
		this.h7 = h7;
	}

	public String getH8() {
		return h8;
	}

	public void setH8(String h8) {
		this.h8 = h8;
	}

	public String getTr() {
		return tr;
	}

	public void setTr(String tr) {
		this.tr = tr;
	}

	public String getTxt5() {
		return txt5;
	}

	public void setTxt5(String txt5) {
		this.txt5 = txt5;
	}

	@Override
	public String toString() {
		return "FBISData [docNum=" + docNum + ", ht=" + ht + ", header=" + header + ", date=" + date + ", ti="
				+ ti + ", text=" + text + ", abs=" + abs + ", au=" + au + ", h1=" + h1 + ", h2=" + h2 + ", h3=" + h3
				+ ", h4=" + h4 + ", h5=" + h5 + ", h6=" + h6 + ", h7=" + h7 + ", h8=" + h8 + ", tr=" + tr + ", txt5="
				+ txt5 + "]";
	}
}
