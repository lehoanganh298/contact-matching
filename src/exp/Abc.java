package exp;

import java.sql.SQLOutput;
import java.util.Locale;

public class Abc {

    public static void main(String[] args) {
        Character c1='ấ';
        Character c2='ấ';
        Character c3='đ';
        Character c4='đ';
        Character c5='a';
        Character c6='a';
        System.out.println(c5==c6);

        System.out.println(c1==c2);
        System.out.println(c3==c4);

//        Character c5='Đ';
//        System.out.println(Character.toString(c5).toLowerCase(Locale.ROOT));
    }

}