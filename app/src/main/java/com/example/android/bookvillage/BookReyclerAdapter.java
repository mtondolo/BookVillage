package com.example.android.bookvillage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bookvillage.data.BookEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookReyclerAdapter extends RecyclerView.Adapter<BookReyclerAdapter.ViewHolder> {
    private List<BookEntry> mBookEntries;
    private final Context mContext;
    /* private Cursor mCursor;*/
    private final BookReyclerAdapterOnClickHandler mClickHandler;

    public interface BookReyclerAdapterOnClickHandler {
        void onClick(int _id);
    }

    /**
     * Constructor for the BookRecyclerAdapter that initializes the Context.
     *
     * @param context      the current Context
     * @param clickHandler the ItemClickListener
     */
    public BookReyclerAdapter(Context context, BookReyclerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new BookViewHolder that holds the view for each task
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_book_list, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //mCursor.moveToPosition(position);
        BookEntry bookEntry = mBookEntries.get(position);

        // Determine the values of the wanted data
        String name = bookEntry.getBook_name();
        int quantity = bookEntry.getQuantity();
        double price = bookEntry.getPrice();

        //Set values
        holder.mTextName.setText(name);
        holder.mTextQuantity.setText(Integer.toString(quantity));
        holder.mTextPrice.setText(Double.toString(price));
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mBookEntries == null) {
            return 0;
        }
        return mBookEntries.size();
    }

    /**
     * When data changes, this method updates the list of bookEntries
     * and notifies the adapter to use the new values on it
     */
    public void setBooks(List<BookEntry> bookEntries) {
        mBookEntries = bookEntries;
        notifyDataSetChanged();
    }


   /* void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextName;
        private final TextView mTextQuantity;
        private final TextView mTextPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextQuantity = itemView.findViewById(R.id.text_quantity);
            mTextPrice = itemView.findViewById(R.id.text_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            BookEntry bookEntry = mBookEntries.get(adapterPosition);
            int bookId = bookEntry.getId();
            mClickHandler.onClick(bookId);
        }
    }
}

