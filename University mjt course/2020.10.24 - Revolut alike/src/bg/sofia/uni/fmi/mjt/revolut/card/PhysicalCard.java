package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class PhysicalCard implements Card {

    private String number;
    private int pin;
    private LocalDate expirationDate;

    public PhysicalCard(String number, int pin, LocalDate expirationDate){
        this.number = number;
        this.pin = pin;
        this.expirationDate = expirationDate;
    }

    @Override
    public String getType() {
        return "PHYSICAL";
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean checkPin(int pin) {
        return this.pin==pin;
    }

    @Override
    public boolean isBlocked() {
        if(expirationDate.isBefore(LocalDate.now())){
            return true;
        }
        return expirationDate.getYear()==0;
    }

    @Override
    public void block() {
        System.out.println("Card is blocked");
        expirationDate=LocalDate.of(0,1,1);
    }
}
