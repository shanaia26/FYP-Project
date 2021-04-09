package com.example.fyp_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_project.Admin.AdminMaintainProductsActivity;
import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Products;
import com.example.fyp_project.Model.UserOrders;
import com.example.fyp_project.Paint.DesignActivity;
import com.example.fyp_project.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private FloatingActionButton floatingCartButton;

    private DatabaseReference productReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type = "";

//    private String saveCurrentDate;
//    private String saveCurrentTime;
//    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Only if you're coming from Admin will this run
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                // To display  icon in toolbar
                toggle.syncState();
            }
        });

        //Makes menu clickable
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view);

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        floatingCartButton = findViewById(R.id.fab_cart);

        floatingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!type.equals("Admin")){
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productReference, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        //Display data on the recyclerview
                        holder.productName.setText(model.getName());
                        holder.productDescription.setText(model.getDescription());
                        holder.productPrice.setText("Price: €" + model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(type.equals("Admin")){
                                    //If it's admin send to maintain products activity
                                    Intent intent = new Intent(MainActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    startActivity(intent);
                                } else {
                                    //If it's user send to product details activity
                                    Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    //intent.putExtra("orderID", orderID);
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    //To avoid closing the application on back press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("name", Common.currentUser.getName());
            intent.putExtra("email", Common.currentUser.getEmail());
            intent.putExtra("phone", Common.currentUser.getPhone());
            startActivity(intent);

        } else if (id == R.id.nav_product) {
            if(!type.equals("Admin")){
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_design) {
            if(!type.equals("Admin")){
                Intent intent = new Intent(MainActivity.this, DesignActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_cart) {
            if(!type.equals("Admin")){
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_search) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(MainActivity.this, SearchProductsActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_history) {
                if(!type.equals("Admin")){
                    Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
                    //intent.putExtra("orderID", orderID);
                    startActivity(intent);
                }

        } else if(id == R.id.nav_track_order){
            if(!type.equals("Admin")){
                Intent intent = new Intent(MainActivity.this, TrackOrderActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_logout) {
            if(!type.equals("Admin")){
                //Removes all saved user information. User has to log in again
                Paper.book().destroy();

                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
