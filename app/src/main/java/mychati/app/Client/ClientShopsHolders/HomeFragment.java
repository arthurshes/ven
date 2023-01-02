package mychati.app.Client.ClientShopsHolders;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mychati.app.Client.ClientShopsModel.ShopAdapter;
import mychati.app.Holders.ChildHolders.GlavChildHolder;
import mychati.app.R;


public class HomeFragment extends Fragment {
    private DatabaseReference proverka;
    private DatabaseReference otzyv;
    private DatabaseReference prover;
private int testintTwo;
    private TextView textesti;
    private Double itog;
    private Double itogt;
    private Double otztvValues;
    private Double otztvValuestwo;
    private DatabaseReference otzest;
    private int testint;

    private String saveCurrentDate, saveCurrentTime, ProductRandomKey;
    private DatabaseReference myname;
    private FirebaseAuth mAuth;
    private RecyclerView rectwohome;
    private FirebaseRecyclerAdapter<ShopAdapter, ClientShopHolder> magAdapter;
    private FirebaseRecyclerAdapter<ShopAdapter, GlavChildHolder> glavAdapter;
    private DatabaseReference shops;
    private RecyclerView recAptek;
    private DatabaseReference shopipht;
    private ValueEventListener refTwoRaw;
    private Dialog dialog;
    private final HashMap<Query, ValueEventListener> listenersTwo = new HashMap<>();
    private final HashMap<Query, ValueEventListener> listeners = new HashMap<>();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        otzyv = reference.child("otzyv");
        myname = reference.child("client");
        proverka = reference.child("oformzakaz");

        dialog = new Dialog(getContext());
        shops = reference.child("shops");

        otzest = reference.child("otzyv");
        recAptek = view.findViewById(R.id.recAptek);
        prover = reference.child("otzyv");
        recAptek.setLayoutManager(new LinearLayoutManager(this.getContext()));
        textesti = view.findViewById(R.id.textesti);

        mAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        shopipht = reference.child("DoCart");

        rectwohome = view.findViewById(R.id.rectwohome);
        rectwohome.setHasFixedSize(true);
        rectwohome.setLayoutManager(
                new LinearLayoutManager(this.getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));


        ValueEventListener mRefUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        ShopAdapter shopAdapter = ds.getValue(ShopAdapter.class);
                        assert shopAdapter != null;

                        Log.d("jipe", shopAdapter.getProductId());

                        dialog.setContentView(R.layout.layot_otzyvov);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        AppCompatButton appCompatButton = dialog.findViewById(R.id.button_otzyv_otprav);
                        RatingBar ratingBar = dialog.findViewById(R.id.ratingBarotzyvdia);
                        TextView texrstyle = dialog.findViewById(R.id.textstatusOtzyv);
                        TextView zavershitzakaz = dialog.findViewById(R.id.zavershitzakaz);
                        EditText opisanieotzyv = dialog.findViewById(R.id.opisanieotzyv);
                        TextView textViewzaverh = dialog.findViewById(R.id.textViewzaverh);

                        textViewzaverh.setHint(shopAdapter.getShopId());
                        zavershitzakaz.setHint(shopAdapter.getProductId());

