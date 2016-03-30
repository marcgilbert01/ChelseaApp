package marc.chelseaapp.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import marc.chelseaapp.Product;

/**
 * Created by gilbertm on 29/03/2016.
 */
public class ProductsDao extends SQLiteOpenHelper{

    static final String PRODUCT_DB_NAME = "products.db";
    static final String PRODUCT_TABLE_NAME = "products";

    Context context;

    public ProductsDao(Context context) {
        super(context, PRODUCT_DB_NAME , null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TABLE
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE_NAME+ " ( " +
                "id INTEGER PRIMARY KEY," +
                "name          TEXT," +
                "price         INTEGER," +
                "stock         INTEGER" +
                ")");

        // ADD PRODUCTS
        List<Product> productsForTest = new ArrayList<>();
        productsForTest.add(new Product("Coca Cola 330ml", 80, 10));
        productsForTest.add(new Product("Coke Zero 330ml", 80, 13));
        productsForTest.add(new Product("Fanta 330ml", 80, 11));
        productsForTest.add(new Product("Sprite 330ml", 80, 12));
        productsForTest.add(new Product("Coca Cola 500ml", 120, 5));
        productsForTest.add(new Product("Coke Zero 500ml", 120, 4));
        productsForTest.add(new Product("Fanta 500ml", 120, 11));
        productsForTest.add(new Product("Sprite 500ml", 120, 8));
        productsForTest.add(new Product("Water 500ml", 100, 5));

        // CREATE QUERY TO INSERT ALL PRODUCTS AT ONE
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO '" + PRODUCT_TABLE_NAME + "' ");
        for (int p = 0; p < productsForTest.size() ; p++) {

            Product product = productsForTest.get(p);
            // SAVE TO DB
            if (p == 0) {

                stringBuilder.append("SELECT " +
                                "         NULL                      AS id," +
                                "       '" + StringEscapeUtils.escapeSql(product.getName()) + "' AS name," +
                                "        " + product.getPrice()+ "  AS price," +
                                "        " + product.getStock() + " AS stock "
                );

            } else {

                stringBuilder.append("UNION ALL SELECT " +
                                "       NULL ," +
                                "       '" + StringEscapeUtils.escapeSql(product.getName() ) + "'," +
                                "        " + product.getPrice() + "," +
                                "        " + product.getStock() + " "
                );
            }
        }
        String sql = stringBuilder.toString();
        db.execSQL(sql);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void saveProducts(List<Product> products) {


        if (products != null && products.size() > 0) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INSERT INTO '" + PRODUCT_TABLE_NAME + "' ");

            for (int p = 0; p < products.size() ; p++) {

                Product product = products.get(p);
                // SAVE TO DB
                if (p == 0) {

                    stringBuilder.append("SELECT " +
                            "         NULL                      AS id," +
                            "       '" + StringEscapeUtils.escapeSql(product.getName()) + "' AS name," +
                            "        " + product.getPrice()+ "  AS price," +
                            "        " + product.getStock() + " AS stock "
                    );

                } else {

                    stringBuilder.append("UNION ALL SELECT " +
                            "       NULL ," +
                            "       '" + StringEscapeUtils.escapeSql(product.getName() ) + "'," +
                            "        " + product.getPrice() + "," +
                            "        " + product.getStock() + " "
                           );
                }
            }

            String sql = stringBuilder.toString();
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();
        }
    }



    public List<Product> getAllProduct() {

        List<Product> products = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PRODUCT_TABLE_NAME , null, null, null, null, null, null);
        if( cursor.getCount() > 0) {
            cursor.moveToFirst();
            for( int r=0 ; r<cursor.getCount() ; r++ ) {
                Product product = readProduct(cursor);
                products.add(product);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return products;
    }




    private Product readProduct(Cursor cursor){

        Product product = new Product();

        product.setId(cursor.getInt(0));
        product.setName(cursor.getString(1));
        product.setPrice(cursor.getInt(2));
        product.setStock(cursor.getInt(3));

        return product;
    }

    public void updateStock(int productId, int newStock) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL( "UPDATE "+PRODUCT_TABLE_NAME+" SET stock = "+newStock+" WHERE id="+productId );
        sqLiteDatabase.close();
    }


}
