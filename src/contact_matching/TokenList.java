package contact_matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    public HashMap<String, ArrayList<Integer>> toTokenMap() {
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        for (int i=0;i<this.tokenList.size();i++) {
            var tk = this.tokenList.get(i);
            String strToneRemoved = tk.removeAllTone();
            if (map.containsKey(strToneRemoved))
                map.get(strToneRemoved).add(i);
            else
                map.put(strToneRemoved,new ArrayList<>(List.of(i)));
        }
        return map;
    }

    // Match this tokenList with tokenList tl2
    // if not match, return -1
    // if match, return score in [0,1]
    // Current: only match each token to the first token that match
    public float match(TokenList tl2) {
        var tokenMap = this.toTokenMap();
        var tokenMap2 = tl2.toTokenMap();
        boolean contain = Boolean.TRUE;
        boolean matchExact = Boolean.TRUE;
        boolean keepOrder = Boolean.TRUE;
        var totalIndexDiffSet = new HashSet<Integer>();
        boolean diffIndexJustInitialized = Boolean.TRUE;
        float sumScore = 0F;

        for (var item: tokenMap.entrySet()) {
            var idxTl1 = item.getValue();
            var idxTl2 = tokenMap2.getOrDefault(item.getKey(),new ArrayList<>());
            if (idxTl2.size()<item.getValue().size()) {
                contain = Boolean.FALSE;
                break;
            }
            if (matchExact && idxTl2.size()!= idxTl1.size()) {
                matchExact = Boolean.FALSE;
            }
            if (keepOrder) {
                for (int idx1: idxTl1) {
                    var diffIndexSet = new HashSet<Integer>();
                    for (int idx2: idxTl2)
                        diffIndexSet.add(idx1-idx2);

                    if (diffIndexJustInitialized) {
                        totalIndexDiffSet.addAll(diffIndexSet);
                        diffIndexJustInitialized=Boolean.FALSE;
                    }
                    else {
                        totalIndexDiffSet.retainAll(diffIndexSet);
                        if (totalIndexDiffSet.size()==0)
                            keepOrder=Boolean.FALSE;
                    }
                }

            }
            for (int i=0;i<idxTl1.size();i++) {
                sumScore+=this.tokenList.get(idxTl1.get(i)).matchToken(tl2.tokenList.get(idxTl2.get(i)));
            }
        }
        if (tokenMap.size()!=tokenMap2.size())
            matchExact=Boolean.FALSE;
        if (!contain)
            return -1F;
        if (matchExact && keepOrder)
            return sumScore/this.tokenList.size();
        if (matchExact && !keepOrder)
            return sumScore/this.tokenList.size()*0.7F;
        if (!matchExact && keepOrder)
            return sumScore/this.tokenList.size()*this.tokenList.size()/tl2.tokenList.size();

        return sumScore/this.tokenList.size()*this.tokenList.size()/tl2.tokenList.size() * 0.7F;
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
