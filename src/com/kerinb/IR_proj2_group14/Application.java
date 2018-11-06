package com.kerinb.IR_proj2_group14;

import com.kerinb.IR_proj2_group14.QueryFiles.QueryData;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
// import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kerinb.IR_proj2_group14.DocumentFiles.DocumentLibrary.callIndexDocuments;
import static com.kerinb.IR_proj2_group14.DocumentFiles.DocumentLibrary.callLoadDocumentsFromFile;
import static com.kerinb.IR_proj2_group14.QueryFiles.QueryLibrary.callLoadQueriesFromFile;
import static com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles.RankAndAnalyzers.*;

public class Application {

    private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
    private final static String absPathToSearchResults = String.format("%s/cran/cranQueryResults", currentRelativePath);
    // private final static String absPathToIndex = String.format("%s/index", currentRelativePath);
    private static final int MAX_RETURN_RESULTS = 1000;
    private static final String ITER_NUM = " 0 ";
    private static Similarity similarityModel = null;
    private static Analyzer analyzer = null;

    public static void main(String[] args) throws ParseException {
        System.out.println(String.format("ranking model: %s\t analyzer:%s", args[0], args[1]));
        if (args.length == 2 && validRankModel(args[0]) && validAnalyzer(args[1])) {

            similarityModel = callSetRankingModel(args[0]);
            analyzer =  callSetAnalyzer(args[1]);
            Directory directory = new RAMDirectory();
            // Directory directory = FSDirectory.open(Paths.get(absPathToIndex));

            // load in the documents
            List<Document> loadedDocs = callLoadDocumentsFromFile();
            System.out.println("loaded in Cranfield Documents");

            // set up the index
            callIndexDocuments(loadedDocs, similarityModel, analyzer, directory);
            System.out.println(String.format("indexed document list provided with: %s", args[0]));

            executeQueries(directory);
            System.out.println("Completed queries");

            analyzer.close();

            try {
                directory.close();
            } catch (IOException e) {
                System.out.println("ERROR: an error occurred when closing the directory!");
                System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
            }
        } else {
            System.out.println("User must provide a ranking model!\nThis should be added in the run.sh file - restore desired ranking model.");
        }git
    }

    private static Map<String, Float> createBoostMap() {
        Map<String, Float> boost = new HashMap<>();
        boost.put("id", (float) 0);
        boost.put("author", (float) 0);
        boost.put("bibliography", (float) 0);
        boost.put("title", (float) 0.2);
        boost.put("content", (float) 0.8);
        return boost;
    }

    private static IndexSearcher createIndexSearcher(IndexReader indexReader){
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(similarityModel);
        return indexSearcher;
    }

    private static void executeQueries(Directory directory) throws ParseException {
        try {
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = createIndexSearcher(indexReader);

            Map<String, Float> boost = createBoostMap();
            QueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "content", "id", "author", "bibliography"}, analyzer, boost);

            PrintWriter writer = new PrintWriter(absPathToSearchResults, "UTF-8");
            List<QueryData> loadedQueries = callLoadQueriesFromFile();

            for (int queryNum = 0; queryNum < loadedQueries.size(); queryNum++) {
                QueryData queryData = loadedQueries.get(queryNum);
                String queryContent = QueryParser.escape(queryData.getQueryContent());
                queryContent = queryContent.trim();
                Query query;

                if (queryContent.length() > 0) {

                    query = queryParser.parse(queryContent);
                    ScoreDoc[] hits = indexSearcher.search(query, MAX_RETURN_RESULTS).scoreDocs;

                    for (int hitIndex = 0; hitIndex < hits.length; hitIndex++) {
                        ScoreDoc hit = hits[hitIndex];
                        writer.println(queryNum + ITER_NUM + (hit.doc + 1) + " " + hitIndex + " " + Math.round(hit.score) + ITER_NUM);
                    }
                }
            }
            writer.flush();
            writer.close();
            indexReader.close();
        } catch (IOException e) {
            System.out.println("ERROR: an error occurred when reading the index from the diretory!");
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }
}

