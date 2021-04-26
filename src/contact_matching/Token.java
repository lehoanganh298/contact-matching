package contact_matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static java.lang.Math.min;

public class Token {
    String token;
    Token(String s) {
        this.token = s;
    }

    @Override
    public String toString() {
        return this.token;
    }
    void lowercase() {
        this.token = this.token.toLowerCase(Locale.ROOT);
    }
    // vy --> vi, thy --> thi
    // Rule: any 'y' at the end of word, follow a consonant
    // Input: single word lower case
    void yToi() {
        String consonants = "bcdđghklmnpqrstvx";
        if (token.length()>=2 && Tone.removeTone(token.charAt(token.length()-1))=='y'
        && consonants.contains(Character.toString(token.charAt(token.length()-2))))
            token=token.substring(0,token.length()-1)+
                    Character.toString(Tone.addTone('i',Tone.getTone(token.charAt(token.length()-1))));
    }

    void numberToString() {
        String[] number2StringMap = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười"};
        try {
            Integer number = Integer.parseInt(token);
            if (number >= 0 && number <= 10) {
                token = number2StringMap[number];
            }
        }
        catch (Exception x){};
    }

    void resolveAbbreviation() {
        HashMap<String,String> abbr = new HashMap<>();
        abbr.put("a","anh");
        abbr.put("c","chị");

        if (abbr.containsKey(token))
            token = abbr.get(token);
    }
    void normalize() {
        lowercase();
        yToi();
        numberToString();
        resolveAbbreviation();
    }

    String removeAllTone() {
        StringBuilder sb = new StringBuilder();
        for (char c: this.token.toCharArray()) {
            sb.append(Tone.removeAllTone(c));
        }
        return sb.toString();
    }

    Float matchToken(Token t) {
        if (this.token.length()!=t.token.length())
            return -1F;
        float minScore = 1F;
        for (int i=0;i<this.token.length();i++) {
            float matchingScore = Tone.compareCharacter(this.token.charAt(i),t.token.charAt(i));

            if (matchingScore<0)
                return -1F;
            else
                minScore = min(minScore,matchingScore);
        }
        return minScore;
    }
}
