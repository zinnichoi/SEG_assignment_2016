package fsis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import static fsis.TextIO.*;

import kengine.*;

/**
 * @author dmle
 * @overview Represents a worksheet program that enables a user to create customer objects,
 * display a report about them, and to search for objects of interest using keywords.
 * @attributes objects    SortedSet<Customer>
 * engine     Engine
 * @abstract_properties mutable(objects)=true /\ optional(objects)=false /\
 * mutable(engine)=false /\ optional(engine)=false /\
 * size(objects) > 0 ->
 * (for all o in objects: o.toHtmlDoc() is in engine)
 */
public class CustomerWorkSheet {
    @DomainConstraint(type = "Collection", mutable = true, optional = false)
    private SortedSet objects;
    @DomainConstraint(type = "Engine", mutable = false, optional = false)
    private Engine engine;

    /**
     * @effects initialise this to include an empty set of objects and an empty engine
     */
    public CustomerWorkSheet() {
        objects = new SortedSet();
        engine = new Engine();
    }

    /**
     * @effects invoke promptForCustomer to prompt the user to enter details of
     * a customer, create a Customer object from these details and
     * invoke addCustomer to add the object to this.
     * <p>
     * If invalid details were entered then throws NotPossibleException.
     */
    public void enterACustomer() throws NotPossibleException {
        putln("enter a customer ");
        Customer customer = null;
        customer = this.promptForCustomer();
        if (customer == null) {
            throw new NotPossibleException(
                    " Invalid Customer to insert.");
        }else {
            this.addCustomer(customer);
            putln("Added: " + customer.toString());
        }

    }


    /**
     * @effects prompt the user whether to enter details for Customer or HighEarner,
     * then prompt the user for the data values needed to create an object
     * for the selected type.
     * <p>
     * Create and return a Customer object from the entered data. Throws
     * NotPosibleException if invalid data values were entered.
     */
    public Customer promptForCustomer() throws NotPossibleException {
        try {
            int choice;
            do {
                putln("choose an option : ");
                putln("1. Input Customer information. ");
                putln("2. Input Higher Earner information.");
                choice = getlnInt();
            } while (choice != 1 && choice != 2);
            put("enter  id : ");
            int id = getlnInt();
            put("enter  name : ");
            String name = getln();
            put("enter phone number :");
            String phoneNumber = getln();
            put("enter address : ");
            String address = getln();
            if (choice == 1) {
                return new Customer(id, name, phoneNumber, address);
            } else {
                put("enter income :");
                Float income = TextIO.getlnFloat();
                return new HighEarner(id, name, phoneNumber, address, income);
            }
        } catch (NotPossibleException ex) {
            putln(ex.getMessage());
            return null;
        }
    }

    /**
     * @effects add c to this.objects and
     * add to this.engine a Doc object created from c.toHtmlDoc
     */
    public void addCustomer(Customer customer) {
        this.objects.insert(customer);
        this.engine.addDoc(new Doc(customer.toHTMLDoc()));

    }

    /**
     * @modifies System.out
     * @effects if this.objects == null
     * prints "empty"
     * else
     * prints each object in this.objects one per line to the standard output
     * invoke writeHTMLReport to write an HTML report to file
     */
    public void displayReport() {
        if (this.objects == null) {
            putln("empty");
        } else {
            Iterator<Customer> iterator = this.objects.iterator();
            while (iterator.hasNext()) {
                Customer customer = iterator.next();
                putln(customer.toString());
            }
            this.writeHTMLReport();
            putln("Report written to file objects.html");
        }
    }

