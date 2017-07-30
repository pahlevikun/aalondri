package com.waperr.aalaundry.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.pojo.History;

import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class HistoryAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<History> categoryItems;

    public HistoryAdapter(Activity activity, List<History> categoryItems) {
        this.activity = activity;
        this.categoryItems = categoryItems;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return categoryItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_listview_history, null);

        //TextView title = (TextView) convertView.findViewById(R.id.text);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewAdapterGambar);
        TextView tvInvoice = (TextView) convertView.findViewById(R.id.textViewAdapterInvoice);
        TextView tvTanggal = (TextView) convertView.findViewById(R.id.textViewAdapterTanggal);
        TextView tvTipe = (TextView) convertView.findViewById(R.id.textViewAdapterTipe);
        TextView tvLayanan = (TextView) convertView.findViewById(R.id.textViewAdapterLayanan);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.textViewAdapterStatus);

        // getting movie data for the row
        History history = categoryItems.get(position);
        if (history.getIs_byitem().equals("0")){
            tvTipe.setText("Londri Kiloan");
        }else if(history.getIs_byitem().equals("1")){
            tvTipe.setText("Londri Satuan");
        }

        if (history.getIs_ekspress().equals("1")){
            tvLayanan.setText("Reguler");
        }else if(history.getIs_ekspress().equals("2")){
            tvLayanan.setText("Ekspress");
        }else if(history.getIs_ekspress().equals("3")){
            tvLayanan.setText("Kilat");
        }

        if (history.getStatusDetail().equals("0")&&!history.getStatus().equals("1")){
            tvStatus.setText("Order Belum Ditangani");
            imageView.setImageResource(R.drawable.riwpermintaaan);
        }else if(history.getStatusDetail().equals("0")&&history.getStatus().equals("1")){
            tvStatus.setText("Order Dibatalkan");
            imageView.setImageResource(R.drawable.riwproses);
        }else if(history.getStatusDetail().equals("1")){
            tvStatus.setText("Order Sedang Dijemput");
            imageView.setImageResource(R.drawable.riwproses);
        }else if(history.getStatusDetail().equals("2")){
            tvStatus.setText("Order Sedang Dicuci");
            imageView.setImageResource(R.drawable.riwproses);
        }else if(history.getStatusDetail().equals("3")){
            tvStatus.setText("Order Sedang Diantar");
            imageView.setImageResource(R.drawable.riwproses);
        }else if(history.getStatusDetail().equals("4")){
            tvStatus.setText("Order Selesai");
            imageView.setImageResource(R.drawable.riwselesai);
        }

        tvInvoice.setText(history.getInvoice_number());
        tvTanggal.setText(history.getTanggal_akhir());

        return convertView;
    }
}
