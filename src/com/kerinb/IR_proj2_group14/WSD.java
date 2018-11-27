package com.kerinb.IR_proj2_group14;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;

import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;

public class WSD {

	final static String TITLE_TYPE = "TITLE";
	final static String BODY_TYPE = "BODY";
	HashMap<String, KeyValuePair> boostMap;


	public WSD() {
		boostMap = new HashMap<>();

	}



	public String disambiguate(String title, String body) throws FileNotFoundException, IOException {

		score(TITLE_TYPE, title);
		score(BODY_TYPE, body);

		return getScore();
	}


	private String getScore() throws FileNotFoundException, IOException {

		//iterate over boost map

		Iterator it = boostMap.entrySet().iterator();
		StringBuilder accBuilder = new StringBuilder();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();

			String word = (String) pair.getKey();


			KeyValuePair infoPair = (KeyValuePair) pair.getValue();

			//boost term query

			String expanded = WordnetSyn.getSynonyms(word).trim();

			if(expanded.contains("(")){
				accBuilder.append(expanded);
				accBuilder.append( "^");
				accBuilder.append(infoPair.key);
				accBuilder.append(" ");
			}

			else {
				accBuilder.append("\"");
				accBuilder.append(word);
				accBuilder.append( "\"~^");
				accBuilder.append(infoPair.key);
				accBuilder.append(" ");
			}

	

		}

