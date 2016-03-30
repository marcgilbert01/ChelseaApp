package marc.chelseaapp.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilbertm on 29/03/2016.
 */
public class CoinDispenser {

    int totalToPay;
    int remainingToPay;
    List<Coin> coinsInserted = new ArrayList<>();


    public CoinDispenser(int totalToPay) {
        this.totalToPay = totalToPay;
        remainingToPay = totalToPay;
    }


    public int insertCoin(Coin coin){

        coinsInserted.add(coin);
        remainingToPay = totalToPay - getTotalFromCoins(coinsInserted);

        return remainingToPay;
    }


    public List<Coin> returnChange(){

        return calculateChange( totalToPay , coinsInserted );
    }


    public List<Coin> calculateChange(long price, List<Coin> coinsInserted){

        List<Coin> coinsForChange = new ArrayList<>();

        // CALCULATE CHANGE DUE
        long totalInserted = getTotalFromCoins(coinsInserted);
        long changeDue = totalInserted-price;

        // CALCULATE COINS TO GIVE BACK
        if( changeDue>0 ){

            for(Coin.CoinType coinType : Coin.CoinType.values()  ){

                int nbCoins = 0;
                nbCoins = (int) (changeDue/coinType.value);
                if( nbCoins>0 ) {
                    Coin coinForChange = new Coin(coinType, nbCoins);
                    coinsForChange.add(coinForChange);

                    changeDue = changeDue - (coinForChange.getCoinType().getValue() * coinForChange.getQty());

                }
            }
        }

        return coinsForChange;
    }



    static public int getTotalFromCoins(List<Coin> coins ){

        int total = 0;

        for(Coin coin : coins  ){
            total += coin.getCoinType().getValue() * coin.getQty();
        }

        return total;
    }








}
