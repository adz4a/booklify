package com.example.booklify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booklify.R;
import com.example.booklify.activity.books.DetailActivity;
import com.example.booklify.model.BasketModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.viewHolder> {

    List<BasketModel> bookModelHolder;
    Context context;
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    public BasketAdapter(List<BasketModel> bookModelHolder, Context context) {
        this.bookModelHolder = bookModelHolder;
        this.context = context;
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_basket,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.title.setText(bookModelHolder.get(position).getTitle());
        holder.price.setText(String.valueOf(bookModelHolder.get(position).getPrice()));
        Glide.with(context)
                .load(this.bookModelHolder.get(position).getImage())
                .into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("id", bookModelHolder.get(position).getId());
                intent.putExtra("title", bookModelHolder.get(position).getTitle());
                intent.putExtra("image", bookModelHolder.get(position).getImage());
                intent.putExtra("content", bookModelHolder.get(position).getContent());
                intent.putExtra("author", bookModelHolder.get(position).getAuthor());
                intent.putExtra("category", bookModelHolder.get(position).getCategory());
                intent.putExtra("popularity", bookModelHolder.get(position).isPopularity());
                intent.putExtra("library", bookModelHolder.get(position).getLibrary());
                intent.putExtra("price", String.valueOf(bookModelHolder.get(position).getPrice()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null) {
                    mDb.collection("CurrentUser").document(mAuth.getCurrentUser().getUid())
                            .collection("cartShop")
                            .document(bookModelHolder.get(position).getDocumentId())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        bookModelHolder.remove(bookModelHolder.get(position));
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Item deleted", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookModelHolder.size();
    }

    class viewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView title, price;
        ImageButton delete;
        Button summary;
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
            img=itemView.findViewById(R.id.image);
            price=itemView.findViewById(R.id.price);
            title=itemView.findViewById(R.id.title);
            delete=itemView.findViewById(R.id.deleteItem);
            summary = itemView.findViewById(R.id.summary);
        }
    }

    public void filterList(ArrayList<BasketModel> filteredList) {
        bookModelHolder = filteredList;
        notifyDataSetChanged();
    }


}

