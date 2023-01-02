package mychati.app.RegisterAndProverka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mychati.app.CategUserActivity;
import mychati.app.HelloActivity.HelloClientActivity;
import mychati.app.HelloActivity.HelloDayActivity;
import mychati.app.HelloActivity.HelloVecherActivity;
import mychati.app.R;


public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference shop;


    private String name;
    private DatabaseReference clientse;
    private HashMap<DatabaseReference, ValueEventListener> listeners = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();


        shop = FirebaseDatabase.getInstance().getReference().child("shops");
        clientse = FirebaseDatabase.getInstance().getReference().child("client");
        currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            openShop();


        } else {
            startActivity(new Intent(SplashScreenActivity.this, RegisterPhoneActivity.class));
            finish();
        }
    }

    private void openShop() {

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    name = snapshot.child("MagName").getValue().toString();
                    openActivity(name, "1");

                } else {
                    openClient();
                    Toast.makeText(SplashScreenActivity.this, "Не курьер", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference reference = shop.child(currentUser.getUid());
        listeners.put(reference, listener);
        reference.addValueEventListener(listener);


    }

    private void openActivity(String name, String value) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        PartOfTheDay part = getPartOfDayByHours(hour);
        Class<?> destination = getDestinationClassByPartOfTheDay(part);

        if (destination == null) return;

        Intent intent = new Intent(
                SplashScreenActivity.this,
                destination);
        intent.putExtra("name", name);
        intent.putExtra("ident", value);

        startActivity(intent);
        finish();
    }

    enum PartOfTheDay {
        MORNING,
        AFTERNOON,
        EVENING,
        NIGHT

    }

    private PartOfTheDay getPartOfDayByHours(int hour) {
        if (hour >= 6 && hour < 12) {
            return PartOfTheDay.MORNING;
        }
        if (hour >= 12 && hour < 16) {
            return PartOfTheDay.AFTERNOON;
        }
        if (hour >= 16 && hour < 24) {
            return PartOfTheDay.EVENING;
        }
        if (hour >= 0 && hour < 6) {
            return PartOfTheDay.NIGHT;
        }
        throw new IllegalStateException("Unknown hour:" + hour);
    }

    private Class<?> getDestinationClassByPartOfTheDay(PartOfTheDay partOfTheDay) {
        switch (partOfTheDay) {
            case MORNING:
                return HelloClientActivity.class;
            case AFTERNOON:
                return HelloDayActivity.class;
            case EVENING:
                return HelloVecherActivity.class;
            case NIGHT:
                return null;
        }
        throw new IllegalStateException("Unknown part of day");
    }

    private void openClient() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    name = snapshot.child("clientName").getValue().toString();
                    Log.d("name", name);
                    openActivity(name, "2");

                } else {
                    startActivity(new Intent(SplashScreenActivity.this, CategUserActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference reference = clientse.child(currentUser.getUid());
        listeners.put(reference, listener);
        reference.addValueEventListener(listener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        for(Map.Entry<DatabaseReference, ValueEventListener> entry: listeners.entrySet()){
            entry.getKey().removeEventListener(entry.getValue());
        }
    }
}