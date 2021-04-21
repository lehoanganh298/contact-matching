package main;

import java.util.HashMap;

// Dealing with tone: `?.~... ă â ê đ ô ơ ...
public class Tone {
    static HashMap<Character, Character> removeAllToneMap;
    static HashMap<Character, Character> removeToneMap;
    static HashMap<Character, Integer> toneMap;
    private static Tone initialize = new Tone(); // Singleton

    Tone() {
        removeAllToneMap = new HashMap<>();
        removeToneMap = new HashMap<>();
        toneMap = new HashMap<>();

        Character[][] characterMatrix = {
                {'a','a','à','á','ạ','ả','ã'},
                {'a','â','ầ','ấ','ậ','ẩ','ẫ'},
                {'a','ă','ằ','ắ','ặ','ẳ','ẵ'},
                {'e','e','è','é','ẹ','ẻ','ẽ'},
                {'e','ê','ề','ế','ệ','ể','ễ'},
                {'i','i','ì','í','ị','ỉ','ĩ'},
                {'o','o','ò','ó','ọ','ỏ','õ'},
                {'o','ô','ồ','ố','ộ','ổ','ỗ'},
                {'o','ơ','ờ','ớ','ợ','ở','ỡ'},
                {'u','u','ù','ú','ụ','ủ','ũ'},
                {'u','ư','ừ','ứ','ự','ử','ữ'},
                {'y','y','ỳ','ý','ỵ','ỷ','ỹ'}};

        for (var clist: characterMatrix)
            for (int i=1;i<clist.length;i++) {
                removeAllToneMap.put(clist[i],clist[0]);
                removeToneMap.put(clist[i],clist[1]);
                toneMap.put(clist[i],i);
            }
        removeToneMap.put('đ','d');
        removeAllToneMap.put('đ', 'd');
    }

    public static Character removeTone(Character c) {
        return removeToneMap.getOrDefault(c, c);
    }

    public static Character removeAllTone(Character c) {
        return removeAllToneMap.getOrDefault(c, c);
    }

    public static Integer getTone(Character c) {
        return toneMap.getOrDefault(c, 0);
    }

    public static float compareCharacter(Character c1, Character c2) {
        if (!toneMap.containsKey(c1) || !toneMap.containsKey(c2)) {
            if (c1 == c2)
                return 1F;
            else
                return -1F;
        }

        if (removeTone(c1)==removeTone(c2)) {
            if (getTone(c1) == getTone(c2))
                return 1F;
            else
                return 0.6F;
        }
        if (removeAllTone(c1)==removeAllTone(c2))
            return 0.3F;
        return -1F;
    }
}
