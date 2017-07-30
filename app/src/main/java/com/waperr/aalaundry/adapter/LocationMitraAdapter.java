package com.waperr.aalaundry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.pojo.Alamat;
import com.waperr.aalaundry.pojo.Mitra;

import java.util.ArrayList;

/**
 * Created by farhan on 5/27/16.
 */
public class LocationMitraAdapter extends ArrayAdapter<Mitra> {

    public LocationMitraAdapter(Context context, ArrayList<Mitra> users) {
        super(context, 0, users);
    }
    //private Locale indo = new Locale("id-ID");

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Mitra mitra = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_listview_location, parent, false);
        }

        ImageView imageViewRiwayat = (ImageView) convertView.findViewById(R.id.ivAdapterLocation);
        TextView tvJenis = (TextView) convertView.findViewById(R.id.tvAdapterLocation);

        tvJenis.setText(mitra.getNama_mitra());
        imageViewRiwayat.setImageResource(R.drawable.ic_londri);

        return convertView;
    }
}
