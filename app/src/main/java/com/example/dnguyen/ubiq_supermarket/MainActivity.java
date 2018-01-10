package com.example.dnguyen.ubiq_supermarket;

import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mPublish;
    String[] listItems;
    String chosenItem;
    RabbitMQConnector rabbitConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        rabbitConn = new RabbitMQConnector();
        mPublish = (Button) findViewById(R.id.btnPublish);
        listItems = getResources().getStringArray(R.array.publish_item);

        ListView view = (ListView)findViewById(R.id.offerListView);
        view.setEmptyView(findViewById(R.id.empty_list_item));

        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Items available");
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position){
                        chosenItem = listItems[position];
                    }
                });
                final EditText offerTextField = new EditText(MainActivity.this);
                offerTextField.setHint("Write your offer here...");
                mBuilder.setView(offerTextField);
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Publish offer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String offerContent = offerTextField.getText().toString();
                        rabbitConn.publishMessage("offers."+ chosenItem + ".*", chosenItem + ": " + offerContent);
                    }
                });
                mBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }
}
