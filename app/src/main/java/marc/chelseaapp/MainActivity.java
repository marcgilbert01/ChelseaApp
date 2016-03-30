package marc.chelseaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import marc.chelseaapp.Dao.ProductsDao;
import marc.chelseaapp.Payment.PaymentActivity;

public class MainActivity extends AppCompatActivity implements ProductsRecyclerViewAdapter.OnProductClickedListener{

    static public final int REQUEST_CODE_PAYMENT = 1;

    RecyclerView productsRecyclerView;
    ProductsRecyclerViewAdapter productsRecyclerViewAdapter;

    Product productSelected;
    ProductsDao productsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });

        // GET PRODUCTS FROM DB
        productsDao = new ProductsDao(this);
        List<Product> products = productsDao.getAllProduct();

        // SET RECYCLER VIEW
        productsRecyclerView = (RecyclerView) findViewById(R.id.productRecyclerView);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter( products , this );
        productsRecyclerView.setAdapter( productsRecyclerViewAdapter );

    }


    @Override
    public void onProductClicked(Product product) {

        productSelected = product;
        if( product.getStock()>0 ) {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PaymentActivity.AMOUNT_TO_PAY_PARAM, product.getPrice());
            intent.putExtra(PaymentActivity.PAYING_FOR_TITLE_PARAM, product.getName());
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
        else{
            Toast.makeText(getApplicationContext(), "OUT OF STOCK", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == REQUEST_CODE_PAYMENT ){

            if( resultCode==RESULT_OK ){

                int amountPaid = data.getExtras().getInt( PaymentActivity.AMOUNT_PAID_PARAM );
                if( productSelected!=null && amountPaid>=productSelected.getPrice() ){

                    // UPDATE STOCK
                    productSelected.setStock(productSelected.getStock() - 1);
                    productsDao.updateStock( productSelected.getId() , productSelected.getStock() );

                    // REFRESH RECYCLERVIEW
                    List<Product> products = productsDao.getAllProduct();
                    productsRecyclerViewAdapter.updateProducts( products );

                }
            }

        }

    }


}
