package com.kerinb.IR_proj2_group14;

import com.kerinb.IR_proj2_group14.DocumentFiles.QueryFiles.QueryObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.TotalHitCountCollector;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.document.IntPoint;

import static com.kerinb.IR_proj2_group14.ApplicationLibrary.*;
import static com.kerinb.IR_proj2_group14.DocumentFiles.FBIS.FBISProcessor.loadFBISDocs;
import static com.kerinb.IR_proj2_group14.DocumentFiles.FederalRegister.FedRegister.loadFedRegisterDocs;
import static com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes.FinTimesLib.loadFinTimesDocs;
import static com.kerinb.IR_proj2_group14.DocumentFiles.LaTimes.LATimesParser.loadLaTimesDocs;
import static com.kerinb.IR_proj2_group14.DocumentFiles.QueryFiles.QueryLib.loadQueriesFromFile;
import static com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles.RankAndAnalyzers.*;

public class Application {

	private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
	private final static String absPathToSearchResults = String.format("%s/DataSet/queryResults", currentRelativePath);
	private final static String absPathToFinTimes = String.format("%s/DataSet/ft", currentRelativePath);
	private final static String absPathToFedRegister = String.format("%s/DataSet/fr94",currentRelativePath);
	private final static String absPathToLaTimes = String.format("%s/DataSet/latimes",currentRelativePath);
	private final static String absPathToFBIS = String.format("%s/DataSet/fbis",currentRelativePath);

	private final static String absPathToIndex = String.format("%s/Index", currentRelativePath);

	private static final int MAX_RETURN_RESULTS = 10000;
	private static final String ITER_NUM = " 0 ";

	private static Similarity similarityModel = null;
	private static Analyzer analyzer = null;

	private static List<Document> finTimesDocs = new ArrayList<>();
	private static List<Document> fedRegisterDocs = new ArrayList<>();
	private static List<Document> laTimesDocs = new ArrayList<>();
	private static List<Document> fbisDocs = new ArrayList<>();

	public static void main(String[] args) throws ParseException, IOException {
		System.out.println(String.format("Ranking model: %s\t Analyzer:%s", args[0], args[1]));
		if (args.length == 2 && validRankModel(args[0]) && validAnalyzer(args[1])) {

			similarityModel = callSetRankingModel(args[0]);
			analyzer =  callSetAnalyzer(args[1]);

			Directory directory;

			// so we don't need to parse &^ index everytime
            // THEREFORE everytime we want to test we need to delete the index
            // in terminal use rm -rf /Index/ to delete the index dir.
			if(!new File(absPathToIndex).exists()){
				directory = FSDirectory.open(Paths.get(absPathToIndex));
				loadDocs();
				indexDocuments(similarityModel, analyzer, directory);
			} else {
                System.out.println("Using previously loaded data!");
				directory = FSDirectory.open(Paths.get(absPathToIndex));
			}

			System.out.println("loading and executing queries");
			executeQueries(directory);

			analyzer.close();

			closeDirectory(directory);
		} else {
			System.out.println("User must provide a correct ranking model or analyser!");
			System.out.println("This should be added in the run.sh file - restore desired ranking model.");
		}
	}

	private static void indexDocuments(Similarity similarity, Analyzer analyzer, Directory directory) {
		IndexWriter indexWriter;
		IndexWriterConfig indexWriterConfig = setIndexWriterConfig(similarity, analyzer);

		try {
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			indexWriter.deleteAll();

			System.out.println("indexing financial times document collection");
			indexWriter.addDocuments(finTimesDocs);

			System.out.println("indexing federal register document collection");
			indexWriter.addDocuments(fedRegisterDocs);

			System.out.println("indexing la times document collection");
			indexWriter.addDocuments(laTimesDocs);

			System.out.println("indexing foreign broadcast information service document collection");
			indexWriter.addDocuments(fbisDocs);
			
			closeIndexWriter(indexWriter);

		} catch (IOException e) {
			System.out.println("ERROR: An error occurred when trying to instantiate a new IndexWriter");
			System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
		}
	}
	private static void loadDocs() throws IOException {
		
		System.out.println("loading financial times documents");
		List<String> finTimesFiles = getFileNamesFromDirTree(absPathToFinTimes);
		finTimesDocs = loadFinTimesDocs(finTimesFiles);
		System.out.println("loaded financial times documents");

		System.out.println("loading federal register documents");
		fedRegisterDocs = loadFedRegisterDocs(absPathToFedRegister);
		System.out.println("loaded federal register documents");

		System.out.println("loading foreign broadcast information service documents");
		fbisDocs= loadFBISDocs(absPathToFBIS);
		System.out.println("loaded foreign broadcast information service documents");

		System.out.println("loading la times documents");
		laTimesDocs= loadLaTimesDocs(absPathToLaTimes);
		System.out.println("loaded la times documents");
	}

