package com.kerinb.IR_proj2_group14.DocumentFiles.FBIS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FBISProcessor {

	private static BufferedReader br;
	private static List<Document> fbisDocList = new ArrayList<Document>();

	public static List<Document> loadFBISDocs(String fbisDirectory) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(fbisDirectory));
		for(String fbisFile : dir.listAll()) {
			br = new BufferedReader(new FileReader(fbisDirectory + "/" + fbisFile));
    		process();
    	}
		return fbisDocList;
	}
	
	private static void process() throws IOException {

		String file = readFile();
		org.jsoup.nodes.Document document = Jsoup.parse(file);
				
		List<Element> list = document.getElementsByTag("doc");
		
		for(Element doc : list) {
			
			FBISData fbisData = new FBISData();
			if(doc.getElementsByTag(FBISTags.DOCNO.name()) != null) 	fbisData.setDocNum(trimData(doc, FBISTags.DOCNO));
			if(doc.getElementsByTag(FBISTags.HEADER.name()) != null)	fbisData.setHeader(trimData(doc, FBISTags.HEADER));
			if(doc.getElementsByTag(FBISTags.TEXT.name()) != null) 		fbisData.setText(trimData(doc, FBISTags.TEXT));
			if(doc.getElementsByTag(FBISTags.ABS.name()) != null) 		fbisData.setAbs(trimData(doc, FBISTags.ABS));
			if(doc.getElementsByTag(FBISTags.AU.name()) != null) 		fbisData.setAu(trimData(doc, FBISTags.AU));
			if(doc.getElementsByTag(FBISTags.DATE1.name()) != null) 	fbisData.setDate(trimData(doc, FBISTags.DATE1));
			if(doc.getElementsByTag(FBISTags.H1.name()) != null) 		fbisData.setH1(trimData(doc, FBISTags.H1));
			if(doc.getElementsByTag(FBISTags.H2.name()) != null) 		fbisData.setH2(trimData(doc, FBISTags.H2));
			if(doc.getElementsByTag(FBISTags.H3.name()) != null) 		fbisData.setH3(trimData(doc, FBISTags.H3));
			if(doc.getElementsByTag(FBISTags.H4.name()) != null) 		fbisData.setH4(trimData(doc, FBISTags.H4));
			if(doc.getElementsByTag(FBISTags.H5.name()) != null) 		fbisData.setH5(trimData(doc, FBISTags.H5));
			if(doc.getElementsByTag(FBISTags.H6.name()) != null) 		fbisData.setH6(trimData(doc, FBISTags.H6));
			if(doc.getElementsByTag(FBISTags.H7.name()) != null) 		fbisData.setH7(trimData(doc, FBISTags.H7));
			if(doc.getElementsByTag(FBISTags.H8.name()) != null) 		fbisData.setH8(trimData(doc, FBISTags.H8));
			if(doc.getElementsByTag(FBISTags.HT.name()) != null) 		fbisData.setHt(trimData(doc, FBISTags.HT));
			if(doc.getElementsByTag(FBISTags.TR.name()) != null) 		fbisData.setTr(trimData(doc, FBISTags.TR));
			if(doc.getElementsByTag(FBISTags.TXT5.name()) != null) 		fbisData.setTxt5(trimData(doc, FBISTags.TXT5));
			if(doc.getElementsByTag(FBISTags.TI.name()) != null) 		fbisData.setTi(trimData(doc, FBISTags.TI));
			
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

        document.add(new StringField("id", fbisData.getDocNum(), Field.Store.YES));
        document.add(new StringField("date", fbisData.getDate(), Field.Store.YES));
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
