package com.kerinb.IR_proj2_group14.QueryFiles;

import com.kerinb.IR_proj2_group14.DocumentFiles.AvailableDocTags;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.kerinb.IR_proj2_group14.DocumentFiles.DocumentLibrary.checkIfDocLineHasTag;

public class QueryLibrary {


    private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
    private final static String absPathToCranfieldQueryFile = String.format("%s/cran/cran.qry", currentRelativePath);

    public static List<QueryData> callLoadQueriesFromFile(){
        return loadQueriesFromFile();
    }

    private static List<QueryData> loadQueriesFromFile() {
        QueryData queryData = new QueryData();

        String idTag = AvailableDocTags.ID.getTag();
        String tempTag = AvailableDocTags.ID.getTag();

        List<QueryData> queries = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(absPathToCranfieldQueryFile));
            String queryLine;
            int counter = 0;
            while ((queryLine = bf.readLine()) != null) {
                String queryLineTag = checkIfDocLineHasTag(queryLine);

                // If still docs to index - create new document object
                if (queryLineTag != null && queryLineTag.equals(idTag)) { // if docLineTag isnt null and if it hasnt changed
                    counter++;
                    tempTag = queryLineTag;
                    queries.add(queryData);
                    queryData = new QueryData();
                } else if (queryLineTag != null && !queryLineTag.equals(idTag)) { // otherwise, update the tag
                    tempTag = queryLineTag;
                }
                populateQueryFields(tempTag, queryLine, queryData, counter);
            }
            queries.add(queryData);
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queries;
    }

    private static void populateQueryFields(String queryLineTag, String queryLine, QueryData queryData, int counter) {
        if (queryLineTag.equals(AvailableDocTags.ID.getTag())) {
            queryData.setQueryId(String.valueOf(counter));
        } else if (queryLineTag.equals(AvailableDocTags.TEXT_BODY.getTag()) && !queryLine.contains(AvailableDocTags.TEXT_BODY.getTag())) {
            queryData.setQueryContent(queryData.getQueryContent() + " " + queryLine);
        }
    }
}
