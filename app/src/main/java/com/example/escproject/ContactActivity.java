package com.example.escproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ContactActivity extends AppCompatActivity {

    //변수 선언
    private Toolbar toolbar;
    private ImageView avatar;
    private TextView name;
    private TextView phone;
    private TextView emil;
    private ImageButton prev;
    private TextView page;
    private ImageButton next;
    //현재 페이지
    private int currentPage = 0;

    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setUpUI();

        setSupportActionBar(toolbar);//툴바를 액션바로
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기

        setInfo(currentPage);

    }

    private void setUpUI() {
        toolbar=findViewById(R.id.contact_toolbar);
        avatar=findViewById(R.id.contact_iv_avatar);
        name=findViewById(R.id.contact_tv_name);
        phone=findViewById(R.id.contact_tv_phone);
        emil=findViewById(R.id.contact_tv_email);
        prev=findViewById(R.id.contact_ibtn_prev);
        page=findViewById(R.id.contact_tv_page);
        next=findViewById(R.id.contact_ibtn_next);

        //prev 눌렀을 때
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                setInfo(currentPage);
            }
        });

        //next 눌렀을 때
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                setInfo(currentPage);
            }
        });
    }

    private void setInfo(int index){
        name.setText(DummyData.contacts.get(index).getName());
        phone.setText(DummyData.contacts.get(index).getPhone());
        emil.setText(DummyData.contacts.get(index).getEmail());

        if(index == 0){
            prev.setVisibility(View.GONE);
        }
        else {
            prev.setVisibility(View.VISIBLE);
        }

        if(index == DummyData.contacts.size()-1){
            next.setVisibility(View.GONE);
        }
        else{
            next.setVisibility(View.VISIBLE);
        }

        page.setText((index+1) + "/" + DummyData.contacts.size());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_contact_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                startActivity(callIntent);
                break;
            case R.id.menu_contact_edit:
                // TODO: 수정
            case R.id.menu_contact_message:
                Intent messageIntent = new Intent(ContactActivity.this, MessageActivity.class);
                messageIntent.putExtra("phone_num",phone.getText().toString()); // 화면이 넘어갈때 번호 넘기기
                startActivity(messageIntent);
                break;
            case R.id.menu_contact_delete:
                DummyData.contacts.remove(currentPage);

                //새로고침
                //연락처가 더이상 없을 때
                if(DummyData.contacts.size() == 0){
                    Toast.makeText(this, "연락처가 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    //첫번째 페이지를 지울 때 예외처리
                    if (currentPage != 0) {
                        currentPage--;
                    }
                    setInfo(currentPage);
                }




        }
        return super.onOptionsItemSelected(item);
    }
}