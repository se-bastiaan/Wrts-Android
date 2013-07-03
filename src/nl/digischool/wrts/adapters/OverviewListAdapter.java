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

    private enum ViewType { HEADER, ITEM };
    private class ViewHolder { TextView text1; TextView count; }
    private ArrayList<Map<String, Object>> mData;
    private LayoutInflater mInflater;

    public OverviewListAdapter(Context context, ArrayList<Map<String, Object>> dataObject) {
        mInflater = LayoutInflater.from(context);
        mData = dataObject;
    }

    @Override
    public int getViewTypeCount() {
        return ViewType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).containsKey("header")) {
            return ViewType.HEADER.ordinal();
        } else {
            return ViewType.ITEM.ordinal();
        }
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
        int itemViewType = getItemViewType(position);
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            if(itemViewType == ViewType.HEADER.ordinal()) {
                convertView = mInflater.inflate(R.layout.activity_overview_drawer_list_header, null);

            } else if(itemViewType == ViewType.ITEM.ordinal()) {
                convertView = mInflater.inflate(R.layout.activity_overview_drawer_list_item, null);
                holder.count = (TextView) convertView.findViewById(R.id.count);
            }
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> object = getItem(position);
        String text = (String) object.get("string");
        if(itemViewType == ViewType.HEADER.ordinal()) text = text.toUpperCase();
        if(itemViewType == ViewType.ITEM.ordinal()) text = Utilities.uppercaseFirst(text);
        holder.text1.setText(text);
        if(object.containsKey("count")) holder.count.setText(object.get("count").toString());

        return convertView;
    }

}
