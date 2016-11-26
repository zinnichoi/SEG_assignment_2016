package fsis;

import kengine.NotPossibleException;

/**
 * @author : Nguyen Manh Tien 3c14
 * @Overview : HigherEarner is a customer who has high income.
 * @Attribute :
 * id           int
 * name         String
 * phoneNumber  String
 * address      String
 * income       float
 * @Objects :
 * @Abstract_properties: mutable(id) = false /\ optional(id) = false /\ length(id) = 10 /\ min = 1 /\ max = 9999999/\
 * mutable(name) = true /\ optional(name) = false /\ length(name) = 80 /\
 * mutable(phoneNumber) = true /\ optional(phoneNumber) = false /\ length = 10 /\
 * mutable(address) = true /\ optional(address) = false /\ length = 100 /\
 * mutable(income) = true /\ optional = false /\ min = 10000000.
 */
public class HighEarner extends Customer {
    @DomainConstraint(type = "float", mutable = true, optional = false, min = 10000000)
    private float income;

    public HighEarner(int id, String name, String phoneNumber, String address, float income) {
        super(id, name, phoneNumber, address);
        if (isValidIncome(income)) {
            this.income = income;
        }
        else {
            throw new NotPossibleException("HighEarner: invalid HighEarner");
        }
    }

    /**
     * @return this.income
     */
    public float getIncome() {
        return income;
    }


    /**
     * if income is valid
     * this.income = income
     * else
     * print err message
     */
    public void setIncome(float income) {
        if (isValidIncome(income)) {
            this.income = income;
        } else {
            System.err.println("invalid income !");
        }

    }

    /**
     * if income > 10000000
     * return true
     * else
     * return false.
     */
    private boolean isValidIncome(float income) {
        if (income > 10000000) {
            return true;
        }
        return false;
    }


    @Override
    /**
     * return a string contain infor of a HigherEarner
     */
    public String toString() {
        return "HigherEarner : " +
                "id = " + id +
                ", name = " + name +
                ", phoneNumber = " + phoneNumber +
                ", address = " + address +
                ", income = "+(int)income;
    }

    @Override
    public String toHTMLDoc() {
        return  " <html> " +
                "<head><title>Higher Earner : "+this.name+"</title></head> " +
                "<body> " +
                this.id +" "+this.name+" "+this.phoneNumber+" "+this.address+" "+(int)this.income+
                "</body></html> ";
    }
}
