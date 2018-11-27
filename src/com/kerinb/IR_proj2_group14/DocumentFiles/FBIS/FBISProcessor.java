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
			if(doc.getElementsByTag(FBISTags.TEXT.name()) != null) 		fbisData.setText(trimData(doc, FBISTags.TEXT));
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
        document.add(new StringField("docno", fbisData.getDocNum(), Field.Store.YES));
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
