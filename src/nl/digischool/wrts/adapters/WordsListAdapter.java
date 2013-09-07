package nl.digischool.wrts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import nl.digischool.wrts.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 5-9-13
 * Time: 22:37
 */
public class WordsListAdapter extends BaseAdapter {

    private class ViewHolder { TextView text1; }
    private ArrayList<Map<String, String>> mWords;
    private LayoutInflater mInflater;
    private String mWordName;

    public WordsListAdapter(Context context, ArrayList<Map<String, String>> words, String wordName) {

        mInflater = LayoutInflater.from(context);
        mWords = words;
        mWordName = wordName;

    }

    @Override
    public int getCount() {

        return mWords.size();

    }

    @Override
    public Map<String, String> getItem(int position) {

        return mWords.get(position);

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
        Map<String, String> object = getItem(position);
        String text = (String) object.get(mWordName);
        holder.text1.setText(text);

        return convertView;

    }

}
