package com.goldenkey.quoties;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class selectTopicActivity extends AppCompatActivity {
    ArrayList<Interest> interests = new ArrayList<>();
    @BindView(R.id.item)
    Button item;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;
    boolean b6 = false;
    boolean b7 = false;
    boolean b8 = false;
    boolean b9 = false;
    boolean b10 = false;
    boolean b11 = false;
    boolean b12 = false;
    boolean b13 = false;
    boolean b14 = false;
    boolean b15 = false;
    boolean b16 = false;
    boolean b17 = false;
    boolean b18 = false;
    boolean b19 = false;
    boolean b20 = false;
    boolean b21 = false;
    boolean b22 = false;
    boolean b23 = false;
    @BindView(R.id.item29)
    Button item29;
    @BindView(R.id.item19)
    Button item19;
    @BindView(R.id.item20)
    Button item20;
    @BindView(R.id.item21)
    Button item21;
    @BindView(R.id.item22)
    Button item22;
    @BindView(R.id.item32)
    Button item32;
    @BindView(R.id.item31)
    Button item31;
    @BindView(R.id.item30)
    Button item30;
    @BindView(R.id.item24)
    Button item24;
    @BindView(R.id.item25)
    Button item25;
    @BindView(R.id.item26)
    Button item26;
    @BindView(R.id.item28)
    Button item28;
    @BindView(R.id.item34)
    Button item34;
    @BindView(R.id.item33)
    Button item33;
    @BindView(R.id.item1)
    Button item1;
    @BindView(R.id.item10)
    Button item10;
    @BindView(R.id.item18)
    Button item18;
    @BindView(R.id.item17)
    Button item17;
    @BindView(R.id.item16)
    Button item16;
    @BindView(R.id.item15)
    Button item15;
    @BindView(R.id.item13)
    Button item13;
    @BindView(R.id.item11)
    Button item11;
    @BindView(R.id.next)
    Button next;
    String DisplayName;
    String Bio;
    String Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        DisplayName = intent.getExtras().getString("DisplayName");
        Bio = intent.getExtras().getString("Bio");
        Gender = intent.getExtras().getString("Gender");
    }

    @OnClick({R.id.item, R.id.item29, R.id.item19, R.id.item20, R.id.item21, R.id.item22, R.id.item32, R.id.item31, R.id.item30, R.id.item24, R.id.item25, R.id.item26, R.id.item28, R.id.item34, R.id.item33, R.id.item1, R.id.item10, R.id.item18, R.id.item17, R.id.item16, R.id.item15, R.id.item13, R.id.item11})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item:
                setcolor(b1, this.item);
                b1 = !b1;
                break;
            case R.id.item29:
                setcolor(b2, this.item29);
                b2 = !b2;
                break;
            case R.id.item19:

                setcolor(b3, this.item19);
                b3 = !b3;
                break;
            case R.id.item20:
                setcolor(b4, this.item20);
                b4 = !b4;
                break;
            case R.id.item21:
                setcolor(b5, this.item21);
                b5 = !b5;
                break;
            case R.id.item22:
                setcolor(b6, this.item22);
                b6 = !b6;
                break;
            case R.id.item32:
                setcolor(b7, this.item32);
                b7 = !b7;
                break;
            case R.id.item31:
                setcolor(b8, this.item31);
                b8 = !b8;
                break;
            case R.id.item30:
                setcolor(b9, this.item30);
                b9 = !b9;
                break;
            case R.id.item24:
                setcolor(b10, this.item24);
                b10 = !b10;
                break;
            case R.id.item25:
                setcolor(b11, this.item25);
                b11 = !b11;
                break;
            case R.id.item26:
                setcolor(b12, this.item26);
                b12 = !b12;
                break;
            case R.id.item28:
                setcolor(b13, this.item28);
                b13 = !b13;
                break;
            case R.id.item34:
                setcolor(b14, this.item34);
                b14 = !b14;
                break;
            case R.id.item33:
                setcolor(b16, this.item33);
                b16 = !b16;
                break;
            case R.id.item1:
                setcolor(b15, this.item1);
                b15 = !b15;
                break;
            case R.id.item10:
                setcolor(b17, this.item10);
                b17 = !b17;
                break;
            case R.id.item18:
                setcolor(b18, this.item18);
                b18 = !b18;
                break;
            case R.id.item17:
                setcolor(b19, this.item17);
                b19 = !b19;
                break;
            case R.id.item16:
                setcolor(b20, this.item16);
                b20 = !b20;
                break;
            case R.id.item15:
                setcolor(b21, this.item15);
                b21 = !b21;
                break;
            case R.id.item13:
                setcolor(b22, this.item13);
                b22 = !b22;
                break;
            case R.id.item11:
                setcolor(b23, this.item11);
                b23 = !b23;
                break;
        }
    }

    public void setcolor(boolean b, Button button) {
        String text = button.getText().toString();
        text = text.substring(0, text.length() - 1);
        if (b) {
            button.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.rounded_btn));
            button.setText(text + "✚");
            for(int i = 0; i < interests.size(); i++) {
                if(interests.get(i).name.equals(text)) {
                    interests.remove(i);

                }
            }
        } else {
            button.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.pink_rounded_bitton));
            button.setText(text + "✓");
            interests.add(new Interest(text));

        }
    }

    @OnClick(R.id.next)
    public void onViewClicked() {
        User s = new User(DisplayName, Gender, Bio, FirebaseAuth.getInstance().getCurrentUser().getUid(), interests, new ArrayList<Quote>());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(s);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