                        refTwoRaw = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                                    textViewzaverh.setText(shopAdapter.getZaverName() +
                                            " завершил заказ. "
                                            + snapshot.child("clientName").getValue().toString()
                                            + ",оцените нас");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        };

                        DatabaseReference child = myname.child(uid);
                        child.addValueEventListener(refTwoRaw);
                        listeners.put(child, refTwoRaw);
                        zavershitzakaz.setOnClickListener(view1 -> {
                            shopipht.child(uid).removeValue();
                            proverka.child(uid + shopAdapter.getShopUid()).removeValue();
                        });

                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                String text = "";
                                if (v < 1) {
                                    text = "Отвратительно";
                                } else if (v < 2) {
                                    text = "Плохо";
                                } else if (v < 3) {
                                    text = "Cредне";
                                } else if (v < 4) {
                                    text = "Нормально";
                                } else if (v < 5) {
                                    text = "Хорошо";
                                } else if (v < 6) {
                                    text = "Отлично";
                                }
                                texrstyle.setText(text);
                                texrstyle.setHint(String.valueOf(v));

                            }
                        });
                        dialog.show();

                        appCompatButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (TextUtils.isEmpty(opisanieotzyv.getText().toString())) {
                                    Toast.makeText(getContext(), "Опишите ваше мнение ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
                                    saveCurrentDate = currentDate.format(calendar.getTime());

                                    SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
                                    saveCurrentTime = currentTime.format(calendar.getTime());

                                    ProductRandomKey = saveCurrentDate + saveCurrentTime;

                                    HashMap<String, Object> men = new HashMap<>();
                                    men.put("Value", texrstyle.getHint().toString());
                                    men.put("ShopUid", shopAdapter.getShopUid());
                                    men.put("text", opisanieotzyv.getText().toString());
                                    otzyv.child(shopAdapter.getShopId()).child(uid + ProductRandomKey).updateChildren(men).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Отзыв отправлен,спасибо", Toast.LENGTH_SHORT).show();

                                                proverka.child(uid + shopAdapter.getProductId() + shopAdapter.getShopId()).removeValue();
                                                dialog.dismiss();
                                            } else {

                                                String message = task.getException().toString();
                                                Toast.makeText(getContext(), "Ошибка" + message, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Query query = proverka.orderByChild("Zakazstatus").equalTo(uid);
        query.addValueEventListener(mRefUserListener);
        listeners.put(query, mRefUserListener);

    }

    @Override
    public void onResume() {
        super.onResume();


        FirebaseRecyclerOptions<ShopAdapter> options = new FirebaseRecyclerOptions.Builder<ShopAdapter>()
                .setQuery(shops, ShopAdapter.class).build();
        glavAdapter = new FirebaseRecyclerAdapter<ShopAdapter, GlavChildHolder>(options) {
            @Override
            protected void onBindViewHolder(@androidx.annotation.NonNull GlavChildHolder holder, int position, @androidx.annotation.NonNull ShopAdapter model) {
                textesti.setVisibility(View.VISIBLE);
                holder.glavmagname.setText(model.getMagName());
holder.glavmagname.setHint(model.getMagUid());





















                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        testintTwo = Integer.valueOf("" + snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                };
                DatabaseReference child = prover.child(holder.glavmagname.getHint().toString());
                child.addListenerForSingleValueEvent(listener);
                listenersTwo.put(child, listener);




                ValueEventListener list2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            double diablot = 0.0;

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ShopAdapter shopAdapter = ds.getValue(ShopAdapter.class);
                                assert shopAdapter != null;
                                String string = shopAdapter.getValue();
                                otztvValuestwo = Double.parseDouble(string);

                                diablot = diablot + otztvValuestwo;
                                itogt = diablot / testint;

                                holder.rateyellow1g.setVisibility(getRateVisibility(itogt >= 0.5));
                                holder.rateyellow2g.setVisibility(getRateVisibility(itogt >= 1.5));
                                holder.rateyellow3g.setVisibility(getRateVisibility(itogt >= 2.5));
                                holder.rateyellow4g.setVisibility(getRateVisibility(itogt >= 3.5));
                                holder.rateyellow5g.setVisibility(getRateVisibility(itogt >= 4.5));



                                holder.textstarg.setText(String.valueOf(itogt));

                            }
                        }
                    }

                    private int getRateVisibility(boolean b) {
                        return b ? View.VISIBLE : View.INVISIBLE;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };


                DatabaseReference child1 = otzest.child(holder.glavmagname.getHint().toString());
                child1.addValueEventListener(list2);

                listenersTwo.put(child1, list2);













                Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(3).cornerRadius(15).oval(false).build();
                Picasso.get().load(model.getMagLogo()).transform(transformation).into(holder.imageGlavMag);


            }

            @Override
            public GlavChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_glavmag, parent, false);
                GlavChildHolder holder = new GlavChildHolder(view);


                return holder;
            }
        };
        rectwohome.setAdapter(glavAdapter);


        FirebaseRecyclerOptions<ShopAdapter> Options = new FirebaseRecyclerOptions.Builder<ShopAdapter>()
                .setQuery(shops, ShopAdapter.class).build();
        magAdapter = new FirebaseRecyclerAdapter<ShopAdapter, ClientShopHolder>(Options) {
            @Override
            protected void onBindViewHolder(@androidx.annotation.NonNull ClientShopHolder holder, int position, @androidx.annotation.NonNull ShopAdapter model) {

                Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(3).cornerRadius(7).oval(false).build();
                Picasso.get().load(model.getMagLogo()).transform(transformation).into(holder.imageLogoApteka);


                holder.aotekaname.setText(model.getMagName());

                holder.aotekaname.setHint(model.getMagUid());

                Log.d("Окси", holder.aotekaname.getHint().toString());


                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        testint = Integer.valueOf("" + snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                };
                DatabaseReference child = prover.child(holder.aotekaname.getHint().toString());
                child.addListenerForSingleValueEvent(listener);
                listeners.put(child, listener);


                ValueEventListener list1 = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            double diablo = 0.0;

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ShopAdapter shopAdapter = ds.getValue(ShopAdapter.class);
                                assert shopAdapter != null;
                                String string = shopAdapter.getValue();
                                otztvValues = Double.parseDouble(string);

                                diablo = diablo + otztvValues;
                                itog = diablo / testint;

                                holder.rateyellow1.setVisibility(getRateVisibility(itog >= 0.5));
                                holder.rateyellow2.setVisibility(getRateVisibility(itog >= 1.5));
                                holder.rateyellow3.setVisibility(getRateVisibility(itog >= 2.5));
                                holder.rateyellow4.setVisibility(getRateVisibility(itog >= 3.5));
                                holder.rateyellow5.setVisibility(getRateVisibility(itog >= 4.5));


                                String result=String.format("%.3f",itog);

holder.textstar.setText(result);
                            }
                        }
                    }

                    private int getRateVisibility(boolean b) {
                        return b ? View.VISIBLE : View.INVISIBLE;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };


                DatabaseReference child1 = otzest.child(holder.aotekaname.getHint().toString());
                child1.addValueEventListener(list1);

                listeners.put(child1, list1);


            }

            @Override
            public ClientShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apteka
                        , parent, false);
                return new ClientShopHolder(view);
            }
        };
        recAptek.setAdapter(magAdapter);


        magAdapter.startListening();
        glavAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        for (Map.Entry<Query, ValueEventListener> entry : listeners.entrySet()) {
            entry.getKey().removeEventListener(entry.getValue());
        }

        for (Map.Entry<Query, ValueEventListener> entry : listenersTwo.entrySet()) {
            entry.getKey().removeEventListener(entry.getValue());
        }













        magAdapter.stopListening();
        glavAdapter.stopListening();

    }


}