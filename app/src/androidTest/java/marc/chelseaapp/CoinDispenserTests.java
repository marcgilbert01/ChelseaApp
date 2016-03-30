package marc.chelseaapp;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import marc.chelseaapp.Payment.Coin;
import marc.chelseaapp.Payment.CoinDispenser;

/**
 * Created by gilbertm on 29/03/2016.
 */
public class CoinDispenserTests extends AndroidTestCase{


   public void testGetTotalFromCoins(){


       CoinDispenser coinDispenser = new CoinDispenser(0);

       List<Coin> coinList = new ArrayList<>();
       coinList.add( new Coin(Coin.CoinType.TWO_POUNDS   , 1 ) );
       coinList.add( new Coin(Coin.CoinType.ONE_POUND    , 2 ) );
       coinList.add( new Coin(Coin.CoinType.FIFTY_PENCE  , 3 ) );
       coinList.add( new Coin(Coin.CoinType.TWENTY_PENCE , 4 ) );
       coinList.add( new Coin(Coin.CoinType.TEN_PENCE    , 5 ) );
       coinList.add( new Coin(Coin.CoinType.FIVE_PENCE   , 6 ) );

       double totalFromCoins = coinDispenser.getTotalFromCoins( coinList );

       assertEquals( 71 , totalFromCoins );

   }



   public void testCalculateChange(){


       CoinDispenser coinDispenser = new CoinDispenser(0);

       List<Coin> coinList = new ArrayList<>();
       coinList.add(new Coin(Coin.CoinType.TWO_POUNDS , 1));

       List<Coin> coinsForChange = coinDispenser.calculateChange( 85 , coinList );

       assertEquals( 115 , coinDispenser.getTotalFromCoins(coinsForChange) );

       assertEquals( Coin.CoinType.ONE_POUND , coinsForChange.get(0).getCoinType() );
       assertEquals(1, coinsForChange.get(0).getQty());

       assertEquals( Coin.CoinType.TEN_PENCE , coinsForChange.get(1).getCoinType() );
       assertEquals( 1 , coinsForChange.get(1).getQty() );

       assertEquals( Coin.CoinType.FIVE_PENCE , coinsForChange.get(2).getCoinType() );
       assertEquals( 1 , coinsForChange.get(2).getQty() );


       coinList = new ArrayList<>();
       coinList.add(new Coin(Coin.CoinType.FIFTY_PENCE , 3));
       coinList.add(new Coin(Coin.CoinType.ONE_POUND   , 1));

       coinsForChange = coinDispenser.calculateChange( 165 , coinList );

       assertEquals( 85 , coinDispenser.getTotalFromCoins(coinsForChange) );

       assertEquals( Coin.CoinType.FIVE_PENCE , coinsForChange.get(0).getCoinType() );
       assertEquals( 1, coinsForChange.get(0).getQty());

       assertEquals( Coin.CoinType.TWENTY_PENCE , coinsForChange.get(1).getCoinType() );
       assertEquals( 2 , coinsForChange.get(1).getQty() );

       assertEquals( Coin.CoinType.FIVE_PENCE , coinsForChange.get(2).getCoinType() );
       assertEquals( 1 , coinsForChange.get(2).getQty() );


   }






}
