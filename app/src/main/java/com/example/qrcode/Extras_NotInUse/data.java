package com.example.qrcode.Extras_NotInUse;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qrcode.Extras_NotInUse.Message;
import com.example.qrcode.Extras_NotInUse.myDbAdapter;
import com.example.qrcode.R;

import androidx.appcompat.app.AppCompatActivity;

public class data extends AppCompatActivity {

    EditText Name, Pass , updateold, updatenew, delete;
    myDbAdapter helper;
    TextView textView;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataeditview);
        Name= findViewById(R.id.editName);
        Pass= findViewById(R.id.editPass);
        delete = findViewById(R.id.editText6);

        helper = new myDbAdapter(this);
    }

    public void addUser(View view)
    {
        String t1 = Name.getText().toString();
        String t2 = Pass.getText().toString();
        if(t1.isEmpty() || t2.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Both Name and Password");
        }
        else
        {
            long id = helper.insertData(t1,t2);
            if(id<=0)
            {
                Message.message(getApplicationContext(),"Insertion Unsuccessful");
                Name.setText("");
                Pass.setText("");
            } else
            {
                Message.message(getApplicationContext(),"Insertion Successful");
                Name.setText("");
                Pass.setText("");
            }
        }
    }

    public void viewdata(View view)
    {

                setContentView(R.layout.scannedresult);
                textView=findViewById(R.id.scannedResultName);
                String data = helper.getData();
                textView.setText(data);


    }

    public void verify(View view)
    {

        setContentView(R.layout.scannedresult);
        textView=findViewById(R.id.scannedResultName);
        String data = helper.verify("3476");
        textView.setText(data);


    }

    public void update( View view)
    {
        String u1 = updateold.getText().toString();
        String u2 = updatenew.getText().toString();
        if(u1.isEmpty() || u2.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Data");
        }
        else
        {
            int a= helper.updateName( u1, u2);
            if(a<=0)
            {
                Message.message(getApplicationContext(),"Unsuccessful");
                updateold.setText("");
                updatenew.setText("");
            } else {
                Message.message(getApplicationContext(),"Updated");
                updateold.setText("");
                updatenew.setText("");
            }
        }

    }

    public void delete( View view)
    {
        String uname = delete.getText().toString();
        if(uname.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Data");
        }
        else{
            int a= helper.delete(uname);
            if(a<=0)
            {
                Message.message(getApplicationContext(),"Unsuccessful");
                delete.setText("");
            }
            else
            {
                Message.message(this, "DELETED");
                delete.setText("");
            }
        }
    }
}

