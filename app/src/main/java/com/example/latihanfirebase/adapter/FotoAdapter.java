package com.example.latihanfirebase.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latihanfirebase.model.Foto;
import com.example.latihanfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Foto model);
    }

    private List<Foto> listInfo;
    private OnItemClickListener listener;

    public FotoAdapter(List<Foto> listInfo, OnItemClickListener listener) {
        this.listInfo = listInfo;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_foto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listInfo.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listInfo.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nama;

        public ViewHolder(View item) {
            super(item);
            foto = (ImageView) item.findViewById(R.id.list_foto);
            nama = (TextView) item.findViewById(R.id.list_teks);
        }

        public void bind(final Foto model, final OnItemClickListener listener) {
            if (model.getUrlFoto() != null) {
                final long MB = 20 * 1024 * 1024;
                StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(model.getUrlFoto());
                ref.getBytes(MB).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if (task.isSuccessful()) {
                            Bitmap fotoDownload = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                            foto.setImageBitmap(fotoDownload);
                        } else {
                            Log.w(FotoAdapter.class.getSimpleName(), "gagal download foto:" + task.getException());
                            foto.setImageResource(R.drawable.access);
                            foto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                    }
                });
            } else {
                foto.setImageResource(R.drawable.ic_camera_black_24dp);
                foto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            nama.setText(model.getNama());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model);
                }
            });
        }
    }
}
