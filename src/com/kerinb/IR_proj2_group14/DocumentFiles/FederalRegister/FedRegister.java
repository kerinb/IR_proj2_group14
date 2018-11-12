package com.kerinb.IR_proj2_group14.DocumentFiles.FederalRegister;

import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FedRegister {

    private static List<Document> fedRegisterDocList = new ArrayList<>();

    public static List<Document> loadFedRegisterDocs(String pathToFedRegister) throws IOException {
        File[] directories = new File(pathToFedRegister).listFiles(File::isDirectory);
        //String docno, text, usdepartment, agency, usbureau, title, address, further, summary, action, signer, signjob, supplem, billing, frfiling, date, crfno, rindock;
        String docno,text,title;
        for (File directory : directories) {
            //System.out.print("DIRECTORY: ");
            //System.out.println(directory.getName());
            File[] files = directory.listFiles();
            for (File file : files) {
                //System.out.print("FILE: ");
                //System.out.println(file.getName());
                org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
                Elements documents = d.select("DOC");

                for (Element document : documents) {
                    docno = document.select("DOCNO").text();
                    text = document.select("TEXT").text();
                    title = document.select("DOCTITLE").text();

//                    usdepartment = document.select("USDEPT").text();
//                    agency = document.select("AGENCY").text();
//                    if (agency.startsWith("AGENCY:")) agency = agency.substring(7);
//                    usbureau = document.select("USBUREAU").text();
//                    address = document.select("ADDRESS").text();
//                    if (address.startsWith("ADDRESSES:")) address = address.substring(10);
//                    further = document.select("FURTHER").text();
//                    if (further.startsWith("FOR FURTHER INFORMATION CONTACT:")) further = further.substring(32);
//                    summary = document.select("SUMMARY").text();
//                    if (summary.startsWith("SUMMARY:")) summary = summary.substring(8);
//                    action = document.select("ACTION").text();
//                    if (action.startsWith("ACTION:")) action = action.substring(7);
//                    signer = document.select("SIGNER").text();
//                    signjob = document.select("SIGNJOB").text();
//                    supplem = document.select("SUPPLEM").text();
//                    if (supplem.startsWith("SUPPLEMENTARY INFORMATION:")) supplem = supplem.substring(26);
//                    billing = document.select("BILLING").text();
//                    if (billing.startsWith("BILLING CODE")) billing = billing.substring(12);
//                    frfiling = document.select("FRFILING").text();
//                    date = document.select("DATE").text();
//                    if (date.startsWith("DATES:")) date = date.substring(6);
//                    else if (date.startsWith("EFFECTIVE DATES:")) date = date.substring(16);
//                    else if (date.startsWith("EFFECTIVE DATE:")) date = date.substring(15);
//                    crfno = document.select("CRFNO").text();
//                    rindock = document.select("RINDOCK").text();

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
        doc.add(new TextField("title", title, Field.Store.YES));

//        doc.add(new TextField("usdepartment", usdepartment, Field.Store.YES));
//        doc.add(new TextField("agency", agency, Field.Store.YES));
//        doc.add(new TextField("usbureau", usbureau, Field.Store.YES));
//        doc.add(new TextField("address", address, Field.Store.YES));
//        doc.add(new TextField("further", further, Field.Store.YES));
//        doc.add(new TextField("summary", summary, Field.Store.YES));
//        doc.add(new TextField("action", action, Field.Store.YES));
//        doc.add(new TextField("signer", signer, Field.Store.YES));
//        doc.add(new TextField("signjob", signjob, Field.Store.YES));
//        doc.add(new TextField("supplem", supplem, Field.Store.YES));
//        doc.add(new TextField("billing", billing, Field.Store.YES));
//        doc.add(new TextField("frfiling", frfiling, Field.Store.YES));
//        doc.add(new TextField("date", date, Field.Store.YES));
//        doc.add(new TextField("crfno", crfno, Field.Store.YES));
//        doc.add(new TextField("rindock", rindock, Field.Store.YES));

        fedRegisterDocList.add(doc);
    }
}
