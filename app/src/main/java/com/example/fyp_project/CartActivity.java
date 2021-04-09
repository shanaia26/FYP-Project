package com.example.fyp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_project.Common.Common;
import com.example.fyp_project.Model.Cart;
import com.example.fyp_project.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE = 9999;

    private RecyclerView recyclerCart;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessButton;
    private TextView totalPrice;
    private TextView orderMessage;

    private int overallTotalPrice = 0;

    //Product & Order ID from Product Details Activity - after items are put in the cart
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productID = getIntent().getStringExtra("productID");

        recyclerCart = findViewById(R.id.recycler_cart);
        recyclerCart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);

        nextProcessButton = findViewById(R.id.next_process_button);
        totalPrice = findViewById(R.id.total_price);
        orderMessage = findViewById(R.id.order_message);

        nextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ShipmentActivity.class);
                intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                intent.putExtra("productID", productID);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //CheckOrderState();

        final DatabaseReference cartListReference
                = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListReference.child("User View")
                                .child(Common.currentUser.getPhone())
                                .child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.textProductQuantity.setText("Quantity: " + model.getQuantity());
                holder.textProductPrice.setText("Price: €" + model.getPrice());
                holder.textProductName.setText(model.getProductName());
                holder.textProductSize.setText("UK Size: " + model.getSize());

                //Calculating the total price
                int oneTypeProductPrice = ((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getQuantity());
                overallTotalPrice = (int) (overallTotalPrice + oneTypeProductPrice);
                //Displaying the amount
                totalPrice.setText("Total Price: €"+ String.valueOf(overallTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("productID", model.getProductID());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    //Remove from User View
                                    DatabaseReference userCartReference
                                            = FirebaseDatabase.getInstance().getReference()
                                            .child("Cart List")
                                            .child("User View")
                                            .child(Common.currentUser.getPhone())
                                            .child("Products")
                                            .child(model.getProductID());

                                    userCartReference.removeValue();

                                    //Remove from Admin View
                                    DatabaseReference adminCartReference
                                            = FirebaseDatabase.getInstance().getReference()
                                            .child("Cart List")
                                            .child("Admin View")
                                            .child(Common.currentUser.getPhone())
                                            .child("Products")
                                            .child(model.getProductID());

                                    adminCartReference.removeValue();

                                    Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerCart.setAdapter(adapter);
        adapter.startListening();
    }

//    private void CheckOrderState(){
//        orderReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String shippingStatus = snapshot.child("shipmentStatus").getValue().toString();
//                    String userName = snapshot.child("name").getValue().toString();
//
//                    if(shippingStatus.equals("Shipped")){
//                        //Let user know their order has been shipped
//                        totalPrice.setText(userName + " Your order has been shipped.");
//                        recyclerCart.setVisibility(View.GONE);
//
//                        orderMessage.setVisibility(View.VISIBLE);
//                        orderMessage.setText("Your order has been shipped.");
//                        nextProcessButton.setVisibility(View.GONE);
//                        Toast.makeText(CartActivity.this, "You can purchase more products once you have received your order.", Toast.LENGTH_LONG).show();
//                    } else if (shippingStatus.equals("Order Not Shipped")){
//                        //Let user know their order has not been shipped
//                        totalPrice.setText("Shipping Status: Not Shipped");
//                        recyclerCart.setVisibility(View.GONE);
//
//                        orderMessage.setVisibility(View.VISIBLE);
//                        nextProcessButton.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
