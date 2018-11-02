package com.kerinb.IR_proj2_group14.RankAndAnalyzerFiles;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.tartarus.snowball.ext.EnglishStemmer;

public class CustomAnalyzer extends StopwordAnalyzerBase {
    CustomAnalyzer(){
        super(StandardAnalyzer.ENGLISH_STOP_WORDS_SET);
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {
        final Tokenizer tokenizer = new StandardTokenizer();

        TokenStream tokenStream = new StandardFilter(tokenizer);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new TrimFilter(tokenStream);
        tokenStream = new SnowballFilter(tokenStream, new EnglishStemmer());
        tokenStream = new StopFilter(tokenStream, this.stopwords);

        return new TokenStreamComponents(tokenizer, tokenStream);
    }
}
