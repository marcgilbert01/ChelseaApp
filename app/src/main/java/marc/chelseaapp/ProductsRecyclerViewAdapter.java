package marc.chelseaapp;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by marc on 28/03/16.
 */
public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ProductViewHolder>{

    List<Product> productList;
    OnProductClickedListener onProductClickedListener;

    public ProductsRecyclerViewAdapter(List<Product> productList, OnProductClickedListener onProductClickedListener) {

        this.productList = productList;
        this.onProductClickedListener = onProductClickedListener;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.product, null );

        ProductViewHolder productViewHolder = new ProductViewHolder(itemView);

        return productViewHolder;
    }



    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {

        holder.textViewProductName.setText( productList.get(position).getName() );
        holder.textViewProductStock.setText(productList.get(position).getStock() + " in stock");
        double price =  ((double)productList.get(position).getPrice())/100;
        holder.textViewProductPrice.setText(NumberFormat.getCurrencyInstance().format(price));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( onProductClickedListener!=null ) {
                    onProductClickedListener.onProductClicked(productList.get(position));
                }
            }
        });

    }


    public void updateProducts(List<Product> productsToUpdate){

        productList.clear();
        productList.addAll(productsToUpdate);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder{


        TextView textViewProductName;
        TextView textViewProductStock;
        TextView textViewProductPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewProductName  = (TextView) itemView.findViewById(R.id.textViewProductName);
            textViewProductStock = (TextView) itemView.findViewById(R.id.textViewProductStock);
            textViewProductPrice = (TextView) itemView.findViewById(R.id.textViewProductPrice);
        }


    }



    interface OnProductClickedListener{

        public void onProductClicked(Product product);

    }




}
