package edu.rosehulman.favoritethings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mColorTextView;
    private TextView mNumberTextView;
    private long mNumber;
    private static final String TAG = "FAVES";
    private DatabaseReference mRef;
    private Boolean updateOnOff;
    private ValueEventListener mColorListener;
    private DatabaseReference mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorTextView = (TextView) findViewById(R.id.color_text_view);
        mNumberTextView = (TextView) findViewById(R.id.number_text_view);

        findViewById(R.id.red_button).setOnClickListener(this);
        findViewById(R.id.white_button).setOnClickListener(this);
        findViewById(R.id.blue_button).setOnClickListener(this);
        findViewById(R.id.update_color_button).setOnClickListener(this);
        findViewById(R.id.increment_number_button).setOnClickListener(this);
        findViewById(R.id.decrement_number_button).setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNumber = (Long) dataSnapshot.getValue();
                mNumberTextView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        generateChildListener();
        updateOnOff = false;
    }

    private void generateChildListener() {
        mColor = mRef.child("color");
        mColorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mColorTextView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.red_button:
                mRef.child("color").setValue("red");
                return;
            case R.id.white_button:
                mRef.child("color").setValue("white");
                return;
            case R.id.blue_button:
                mRef.child("color").setValue("blue");
                return;
            case R.id.update_color_button:
                Log.d(TAG, "Updating from Firebase");
                Button button = (Button) view;
                if (!updateOnOff) {
                    updateOnOff = true;
                    button.setPressed(true);
                    button.setTextColor(Color.BLUE);
                    mColor.addValueEventListener(mColorListener);
                    return;
                } else {
                    button.setPressed(false);
                    button.setTextColor(Color.BLACK);
                    updateOnOff = false;
                    mColor.removeEventListener(mColorListener);
                    return;
                }
            case R.id.increment_number_button:
                mNumber++;
                mNumberTextView.setText("" + mNumber);
                mRef.child("number").setValue(mNumber);
                return;
            case R.id.decrement_number_button:
                mNumber--;
                mRef.child("number").setValue(mNumber);
                mNumberTextView.setText("" + mNumber);
                return;

        }
    }
}
