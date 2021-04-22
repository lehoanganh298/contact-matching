package contact_matching;

import java.util.ArrayList;

public class TokenList {
    ArrayList<Token> tokenList;
    TokenList(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    TokenList(String str) {
        this.tokenList = new ArrayList<Token>();

        var split = str.split("[\\s\\.()\\-_]");
        for (String w: split) {
            if (w.length()>0) {
                Token t = new Token(w);
                t.normalize();
                this.tokenList.add(t);
            }
        }

    }

    @Override
    public String toString() {
        String result="";
        for (var token: this.tokenList)
            result+=token.toString()+"|";
        return result;
    }
    // Match this tokenList with tokenList tl2
    // if not match, return -1
    // if match, return score in [0,1]
    // Current rule: all token in this tokenList must be contained in tl2
    //              score = sum score of each token pair / this tl length
    public float match(TokenList tl2) {
        float sumScore = 0.0F;
        int keepOrder = 0;
        int matchingPosDiff = 0;
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
//    public float matchWithOrder(TokenList tl2) {
//        float sumScore = 0.0F;
//        int matchWithOrder = 0;
//        int maxMatchWithOrder = 0;
//        int lastMatchPos = 0;
//        for (int i=0;i<this.tokenList.size();i++) {
//            if (lastMatchPos<this.tokenList.size()-1) {
//                float matchScore = this.tokenList.get(i).matchToken(tl2.tokenList.get(lastMatchPos+1));
//                if (matchScore>0) {
//                    matchWithOrder++;
//                    if (matchWithOrder>maxMatchWithOrder)
//                        maxMatchWithOrder=matchWithOrder;
//                    lastMatchPos+=1;
//                }
//                else {
//                    matchWithOrder=0;
//                }
//            }
//            boolean hasToken = Boolean.FALSE;
//            for (int j=0;j<tl2.tokenList.size();j++) {
//                float matchScore = this.tokenList.get(i).matchToken(tl2.tokenList.get(j));
//                if (matchScore>0) {
//                    hasToken=Boolean.TRUE;
//                    sumScore+=matchScore;
//                    lastMatchPos=j;
//                    matchWithOrder=1;
//                    if (i>0) {
//                        if (i-j==matchingPosDiff)
//                            keepOrder++;
//                    }
//                    matchingPosDiff=i-j;
//                }
//            }
//            if (!hasToken)
//                return -1F;
//        }
//        return sumScore/this.tokenList.size();
//    }
}
