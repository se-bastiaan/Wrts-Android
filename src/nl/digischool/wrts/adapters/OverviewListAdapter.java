package nl.digischool.wrts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import nl.digischool.wrts.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OverviewListAdapter extends BaseAdapter {

    private class ViewHolder { TextView text1; }
    private ArrayList<Map<String, Object>> mData;
    private LayoutInflater mInflater;
    private HashSet<Integer> mCheckedItems;
    private Boolean mMultiMode;

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

    public void remove(int position) {
        mData.remove(position);
    }

    public void startMultiMode() {
        mMultiMode = true;
        this.notifyDataSetChanged();
    }

    public void stopMultiMode() {
        mCheckedItems.clear();
        mMultiMode = false;
        notifyDataSetChanged();
    }

    public void setChecked(int pos, boolean checked) {
        if (checked) {
            mCheckedItems.add(pos);
        } else {
            mCheckedItems.remove(pos);
        }
        if (mMultiMode) {
            this.notifyDataSetChanged();
        }
    }

    public boolean isChecked(int pos) {
        return mCheckedItems.contains(pos);
    }

    public void toggleChecked(int pos) {
        if (mCheckedItems.contains(pos)) {
            mCheckedItems.remove(pos);
        } else {
            mCheckedItems.add(pos);
        }
        notifyDataSetChanged();
    }

    public int getCheckedItemCount() {
        return mCheckedItems.size();
    }

    public Set<Integer> getCheckedItems() {
        return mCheckedItems;
    }

}
