package com.example.latihanfirebase.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latihanfirebase.R;
import com.example.latihanfirebase.model.Foto;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.latihanfirebase.Util.DIR_FOTO;
import static com.example.latihanfirebase.Util.TABEL_FOTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputFoto extends DialogFragment {

    public InputFoto() {
        // Required empty public constructor
    }

    private static final String TAG = InputFoto.class.getSimpleName();
    private Uri uriFoto;
    private Bitmap preview;
    private int pilihan;
    private ImageView foto;
    private TextView kirim, batal;
    private EditText inputNama;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private StorageReference st;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_foto, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        foto = (ImageView) view.findViewById(R.id.foto_dialog);
        inputNama = (EditText) view.findViewById(R.id.input_nama);
        kirim = (TextView) view.findViewById(R.id.kirim);
        batal = (TextView) view.findViewById(R.id.batal);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pilihan == 1) {
            foto.setImageBitmap(preview);
        }

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama = inputNama.getText().toString();
                if (pilihan == 0) {
                    Foto foto = new Foto(null, nama);
                    ref.child(TABEL_FOTO).push().setValue(foto);
                    dismiss();
                } else {
                    st = FirebaseStorage.getInstance().getReference(DIR_FOTO).child(uriFoto.getLastPathSegment());
                    UploadTask kirimFoto = st.putFile(uriFoto);

                    Task<Uri> urlTask = kirimFoto.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Gagal upload:" + task.getException());
                            }
                            return st.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                //url download disini
                                Uri downloadUri = task.getResult();
                                Log.d(TAG, "url download:" + downloadUri.toString());
                                Foto uploadDB = new Foto(downloadUri.toString(), nama);
                                ref.child(TABEL_FOTO).push().setValue(uploadDB);
                                Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_LONG).show();
                                dismiss();
                            } else {
                                Log.w(TAG, "Gagal mengambil url:" + task.getException());
                            }
                        }
                    });

                    //TODO dibawah ini kode untuk mengambil url yang paling sederhana
//                    st = FirebaseStorage.getInstance().getReference(DIR_FOTO).child(uriFoto.getLastPathSegment());
//                    st.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Log.d(TAG, "url download:" + uri.toString());
//                                }
//                            });
//                        }
//                    });

                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setPreview(Bitmap foto) {
        preview = foto;
    }

    public void setUriFoto(Uri uri) {
        uriFoto = uri;
    }

    public void setPilihan(int pilihan) {
        this.pilihan = pilihan;
    }
}
