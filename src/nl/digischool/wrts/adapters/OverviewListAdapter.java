package nl.digischool.wrts.adapters;

import java.util.ArrayList;
import java.util.Map;

import nl.digischool.wrts.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import nl.digischool.wrts.classes.Utilities;

public class OverviewListAdapter extends BaseAdapter {

    private class ViewHolder { TextView text1; }
    private ArrayList<Map<String, Object>> mData;
    private LayoutInflater mInflater;

    public OverviewListAdapter(Context context, ArrayList<Map<String, Object>> dataObject) {
        mInflater = LayoutInflater.from(context);
        mData = dataObject;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_overview_list_item, null);
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> object = getItem(position);
        String text = (String) object.get("string");
        holder.text1.setText(text);

        return convertView;
    }

}
