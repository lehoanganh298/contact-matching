package main;

import java.util.ArrayList;
import java.util.Arrays;

public class TokenList {
    ArrayList<Token> tokenList;
    TokenList(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    TokenList(String str) {
        this.tokenList = new ArrayList<Token>();

        var split = str.split("[., ]");
        for (String w: split) {
            Token t = new Token(w);
            t.normalize();
            this.tokenList.add(t);
        }
    }


    // Match this tokenList with tokenList tl2
    // if not match, return -1
    // if match, return score in [0,1]
    // Current rule: all token in this tokenList must be contained in tl2
    //              score = sum score of each token pair / this tl length
    public float match(TokenList tl2) {
        float sumScore = 0.0F;
        for (Token tk: this.tokenList) {
            boolean hasToken = Boolean.FALSE;
            for (Token tk2:tl2.tokenList) {
                float matchScore = tk.matchToken(tk2);
                if (matchScore>0) {
                    hasToken=Boolean.TRUE;
                    sumScore+=matchScore;
                }
            }
            if (!hasToken)
                return -1F;
        }
        return sumScore/this.tokenList.size();
    }
}
