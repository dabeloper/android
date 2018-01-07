package com.dabeloper.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dabeloper.android.R;
import com.dabeloper.android.model.DummyData;

import java.util.ArrayList;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 23/08/2017.
 */

public class MenuActivityDataAdapter extends RecyclerView.Adapter<MenuActivityDataAdapter.DummyDataViewHolder> {

    private ArrayList<DummyData> datos;


    public MenuActivityDataAdapter(ArrayList<DummyData> datos) {
        this.datos = datos;
    }


    @Override
    public DummyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_data, parent, false);

        DummyDataViewHolder vh = new DummyDataViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(DummyDataViewHolder holder, int position) {
        DummyData item = datos.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class DummyDataViewHolder
            extends RecyclerView.ViewHolder {

        private TextView txtTitulo;
        private TextView txtId;
        private TextView txtSubtitulo;

        public DummyDataViewHolder(View itemView) {
            super(itemView);

            txtTitulo = (TextView)itemView.findViewById(R.id.tv_dummy_title);
            txtId = (TextView)itemView.findViewById(R.id.tv_dummy_id);
            txtSubtitulo = (TextView)itemView.findViewById(R.id.tv_dummy_body);
        }

        public void bindData(DummyData t) {
            txtTitulo.setText(t.getTitle());
            txtId.setText(t.getId());
            txtSubtitulo.setText(t.getBody());
        }
    }
}
