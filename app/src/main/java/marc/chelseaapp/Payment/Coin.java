package marc.chelseaapp.Payment;

/**
 * Created by gilbertm on 29/03/2016.
 */

public class Coin{

    private CoinType coinType;
    private int qty = 1;

    public Coin(CoinType coinType, int qty) {
        this.coinType = coinType;
        this.qty = qty;
    }

    public CoinType getCoinType() {

        return coinType;
    }

    public int getQty() {
        return qty;
    }




    public enum CoinType {

        TWO_POUNDS( "Two pound" , 200 ),
        ONE_POUND( "One pound" , 100 ),
        FIFTY_PENCE( "Fifty pence" , 50 ),
        TWENTY_PENCE( "Twenty pence" , 20 ),
        TEN_PENCE( "Ten pence" , 10 ),
        FIVE_PENCE( "Five pence" , 5 );

        String name;
        long value;

        CoinType(String name, long value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public long getValue() {
            return value;
        }


    }





}

