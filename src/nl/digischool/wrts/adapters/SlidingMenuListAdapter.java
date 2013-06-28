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

public class SlidingMenuListAdapter extends BaseAdapter {
	
	private enum ViewType { HEADER, ITEM };
	private class ViewHolder { TextView text1; }
	private ArrayList<Map<String, Object>> data;
	private LayoutInflater inflater;

	public SlidingMenuListAdapter(Context context, ArrayList<Map<String, Object>> dataObject) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = dataObject;
	}
	
	@Override
    public int getViewTypeCount() {
        return ViewType.values().length;
    }
	
	@Override
    public int getItemViewType(int position) {
		if(data.get(position).containsKey("header")) {
			return ViewType.HEADER.ordinal();
		} else {
			return ViewType.ITEM.ordinal();
		}
    }
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		return data.get(position);
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
			if(itemViewType == ViewType.HEADER.ordinal()) {
				convertView = inflater.inflate(R.layout.activity_overview_drawer_list_header, null);
			} else if(itemViewType == ViewType.ITEM.ordinal()) {
				convertView = inflater.inflate(R.layout.activity_overview_drawer_list_item, null);
			}
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> object = getItem(position);
		String text = (String) object.get("text");
		if(itemViewType == ViewType.HEADER.ordinal()) text = text.toUpperCase();
		holder.text1.setText(text);
		
		if(position != getCount() - 1 && getItemViewType(position + 1) != ViewType.HEADER.ordinal() && itemViewType != ViewType.HEADER.ordinal()) convertView.findViewById(R.id.list_item_separator).setVisibility(View.VISIBLE);
		if(position != getCount() - 1 && getItemViewType(position + 1) == ViewType.HEADER.ordinal()) convertView.findViewById(R.id.list_item_separator).setVisibility(View.GONE);
		return convertView;
	}

}
