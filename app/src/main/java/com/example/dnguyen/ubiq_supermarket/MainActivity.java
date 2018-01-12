package com.example.dnguyen.ubiq_supermarket;

import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button mPublish;
    String[] listItems;
    String chosenItem;
    RabbitMQConnector rabbitConn;
    HashMap<String, Product> prodMap;
    ArrayList<String> offerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        rabbitConn = new RabbitMQConnector();
        mPublish = (Button) findViewById(R.id.btnPublish);

        offerItems = new ArrayList<>();

        prodMap = new HashMap<>();
        prodMap.put("Mascara", new Product("Mascara", "4.99 Euro", "Beauty"));
        prodMap.put("Banana", new Product("Banana", "1.99 Euro/Kg", "Fruits"));
        prodMap.put("Chicken", new Product("Chicken", "3.99 Euro", "Meat"));
        prodMap.put("Beer", new Product("Beer", "0.99 Euro", "Alcohol"));
        prodMap.put("Cola", new Product("Cola", "0.49 Euro", "Soft Drink"));
        listItems = prodMap.keySet().toArray(new String[prodMap.size()]);

        ListView view = (ListView)findViewById(R.id.offerListView);
        view.setEmptyView(findViewById(R.id.empty_list_item));

        final List< String > listElementsArrayList = new ArrayList<String>();
        final ArrayAdapter < String > adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listElementsArrayList);
        view.setAdapter(adapter);

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
                        Date now = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
                        listElementsArrayList.add("[" + ft.format(now) + "] " + chosenItem + ": " + offerContent);
                        offerItems.add(chosenItem);
                        adapter.notifyDataSetChanged();
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
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Product currentSelectedProd = prodMap.get(offerItems.get(position));
                Snackbar.make(view, currentSelectedProd.getProduct_type()+" - "+currentSelectedProd.getName()+" current price: "+currentSelectedProd.getPrice(), Snackbar.LENGTH_LONG).setAction("No action", null).show();;
            }
        });
    }
}
