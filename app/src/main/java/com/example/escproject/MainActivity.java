package com.example.escproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    //멤버 변수
    private ImageButton addContact;
    private ImageButton contact;
    private TextView phoneNum;
    private TextView[] dials = new TextView[10];
    private TextView star;
    private TextView sharp;
    private ImageButton message;
    private ImageButton call;
    private ImageButton backspace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();

    }

    //시작할 때 화면
    private void setUpUI() {
        //변수와 레이아웃 연결
        addContact = findViewById(R.id.main_ibtn_add);
        contact = findViewById(R.id.main_ibtn_contact);
        phoneNum = findViewById(R.id.main_tv_phone);

        for (int i = 0; i < dials.length; i++) {
            dials[i] = findViewById(getResourceID("main_tv_" + i, "id", this));
        }

        star = findViewById(R.id.main_tv_star);
        sharp = findViewById(R.id.main_tv_sharp);
        message = findViewById(R.id.main_ibtn_message);
        call = findViewById(R.id.main_ibtn_call);
        backspace = findViewById(R.id.main_ibtn_backspace);

        //이벤트 설정
        addContact.setOnClickListener(new View.OnClickListener() {//addContact 저장
            @Override
            public void onClick(View v) {
                // TODO: 연락처 추가
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {//contact 연락처
            @Override
            public void onClick(View v) {
                // TODO: 연락처
            }
        });

        //dial 눌렀을 때 이벤트
        setOnClickDial(star, "*");
        setOnClickDial(sharp,"#");

        for(int i = 0; i<10; i++){
            setOnClickDial(dials[i], String.valueOf(i));
        }

        //message 눌렀을 때
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:메세지
            }
        });

        //call 눌렀을 때
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 전화
            }
        });

        //backspace 눌렀을 때
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (phoneNum.getText().length() > 0) { // 아무것도 없을 때 예외처리
//                    String formatPhoneNum = PhoneNumberUtils.formatNumber(phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString(),
//                            Locale.getDefault().getCountry()); //숫자 하나 지우기, 하이픈 자동 입력
//                    phoneNum.setText(formatPhoneNum);//기존번호 + 입력한것

                phoneNum.setText(changeToDial(phoneNum.getText().subSequence(0,phoneNum.getText().length()-1).toString())); //숫자 하나 지우기
//
                }

        });


        //backspace 길게 눌렀을 때
        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                phoneNum.setText("");
                backspace.setVisibility(View.GONE);
                return true;
            }
        });


    }


    //dial 눌렀을 때 이벤트를 위한 함수
    private void setOnClickDial(View view, final String input){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String formatPhoneNum = PhoneNumberUtils.formatNumber(phoneNum.getText() + input, Locale.getDefault().getCountry()); //하이픈 자동 입력
                //삼성 폰은 가능하지만 애뮬은 국가 지정이안되어있어 오류가 생김
                //phoneNum.setText(formatPhoneNum);//기존번호 + 입력한것

                phoneNum.setText(changeToDial(phoneNum.getText() + input));
            }
        });
    }

    //dial id 호출을 위한 함수
    private int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }

    //하이픈 입력되는 함수
    private String changeToDial(String phoneNum){
        //전화번호 기준
        //4글자 이상일 때 3번째 숫자 다음에 -  (010-3
        // 8글자 이상일 때 3번째 다음과 7번째 다음에 - (010-3744-0
        //12글자 이상이면 - 전부 제거
        //특수문자 * # 있으면 - 전부 제거



        phoneNum = phoneNum.replaceAll("-","");
        if (phoneNum.length() >= 4 && phoneNum.length() <=7) {//010539
            phoneNum = phoneNum.substring(0,3) + "-" + phoneNum.substring(3);
        } else if (phoneNum.length() >= 8 && phoneNum.length() <= 11 ) {
            phoneNum = phoneNum.substring(0,3) + "-" +phoneNum.substring(3,7 )+ "-" + phoneNum.substring(7);
        } else if (phoneNum.length() > 11) {
            phoneNum = phoneNum.replaceAll("-","");

        }

        if(phoneNum.contains("*") || phoneNum.contains("#") ||phoneNum.contains("+")){
            phoneNum = phoneNum.replaceAll("-","");

        }

        return phoneNum;

    }


}
