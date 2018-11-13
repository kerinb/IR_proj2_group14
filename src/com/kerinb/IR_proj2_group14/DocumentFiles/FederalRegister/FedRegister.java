package com.kerinb.IR_proj2_group14.DocumentFiles.FederalRegister;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FedRegister {

    private static List<Document> fedRegisterDocList = new ArrayList<>();

    public static List<Document> loadFedRegisterDocs(String pathToFedRegister) throws IOException {
        File[] directories = new File(pathToFedRegister).listFiles(File::isDirectory);
        String docno,text,title;
        for (File directory : directories) {
            File[] files = directory.listFiles();
            for (File file : files) {
                org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
                Elements documents = d.select("DOC");

                for (Element document : documents) {
                    docno = document.select("DOCNO").text();
                    text = document.select("TEXT").text();
                    title = document.select("DOCTITLE").text();

                    addFedRegisterDoc(docno, text, title);
                }
            }
        }
        return fedRegisterDocList;
    }

    private static void addFedRegisterDoc(String docno, String text, String title) {
        Document doc = new Document();
        doc.add(new TextField("docno", docno, Field.Store.YES));
        doc.add(new TextField("text", text, Field.Store.YES));
        doc.add(new TextField("headline", title, Field.Store.YES));

        fedRegisterDocList.add(doc);
    }
}
