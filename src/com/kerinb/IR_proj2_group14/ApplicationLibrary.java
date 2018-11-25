package com.kerinb.IR_proj2_group14;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ApplicationLibrary {

    static List<String> getFileNamesFromDirTree(String rootDir){
        List<String> files = new ArrayList<>();
        try {
            Files.walk(Paths.get(rootDir)).forEach(path ->{
                File file = new File(path.toString());
                if(file.isFile() && ! file.getName().contains("read")){
                    files.add(path.toString());
                }
            });
        } catch (IOException e) {
            System.out.println(String.format("ERROR: IOException occurred when walking file tree: %s", rootDir));
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
        return files;
    }

    static IndexWriterConfig setIndexWriterConfig(Similarity similarity, Analyzer analyzer){
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        return  indexWriterConfig.setSimilarity(similarity).setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    }

    static Map<String, Float> createBoostMap() {
        Map<String, Float> boost = new HashMap<>();
        boost.put("headline", (float) 0.1);
        boost.put("text", (float) 0.9);
        return boost;
    }

    static IndexSearcher createIndexSearcher(IndexReader indexReader, Similarity similarityModel){
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(similarityModel);
        return indexSearcher;
    }

    static void closePrintWriter(PrintWriter writer){
        writer.flush();
        writer.close();
    }

    static void closeIndexReader(IndexReader indexReader) {
        try {
            indexReader.close();
        } catch (IOException e) {
            System.out.println("ERROR: an error occurred when closing the index from the directory!");
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }

    static void closeDirectory(Directory directory){
        try {
            directory.close();
        } catch (IOException e) {
            System.out.println("ERROR: an error occurred when closing the directory!");
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }

    static void closeIndexWriter(IndexWriter indexWriter) {
        try {
            indexWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR: an error occurred when closing the index from the directory!");
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }
}