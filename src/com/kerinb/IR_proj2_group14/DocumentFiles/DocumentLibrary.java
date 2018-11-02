package com.kerinb.IR_proj2_group14.DocumentFiles;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocumentLibrary {
    private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
    private final static String absPathToCranfieldDocFile = String.format("%s/cran/cran.all.1400", currentRelativePath);

    public static void callIndexDocuments(List<Document> loadedDocs, Similarity similarity, Analyzer analyzer, Directory directory){
        indexDocuments(loadedDocs, similarity, analyzer, directory);
    }

    private static IndexWriterConfig setIndexWriterConfig(Similarity similarity, Analyzer analyzer){
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        return  indexWriterConfig.setSimilarity(similarity).setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    }

    private static void indexDocuments(List<Document> loadedDocs, Similarity similarity, Analyzer analyzer, Directory directory) {
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

    public static List<Document> callLoadDocumentsFromFile(){
        return loadDocumentsFromFile();
    }

    private static List<Document> loadDocumentsFromFile() {
        DocData docData = new DocData();

        String idTag = AvailableDocTags.ID.getTag();
        String tempTag = AvailableDocTags.ID.getTag();

        Document document;
        List<Document> documentList = new ArrayList<>();
        int counter = 0;

        try {
            BufferedReader bf = new BufferedReader(new FileReader(absPathToCranfieldDocFile));
            String docLine;

            while ((docLine = bf.readLine()) != null) {
                String docLineTag = checkIfDocLineHasTag(docLine);

                // If still docs to index - create new document object
                if (docLineTag != null && docLineTag.equals(idTag) && counter !=0) { // if docLineTag isnt null and if it hasnt changed
                    tempTag = docLineTag;
                    document = createNewDoc(docData);
                    documentList.add(document);
                    docData = new DocData();
                } else if (docLineTag != null && !docLineTag.equals(idTag)) { // otherwise, update the tag
                    tempTag = docLineTag;
                    counter++;
                }

                populateDocumentFields(tempTag, docLine, docData);
            }
            document = createNewDoc(docData);
            documentList.add(document);
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return documentList;
    }

    private static Document createNewDoc(DocData docData) {
        Document document = new Document();

        // Strings are a single unit not to be separated/analysed.
        document.add(new StringField("id", docData.getDocId(), Field.Store.YES));
        document.add(new StringField("author", docData.getDocAuthors(), Field.Store.YES));
        document.add(new StringField("bibliography", docData.getDocBibliography(), Field.Store.YES));

        // Text is content and is to be separated/analysed
        document.add(new TextField("title", docData.getDocTitle(), Field.Store.YES));
        document.add(new TextField("content", docData.getDocContent(), Field.Store.YES));

        return document;
    }

    private static void populateDocumentFields(String docLineTag, String docLine, DocData docData) {
        if (docLineTag.equals(AvailableDocTags.ID.getTag())) {
            docData.setDocId(docLine.replace(".I", ""));
        } else if (docLineTag.equals(AvailableDocTags.TITLE.getTag()) && !docLine.contains(AvailableDocTags.TITLE.getTag())) {
            docData.setDocTitle(docData.getDocTitle() + " " + docLine);
        } else if (docLineTag.equals(AvailableDocTags.AUTHORS.getTag()) && !docLine.contains(AvailableDocTags.AUTHORS.getTag())) {
            docData.setDocAuthors(docData.getDocAuthors() + " " + docLine);
        } else if (docLineTag.equals(AvailableDocTags.BIBLIOG.getTag()) && !docLine.contains(AvailableDocTags.BIBLIOG.getTag())) {
            docData.setDocBibliography(docData.getDocBibliography() + " " + docLine);
        } else if (docLineTag.equals(AvailableDocTags.TEXT_BODY.getTag()) && !docLine.contains(AvailableDocTags.TEXT_BODY.getTag())) {
            docData.setDocContent(docData.getDocContent() + " " + docLine);
        }
    }

    public static String checkIfDocLineHasTag(String docLine) {
        for (AvailableDocTags tag : AvailableDocTags.values()) {
            if (docLine.contains(tag.getTag())) {
                return tag.getTag();
            }
        }
        return null;
    }

}