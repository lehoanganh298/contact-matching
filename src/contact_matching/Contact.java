package contact_matching;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Contact {
    private ArrayList<String> contact;
    private ArrayList<TokenList> contactTokenized;

    public Contact() {
        contact = new ArrayList<String>();
        contactTokenized = new ArrayList<TokenList>();
    }
    public Contact(ArrayList<String> nameList) {
        contact = new ArrayList<String>();
        contactTokenized = new ArrayList<TokenList>();
        for (String name : nameList) {
            contact.add(name);
            contactTokenized.add(new TokenList(name.toLowerCase(Locale.ROOT)));
        }
    }
    public Contact(String contactFile) {
        contact = new ArrayList<String>();
        contactTokenized = new ArrayList<TokenList>();
        try {
            List<String> allLines = Files.readAllLines(Paths.get(contactFile));
            for (String line : allLines) {
                contact.add(line);
                contactTokenized.add(new TokenList(line.toLowerCase(Locale.ROOT)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        for (String name: contact) {
            System.out.println(name);
        }
    }
    public void printTokenized() {
        for (var tl: contactTokenized) {
            System.out.println(tl);
        }
    }

    public int length() {
        return contact.size();
    }

    public class MatchingName{
        public Integer index;
        public String name;
        public float matchingScore;
        MatchingName(Integer index, String name, Float matchingScore) {
            this.index = index;
            this.name = name;
            this.matchingScore = matchingScore;
        }
        float machingScore() {
            return matchingScore;
        }
    }

    public ArrayList<MatchingName> query(String queryName) {
        TokenList queryTokenized = new TokenList(queryName.toLowerCase(Locale.ROOT));
        ArrayList<MatchingName> result = new ArrayList<>();
        for (int i=0;i< contactTokenized.size();i++) {
            float matchScore = queryTokenized.match(contactTokenized.get(i));
            if (matchScore>0)
                result.add(new MatchingName(i, contact.get(i), matchScore));
        }
        result.sort(Comparator.comparing(MatchingName::machingScore).reversed());
        return result;
    }
}
