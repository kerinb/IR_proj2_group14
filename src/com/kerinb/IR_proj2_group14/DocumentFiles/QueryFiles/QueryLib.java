package com.kerinb.IR_proj2_group14.DocumentFiles.QueryFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QueryLib {

    private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
    private final static String absPathToQueries = String.format("%s/DataSet/topics.401-450", currentRelativePath);

    public static List<QueryObject> loadQueriesFromFile() {
        QueryObject queryObject = new QueryObject();
        String tempTag = QueryTags.TOP_START.getTag();
        String topTag = QueryTags.TOP_START.getTag();

        List<QueryObject> queries = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader(absPathToQueries));
            String queryLine;

            int counter = 0;
            while ((queryLine = bf.readLine()) != null) {
                String queryLineTag = checkIfDocLineHasTag(queryLine);

                if (queryLineTag != null && queryLineTag.equals(topTag)) { // if docLineTag isnt null and if it hasnt changed
                    counter++;
                    tempTag = queryLineTag;
                    queries.add(queryObject);
                    queryObject = new QueryObject();
                } else if (queryLineTag != null && !queryLineTag.equals(topTag)) { // otherwise, update the tag
                    tempTag = queryLineTag;
                }
                populateQueryFields(tempTag, queryLine, queryObject, counter);
            }
            queries.add(queryObject);
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queries;
    }

    private static String checkIfDocLineHasTag(String docLine) {
        for (QueryTags tag : QueryTags.values()) {
            if (docLine.contains(tag.getTag())) {
                return tag.getTag();
            }
        }
        return null;
    }

    private static void populateQueryFields(String queryLineTag, String queryLine, QueryObject queryObject, int counter) {
        if (queryLineTag.equals(QueryTags.QUERY_NUMBER.getTag())) {
            queryObject.setQueryNum(queryLine.replaceAll(QueryTags.QUERY_NUMBER.getTag(), ""));
        } else if (queryLineTag.equals(QueryTags.QUERY_TITLE.getTag())){
            queryObject.setTitle(queryObject.getTitle() + " " + queryLine.replaceAll(QueryTags.QUERY_TITLE.getTag(),
                    ""));
        } else if (queryLineTag.equals(QueryTags.QUERY_DESCRIPTION.getTag())){
            queryObject.setDescription(queryObject.getDescription() + " " + queryLine.replaceAll(
                    QueryTags.QUERY_DESCRIPTION.getTag(), ""));
        } else if (queryLineTag.equals(QueryTags.QUERY_NARRATIVE.getTag())){
            queryObject.setNarrative(queryObject.getNarrative() + " " + queryLine.replaceAll(
                    QueryTags.QUERY_NARRATIVE.getTag(), ""));
        } else{
            queryObject.setQueryId(String.valueOf(counter));
        }
    }
}