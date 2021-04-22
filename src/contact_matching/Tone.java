package contact_matching;

import java.util.HashMap;

// Dealing with tone: `?.~... ă â ê đ ô ơ ...
public class Tone {
    static HashMap<Character, Character> removeAllToneMap;
    static HashMap<Character, Character> removeToneMap;
    static HashMap<Character, Integer> toneMap;
    static HashMap<Character, Integer> vowelMap;
    static Character[][] characterMatrix = {
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
    private static Tone initialize = new Tone(); // Singleton

    Tone() {
        removeAllToneMap = new HashMap<>();
        removeToneMap = new HashMap<>();
        toneMap = new HashMap<>();
        vowelMap = new HashMap<>();

        for (int i=0;i< characterMatrix.length;i++) {
            vowelMap.put(characterMatrix[i][1],i);
            for (int j = 1; j < characterMatrix[i].length; j++) {
                removeAllToneMap.put(characterMatrix[i][j], characterMatrix[i][0]);
                removeToneMap.put(characterMatrix[i][j], characterMatrix[i][1]);
                toneMap.put(characterMatrix[i][j], j);
            }
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

        public static Character addTone(Character c, int tone) {
        if (vowelMap.containsKey(removeTone(c))) {
            return characterMatrix[vowelMap.get(removeTone(c))][tone];
        }
        return c;
    }

    public static float compareCharacter(char c1, char c2) {
        if (c1 == c2)
            return 1F;

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
