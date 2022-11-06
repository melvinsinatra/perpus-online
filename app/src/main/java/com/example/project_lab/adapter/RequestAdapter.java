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
import com.example.project_lab.R;
import com.example.project_lab.RequestDetailActivity;
import com.example.project_lab.model.Book;
import com.example.project_lab.database.DatabaseHelper;
import com.example.project_lab.model.Requests;
import com.example.project_lab.model.User;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Requests> requestsList;
    private DatabaseHelper db;

    public RequestAdapter(Context context, ArrayList<Requests> requestsList) {
        this.context = context;
        if (requestsList == null) {
            this.requestsList = new ArrayList<>();
        } else {
            this.requestsList = requestsList;
        }
    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, int position) {
        db = new DatabaseHelper(context);
        Requests req = requestsList.get(position);

        int requesterId = req.getRequester_id();
        User requester = db.getUserData(requesterId);
        holder.requesterEmail.setText(requester.getEmail());
        if (req.getReceiver_id()==0) {
            holder.receiverEmail.setText("-");
        } else {
            int receiverId = req.getReceiver_id();
            User receiver = db.getUserData(receiverId);
            holder.receiverEmail.setText(receiver.getEmail());
        }

        int book_id = requestsList.get(position).getBook_id();
        for(int i=0; i< db.getBookData().size(); i++) {
            Book book = db.getBookData().get(i);
            if(book_id == book.getId()) {
                String imageURL = "https://isys6203-perpus-online.herokuapp.com/" + book.getCover();
                Glide.with(context).load(imageURL).into(holder.cover);
                holder.name.setText(book.getName());
                holder.author.setText(book.getAuthor());
                Book requestedBook = db.getBookData().get(i);

                holder.itemView.setOnClickListener(view -> {
                    Intent in = new Intent(holder.itemView.getContext(), RequestDetailActivity.class);
                    in.putExtra("Chosen Requests", requestsList.get(position));
                    in.putExtra("Chosen Book", requestedBook);
                    in.putExtra("Chosen Book Cover", imageURL);
                    holder.itemView.getContext().startActivity(in);
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView requesterEmail, receiverEmail, name, author;

        public ViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.requestList_img_bookCover);
            requesterEmail = itemView.findViewById(R.id.requestList_tv_requesterEmail);
            receiverEmail = itemView.findViewById(R.id.requestList_tv_receiverEmail);
            name = itemView.findViewById(R.id.requestList_tv_bookTitle);
            author = itemView.findViewById(R.id.requestList_tv_bookAuthor);
        }
    }
}
