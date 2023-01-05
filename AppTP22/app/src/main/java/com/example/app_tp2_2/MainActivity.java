package com.example.app_tp2_2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.app_tp2_2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_CONTACT=1;
    private static final int CALL_Perm=1;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button buttonChooseContact = findViewById(R.id.button);
        buttonChooseContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,1);

            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver cr = getContentResolver();
                    Cursor cur = cr.query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {// thats mean some resutl has been found
                        if(cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            TextView textView = findViewById(R.id.textView3);
                            textView.setText(name);
                            Log.e("Names", name);
                            Button button2 = findViewById(R.id.button2);
                            button2.setEnabled(true);
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {
                                // Query phone here. Covered next
                                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                                while (phones.moveToNext()) {
                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    Log.e("Number", phoneNumber);
                                    button2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                           textView.setText(name + phoneNumber);
                                            Button button33 = findViewById(R.id.button3);
                                            button33.setEnabled(true);

                                        }
                                    });

                                }
                                phones.close();
                            }

                        }
                    }
                    else if (resultCode == Activity.RESULT_CANCELED) {
                        // Si l'utilisateur annule le choix de contact, affichage du message "opération annulée"
                        TextView textView = findViewById(R.id.textView3);
                        textView.setText("Opération annulée");
                    }
                    cur.close();
                }
                break;
        }

    }

}