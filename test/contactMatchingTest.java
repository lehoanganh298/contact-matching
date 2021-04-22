import contact_matching.Contact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class contactMatchingTest {
    class TestCase {
        String query;
        ArrayList<Integer> expectedResult;
        TestCase(String query, ArrayList<Integer> expectedResult)  {
            this.query = query;
            this.expectedResult = expectedResult;
        }
    }

    static Contact contact;
    static ArrayList<TestCase> testcases;
    static contactMatchingTest singleton;

    static {
        try {
            singleton = new contactMatchingTest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    contactMatchingTest() throws FileNotFoundException {
        contact = new Contact("D:/kiki-IE/contact_matching/src/contact_test.txt");
//        Assertions.assertEquals(contact.length(),15);

        testcases = new ArrayList<>();
        File file = new File("D:/kiki-IE/contact_matching/src/test_cases.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String query = sc.nextLine();
            String expectedResultStr = sc.nextLine();
            var split = expectedResultStr.split("\\s");
            ArrayList<Integer> expectedResult = new ArrayList<>();
            for (String w:split) {
                expectedResult.add(Integer.parseInt(w));
            }
            testcases.add(new TestCase(query,expectedResult));
        }
    }


    @Test
    public void testContactMatching(){
        for (var testcase: testcases) {
            var result = contact.query(testcase.query);
            if (testcase.expectedResult.size()<3){
                Assertions.assertEquals(result.size(),testcase.expectedResult.size());
            }

            for (int i=0;i<testcase.expectedResult.size();i++) {
                try {
                    Assertions.assertEquals(result.get(i).index,testcase.expectedResult.get(i)-1);
                }
                catch (AssertionError e) {
                    System.out.println("Query: " + testcase.query);
                    System.out.println("Result " + i);
                    throw e;
                }
            }
        }
    }
}
