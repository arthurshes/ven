package mychati.app.Client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mychati.app.Client.CartAdapter.CartAdapter;
import mychati.app.Client.CartAdapter.MartAdapter;
import mychati.app.Client.CartHolder.cartHolder;
import mychati.app.Client.ClientBottomInfo.ClientInfoFromZakaz;
import mychati.app.Client.ClientShopsHolders.ClientShopHolder;
import mychati.app.Client.ClientShopsModel.ShopAdapter;
import mychati.app.R;
import mychati.app.TestHolders.ChildHolder;
import mychati.app.TestHolders.ParentHolder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {


private FirebaseRecyclerAdapter <CartAdapter, ParentHolder> adapters;
private FirebaseRecyclerAdapter<MartAdapter,ChildHolder> adapterTwo;
    private DatabaseReference karzinaRef;
    private DatabaseReference shopRef;
    private LottieAnimationView lottieAnimationView;
    private RoundedImageView shopimege;
private int totalnyPrice;
    private int diablo;
    private ValueEventListener mRefUserListener;
    private HashMap <DatabaseReference,ValueEventListener> value=new HashMap<>() ;
private int dodo=0;
    private int kol;
    private AppCompatButton buybutton;
private int overTovar;
    private TextView textpricecarti,textkarzininziv,textViewnocart;
    private RecyclerView reccart;
    private FirebaseAuth mAuth;
    private DatabaseReference hophh;

    private ImageView deletezakazcart;
    RecyclerView.LayoutManager layoutManager;
View cart;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
   cart= inflater.inflate(R.layout.fragment_cart, container, false);
reccart=cart.findViewById(R.id.reccart);
        layoutManager = new LinearLayoutManager(this.getContext());

        deletezakazcart=cart.findViewById(R.id.deletedocart);
        reccart.setHasFixedSize(true);
        reccart.setLayoutManager(layoutManager);
        karzinaRef= FirebaseDatabase.getInstance().getReference().child("DoCart");
        shopRef=FirebaseDatabase.getInstance().getReference().child("shops");

        mAuth=FirebaseAuth.getInstance();
lottieAnimationView=cart.findViewById(R.id.lottieAnimationViewnottovarcart);
textViewnocart=cart.findViewById(R.id.texxtnottovercart);




































        FirebaseRecyclerOptions<CartAdapter> options=new FirebaseRecyclerOptions.Builder<CartAdapter>()
                .setQuery(karzinaRef.child(mAuth.getCurrentUser().getUid()),CartAdapter.class).build();
       adapters=new FirebaseRecyclerAdapter<CartAdapter, ParentHolder>(options) {
            @Override
            protected void onBindViewHolder( @androidx.annotation.NonNull ParentHolder holder, int position,  @androidx.annotation.NonNull CartAdapter model) {


                Transformation transformation=new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(3).cornerRadius(8).oval(false).build();
                Picasso.get().load(model.getMagLogo()).transform(transformation).into(holder.roundedImagemaglogoparent);
                holder.buylist.setText("Оформить заказ из  <<"+model.getMagName()+">>");

  holder.buylist.setHint(model.getShopUid());




holder.buylist.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent nmk=new Intent(getActivity(),ClientInfoFromZakaz.class);
        nmk.putExtra("Uid",holder.buylist.getHint().toString());
        startActivity(nmk);
    }
});














                FirebaseRecyclerOptions<MartAdapter> options=new FirebaseRecyclerOptions.Builder<MartAdapter>()
                        .setQuery(karzinaRef.child(mAuth.getCurrentUser().getUid()).child(holder.buylist.getHint().toString()).orderByChild("tovarcartShopuid").equalTo(holder.buylist.getHint().toString()),MartAdapter.class).build();
                 adapterTwo=new FirebaseRecyclerAdapter<MartAdapter, ChildHolder>(options) {
                  int soul;

                    @Override


                    protected void onBindViewHolder( @androidx.annotation.NonNull ChildHolder holder, int position,  @androidx.annotation.NonNull MartAdapter model) {

                        holder.textnamechild.setText(model.getTovarname());
holder.textpricechildtovar.setHint(model.getFixprice());
                        holder.tovarpluschildto.setHint(model.getProductId());

                        holder.tovarcartchild.setHint(model.getPrice());
                        holder.textnamechild.setHint(model.getTovarcartShopuid());
                        Transformation transformation=new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(3).cornerRadius(15).oval(false).build();
                        Picasso.get().load(model.getTovarImage()).transform(transformation).into(holder.imagetovarchild);




soul=Integer.valueOf(model.getPrice());

dodo=dodo+soul;
                        Log.d("pr",String.valueOf(dodo));






mRefUserListener=new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
            holder.tovarcartchild.setText(snapshot.child("TovarValue").getValue().toString());
            holder.textpricechildtovar.setText(snapshot.child("Price").getValue().toString()+"₽");
            holder.tovarminuschildto.setHint(snapshot.child("Price").getValue().toString());






        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
};







                        karzinaRef.child(mAuth.getCurrentUser().getUid()).child(holder.textnamechild.getHint().toString()).child(holder.tovarpluschildto.getHint().toString()+mAuth.getCurrentUser().getUid()).addValueEventListener(mRefUserListener);


                        value.put(karzinaRef,mRefUserListener);

























             ///           karzinaRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
          //                  @Override
         //                   public void onDataChange(@NonNull DataSnapshot snapshot) {
        ///                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

//





         ///                       }else {
            //                        lottieAnimationView.setVisibility(View.VISIBLE);
         //                           textViewnocart.setVisibility(View.VISIBLE);
                //                }
           ///                 }

             //               @Override
            //                public void onCancelled(@NonNull DatabaseError error) {
//
            //                }
             ///           });





















                        holder.tovarpluschildto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String  CurrentValue=holder.tovarcartchild.getText().toString();
                                int value=Integer.parseInt(CurrentValue);
                                value++;
                                holder.tovarcartchild.setText(String.valueOf(value));
                                int pricInt = Integer.valueOf(holder.textpricechildtovar.getHint().toString());
                                int valInt = Integer.valueOf(holder.tovarcartchild.getHint().toString());
                                int itogov=valInt+pricInt;
                                dodo=dodo+pricInt;
                                Log.d("pr",String.valueOf(dodo));
String iogovStr=String.valueOf(itogov);
                                HashMap<String,Object>hip=new HashMap<>();
                                hip.put("TovarValue",holder.tovarcartchild.getText().toString());
                                hip.put("Price",iogovStr);
                                karzinaRef.child(mAuth.getCurrentUser().getUid()).child(holder.textnamechild.getHint().toString()).child(holder.tovarpluschildto.getHint().toString()+mAuth.getCurrentUser().getUid()).updateChildren(hip).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {



                                        }else{
                                            String message = task.getException().toString();
                                            Toast.makeText(getContext(), "Ошибка" + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });


                        holder.tovarminuschildto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String  CurrentValue=holder.tovarcartchild.getText().toString();
                                int value=Integer.parseInt(CurrentValue);
                                value--;
                                holder.tovarcartchild.setText(String.valueOf(value));





                                int pricInt = Integer.valueOf(holder.textpricechildtovar.getHint().toString());
                                int valInt = Integer.valueOf(holder.tovarcartchild.getHint().toString());
                                int itogov=valInt-pricInt;
dodo=dodo-pricInt;
                                String iogovStr=String.valueOf(itogov);

                                Log.d("pr",String.valueOf(dodo));

                                HashMap<String,Object>hip=new HashMap<>();
                                hip.put("TovarValue",holder.tovarcartchild.getText().toString());
                                hip.put("Price",iogovStr);
                                karzinaRef.child(mAuth.getCurrentUser().getUid()).child(holder.textnamechild.getHint().toString()).child(holder.tovarpluschildto.getHint().toString()+mAuth.getCurrentUser().getUid()).updateChildren(hip).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }else{
                                            String message = task.getException().toString();
                                            Toast.makeText(getContext(), "Ошибка" + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });







                    }

                    @Override
                    public ChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item,parent,false);
                        ChildHolder holder=new ChildHolder(view);


                        return holder;
                    }
                };
                holder.nestedrecer.setAdapter(adapterTwo);
                adapterTwo.startListening();



















            }

            @Override
            public ParentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
                ParentHolder holder=new ParentHolder(view);


                return holder;
            }
        };
        reccart.setAdapter(adapters);
        adapters.startListening();




















































        return cart;

    }

    @Override
    public void onPause() {
        super.onPause();
        for (Map.Entry<DatabaseReference,ValueEventListener> entry:value.entrySet()){
            entry.getKey().removeEventListener(entry.getValue());
adapters.stopListening();
adapterTwo.stopListening();
        }
    }
}