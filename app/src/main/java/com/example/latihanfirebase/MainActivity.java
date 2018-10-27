package com.example.latihanfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.latihanfirebase.adapter.FotoAdapter;
import com.example.latihanfirebase.fragment.InputFoto;
import com.example.latihanfirebase.fragment.Pilihan;
import com.example.latihanfirebase.model.Foto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 2;
    private static final String TAG = MainActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    RecyclerView listFoto;
    List<Foto> modelList;
    String teksPilihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        modelList = new ArrayList<>();

        ref.child(Util.TABEL_FOTO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    modelList.add(data.getValue(Foto.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pilihan dialogPilihan = new Pilihan();
                dialogPilihan.setOnOptionDialogListener(new Pilihan.OnOptionDialogListener() {
                    @Override
                    public void onOptionChoosen(String text) {
                        teksPilihan = text;
                        if (text.equals("teks")) {

                        } else if (text.equals("foto")) {
//                            cekIzin();
                            Intent intent = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            }
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent, REQUEST_IMAGE);
                        }
                    }
                });
                dialogPilihan.show(getFragmentManager(), "dialog_pilihan");
            }
        });

        listFoto = (RecyclerView) findViewById(R.id.list_main);
        FotoAdapter adapter = new FotoAdapter(modelList, new FotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Foto model) {

            }
        });
        listFoto.setAdapter(adapter);
        listFoto.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());
                    Bitmap foto = null;
                    try {
                        foto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InputFoto dialog = new InputFoto();

                    if (foto != null) {
                        dialog.setPreview(foto);
                        dialog.setUriFoto(uri);
                        if (teksPilihan.equals("teks")) {
                            dialog.setPilihan(0);
                        } else if (teksPilihan.equals("foto")) {
                            dialog.setPilihan(1);
                        }
                        dialog.show(getSupportFragmentManager(), "dialog");
                    }
                }
            }
        }
    }

//    private void cekIzin() {
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL);
//        }
//    }

}
