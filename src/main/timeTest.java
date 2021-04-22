package main;

import contact_matching.Contact;
import contact_matching.TokenList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class timeTest {
    public static void main(String[] args) {
//        Contact contact = new Contact("src/contact_long.txt");
        ArrayList<String> nameList = new ArrayList<>();
        try {
            List<String> allLines = Files.readAllLines(Paths.get("src/contact_long.txt"));
            for (String line : allLines) {
                nameList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();

        /* … The code being measured starts … */
        int numQuery = 100;
        String[] query = {"anh đức","anh tuấn","chị năm","anh khải","anh khãi","my phương","phương vi","anh tuấn"};
        for (int i=0;i<numQuery;i++) {
            long startTime1 = System.nanoTime();

            Contact contact = new Contact(nameList);
            var result = contact.query(query[i%query.length]);
            long endTime1 = System.nanoTime();
            if (i%10==0)
                System.out.println("Query " +i + " : " + (double)(endTime1-startTime1)/1000000);


        }
        /* … The code being measured ends … */

        long endTime = System.currentTimeMillis();

        // get the difference between the two nano time values
        long timeElapsed = endTime - startTime;

        System.out.println("Time each query in miliseconds: " + (double)timeElapsed/numQuery);
    }
}