	private static void executeQueries(Directory directory) throws ParseException {
		try {
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = createIndexSearcher(indexReader, similarityModel);

			Map<String, Float> boost = createBoostMap();
			QueryParser queryParser = new MultiFieldQueryParser(new String[]{"headline", "text"}, analyzer, boost);

            PrintWriter writer = new PrintWriter(absPathToSearchResults, "UTF-8");
			List<QueryObject> loadedQueries = loadQueriesFromFile();

			for (QueryObject queryData : loadedQueries) {

			    List<String> splitNarrative = splitNarrIntoRelNotRel(queryData.getNarrative());
                String relevantNarr = splitNarrative.get(0).trim();

				BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

				if (queryData.getTitle().length() > 0) {

					Query titleQuery = queryParser.parse(QueryParser.escape(queryData.getTitle()));
					Query descriptionQuery = queryParser.parse(QueryParser.escape(queryData.getDescription()));
					Query narrativeQuery = null;
					if(relevantNarr.length()>0) {
						narrativeQuery = queryParser.parse(QueryParser.escape(relevantNarr));
					}

					booleanQuery.add(new BoostQuery(titleQuery, (float) 4), BooleanClause.Occur.SHOULD);
					booleanQuery.add(new BoostQuery(descriptionQuery, (float) 1.7), BooleanClause.Occur.SHOULD);

					if (narrativeQuery != null) {
						booleanQuery.add(new BoostQuery(narrativeQuery, (float) 1.2), BooleanClause.Occur.SHOULD);
					}
					ScoreDoc[] hits = indexSearcher.search(booleanQuery.build(), MAX_RETURN_RESULTS).scoreDocs;

					for (int hitIndex = 0; hitIndex < hits.length; hitIndex++) {
						ScoreDoc hit = hits[hitIndex];
						writer.println(queryData.getQueryNum() + ITER_NUM + indexSearcher.doc(hit.doc).get("docno") +
								" " + hitIndex + " " + hit.score + ITER_NUM);
					}
				}
			}

			closeIndexReader(indexReader);
			closePrintWriter(writer);
			System.out.println("queries executed");

		} catch (IOException e) {
			System.out.println("ERROR: an error occurred when instantiating the printWriter!");
			System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
		}
	}

    private static List<String> splitNarrIntoRelNotRel(String narrative) {
        StringBuilder relevantNarr = new StringBuilder();
        StringBuilder irrelevantNarr = new StringBuilder();
        List<String> splitNarrative = new ArrayList<>();

        BreakIterator bi = BreakIterator.getSentenceInstance();
        bi.setText(narrative);
        int index = 0;
        while (bi.next() != BreakIterator.DONE) {
            String sentence = narrative.substring(index, bi.current());

            if (!sentence.contains("not relevant") && !sentence.contains("irrelevant")) {
                relevantNarr.append(sentence.replaceAll(
                        "a relevant document identifies|a relevant document could|a relevant document may|a relevant document must|a relevant document will|a document will|to be relevant|relevant documents|a document must|relevant|will contain|will discuss|will provide|must cite",
                        ""));
            } else {
                irrelevantNarr.append(sentence.replaceAll("are also not relevant|are not relevant|are irrelevant|is not relevant|not|NOT", ""));
            }
            index = bi.current();
        }
        splitNarrative.add(relevantNarr.toString());
        splitNarrative.add(irrelevantNarr.toString());
        return splitNarrative;
    }
}