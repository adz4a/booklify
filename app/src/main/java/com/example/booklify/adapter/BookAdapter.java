package com.example.booklify.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.booklify.R;
import com.example.booklify.activity.books.DetailActivity;
import com.example.booklify.model.BookModel;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>  {
    private Context context;
    private List<BookModel> bookList;


    public BookAdapter(Context context, List<BookModel> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovieList(List<BookModel> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_books, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.MyViewHolder holder, int position) {
        holder.title.setText(this.bookList.get(position).getTitle().toString());

        Glide.with(context)
                .load(this.bookList.get(position).getImage())
                .apply(RequestOptions.overrideOf(1300,1900))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("id", bookList.get(position).getId());
                intent.putExtra("title", bookList.get(position).getTitle());
                intent.putExtra("image", bookList.get(position).getImage());
                intent.putExtra("content", bookList.get(position).getContent());
                intent.putExtra("author", bookList.get(position).getAuthor());
                intent.putExtra("category", bookList.get(position).getCategory());
                intent.putExtra("popularity", bookList.get(position).isPopularity());
                intent.putExtra("price", bookList.get(position).getPrice());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.bookList != null) {
            return this.bookList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;


        public MyViewHolder(View itemView)  {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }


    public void filterList(ArrayList<BookModel> filteredList) {
        bookList = filteredList;
        notifyDataSetChanged();
    }

}
