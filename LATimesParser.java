package com.lucene.document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LATimesParser {

	public static List<LATimesDocument> parse() throws IOException {

		List<LATimesDocument> parsedLADocsList = new ArrayList<>();
		String workingDir = System.getProperty("user.dir");
		String folderDir = workingDir.concat("/latimes");

		File folder = new File(folderDir);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			System.out.println("parsing: " + file.getName());

			String sgml = FileUtils.readFileToString(file, "utf-8");
			Document laTimesContent = Jsoup.parse(sgml);

			Elements docs = laTimesContent.select("DOC");

			for(Element doc: docs) {
				LATimesDocument laTimesDoc = new LATimesDocument();

				//get doc id
				laTimesDoc.setDocId(doc.select("DOCID").text());

				//get doc number
				laTimesDoc.setDocNo(doc.select("DOCNO").text());

				//get document date
				laTimesDoc.setDate(doc.select("DATE").select("P").text());

				//get headline
				laTimesDoc.setHeadline(doc.select("HEADLINE").select("P").text());

				//get section
				laTimesDoc.setSection(doc.select("SECTION").select("P").text());

				//get text 
				laTimesDoc.setText(doc.select("TEXT").select("P").text());

				//get byline
				laTimesDoc.setByline(doc.select("BYLINE").select("P").text());

				//add do parsed doc list
				parsedLADocsList.add(laTimesDoc);
			}
		}

//		for(LATimesDocument parsedDoc: parsedLADocsList) {
//			System.out.println("DocNumber: " + parsedDoc.getDocNo());
//
//			System.out.println("DocId: " + parsedDoc.getDocId());
//			System.out.println("Section:" + parsedDoc.getSection());
//			System.out.println("Date: " + parsedDoc.getDate());
//			System.out.println("Headline: " + parsedDoc.getHeadline());
//			System.out.println("Text: " + parsedDoc.getText());		
//			System.out.println("\n");
//		}

		return parsedLADocsList;
	}

}
