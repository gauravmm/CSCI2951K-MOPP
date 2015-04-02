/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.util.support;

import java.util.Arrays;
import java.util.stream.Collectors;
import ontopt.pen.Sentence;

/**
 *
 * @author Gaurav Manek
 */
public final class TokenizedSentence extends Sentence {

    private final String sepRegex;
    private String[] tokens;

    public TokenizedSentence(String separator, String sentence) {
        this.sepRegex = separator;
        tokenize(sentence);
    }

    public TokenizedSentence(String sentence) {
        this("[\\W]", sentence);
    }

    @Override
    protected void tokenize(String sentence) {
        tokens = sentence.toLowerCase().split(this.sepRegex);
    }

    @Override
    public String getWord(int index) {
        return tokens[index];
    }

    @Override
    public int getSentenceSize() {
        return tokens.length;
    }

    @Override
    public String toString() {
        return Arrays.stream(tokens).collect(Collectors.joining(" "));
    }

}
