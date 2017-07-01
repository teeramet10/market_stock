package com.market.application.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.market.application.activity.BaseActivity;
import com.market.application.MyFilter;
import com.market.application.R;
import com.market.application.activity.AddStockActivity;
import com.market.application.activity.DetailProductActivity;
import com.market.application.activity.MainActivity;
import com.market.application.activity.ProductActivity;
import com.market.application.activity.ProductStockActivity;
import com.market.application.activity.StockActivity;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Stock;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 22/3/2560.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {


    private Context context;
    private ArrayList<Stock> stockArrayList;
    private LayoutInflater inflater;
    private MyFilter myFilter;
    private Activity activity;
    private myViewHolder viewHolder;


    public ProductAdapter(Context context, ArrayList<Stock> stocksArrayList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.stockArrayList = stocksArrayList;
        myFilter = new MyFilter(stocksArrayList, this);
        this.activity = (Activity) context;
    }

    @Override
    public ProductAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (activity instanceof ProductActivity) {
            view = inflater.inflate(R.layout.item_view_product, parent, false);
            viewHolder = new myViewHolder(view);
            return viewHolder;
        } else if (activity instanceof ProductStockActivity || activity instanceof StockActivity) {
            view = inflater.inflate(R.layout.item_product_manage, parent, false);
            viewHolder = new myViewHolder(view);
            return viewHolder;
        } else {
            return null;
        }


    }

    @Override
    public void onBindViewHolder(ProductAdapter.myViewHolder holder, final int i) {

        holder.tvName.setText(stockArrayList.get(i).getProduct().getName());
        holder.tvPrice.setText(String.valueOf((int)stockArrayList.get(i).getProduct_price())
                + " " + context.getResources().getString(R.string.unit));

        if (stockArrayList.get(i).getProduct().getPathimage() != null) {
            File file = new File(stockArrayList.get(i).getProduct().getPathimage());
            if (file.exists()) {
                Picasso.with(context).load(file).fit().centerCrop().error(R.drawable.pack).into(holder.image);
            }
        } else {
            Picasso.with(context).load(R.drawable.pack).centerCrop().fit().into(holder.image);
        }
        //TODO เพิ่มสินค้าลงตระกร้า
        if (stockArrayList.get(i).getProduct_quality() != 0) {
            if (activity instanceof ProductActivity || activity instanceof MainActivity) {
                holder.btnAddcart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductActivity productActivity = (ProductActivity) activity;
                        productActivity.createOrder(stockArrayList.get(i));

                    }
                });
            }

            if (holder.rlOutStock.getVisibility() == View.VISIBLE) {
                holder.rlOutStock.setVisibility(View.INVISIBLE);
            }
        }

        if (activity instanceof ProductStockActivity || activity instanceof StockActivity) {
            holder.tvQual.setText(String.valueOf(stockArrayList.get(i).getProduct_quality()));
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteProduct(i);
                    return true;
                }
            });


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setItems(((BaseActivity) activity).menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            if (j == 0) {

                                Intent intent = new Intent(context, DetailProductActivity.class);
                                intent.putExtra(SqliteHelper.ID_PRODUCT, stockArrayList.get(i).getProduct().getId());
                                context.startActivity(intent);


                            } else {
                                Intent intent = new Intent(context, AddStockActivity.class);
                                intent.putExtra(SqliteHelper.PRODUCT_NAME, stockArrayList.get(i).getProduct().getName());
                                intent.putExtra(SqliteHelper.TYPE_NAME, stockArrayList.get(i).getProduct().getType().getName());
                                intent.putExtra(SqliteHelper.PRODUCT_PICTURE, stockArrayList.get(i).getProduct().getPathimage());
                                intent.putExtra(SqliteHelper.ID_PRODUCT,stockArrayList.get(i).getProduct().getId());
                                context.startActivity(intent);
                            }
                        }
                    });

                    alert.create().show();
                }
            });

        }
    }


    public void setStockArrayList(ArrayList<Stock> stockArrayList) {
        this.stockArrayList = stockArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;


    }

    @Override
    public int getItemCount() {
        return stockArrayList.size();
    }

    public void setList(ArrayList<Stock> list) {

        this.stockArrayList = list;
    }

    //call when you want to filter
    public void filterList(String text) {
        myFilter.filter(text);
    }

    //TODO ลบสินค้า
    private void deleteProduct(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(context.getResources().getString(R.string.del_product));
        alertDialog.setIcon(android.R.drawable.ic_delete);
        alertDialog.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseManagement db = new DatabaseManagement(context);
                long delstock = db.deleteStock(stockArrayList.get(position).getStockid());
                int delproduct = db.disableProduct(stockArrayList.get(position).getProduct().getId());
                ArrayList<Integer> deldetail = db.deleteStockInDetailCart(stockArrayList.get(position).getStockid());

                if (deldetail.size() != 0) {
                    for (int j = 0; j < deldetail.size(); j++) {
                        long delcart = db.deleteDetailCartInCart(deldetail.get(j));
                    }
                }

                if (delproduct != 0 && delstock != 0) {
                    stockArrayList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, context.getResources().getString(R.string.finish), Toast.LENGTH_SHORT).show();
                }
                db.closeDatabase();
            }
        });
        alertDialog.setNegativeButton(context.getResources().getString(R.string.cancel), null);
        alertDialog.setCancelable(false);
        alertDialog.create().show();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvName;
        TextView tvPrice;
        TextView tvQual;
        CardView cardView;
        Button btnAddcart;
        RelativeLayout rlOutStock;

        private myViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageproduct);
            tvName = (TextView) itemView.findViewById(R.id.txtName);
            tvPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            rlOutStock = (RelativeLayout) itemView.findViewById(R.id.outstock);

            if (activity instanceof ProductActivity || activity instanceof MainActivity) {
                btnAddcart = (Button) itemView.findViewById(R.id.btnaddCart);
            } else if (activity instanceof ProductStockActivity || activity instanceof StockActivity) {
                tvQual = (TextView) itemView.findViewById(R.id.txtQual);
            }

        }
    }

}
