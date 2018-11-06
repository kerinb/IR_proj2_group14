package com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.misc.SweetSpotSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

public class RankAndAnalyzers {
    // Ranking models
    private static final String BM25 = "BM25";
    private static final String BOOLEAN = "BOOLEAN";
    private static final String CLASSIC = "CLASSIC";
    private static final String LM_DIRICHLET = "LM_DIRICHLET";
    private static final String SWEET_SPOT = "SWEET_SPOT";
    private static final String[] ALL_RANKING_MODELS = {BM25, BOOLEAN, CLASSIC, LM_DIRICHLET, SWEET_SPOT};

    // Analysers
    private static final String KEYWORD = "KEYWORD";
    private static final String MORFOLOGIC = "MORFOLOGIC";
    private static final String STOP = "STOP";
    private static final String SIMPLE = "SIMPLE";
    private static final String STANDARD = "STANDARD";
    private static final String CUSTOM = "CUSTOM";
    private static final String WHITESPACE = "WHITESPACE";
    private static final String[] ALL_ANALYZERS = {CUSTOM, KEYWORD, MORFOLOGIC, STOP, SIMPLE, STANDARD, WHITESPACE};

    public static Similarity callSetRankingModel(String rankingModel){
        return setRankingModel(rankingModel);
    }

    public static Analyzer callSetAnalyzer(String analyzerSupplied){
        Analyzer analyzer = null;
        try {
            analyzer =  setAnalyzer(analyzerSupplied);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return analyzer;
    }

    private static Similarity setRankingModel(String rankingModel) {
        Similarity similarity = null;
        switch (rankingModel){
            case "BM25":
                similarity = new BM25Similarity();
                break;
            case "BOOLEAN":
                similarity = new BooleanSimilarity();
                break;
            case "CLASSIC":
                similarity = new ClassicSimilarity();
                break;
            case "LM_DIRICHLET":
                similarity = new LMDirichletSimilarity();
                break;
            case "SWEET_SPOT":
                similarity = new SweetSpotSimilarity();
                break;
        }
        return similarity;
    }

    private static Analyzer setAnalyzer(String analyzerSupplied) throws Exception {
        Analyzer analyzer;
        switch (analyzerSupplied){
            case "MORFOLOGIC":
                analyzer = new MorfologikAnalyzer();
                break;
            case "STOP":
                analyzer = new StopAnalyzer(StandardAnalyzer.ENGLISH_STOP_WORDS_SET);
                break;
            case "SIMPLE":
                analyzer = new SimpleAnalyzer();
                break;
            case "KEYWORD":
                analyzer = new KeywordAnalyzer();
                break;
            case "STANDARD":
                analyzer = new StandardAnalyzer(StandardAnalyzer.ENGLISH_STOP_WORDS_SET);
                break;
            case "WHITESPACE":
                analyzer = new WhitespaceAnalyzer();
                break;
            case "CUSTOM":
                analyzer = new CustomAnalyzer();
                break;
            default:
                throw new Exception(String.format("ERROR: Invalid analyzer supplied: %s", analyzerSupplied));
        }
        return analyzer;
    }

    public static boolean validAnalyzer(String analyzerSupplied) {
        for (String rankingModel : ALL_ANALYZERS) {
            if (analyzerSupplied.equals(rankingModel)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validRankModel(String rankModelSupplied) {
        for(String rankingModel : ALL_RANKING_MODELS) {
            if(rankModelSupplied.equals(rankingModel)){
                return true;
            }
        }
        return false;
    }
}