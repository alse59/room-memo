package com.example.wataru.room_memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wataru.greendao.db.PreviewConfirm;

import java.util.List;

/**
 * Created by wataru on 2016/01/31.
 */
public class PreviewConfirmAdapter extends ArrayAdapter<PreviewConfirm> {
    private static final String TAG = "PreviewConfirmAdapter";
    private LayoutInflater mInflater;
    private int mResource;
    public PreviewConfirmAdapter(Context context, int resource, List<PreviewConfirm> objects) {
        super(context, resource, objects);

        mInflater = LayoutInflater.from(context);
        mResource = resource;
    }

    class ViewHolder {
        TextView personName;
        TextView personAge;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, null);
            holder = new ViewHolder();
            holder.personName = (TextView)convertView.findViewById(R.id.person_name);
            holder.personAge = (TextView)convertView.findViewById(R.id.person_age);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        PreviewConfirm previewConfirm = getItem(position);
        holder.personName.setText(previewConfirm.getPcName());
        holder.personAge.setText(String.valueOf(previewConfirm.getId()));

        return convertView;
    }
}
