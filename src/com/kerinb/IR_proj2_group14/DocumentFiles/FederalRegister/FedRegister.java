package com.kerinb.IR_proj2_group14.DocumentFiles.FederalRegister;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.IntPoint;
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
        String docno,text,title,date = "";
        for (File directory : directories) {
            File[] files = directory.listFiles();
            for (File file : files) {
                org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");

                date = file.getName().trim();
                date = date.substring(2, date.length()-2);

                Elements documents = d.select("DOC");

                for (Element document : documents) {

                    title = document.select("DOCTITLE").text();

                    document.select("DOCTITLE").remove();
                    document.select("ADDRESS").remove();
                    document.select("SIGNER").remove();
                    document.select("SIGNJOB").remove();
                    document.select("BILLING").remove();
                    document.select("FRFILING").remove();
                    document.select("DATE").remove();
                    document.select("CRFNO").remove();
                    document.select("RINDOCK").remove();

                    docno = document.select("DOCNO").text();
                    text = document.select("TEXT").text();

                    addFedRegisterDoc(docno, text, title, date);
                }
            }
        }
        return fedRegisterDocList;
    }

    private static void addFedRegisterDoc(String docno, String text, String title, String date) {
        Document doc = new Document();
        doc.add(new TextField("docno", docno, Field.Store.YES));
        doc.add(new TextField("text", text, Field.Store.YES));
        doc.add(new TextField("headline", title, Field.Store.YES));
        doc.add(new IntPoint("date", Integer.valueOf(date)));
        fedRegisterDocList.add(doc);
    }
}
