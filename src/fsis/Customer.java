package fsis;
import static fsis.TextIO.putln;

/**
 * @author : Nguyen Manh Tien 3c14
 * @Overview : Customer is a person who has information about id, name, phoneNumber,address.
 * @Attribute :
 * id           int
 * name         String
 * phoneNumber  String
 * address      String
 * @Objects :
 * @Abstract_properties: mutable(id) = false /\ optional(id) = false /\ length(id) = 10 /\ min = 1 /\ max = 9999999/\
 * mutable(name) = true /\ optional(name) = false /\ length(name) = 80 /\
 * mutable(phoneNumber) = true /\ optional(phoneNumber) = false /\ length = 10 /\
 * mutable(address) = true /\ optional(address) = false /\ length = 100.
 */

public class Customer implements Comparable<Customer>, Document {
    @DomainConstraint(type = "int", mutable = false, optional = false, length = 10, min = 1, max = 9999999)
    protected int id;
    @DomainConstraint(type = "String", mutable = true, optional = false, length = 80)
    protected String name;
    @DomainConstraint(type = "String", mutable = true, optional = false, length = 10)
    protected String phoneNumber;
    @DomainConstraint(type = "String", mutable = true, optional = false, length = 100)
    protected String address;

    /**
     * @effect if name and phone number is valid initialise Customer with string input.
     */
    public Customer(int id, String name, String phoneNumber, String address) {
        if (isvalidId(id) && isValidName(name) && isValidPhoneNumber(phoneNumber) && isValidAddress(address)) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.address = address;
        } else {
            putln("invalid input");
        }

    }

    /**
     * @effect if name is valid
     * this.name = name
     * else
     * print error
     */
    public void setName(String name) {
        if (isValidName(name)) {
            this.name = name;
        } else {
            putln("invalid name!");
        }
    }

    /**
     * @effect if phone number is valid
     * this.phoneNumber = phoneNumber
     * else
     * print error
     */
    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            putln("invalid phone number!");
        }
    }

    /**
     * @effect this.address = address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @effect return id
     */
    public int getId() {
        return id;
    }

    /**
     * @effect return name
     */
    public String getName() {
        return name;
    }

    /**
     * @effect return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @effect return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @effect if 0 < id < 9999999
     * return true
     * else
     * return false
     */
    private boolean isvalidId(int id) {
        if (id > 0 && id < 9999999) {
            return true;
        }
        return false;
    }

    /**
     * @effect if name id valid
     * return true
     * else
     * return fasle
     */
    private boolean isValidName(String name) {
        if (name != null && !name.isEmpty() && name.length() <= 50) {
            return true;
        }
        return false;
    }

    /**
     * @effect if phone number is valid
     * return true
     * else
     * return false
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.length() <= 10) {
            return true;
        }
        return false;
    }

    /**
     * if address is valid
     * return true
     * else
     * return false
     */
    private boolean isValidAddress(String address) {
        if (address != null && !address.isEmpty() && address.length() <= 100) {
            return true;
        }
        return false;
    }

    /**
     * @effect return string from constructor
     */
    @Override
    public String toString() {
        return "Customer : " +
                "id = " + id +
                ", name = " + name +
                ", phoneNumber = " + phoneNumber +
                ", address = " + address;
    }

    @Override
    public String toHTMLDoc() {
        return " <html> " +
                "<head><title>Customer:" + this.name + "</title></head> " +
                "<body> " +
                this.id + " " + this.name + " " + this.phoneNumber + " " + this.address +
                "</body></html> ";
    }

    @Override
    public int compareTo(Customer o) {
        return this.name.compareTo(o.name);
    }
}

