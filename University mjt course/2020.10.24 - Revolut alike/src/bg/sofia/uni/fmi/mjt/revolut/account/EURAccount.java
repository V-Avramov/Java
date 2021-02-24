package bg.sofia.uni.fmi.mjt.revolut.account;

public class EURAccount extends Account {

    private double amount;
    private String IBAN;

    public EURAccount(String IBAN) {
        super(IBAN,0);
    }

    public EURAccount(String IBAN, double amount) {
        super(IBAN, amount);
        this.IBAN=IBAN;
        this.amount=amount;
    }

    @Override
    public String getCurrency() {
        return "EUR";
    }

    public double getAmount(){
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
