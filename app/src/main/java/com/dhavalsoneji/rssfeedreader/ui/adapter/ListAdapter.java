package com.dhavalsoneji.rssfeedreader.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.model.Entry;

import java.util.List;

public class ListAdapter extends BaseListAdapter<Entry> {

    public static final String TAG = ListAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public ListAdapter(Context context, List<Entry> objectsList) {
        super(context, objectsList);
        mContext = context;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Holder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_entry, viewGroup, false);
            viewHolder = new Holder();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.txtPublishedDate = (TextView) convertView.findViewById(R.id.txtPublishedDate);
            viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }

        viewHolder.txtTitle.setText(getList().get(position).getTitle());
        viewHolder.txtPublishedDate.setText(getList().get(position).getPublished());

        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(getList().get(position).getContent()
                    , Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(getList().get(position).getContent());
        }
        viewHolder.txtContent.setText(result);
        return convertView;
    }

    private class Holder {
        TextView txtTitle;
        TextView txtPublishedDate;
        TextView txtContent;
    }
}
