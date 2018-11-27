package com.kerinb.IR_proj2_group14;


import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.LogManager;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Constants;
import org.apache.lucene.wordnet.SynonymMap;
import org.apache.lucene.wordnet.Syns2Index;


import org.apache.lucene.wordnet.SynExpand;
public class WordnetSyn {




	private static FSDirectory  directory;

	private static IndexSearcher  searcher;

	private static String INDEX_DIR;


	public static void main(String...args) throws Throwable {

		List<String> qryList = new ArrayList<>();
		qryList.add("shipwreck");

		String expanded = getSynonyms("difficulty");
		System.out.println("expanded: " + expanded);

	}


	public static String expandQuery(String words) throws IOException {
		String workingDir = System.getProperty("user.dir");
		//build the index
		String fileDir = workingDir.concat("/wordnet/wn_s.pl");

		String arr[] = {fileDir, words};

		SynExpand.main(arr);
		//synExpand.expand("big dog", null,null, null, 0.8)

		return null;
	}


	public static String getSynonyms(String queryContent) throws FileNotFoundException, IOException {

		queryContent = removeStopwords(queryContent);

		String workingDir = System.getProperty("user.dir");
		//build the index
		String fileDir = workingDir.concat("/wordnet/wn_s.pl");
		HashMap<String, List<String>> hmap = new HashMap<String, List<String>>();

		System.out.println("parsing: " + queryContent);
		List<String> words = Arrays.asList(queryContent.split("\\s+"));


		SynonymMap map = new SynonymMap(new FileInputStream(fileDir)); 	

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < words.size(); i++) 
		{ 
			//	System.out.println("word: " + words.get(i));

			String[] synsArray = map.getSynonyms(words.get(i)); 
			//check if has synonyms
			if(synsArray.length > 1) {
				//sub-query
				builder.append("(");
				//append the word itself
				builder.append(words.get(i));
				builder.append(" OR ");

				for(int k=0;k<synsArray.length - 1;k++) {
					//append its synonyms
					builder.append(synsArray[k]);
					builder.append(" OR ");
				}
				builder.append(synsArray[synsArray.length - 1]);
				builder.append(") ");
			}

			else {
				builder.append(words.get(i));
				builder.append(" ");

			}	

		}

		return builder.toString();
	}



	private static String removeStopwords(String queryContent) {
		
		
		
		queryContent = queryContent.replace("/", " ");
		queryContent = queryContent.replace("\\", " ");
		queryContent = queryContent.replace("?", "");
		queryContent = queryContent.replace(":", " ");
		List<String> stopWords = Arrays.asList(
				"a", "an", "and", "are", "as", "at", "be", "but", "by",
				"for", "if", "in", "into", "is", "it",
				"no", "not", "of", "on", "or", "such",
				"that", "the", "their", "then", "there", "these",
				"they", "this", "to", "was", "will", "with", "what", "?", "/", "\\", ";", "-", "(", ")"
				);
		for(String stopword: stopWords) {
			queryContent.replace(stopword, "");
		}
		return queryContent;
	}


	private static void getSynonyms() throws FileNotFoundException, IOException {
		String workingDir = System.getProperty("user.dir");
		//build the index
		String fileDir = workingDir.concat("/wordnet/wn_s.pl");

		String[] words = new String[] { "similarity", "laws", "heatedup", "heridtary"}; 
		SynonymMap map = new SynonymMap(new FileInputStream(fileDir)); 
		for (int i = 0; i < words.length; i++) 
		{ 
			String[] synonyms = map.getSynonyms(words[i]); 
			System.out.println(words[i] + ":" + java.util.Arrays.asList(synonyms).toString());
		}
	}


	private static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}