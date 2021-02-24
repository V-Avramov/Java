package bg.sofia.uni.fmi.mjt.revolut.account;

public class BGNAccount extends Account {

    private double amount;
    private String IBAN;

    public BGNAccount(String IBAN) {
        super(IBAN,0);
    }

    public BGNAccount(String IBAN, double amount) {
        super(IBAN, amount);
        this.IBAN=IBAN;
        this.amount=amount;
    }

    @Override
    public String getCurrency() {
        return "BGN";
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
