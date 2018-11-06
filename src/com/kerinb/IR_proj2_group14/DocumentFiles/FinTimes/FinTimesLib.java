package com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FinTimesLib {

    private static List<Document> finTimesDocList = new ArrayList<>();
    private static boolean headlineFlag = false, textFlag = false, byLineFlag = false;

    public static List<Document> loadFinTimesDocs(List<String> finTimesFiles) throws IOException {
        FinTimesObject finTimesObject = new FinTimesObject();

        for(String fileName : finTimesFiles){

            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                String currLine;

                while((currLine = br.readLine()) != null){
                    currLine = currLine.trim();
                    finTimesObject = setFinTimesObjData(currLine, finTimesObject);
                }
                finTimesObject = addCreateFinTimesObject(finTimesObject);

                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(String.format("ERROR: IOException occurred when clsoing file: %s", fileName));
                    System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
                }

            } catch (FileNotFoundException e) {
                System.out.println(String.format("ERROR: FileNotFoundExcpeiton occurred when trying to read file: %s",
                        fileName));
                System.out.println(String.format("ERROR MESSAGE: %s", e.getMessage()));
            }
        }

        return finTimesDocList;
    }

    private static FinTimesObject setFinTimesObjData(String currLine, FinTimesObject finTimesObject){
        if(currLine.contains(FinTimesTags.DOC_END.getTag())){
            finTimesObject = addCreateFinTimesObject(finTimesObject);
        } else if (currLine.contains(FinTimesTags.DOC_NO_START.getTag())){
            finTimesObject.setDocNo(parseFinTimesDoc(currLine, "docNo"));
        } else if (currLine.equals(FinTimesTags.HEADLINE_START.getTag())){
            headlineFlag = true;
        } else if(currLine.contains(FinTimesTags.HEADLINE_END.getTag())){
            headlineFlag = false;
        } else if (currLine.contains(FinTimesTags.BYLINE_START.getTag())){
            byLineFlag = true;
        } else if (currLine.contains(FinTimesTags.BYLINE_END.getTag())){
            byLineFlag = false;
        } else if (currLine.contains(FinTimesTags.TEXT_START.getTag())){
            textFlag = true;
        } else if (currLine.contains(FinTimesTags.TEXT_END.getTag())){
            textFlag = false;
        } else if (currLine.contains(FinTimesTags.DOC_ID_START.getTag())){
            finTimesObject.setDocId(parseFinTimesDoc(currLine, "docId"));
        }

        if(headlineFlag){
            finTimesObject.setHeadline(finTimesObject.getHeadline() + " " + parseFinTimesDoc(currLine,
                    "headLine"));
        } else if(textFlag){
            finTimesObject.setText(finTimesObject.getText() + " " + parseFinTimesDoc(currLine,
                    "text"));
        } else if(byLineFlag){
            finTimesObject.setByLine(finTimesObject.getByLine() + " " + parseFinTimesDoc(currLine,
                    "byLine"));
        }
        return finTimesObject;
    }

    private static FinTimesObject addCreateFinTimesObject(FinTimesObject finTimesObject){
        Document finTimesDoc = createNewFinTimesDoc(finTimesObject);
        finTimesDocList.add(finTimesDoc);
        return new FinTimesObject();
    }

    private static String parseFinTimesDoc(String currLine, String textField){
        switch (textField){
            case "docId":
                return currLine.replaceAll(FinTimesTags.TEXT_START.getTag(), "").replaceAll(
                        FinTimesTags.TEXT_END.getTag(), "");
            case "text":
                return currLine.replaceAll(FinTimesTags.TEXT_START.getTag(), "").replaceAll(
                        FinTimesTags.TEXT_END.getTag(), "");
            case "byLine":
                return currLine.replaceAll(FinTimesTags.BYLINE_START.getTag(), "").replaceAll(
                        FinTimesTags.BYLINE_END.getTag(), "");
            case "headLine":
                return currLine.replaceAll(FinTimesTags.HEADLINE_START.getTag(), "").replaceAll(
                        FinTimesTags.HEADLINE_END.getTag(), "");
            case "docNo":
                return currLine.replaceAll(FinTimesTags.DOC_NO_START.getTag(), "").replaceAll(
                        FinTimesTags.DOC_NO_END.getTag(), "");
            default:
                return null;
        }
    }

    private static Document createNewFinTimesDoc(FinTimesObject finTimesObject) {
        Document document = new Document();

        // Strings are a single unit not to be separated/analysed.
        document.add(new StringField("id", finTimesObject.getDocId(), Field.Store.YES));
        document.add(new StringField("byLine", finTimesObject.getByLine(), Field.Store.YES));
        document.add(new StringField("docNo", finTimesObject.getDocNo(), Field.Store.YES));

        // Text is content and is to be separated/analysed
        document.add(new TextField("headline", finTimesObject.getHeadline(), Field.Store.YES));
        document.add(new TextField("text", finTimesObject.getText(), Field.Store.YES));

        return document;
    }
}