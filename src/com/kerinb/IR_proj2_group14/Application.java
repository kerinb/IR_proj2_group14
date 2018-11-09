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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kerinb.IR_proj2_group14.ApplicationLibrary.closeDirectory;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.createBoostMap;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.closePrintWriter;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.closeIndexReader;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.closeIndexWriter;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.getFileNamesFromDirTree;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.setIndexWriterConfig;
import static com.kerinb.IR_proj2_group14.ApplicationLibrary.createIndexSearcher;
import static com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes.FinTimesLib.loadFinTimesDocs;
import static com.kerinb.IR_proj2_group14.DocumentFiles.QueryFiles.QueryLib.loadQueriesFromFile;
import static com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles.RankAndAnalyzers.callSetRankingModel;
import static com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles.RankAndAnalyzers.validAnalyzer;
import static com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles.RankAndAnalyzers.validRankModel;
import static com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles.RankAndAnalyzers.callSetAnalyzer;

import static com.kerinb.IR_proj2_group14.DocumentFiles.FederalRegister.FedRegister.loadFedRegisterDocs;

public class Application {

    private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
    private final static String absPathToSearchResults = String.format("%s/DataSet/queryResults", currentRelativePath);
    private final static String absPathToFinTimes = String.format("%s/DataSet/ft", currentRelativePath);
    private final static String absPathToFedRegister = String.format("%s/DataSet/fr94",currentRelativePath);

    private final static String absPathToIndex = String.format("%s/Index", currentRelativePath);

    private static final int MAX_RETURN_RESULTS = 1000;
    private static final String ITER_NUM = " 0 ";

    private static Similarity similarityModel = null;
    private static Analyzer analyzer = null;

    private static List<Document> finTimesDocs = new ArrayList<>();
    private static List<Document> fedRegisterDocs = new ArrayList<>();
    // @TODO - other List<Document> can be added here for the other document collections.

    public static void main(String[] args) throws ParseException, IOException {
        System.out.println(String.format("Ranking model: %s\t Analyzer:%s", args[0], args[1]));
        if (args.length == 2 && validRankModel(args[0]) && validAnalyzer(args[1])) {

            similarityModel = callSetRankingModel(args[0]);
            analyzer =  callSetAnalyzer(args[1]);
            Directory directory = FSDirectory.open(Paths.get(absPathToIndex));
            loadDocs();
            indexDocuments(similarityModel, analyzer, directory);
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
            // @TODO - Add your document collections to the index here

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
        // @TODO - Other document collections can be added here
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
                String queryContent = QueryParser.escape(queryData.getDescription());
                queryContent = queryContent.trim();

                Query query;

                if (queryContent.length() > 0) {

                    query = queryParser.parse(queryContent);
                    ScoreDoc[] hits = indexSearcher.search(query, MAX_RETURN_RESULTS).scoreDocs;

                    for (int hitIndex = 0; hitIndex < hits.length; hitIndex++) {
                        ScoreDoc hit = hits[hitIndex];
                        writer.println(queryData.getQueryNum() + ITER_NUM + indexSearcher.doc(hit.doc).get("docno") +
                                " " + hitIndex + " " + hit.score + ITER_NUM);
                    }
                }
            }

            closeIndexReader(indexReader);
            closePrintWriter(writer);

        } catch (IOException e) {
            System.out.println("ERROR: an error occurred when instantiating the printWriter!");
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }
}