    /**
     * @effects if objects is empty
     * return "empty"
     * else
     * return a string containing each object in this.objects one per line
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.objects.isEmpty()) {
            return "empty";
        }
        Iterator<Customer> iterator = this.objects.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            sb.append(customer.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @modifies objects.html (in the program directory)
     * @effects if this.objects is empty
     * write an HTML document to file with the word "empty" in the body
     * else
     * write an HTML document to file containing a table, each row of which
     * lists an object in this.objects
     * <p>
     * The HTML document must be titled "Customer report".
     */
    public void writeHTMLReport() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("object.html"))) {
            if (this.objects.isEmpty()) {
                bufferedWriter.write("empty");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<head><title>Customer report</title></head>");
                sb.append("<body>");
                sb.append("<table border=1>");
                sb.append("<tr><th>Id</th><th>Name</th><th>Phone number</th><th>Address</th><th>Income</th></tr>");
                Iterator<Customer> iterator = this.objects.iterator();
                while (iterator.hasNext()) {
                    Customer customer = iterator.next();
                    sb.append("<tr>");
                    sb.append("<td>" + customer.getId() + "</td>");
                    sb.append("<td>" + customer.getName() + "</td>");
                    sb.append("<td>" + customer.getPhoneNumber() + "</td>");
                    sb.append("<td>" + customer.getAddress() + "</td>");
                    if (customer instanceof HighEarner) {
                        HighEarner highEarner = (HighEarner) customer;
                        sb.append("<td>" + highEarner.getIncome() + "</td>");
                    }
                    sb.append("</tr>");
                }
                sb.append("</table>");
                sb.append("</body></html>");
                bufferedWriter.write(sb.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @modifies System.out
     * @effects prompt the user to enter one or more keywords
     * if keywords != null AND keywords.length > 0
     * invoke operation search(String[]) to search using keywords,
     * <p>
     * if fails to execute the query
     * throws NotPossibleException
     * else
     * print the query string to the standard output.
     * invoke operation writeSearchReport(Query) to output the query to an HTML file
     * else
     * print "no search keywords"
     */
    public void search() throws NotPossibleException {
        String[] keywords = promptForKeywords();
        if (keywords != null && keywords.length > 0) {
            try {
                putln("Searching for customers using keywords :" +keywords.toString());
                Query query = search(keywords);
                putln(query.toString());
                writeSearchReport(query);
            } catch (NotPossibleException e) {
                throw new NotPossibleException("Fail to execute query");
            }
        } else {
            putln("no search keyword");
        }
    }

    /**
     * @effects prompt the user to enter some keywords and
     * return an array containing these or null if no keywords were entered
     */
    public String[] promptForKeywords() {
        putln("enter some keyword(separatered by space) : ");
        String keywords = getln();
        if (keywords != null || keywords.length() > 0) {
            return keywords.split(" ");
        }
        return null;
    }

    /**
     * @requires words != null /\ words.length > 0
     * @effects search for objects whose HTML documents match with the query containing words
     * and return a Query object containing the result
     * <p>
     * If fails to execute query using words
     * throws NotPossibleException
     */
    public Query search(String[] words) throws NotPossibleException {
        Query query = null;
        try {
            query = this.engine.queryFirst(words[0]);
            for (int i = 1; i < words.length; i++) {
                query = this.engine.queryMore(words[i]);
            }
        } catch (NotPossibleException ex) {
            ex.printStackTrace();
        }
        return query;
    }

    /**
     * @requires query != null
     * @modifies search.html (in the program directory)
     * @effects write to file an HTML document containing the query keys and a table,
     * each row of which lists a match
     * <p>
     * The HTML document must be titled "Search report".
     */
    public void writeSearchReport(Query query) {
        putln("Writing query report(sorted in descending order) to file search.html...");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("search.html"))) {
            Iterator iterator = query.matchIterator();
            bufferedWriter.write("<html><title> Search Report </title><body>Query: ");
            bufferedWriter.write(Arrays.toString(query.keys()) + "<br> Results: " + "<br><table border = 1 > <th> Documents </th> <th>Sum freqs</th>");
            while (iterator.hasNext()) {
                DocCnt docCnt = (DocCnt) iterator.next();
                Doc doc = docCnt.getDoc();
                bufferedWriter.write("<tr><td>" + doc.title() + "</td><td>" + docCnt.getCount() + "</td></tr>");
            }
            bufferedWriter.write("</table> </body> </html>");
            putln("Report writed to file search.html ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @effects initialise a CustomerWorkSheet
     * ask the users to create the five Customer objects
     * display report about the objects
     * ask the users to enter a keyword query to search for objects and display
     * the result
     */
    public static void main(String[] args) {
        // initialise a CustomerWorkSheet
        putln("Initialising program...");
        CustomerWorkSheet worksheet = new CustomerWorkSheet();

        try {
            // ask user to create 5 test customer objects
            putln("\nCreating some customers...");
            int num = 5;
            for (int i = 0; i < num; i++) {
                putln("*------------------------------------------*");
                worksheet.enterACustomer();
            }
                putln("********************************************");

            // display report about the objects
            worksheet.displayReport();

            putln("********************************************");

            // ask the users to enter a keyword query to search for objects and display
            // the result
            worksheet.search();
            putln("********************************************");

            // end

            putln("Good bye.");
        } catch (NotPossibleException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

