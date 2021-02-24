package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.account.BGNAccount;
import bg.sofia.uni.fmi.mjt.revolut.account.EURAccount;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;
import bg.sofia.uni.fmi.mjt.revolut.card.PhysicalCard;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        PhysicalCard MasterCard=new PhysicalCard("1234", 1234, LocalDate.of(2021, 11, 13));
        Card c[]= new Card[1];

        c[0]=MasterCard;

        BGNAccount bgnAccount = new BGNAccount("asdf", 1000);
        EURAccount eurAccount = new EURAccount("aa", 1000);
        EURAccount eurAccount1 = new EURAccount("aaa", 1000);

        Account acc[] = new Account[3];
        acc[0]=bgnAccount;
        acc[1]=eurAccount;
        acc[2]=eurAccount1;

        Revolut rev = new Revolut(acc, c);

        System.out.println(rev.getTotalAmount());
    }
}
