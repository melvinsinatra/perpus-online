package com.example.project_lab.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_lab.BookDetailActivity;
import com.example.project_lab.R;
import com.example.project_lab.model.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private ArrayList<Book> bookList;
    private Context context;

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflater untuk menerapkan Layout yang sudah dibuat
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, int position) {
        holder.name.setText(bookList.get(position).getName());
        holder.author.setText(bookList.get(position).getAuthor());
        holder.synopsis.setText(bookList.get(position).getSynopsis());
        String imageURL = "https://isys6203-perpus-online.herokuapp.com/" + bookList.get(position).getCover();
        Glide.with(context).load(imageURL).into(holder.cover);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(holder.itemView.getContext(), BookDetailActivity.class);
            i.putExtra("Book", bookList.get(position));
            i.putExtra("Book Cover", imageURL);
            holder.itemView.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

//    public void replaceData(ArrayList<Book> bookList) {
//        this.bookList = bookList;
//        notifyDataSetChanged();
//    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView name, author, synopsis;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bookList_bookTitle);
            author = itemView.findViewById(R.id.bookList_bookAuthor);
            synopsis = itemView.findViewById(R.id.bookList_bookSynopsis);
            cover = itemView.findViewById(R.id.bookList_bookCover);
        }


    }

}