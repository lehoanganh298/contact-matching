package main;


import java.util.Scanner;
import contact_matching.Contact;

public class Main {

    public static void main(String[] args) {
        Contact contact = new Contact("D:/kiki-IE/contact_matching/src/contact_test.txt");
        /* Contact contact = new Contact(); */
        contact.printTokenized();
        Scanner sc= new Scanner(System.in);

        while (true) {
            System.out.print("Enter name: ");
            String query_name= sc.nextLine();
            if (query_name=="") break;

            var x = contact.query(query_name);
            x.forEach(name -> System.out.println(name.name + "  "+name.matchingScore));
        }

    }
}
