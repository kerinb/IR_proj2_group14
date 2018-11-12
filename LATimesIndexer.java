package com.lucene.document;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BasicModelIn;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.NormalizationZ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LATimesIndexer 
{
	private String index_dir;

	public LATimesIndexer(String index_dir) {
		this.index_dir = index_dir;
	}

	public void indexDocs() throws IOException {

		IndexWriter writer = createWriter();
		List<LATimesDocument> parsedDocs = LATimesParser.parse();

		List<Document> documents = new ArrayList<>();

		Document tempDoc = new Document();
		for(LATimesDocument parsedDoc:parsedDocs) {
			tempDoc = createDocument(parsedDoc.getDocNo(), parsedDoc.getDocId(), parsedDoc.getDate(), parsedDoc.getHeadline(),
					parsedDoc.getSection(), parsedDoc.getText());
			documents.add(tempDoc);
		}

		//clean everything 
		writer.deleteAll();
		//add the documents
		writer.addDocuments(documents);
		System.out.println(documents.size() + " documents indexed");
		writer.commit();
		writer.close();
	}

	private Document createDocument(String docNo, String docId, String date, String headline, String section,
			String text) {
		Document document = new Document();
		document.add(new StringField("docNo", docNo, Field.Store.YES));
		document.add(new StringField("docId", docId, Field.Store.YES));
		document.add(new TextField("date", date, Field.Store.YES) );
		document.add(new TextField("headline", headline, Field.Store.YES) );
		document.add(new TextField("section", section, Field.Store.YES) );
		document.add(new TextField("text", text, Field.Store.YES) );
		return document;
	}


	private IndexWriter createWriter() throws IOException 
	{
		FSDirectory dir = FSDirectory.open(Paths.get(index_dir));
		IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer(CharArraySet.EMPTY_SET));
		IndexWriter writer = new IndexWriter(dir, config);
		return writer;
	}
}
