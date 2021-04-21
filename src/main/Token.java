package main;

import java.util.HashMap;
import java.util.Locale;

public class Token {
    String token;
    Token(String s) {
        this.token = s;
    }


    void lowercase() {
        this.token = this.token.toLowerCase(Locale.ROOT);
    }
    // vy --> vi, thy --> thi
    // Rule: any 'y' at the end of word, follow a consonant
    // Input: single word lower case
    void yToi() {
        String consonants = "bcdđghklmnpqrstvx";
        if (token.length()>=2 && token.charAt(token.length()-1)=='y'
        && consonants.contains(Character.toString(token.charAt(token.length()-2))))
            token=token.substring(0,token.length()-1)+"i";
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

    Float matchToken(Token t) {
        if (this.token.length()!=t.token.length())
            return -1F;
        float sum_score = 0F;
        for (int i=0;i<this.token.length();i++) {
            float matching_score = Tone.compareCharacter(this.token.charAt(i),t.token.charAt(i));

            if (matching_score<0)
                return -1F;
            else
                sum_score+=matching_score;
        }
        return sum_score/this.token.length();
    }
}
