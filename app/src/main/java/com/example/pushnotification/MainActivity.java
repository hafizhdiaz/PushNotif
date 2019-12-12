package com.example.pushnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private EditText editTextNik, editTextNama, editTextAlamat, editTextHandphone ;
    private String nik, nama, alamat, handphone;
    private DatabaseReference mDatabase;

    private NotificationManager mNotificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // define the textview
        editTextNik = findViewById(R.id.edit_text_nis);
        editTextNama = findViewById(R.id.edit_text_nama);
        editTextAlamat = findViewById(R.id.edit_text_alamat);
        editTextHandphone = findViewById(R.id.edit_text_handphone);
    }

    public void onSubmit(View view) {
        nik= editTextNik.getText().toString();
        nama = editTextNama.getText().toString();
        alamat = editTextAlamat.getText().toString();
        handphone = editTextHandphone.getText().toString();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("SATU DATA") // title for notification
                .setContentText("Biodata Berhasil Ditambahkan!")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());

        DatabaseReference newData = mDatabase.child("data");
        newData.child("alamat").setValue(alamat);
        newData.child("handphone").setValue(handphone);
        newData.child("nama").setValue(nama);
        newData.child("nik").setValue(nik).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, mBuilder.build());

            }
        });
    }

}
