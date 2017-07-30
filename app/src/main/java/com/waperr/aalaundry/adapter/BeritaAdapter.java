package com.waperr.aalaundry.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.pojo.Berita;

import java.util.List;

/**
 * Created by farhan on 1/7/17.
 */

public class BeritaAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Berita> beritaItem;

    private Context context;

    public BeritaAdapter(Activity activity, List<Berita> beritaItem) {

        this.activity = activity;
        this.beritaItem = beritaItem;
    }


    @Override
    public int getCount() {
        return beritaItem.size();
    }

    @Override
    public Object getItem(int location) {
        return beritaItem.get(location);
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
            convertView = inflater.inflate(R.layout.adapter_listview_berita, null);

        Berita berita = beritaItem.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.tvJudulBerita);
        TextView status = (TextView) convertView.findViewById(R.id.tvIsiBerita);
        ImageView image = (ImageView) convertView.findViewById(R.id.ivBerita);

        status.setText(String.valueOf(berita.getIsi()));
        title.setText(berita.getJudul());
        Picasso.with(activity).load(berita.getGambar()).into(image);


        return convertView;
    }

    /**
     * Created by farhan on 10/31/16.
     */

    public static class AdapterListviewMenu extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] titleMenu;
        private final Integer[] imageId;

        public AdapterListviewMenu(Activity context, String[] titleMenu, Integer[] imageId) {
            super(context, R.layout.adapter_listview_menu, titleMenu);
            this.context = context;
            this.titleMenu = titleMenu;
            this.imageId = imageId;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.adapter_listview_menu, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.tvAdapterMenu);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.ivAdapterMenu);

            txtTitle.setText(titleMenu[position]);
            imageView.setImageResource(imageId[position]);

            return rowView;
        }
    }
}
