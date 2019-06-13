package com.example.provapedroandroid2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.provapedroandroid2.R;
import com.example.provapedroandroid2.models.Ordenha;

import java.util.List;

public class OrdenhaAdapter extends BaseAdapter {
    LayoutInflater myInflater;
    List<Ordenha> ordenhaList;

    public OrdenhaAdapter(Context context, List<Ordenha> ordenhaList) {
        this.ordenhaList = ordenhaList;
        myInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ordenhaList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordenhaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ordenha ordenha = ordenhaList.get(position);
        convertView = myInflater.inflate(R.layout.item_ordenha_adapter, null);
        ((TextView) convertView.findViewById(R.id.tvIdentificador)).setText(String.valueOf(ordenha.getIdentificador()));
        ((TextView) convertView.findViewById(R.id.tvDesc)).setText(ordenha.getMatrizLeiteira().getDescricao());
        ((TextView) convertView.findViewById(R.id.tvQtLitros)).setText(String.valueOf(ordenha.getQtLitros()));
        ((TextView) convertView.findViewById(R.id.tvDtOrdenha)).setText(ordenha.getDtOrdenha().getDay()+
                "/"+ordenha.getDtOrdenha().getMonth()+"/"+ordenha.getDtOrdenha().getYear()+" "+
                ordenha.getDtOrdenha().getHours()+":"+ordenha.getDtOrdenha().getMinutes());
        return convertView;
    }
}
