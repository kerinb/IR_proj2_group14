package com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinTimesLib {
    public static List<Document> loadFinTimesDocs(List<String> finTimesFiles) {
        for(String fileName : finTimesFiles){
            System.out.println(fileName);

            try {
                BufferedReader bf = new BufferedReader(new FileReader(fileName));

                // need to parse the text files here
                // and then use the parsed data to create document objects.

                try {
                    bf.close();
                } catch (IOException e) {
                    System.out.println(String.format("ERROR: IOException occurred when clsoing file: %s", fileName));
                    System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
                }
            } catch (FileNotFoundException e) {
                System.out.println(String.format("ERROR: FileNotFoundExcpeiton occurred when trying to read file: %s", fileName));
                System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
            }
        }

        return null;
    }


    private static IndexWriterConfig setIndexWriterConfig(Similarity similarity, Analyzer analyzer){
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        return  indexWriterConfig.setSimilarity(similarity).setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    }

    public static void indexDocuments(List<Document> loadedDocs, Similarity similarity, Analyzer analyzer, Directory directory) {
        IndexWriter indexWriter;
        IndexWriterConfig indexWriterConfig = setIndexWriterConfig(similarity, analyzer);

        try {
            indexWriter = new IndexWriter(directory, indexWriterConfig);

            try {
                indexWriter.addDocuments(loadedDocs);
            } catch (Exception e) {
                System.out.println("ERROR: an error occurred when adding a new document to the index!");
                System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
            }

            try {
                indexWriter.close();
            } catch (Exception e) {
                System.out.println("ERROR: An error occurred when trying to close the indexWrite");
                System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
            }

        } catch (IOException e) {
            System.out.println("ERROR: An error occurred when trying to instantiate a new IndexWriter");
            System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
        }
    }

    private static Document createNewDoc(FinTimesObject finTimesObject) {
//        Document document = new Document();
//
//        // Strings are a single unit not to be separated/analysed.
//        document.add(new StringField("id", finTimesObject.getDocId(), Field.Store.YES));
//        document.add(new StringField("author", finTimesObject.getDocAuthors(), Field.Store.YES));
//        document.add(new StringField("bibliography", finTimesObject.getDocBibliography(), Field.Store.YES));
//
//        // Text is content and is to be separated/analysed
//        document.add(new TextField("title", finTimesObject.getDocTitle(), Field.Store.YES));
//        document.add(new TextField("content", finTimesObject.getDocContent(), Field.Store.YES));
//
//        return document;
        return null;
    }

    private static void populateDocumentFields(String docLineTag, String docLine, FinTimesObject finTimesObject) {
//        if (docLineTag.equals(FinTimesTags.ID.getTag())) {
//            finTimesObject.setDocId(docLine.replace(".I", ""));
//        } else if (docLineTag.equals(FinTimesTags.TITLE.getTag()) && !docLine.contains(FinTimesTags.TITLE.getTag())) {
//            finTimesObject.setDocTitle(finTimesObject.getDocTitle() + " " + docLine);
//        } else if (docLineTag.equals(FinTimesTags.AUTHORS.getTag()) && !docLine.contains(FinTimesTags.AUTHORS.getTag())) {
//            finTimesObject.setDocAuthors(finTimesObject.getDocAuthors() + " " + docLine);
//        } else if (docLineTag.equals(FinTimesTags.BIBLIOG.getTag()) && !docLine.contains(FinTimesTags.BIBLIOG.getTag())) {
//            finTimesObject.setDocBibliography(finTimesObject.getDocBibliography() + " " + docLine);
//        } else if (docLineTag.equals(FinTimesTags.TEXT_BODY.getTag()) && !docLine.contains(FinTimesTags.TEXT_BODY.getTag())) {
//            finTimesObject.setDocContent(finTimesObject.getDocContent() + " " + docLine);
//        }
    }

    public static String checkIfDocLineHasTag(String docLine) {
        for (FinTimesTags tag : FinTimesTags.values()) {
            if (docLine.contains(tag.getTag())) {
                return tag.getTag();
            }
        }
        return null;
    }

}