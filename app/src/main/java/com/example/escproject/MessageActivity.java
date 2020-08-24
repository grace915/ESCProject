package com.example.escproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MessageActivity extends AppCompatActivity {

    //멤버 변수
    private Toolbar toolbar;
    private EditText phoneNum;
    private EditText content;
    private FloatingActionButton send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setUpUI();

        //toolbar를 actionbar로 설정
        setSupportActionBar(toolbar);
        //actionbar에 뒤로가기 버튼 보이게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //MainActivity에서 넘긴 번호 정보 받기
        String getPhoneNum = getIntent().getStringExtra("phone_num");
        //phoneNum에 받은 번호 넣기
         phoneNum.setText(getPhoneNum);
         //번호 '-'나오게 하기
        phoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //애뮬 지원안됨


    }

    private void setUpUI(){
        //연력
        toolbar = findViewById(R.id.message_toolbar);
        phoneNum = findViewById(R.id.message_et_phone);
        content = findViewById(R.id.message_et_content);
        send = findViewById(R.id.message_fab_send);

        //send 버튼 눌렀을 때
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum.getText().toString(), null, content.getText().toString(), null, null);
                    finish();
                    Toast.makeText(MessageActivity.this, "success",Toast.LENGTH_SHORT).show();
                }catch (Exception e){ //예외처리
                    Toast.makeText(MessageActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //뒤로가기 버튼 눌렀을 때 뒤로가기
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //툴바의 메뉴를 눌러을 때 기능정하기
        switch (item.getItemId()){
            case android.R.id.home ://뒤로가기
                finish(); //MessageActivity 꺼지기
        }
        return super.onOptionsItemSelected(item);
    }
}