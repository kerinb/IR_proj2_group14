package com.kerinb.IR_proj2_group14.DocumentFiles.LaTimes;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.IntPoint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LATimesParser {

	public static List<org.apache.lucene.document.Document> loadLaTimesDocs(String pathToLATimesRegister) throws IOException {

		List<org.apache.lucene.document.Document> parsedLADocsList = new ArrayList<>();

		File folder = new File(pathToLATimesRegister);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			String filename = file.getName().substring(2);
			String date = filename.substring(4) + filename.substring(0,2) + filename.substring(2,4);

			Document laTimesContent = Jsoup.parse(file, null, "");

			Elements docs = laTimesContent.select("DOC");

			for(Element doc: docs) {
				String docNo, headline, text;
				docNo = (doc.select("DOCNO").text());
				headline = (doc.select("HEADLINE").select("P").text());
				text = (doc.select("TEXT").select("P").text());
				parsedLADocsList.add(createDocument(docNo, headline, text, date));
			}

		}
		return parsedLADocsList;
	}

	private static org.apache.lucene.document.Document createDocument(
			String docNo, String headline,String text,String date) {
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
		document.add(new StringField("docno", docNo, Field.Store.YES));
		document.add(new TextField("headline", headline, Field.Store.YES) );
		document.add(new TextField("text", text, Field.Store.YES) );
		document.add(new IntPoint("date", Integer.valueOf(date)) );
		return document;
	}
}


