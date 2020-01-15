package com.example.android.bookvillage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookReyclerAdapter extends RecyclerView.Adapter<BookReyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public BookReyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_book_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextQuantity;
        private final TextView mTextName;
        private final TextView mTextPrice;
        private final TextView mTextSell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextQuantity = itemView.findViewById(R.id.text_quantity);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextPrice = itemView.findViewById(R.id.text_price);
            mTextSell = itemView.findViewById(R.id.text_sell);
        }
    }
}
