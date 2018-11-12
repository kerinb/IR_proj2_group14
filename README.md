# Rage Against The Lucene - IR_proj2_group14
git repository for CS7IS3 - Information Retrieval and Web Search in TCD. 
This project envolves a team effort in designing and implementing a Java project using the Apache Lucene library to search 
a large corpus of documents provided by the lecturer. 

The data set contains files from:
1. The Financial Times Limited (1991, 1992, 1993, 1994) 
2. The Federal Register (1994)
3. The Foreign Broadcast Information Service (1996) 
4. The Los Angeles Times (1989, 1990).

## Project Setup
1. Install Java 1.8, Git, Maven, lucene and trec_eval
2. Clone project
3. Compile project with: mvn clean install source:jar
4. Edit the _run.sh_ script to select similarity and analyser models.
5. Execute the _run.sh_ to generate results for the search engine - Results are stored in: __DataSet/queryResults__
6. Execute *run_trec_eval.sh* to generate MAP, Recall and other such metrics for the search engine. 
__Awating qrels file for project evaluation.__ 

