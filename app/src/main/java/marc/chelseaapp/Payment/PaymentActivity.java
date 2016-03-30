package marc.chelseaapp.Payment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import marc.chelseaapp.Payment.Coin;
import marc.chelseaapp.Payment.CoinDispenser;
import marc.chelseaapp.R;

public class PaymentActivity extends AppCompatActivity {

    static public final String AMOUNT_TO_PAY_PARAM = "amountToPay";
    static public final String PAYING_FOR_TITLE_PARAM = "payingFor";
    static public final String AMOUNT_PAID_PARAM = "amountPaid";


    int amountToPay;
    String payingFor = "";
    TextView textViewPayingFor;
    TextView textViewAmountToPay;
    Button buttonTwoPoundCoin;
    Button buttonOnePoundCoin;
    Button buttonFiftyPenceCoin;
    Button buttonTwentyPenceCoin;
    Button buttonTenPenceCoin;
    Button buttonFivePenceCoin;
    CoinDispenser coinDispenser;

    Handler handler;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        handler = new Handler();
        context = this;

        // GET PARAMS
        amountToPay = getIntent().getExtras().getInt(AMOUNT_TO_PAY_PARAM, 0);
        payingFor   = getIntent().getExtras().getString(PAYING_FOR_TITLE_PARAM , "");
        if( amountToPay<=0 ){
            finish();
        }
        else {

            // INIT COIN DISPENSER
            coinDispenser = new CoinDispenser(amountToPay);

            // DISPLAY AMOUNT AND WHAT WE ARE PAYING FOR
            textViewPayingFor = (TextView) findViewById(R.id.textViewPayingFor);
            textViewAmountToPay = (TextView) findViewById(R.id.textViewAmountTopPay);
            double price = ((double) amountToPay) / 100;
            textViewAmountToPay.setText(NumberFormat.getCurrencyInstance().format(price));
            textViewPayingFor.setText(payingFor);

            // SET LISTENER ON COINS BUTTON
            buttonTwoPoundCoin = (Button) findViewById(R.id.buttonTwoPoundCoin);
            buttonOnePoundCoin = (Button) findViewById(R.id.buttonOnePoundCoin);
            buttonFiftyPenceCoin = (Button) findViewById(R.id.buttonFiftyPenceCoin);
            buttonTwentyPenceCoin = (Button) findViewById(R.id.buttonTwentyPenceCoin);
            buttonTenPenceCoin = (Button) findViewById(R.id.buttonTenPenceCoin);
            buttonFivePenceCoin = (Button) findViewById(R.id.buttonFivePenceCoin);

            buttonTwoPoundCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCoin(new Coin(Coin.CoinType.TWO_POUNDS,1) );
                }
            });
            buttonOnePoundCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCoin(new Coin(Coin.CoinType.ONE_POUND,1));
                }
            });
            buttonFiftyPenceCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCoin(new Coin(Coin.CoinType.FIFTY_PENCE,1));
                }
            });
            buttonTwentyPenceCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCoin(new Coin(Coin.CoinType.TWENTY_PENCE,1) );
                }
            });
            buttonTenPenceCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCoin(new Coin(Coin.CoinType.TEN_PENCE,1) );
                }
            });
            buttonFivePenceCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCoin(new Coin(Coin.CoinType.FIVE_PENCE,1));
                }
            });

        }

    }



    private void insertCoin(final Coin coin){

        new Thread(){
            @Override
            public void run() {
                super.run();
                final double amountRemaining = coinDispenser.insertCoin(coin);

                // IF PAYMENT NOT COMPLETED SHOW REMAINING AMOUNT
                if( amountRemaining>0 ) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewAmountToPay.setText(NumberFormat.getCurrencyInstance().format(amountRemaining / 100));
                        }
                    });
                }
                // IF EXACT CHANGE FINISH
                if( amountRemaining==0 ){
                    Intent intent=new Intent();
                    intent.putExtra(AMOUNT_PAID_PARAM , amountToPay );
                    setResult( RESULT_OK , intent);
                    finish();
                    finish();
                }
                // IF EXTRA CHANGE , CALCULATE AND RETURN COINS FOR CHANGE
                if( amountRemaining<0 ){

                    // SHOW AMOUNT TO REMAINING TO PAY AS 0
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewAmountToPay.setText(NumberFormat.getCurrencyInstance().format(0));
                        }
                    });

                    // CALCULATE CHANGE TO RETURN
                    List<Coin> coinsReturned = coinDispenser.returnChange();
                    final StringBuilder stringBuilder = new StringBuilder();
                    for(Coin coin : coinsReturned  ){
                        stringBuilder.append( coin.getQty()+"x "+coin.getCoinType().getName()+"\n" );
                    }

                    // SHOW COINS RETURNED
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Coins Returned");
                            builder.setMessage(stringBuilder.toString());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                    // RETURN SUCCESS
                                    Intent intent=new Intent();
                                    intent.putExtra(AMOUNT_PAID_PARAM , amountToPay );
                                    setResult( RESULT_OK , intent);
                                    finish();

                                }
                            });
                            builder.show();
                        }
                    });

                }
            }
        }.start();


    }



}


