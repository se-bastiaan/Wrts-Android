package nl.digischool.wrts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import nl.digischool.wrts.R;
import nl.digischool.wrts.database.Word;

import java.util.List;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 5-9-13
 * Time: 22:37
 */
public class WordsListAdapter extends BaseAdapter {

    private class ViewHolder { TextView text1; }
    private List<Word> mWords;
    private LayoutInflater mInflater;
    private Integer mLanguageIndex;

    public WordsListAdapter(Context context, List<Word> words, Integer langIndex) {
        mInflater = LayoutInflater.from(context);
        mWords = words;
        mLanguageIndex = langIndex;
    }

    @Override
    public int getCount() {

        return mWords.size();

    }

    @Override
    public Word getItem(int position) {
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
        Word object = getItem(position);
        String text = getWord(object);
        holder.text1.setText(text);

        return convertView;

    }

    private String getWord(Word word) {
        switch (mLanguageIndex) {
            case 0:
                if(word.getWord_a() != null && !word.getWord_a().isEmpty()) return word.getWord_a();
                break;
            case 1:
                if(word.getWord_b() != null && !word.getWord_b().isEmpty()) return word.getWord_b();
                break;
            case 2:
                if(word.getWord_c() != null && !word.getWord_c().isEmpty()) return word.getWord_c();
                break;
            case 3:
                if(word.getWord_d() != null && !word.getWord_d().isEmpty()) return word.getWord_d();
                break;
            case 4:
                if(word.getWord_e() != null && !word.getWord_e().isEmpty()) return word.getWord_e();
                break;
            case 5:
                if(word.getWord_f() != null && !word.getWord_f().isEmpty()) return word.getWord_f();
                break;
            case 6:
                if(word.getWord_g() != null && !word.getWord_g().isEmpty()) return word.getWord_g();
                break;
            case 7:
                if(word.getWord_h() != null && !word.getWord_h().isEmpty()) return word.getWord_h();
                break;
            case 8:
                if(word.getWord_i() != null && !word.getWord_i().isEmpty()) return word.getWord_i();
                break;
            case 9:
                if(word.getWord_j() != null && !word.getWord_j().isEmpty()) return word.getWord_j();
                break;
        }
        return "";
    }

}
