package com.lucene.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;

public class Main {

	public static void main(String...args) throws IOException, ParseException, java.text.ParseException {
		//		String workingDir = System.getProperty("user.dir");
		//		String indexStoreDir = workingDir.concat("/INDEX_DIR");
		//		//write
		//		LATimesIndexer indexWriter = new LATimesIndexer(indexStoreDir);
		//		indexWriter.indexDocs();	
		//		System.out.println("docs indexed");
		SynonymMap synMap = buildSynonym();


	}



	private static SynonymMap buildSynonym() throws IOException, ParseException, java.text.ParseException
	{   
		
		System.out.print("build");
		
		String workingDir = System.getProperty("user.dir");
		String fileDir = workingDir.concat("/latimes/wn_s.pl");
		
		File file = new File(fileDir);
		
		InputStream stream = new FileInputStream(file);
		
		Reader rulesReader = new InputStreamReader(stream); 
		SynonymMap.Builder parser = null;
		parser = new WordnetSynonymParser(true, true, new StandardAnalyzer(CharArraySet.EMPTY_SET));
		System.out.print(parser.toString());
		((WordnetSynonymParser) parser).parse(rulesReader);  
		SynonymMap synonymMap = parser.build();
		return synonymMap;
	}

}
