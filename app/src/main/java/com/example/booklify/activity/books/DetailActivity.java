package com.example.booklify.activity.books;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.booklify.R;
import com.example.booklify.model.BookModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    ImageView img, img_bg;
    TextView title, library, price, content;
    RecyclerView recyclerView;
    LinearLayout back;

    LottieAnimationView bookmark;
    private boolean btnBookmark = false;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private BookModel bookModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        img = findViewById(R.id.image);
        img_bg = findViewById(R.id.image1);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);



        final boolean newObject = getIntent().getBooleanExtra("bookmark",false);

        bookmark = findViewById(R.id.lottie_bookmark);
        if (newObject) {
            bookmark.playAnimation();
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnBookmark){
                    return;
                }

                else if (newObject){
                    bookmark.cancelAnimation();

                }

                else{
                    bookmark.playAnimation();
                    BookmarkBook();
                }

            }
        });


//        library = findViewById(R.id.library);
//        price = findViewById(R.id.price);


        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .apply(RequestOptions.overrideOf(1300,1900))
                .into(img);

        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .apply(RequestOptions.overrideOf(1300,1900))
                .into(img_bg);

        title.setText(getIntent().getStringExtra("title"));
        content.setText(getIntent().getStringExtra("content"));
//        price.setText(getIntent().getStringExtra("price"));
//        library.setText(getIntent().getStringExtra("library"));

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof BookModel){
            bookModel = (BookModel) object;
        }




        if(bookModel != null){
            Glide.with(getApplicationContext()).load(bookModel.getImage()).into(img);
        }


        recyclerView = findViewById(R.id.shopRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void BookmarkBook() {
        final HashMap<String, Object> bookmarkMap = new HashMap<>();

           bookmarkMap.put("id", getIntent().getStringExtra("id"));
           bookmarkMap.put("title", getIntent().getStringExtra("title"));
           bookmarkMap.put("image", getIntent().getStringExtra("image"));
           bookmarkMap.put("content",  getIntent().getStringExtra("content"));
           bookmarkMap.put("author",  getIntent().getIntExtra("author",0));
           bookmarkMap.put("category",  getIntent().getIntExtra("category",0));
           bookmarkMap.put("popularity", getIntent().getBooleanExtra("popularity",false));

        if(mAuth.getCurrentUser() != null) {
            mFirestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("bookmark").add(bookmarkMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(getApplicationContext(), "You may seen in bookmark", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}