		return accBuilder.toString();

	}



	public String score(String type,String query ) {


		query = query.replace("/", " ");
		query = query.replace("\\", " ");
		query = query.replace("?", "");
		query = query.replace(":", " ");
		String queryText = relevantQuery(query);
		System.out.println("relv query: " + queryText);

		BabelfyParameters bp = new BabelfyParameters();
		bp.setAnnotationResource(SemanticAnnotationResource.BN);
		bp.setMCS(MCS.ON_WITH_STOPWORDS);
		bp.setScoredCandidates(ScoredCandidates.ALL);
		Babelfy bfy = new Babelfy(bp);
		StringBuilder accBuilder = new StringBuilder();

		HashMap<String, List<String>> wsdMap = new HashMap<>();


		if(!queryText.isEmpty()) {
			List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(queryText, Language.EN);


			//key: word
			//value: babel synset id

			for (SemanticAnnotation annotation : bfyAnnotations)
			{

				//splitting the input text using the CharOffsetFragment start and end anchors
				String frag = queryText.substring(annotation.getCharOffsetFragment().getStart(),
						annotation.getCharOffsetFragment().getEnd() + 1);

				if(wsdMap.get(frag) == null) {
					List<String> list = new ArrayList<>();
					list.add(annotation.getScore() + "");
					list.add(annotation.getBabelSynsetID());

					wsdMap.put(frag, list);
				}
				else {

					//check the score
					if(Double.parseDouble(wsdMap.get(frag).get(0)) < annotation.getScore()) {
						List<String> list = new ArrayList<>();
						list.add(annotation.getScore() + "");
						list.add(annotation.getBabelSynsetID());

						wsdMap.put(frag, list);
					}

				}

			}

			Iterator it = wsdMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				String word = (String) pair.getKey();
				System.out.print(pair.getKey() + " = ");
				List<String> list =  (List<String>) pair.getValue();
				for(String vals: list)
					System.out.print(vals + "-");
				System.out.println();


				//check if already in score map, if last edit by title, then multiply by 1.5 else if last edit by body then 1.1

				System.out.println("for " + word);

				if(type.equals(TITLE_TYPE)) {
					System.out.println("Entering title");
					
//					if(Double.parseDouble(list.get(0)) > 0.65) {
						double score = (Double.parseDouble(list.get(0))) + 1;
						double boostScore = (Double.parseDouble(list.get(0)) + 1) * 2;
						//put in score map, indicating it is the title
						KeyValuePair infoPair = new KeyValuePair(2, TITLE_TYPE);
						boostMap.put(word, infoPair);
						System.out.println("boosting for first time: " );

//
//					}
//
//					else {
//						double score = (Double.parseDouble(list.get(0)));
//						System.out.println("score: " + score);
//
//
//						if(score > 0) {
//							System.out.println("score is greater than 0.65");
//							double boostScore = (score + 1) * 1.5;
//							KeyValuePair infoPair = new KeyValuePair(1.3, TITLE_TYPE);
//							boostMap.put(word, infoPair);
//							System.out.println("boosting for first time: " );
//
//						}
//						else {
//							System.out.println("score is less than 0.65");
//
//							KeyValuePair infoPair = new KeyValuePair(0.7, TITLE_TYPE);
//							boostMap.put(word, infoPair);
//							System.out.println("boosting for first time: 1" );
//
//						}
//
//
//
//
//					}


					System.out.println("FINSIHED BOOSTING  \n\n");


				}

				else {
					System.out.println(" \n\n Entering body ");

					//else body

					double score = (Double.parseDouble(list.get(0)));

					if(Double.parseDouble(list.get(0)) > 0.5) {

						System.out.println("relevance > 0.5'");
						//check if already in score boost map

						//if boost was from title
						if(boostMap.get(word) != null) {
							KeyValuePair infoPair = boostMap.get(word);

							if(infoPair.value.equals(TITLE_TYPE)) {
								//was already in title
								double boostScore = (infoPair.key) * 1.1;
								KeyValuePair newInfoPair = new KeyValuePair(1.5, BODY_TYPE);
								boostMap.put(word, newInfoPair);
								System.out.println("boosing again + found title: " + boostScore);

							}
						}
						else {
							double boostScore = (score + 1) * 1.2;
							KeyValuePair newInfoPair = new KeyValuePair(1.5, BODY_TYPE);
							boostMap.put(word, newInfoPair);
							System.out.println("boosting for first time: " + boostScore);

						}





						System.out.println("finsihed score for > 0.5");
					}

					//else {
						System.out.println("score less than 0.5");
						if(score > 0) {
							double boostScore = (score + 1) * 1.3;
							KeyValuePair newInfoPair = new KeyValuePair(1.2, BODY_TYPE);
							boostMap.put(word, newInfoPair);
							System.out.println("boosting for first time/ body" + boostScore);
//						}
//						else {
							//score less than or equal to 0
//							double boostScore = 1;
//							KeyValuePair newInfoPair = new KeyValuePair(0.5, BODY_TYPE);
//							boostMap.put(word, newInfoPair);
//							System.out.println("boosting for first time/ body" + boostScore);

					//	}


					}
					//System.out.println("FINSIHED BOOSTING  \n\n");


				}



				it.remove(); // avoids a ConcurrentModificationException


			}

		}

		return accBuilder.toString();

	}



	private String relevantQuery(String query) {

		String[] words = query.split("\\s+");
		StringBuilder relvBuilder = new StringBuilder();
		for(String word: words) {

			if(! isIrrelevant(word)) {
				relvBuilder.append(word);
				relvBuilder.append(" ");
				//System.out.println(relvBuilder.toString());
			}

		}
		return relvBuilder.toString();
	}

	private boolean isIrrelevant(String word) {


		List<String> stopWords = Arrays.asList(
				"a", "an", "and", "are", "as", "at", "be", "but", "by",
				"for", "if", "in", "into", "is", "it",
				"no", "not", "of", "on", "or", "such",
				"that", "the", "their", "then", "there", "these",
				"they", "this", "to", "was", "will", "with", "what", "?", "/", "\\", ";", "-", "(", ")"
				);

		List<String> irlvWords = new ArrayList();
		irlvWords.add("discuss");
		irlvWords.add("focus");
		irlvWords.add("focuses");
		irlvWords.add("discussion");
		irlvWords.add("discussions");
		irlvWords.add("document");
		irlvWords.add("mention");
		irlvWords.add("mere");
		irlvWords.add("relevant");
		irlvWords.add("documents");
		irlvWords.add("relevant");
		irlvWords.add("describe");
		irlvWords.add("find");
		irlvWords.add("information");
		irlvWords.add("include");
		irlvWords.add("any");
		irlvWords.add("must");
		irlvWords.add("deemed");
		irlvWords.add("indicate");

		for(String irlvWord: irlvWords) {

			if(word.toLowerCase().equals(irlvWord) || word.toLowerCase().startsWith(irlvWord)) {
				//System.out.println("the word " + word + "is irrelevant" );
				return true;

			}
		}

		if(stopWords.contains(word)) {
			return true;
		}


		return false;
	}


}
