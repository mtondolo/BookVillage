package com.example.android.bookvillage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookReyclerAdapter extends RecyclerView.Adapter<BookReyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;

    public BookReyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
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
        mCursor.moveToPosition(position);

        String quantity = mCursor.getString(CatalogActivity.INDEX_QUANTITY);
        String name = mCursor.getString(CatalogActivity.INDEX_NAME);
        String price = mCursor.getString(CatalogActivity.INDEX_PRICE);

        holder.mTextQuantity.setText(quantity);
        holder.mTextName.setText(name);
        holder.mTextPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextQuantity;
        private final TextView mTextName;
        private final TextView mTextPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextQuantity = itemView.findViewById(R.id.text_quantity);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextPrice = itemView.findViewById(R.id.text_price);
        }
    }
}
