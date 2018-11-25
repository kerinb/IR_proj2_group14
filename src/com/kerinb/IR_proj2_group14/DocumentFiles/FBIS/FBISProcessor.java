package com.kerinb.IR_proj2_group14.DocumentFiles.FBIS;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FBISProcessor {

	private static BufferedReader br;
	private static List<Document> fbisDocList = new ArrayList<>();
	private static final String [] IGNORE_FILES = {"readchg.txt", "readmefb.txt"};

	public static List<Document> loadFBISDocs(String fbisDirectory) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(fbisDirectory));
		for(String fbisFile : dir.listAll()) {
			if(!fbisFile.equals(IGNORE_FILES[0]) && !fbisFile.equals(IGNORE_FILES[1])) {
				br = new BufferedReader(new FileReader(fbisDirectory + "/" + fbisFile));
				process();
			}
    	}
		return fbisDocList;
	}
	
	private static void process() throws IOException {

		String file = readFile();
		org.jsoup.nodes.Document document = Jsoup.parse(file);
				
		List<Element> list = document.getElementsByTag("doc");

		String date = "",day = "",month ="",year ="";
		
		for(Element doc : list) {
			
			FBISData fbisData = new FBISData();
			if(doc.getElementsByTag(FBISTags.DOCNO.name()) != null) 	fbisData.setDocNum(trimData(doc, FBISTags.DOCNO));
			if(doc.getElementsByTag(FBISTags.HEADER.name()) != null)	fbisData.setHeader(trimData(doc, FBISTags.HEADER));
			if(doc.getElementsByTag(FBISTags.TEXT.name()) != null) 		fbisData.setText(trimData(doc, FBISTags.TEXT));
            if(doc.getElementsByTag(FBISTags.TI.name()) != null) 		fbisData.setTi(trimData(doc, FBISTags.TI));

            if(doc.getElementsByTag(FBISTags.DATE1.name()) != null){

				date = trimData(doc, FBISTags.DATE1).trim();
				day = date.substring(0,2).trim();
				if(day.length()==1) day = "0" + day;
				month = date.substring(2,date.length()-4).trim();
				if(date.endsWith("*")) year = date.substring(date.length()-3,date.length()-1).trim();
				else year = date.substring(date.length()-2,date.length()).trim();

				if(date.startsWith("M")) {
					month = date.substring(0,date.length()-7).trim();
					day = date.substring(date.length()-7,date.length()-4).trim();
					year = date.substring(date.length()-2,date.length()).trim();
				}

				if(month.startsWith("Jan")) month = "01";
				else if(month.startsWith("Feb")) month = "02";
                else if(month.startsWith("Mar")|| month.startsWith("MAR")) month = "03";
                else if(month.startsWith("Apr")) month = "04";
                else if(month.startsWith("May")) month = "05";
                else if(month.startsWith("Jun")) month = "06";
                else if(month.startsWith("Jul")) month = "07";
                else if(month.startsWith("Aug")) month = "08";
                else if(month.startsWith("Sep")) month = "09";
                else if(month.startsWith("Oct")) month = "10";
                else if(month.startsWith("Nov")) month = "11";
                else if(month.startsWith("Dec")) month = "12";

				date = year + month + day;

				fbisData.setDate(date);
			}

			fbisDocList.add(createFBISDocument(fbisData));
		}
	}

	private static String trimData(Element doc, FBISTags tag) {
		
		Elements element = doc.getElementsByTag(tag.name());
		Elements tmpElement = element.clone(); 
		//remove any nested 
		removeNestedTags(tmpElement, tag);
		String data = tmpElement.toString(); 
		
		//remove any instance of "\n" 
		if(data.contains("\n"))
			data = data.replaceAll("\n", "").trim();
		//remove start and end tags 
		if(data.contains(("<" + tag.name() + ">").toLowerCase()))
			data = data.replaceAll("<" + tag.name().toLowerCase() + ">", "").trim();
		if(data.contains(("</" + tag.name() + ">").toLowerCase()))
			data = data.replaceAll("</" + tag.name().toLowerCase() + ">", "").trim();
		
		return data; 
	}
	
	private static void removeNestedTags(Elements element, FBISTags currTag) {

		for(FBISTags tag : FBISTags.values()) {
			if(element.toString().contains("<" + tag.name().toLowerCase() + ">") && 
					element.toString().contains("</" + tag.name().toLowerCase() + ">") && !tag.equals(currTag)) {
				element.select(tag.toString()).remove();
			}
		}
	}

	private static Document createFBISDocument(FBISData fbisData) {
        Document document = new Document();
        int date = 0;
		if(fbisData.getDate().chars().allMatch(Character::isDigit)) {
			date = Integer.valueOf(fbisData.getDate());
		}

        document.add(new StringField("docno", fbisData.getDocNum(), Field.Store.YES));
       	document.add(new IntPoint("date", date));
        document.add(new TextField("headline", fbisData.getTi(), Field.Store.YES));
        document.add(new TextField("text", fbisData.getText(), Field.Store.YES));

        return document;
    }
	
	private static String readFile() throws IOException {
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
}
