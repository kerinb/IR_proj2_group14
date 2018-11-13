package com.kerinb.IR_proj2_group14.DocumentFiles.LaTimes;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LATimesParser {

	public static List<org.apache.lucene.document.Document> loadLaTimesDocs(String pathToLATimesRegister) throws IOException {

		List<org.apache.lucene.document.Document> parsedLADocsList = new ArrayList<>();


		File folder = new File(pathToLATimesRegister);
		File[] listOfFiles = folder.listFiles();

		//iterate through the files
		for (File file : listOfFiles) {

			//String sgml = FileUtils.readFileToString(file, "utf-8");

			//	System.out.println("smgl:" + sgml);
			//	Document laTimesContent = Jsoup.parse(sgml);
			Document laTimesContent = Jsoup.parse(file, null, "");

			Elements docs = laTimesContent.select("DOC");

			for(Element doc: docs) {
				String docId,docNo,date,headline,section,text, byline;

				//get doc number
				docNo = (doc.select("DOCNO").text());

				//get doc id
				docId = doc.select("DOCID").text();

				//get document date
				date = (doc.select("DATE").select("P").text());

				//get headline
				headline = (doc.select("HEADLINE").select("P").text());

				//get section
				section = (doc.select("SECTION").select("P").text());

				//get text 
				text = (doc.select("TEXT").select("P").text());

				//get byline
				byline = (doc.select("BYLINE").select("P").text());

				//add do parsed doc list
				parsedLADocsList.add(createDocument(docNo, docId, date, headline, section, text, byline));

			}

		}
		return parsedLADocsList;

	}


	private static org.apache.lucene.document.Document createDocument(
			String docNo, String docId, String date, String headline, String section,String text, String byline) 
	{
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
		document.add(new StringField("docNo", docNo, Field.Store.YES));
		document.add(new StringField("docId", docId, Field.Store.YES));
		document.add(new TextField("date", date, Field.Store.YES) );
		document.add(new TextField("headline", headline, Field.Store.YES) );
		document.add(new TextField("section", section, Field.Store.YES) );
		document.add(new TextField("text", text, Field.Store.YES) );
		document.add(new TextField("byline", byline, Field.Store.YES) );
		return document;
	}

}
