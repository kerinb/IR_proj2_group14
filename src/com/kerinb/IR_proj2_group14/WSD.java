	//WSN implementation

				//                System.out.println("original query: " + queryData.getTitle().concat(" " + queryData.getDescription()).concat(" " + queryData.getNarrative()));
				//				String queryContent = WSD.disambiguate(queryData.getTitle().concat(" " + queryData.getDescription()).concat(" " + queryData.getNarrative()));
				//				System.out.println("query content: " + queryContent);
				//	



package com.kerinb.IR_proj2_group14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;

public class WSD {



	public static String disambiguate(String query) {

		String queryText = relevantQuery(query);
		System.out.println("relv query: " + queryText);
		BabelfyParameters bp = new BabelfyParameters();
		bp.setAnnotationResource(SemanticAnnotationResource.BN);
		bp.setMCS(MCS.ON_WITH_STOPWORDS);

		bp.setScoredCandidates(ScoredCandidates.ALL);
		Babelfy bfy = new Babelfy(bp);
		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(queryText, Language.EN);
		//bfyAnnotations is the result of Babelfy.babelfy() call


		//key: word
		//value: babel synset id
		HashMap<String, List<String>> wsdMap = new HashMap<>();

		for (SemanticAnnotation annotation : bfyAnnotations)
		{
			//System.out.println("annotation: " + annotation);

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

			//			System.out.println(frag + "\t" + annotation.getBabelSynsetID());
			//			System.out.println("\t" + annotation.getBabelNetURL());
			//			System.out.println("\t" + annotation.getDBpediaURL());
			//			System.out.println("\t" + annotation.getSource());
		}

		StringBuilder accBuilder = new StringBuilder();
		Iterator it = wsdMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.print(pair.getKey() + " = ");
			List<String> list =  (List<String>) pair.getValue();
			for(String vals: list)
				System.out.print(vals + "-");
			System.out.println();

		//	if(Double.parseDouble(list.get(0)) > 0.4) {
				//double score = Math.pow((Double.parseDouble(list.get(0)) + 1), 3);
			
				if(Double.parseDouble(list.get(0)) > 0.35) {
					
					double score = (Double.parseDouble(list.get(0)) + 1) * 10;
					System.out.println("score: " + score);
					System.out.println("\"" + pair.getKey() + "\"^"+score + " ");
					accBuilder.append("\"" + pair.getKey() + "\"^"+score + " ");
					
					
				}
				
				
				else {
					double score = (Double.parseDouble(list.get(0))) * 1;
					
					if(score > 0) {
						System.out.println("\"" + pair.getKey() + "\"^1"+  " ");
						accBuilder.append("\"" + pair.getKey() + "\"^1"+  " ");	
					}
					else {
						System.out.println("\"" + pair.getKey() + "\"^0" + " ");
						accBuilder.append("\"" + pair.getKey() + "\"^0" + " ");	
					}
					
				}
				
				System.out.println("\n");

	
//			}
//			else {
//			//	double score = (Double.parseDouble(list.get(0)) ) * 0.2;
//
//				if(Double.parseDouble(list.get(0)) > 0.4) {
//					
//				}
//
//			}


			it.remove(); // avoids a ConcurrentModificationException


		}

		return accBuilder.toString();
	}

	private static String relevantQuery(String query) {

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

	private static boolean isIrrelevant(String word) {

		List<String> stopWords = Arrays.asList(
				"a", "an", "and", "are", "as", "at", "be", "but", "by",
				"for", "if", "in", "into", "is", "it",
				"no", "not", "of", "on", "or", "such",
				"that", "the", "their", "then", "there", "these",
				"they", "this", "to", "was", "will", "with", "what", "?"
				);

		List<String> irlvWords = new ArrayList();
		irlvWords.add("discuss");
		irlvWords.add("discussion");
		irlvWords.add("discussions");
		irlvWords.add("document");
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
