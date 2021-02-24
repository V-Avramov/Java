package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;

import java.util.Arrays;

public class Revolut implements RevolutAPI{

    private Account[] accounts;
    private Card[] cards;
    private int[] attempts;//the number positioned on index i refers to the
                           //card positioned at index i in the 'cards' array

    private boolean isVirtual=false;//used in the pay method to make sure if its virtual or not

    Revolut(Account[] accounts, Card[] cards){
        this.accounts=accounts;
        this.cards=cards;
        this.attempts=new int[cards.length];
        Arrays.fill(attempts,0);
    }

    @Override
    public boolean pay(Card card, int pin, double amount, String currency) {

        int cardsLen=this.cards.length;

        int i=0;
        for (; i < cardsLen; i++) {//Checks if the card exists in Revolut
            if(this.cards[i]==card){//Found the card!
                break;
            }
            else if(i==cardsLen-1){ //The card is nonexistent
                return false;
            }
        }

        if(!card.getType().equals("PHYSICAL")&&!isVirtual){//if the card is not physical and we are not paying online
            System.out.println("Physical card only");      //warn the user
            return false;
        }
        isVirtual=false;

        if(!card.checkPin(pin)){
            attempts[i]++;
            if(attempts[i]>=3){//Here we check if the card was declined atleast 3 times
                card.block();
            }
            return false;
        }

        if(card.isBlocked()){
            return false;
        }

        if(card.getType().equals("VIRTUALONETIME")){
            card.block();
        }

        for (int j = 0; j < accounts.length; j++) {
            if (accounts[j].getCurrency().equals(currency)){
                if(accounts[j].getAmount()>=amount){
                    accounts[j].setAmount(accounts[j].getAmount()-amount);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean payOnline(Card card, int pin, double amount, String currency, String shopURL) {
        if(shopURL.contains(".biz")){
            System.out.println("Not verified website");
            return false;
        }
        isVirtual=true;
        return pay(card, pin, amount, currency);
    }

    @Override
    public boolean addMoney(Account account, double amount) {
        for (int i = 0; i < accounts.length; i++) {
            if(accounts[i]==account){
                break;
            }
            if(i==accounts.length-1){
                return false;
            }
        }
        account.setAmount(account.getAmount()+amount);
        return true;
    }

    @Override
    public boolean transferMoney(Account from, Account to, double amount) {
        if(from.getAmount()<amount){
            return false;
        }

        boolean firstExists=false;
        boolean secondExists=false;

        for (int i = 0; i < accounts.length; i++) {
            if(accounts[i]==from){
                firstExists=true;
            }
            else if(accounts[i]==to){
                secondExists=true;
            }
            if(i==accounts.length-1&&(!firstExists||!secondExists)){
                return false;
            }
        }

        if(from.getCurrency().equals(to.getCurrency())){
            from.setAmount(from.getAmount()-amount);
            to.setAmount(to.getAmount()+amount);
        }
        else{
            if(from.getCurrency().equals("BGN")){
                from.setAmount(from.getAmount()-amount);
                to.setAmount(to.getAmount()+(amount/1.95583));
            }
            else{
                from.setAmount(from.getAmount()-amount);
                to.setAmount(to.getAmount()+(amount*1.95583));
            }
        }

        return true;
    }

    @Override
    public double getTotalAmount() {

        double amount=0;

        for (int i = 0; i < accounts.length; i++) {
            if(accounts[i].getCurrency().equals("EUR")){
                amount+=(accounts[i].getAmount()*1.95583);
                continue;
            }
            amount+=accounts[i].getAmount();
        }

        return amount;
    }
}
