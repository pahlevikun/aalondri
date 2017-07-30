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
import com.waperr.aalaundry.pojo.Riwayat;

import java.util.ArrayList;

/**
 * Created by farhan on 5/27/16.
 */
public class LocationAlamatAdapter extends ArrayAdapter<Alamat> {

    public LocationAlamatAdapter(Context context, ArrayList<Alamat> users) {
        super(context, 0, users);
    }
    //private Locale indo = new Locale("id-ID");

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Alamat alamat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_listview_location, parent, false);
        }

        ImageView imageViewRiwayat = (ImageView) convertView.findViewById(R.id.ivAdapterLocation);
        TextView tvJenis = (TextView) convertView.findViewById(R.id.tvAdapterLocation);

        tvJenis.setText(alamat.getNamaAlamat());
        imageViewRiwayat.setImageResource(R.drawable.ic_location_home);

        return convertView;
    }
}
