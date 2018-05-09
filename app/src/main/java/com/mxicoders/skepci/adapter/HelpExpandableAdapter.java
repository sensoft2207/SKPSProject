package com.mxicoders.skepci.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.demolist.Continent;
import com.mxicoders.skepci.demolist.Country;
import com.mxicoders.skepci.model.HelpData;
import com.mxicoders.skepci.model.HelpDataChild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mxicoders on 26/7/17.
 */

public class HelpExpandableAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context _context;
    private ArrayList<HelpData> header;
    private ArrayList<ArrayList<HelpDataChild>> child;

    private ArrayList<HelpData> mFilteredList;


    public HelpExpandableAdapter(Context context, ArrayList<HelpData> listDataHeader,
                                 ArrayList<ArrayList<HelpDataChild>> listChildData) {
        this._context = context;
        this.header = listDataHeader;
        this.mFilteredList = listDataHeader;
        /*this.originalList = new ArrayList<HelpData>();
        this.originalList.addAll(header);*/
        this.child = listChildData;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public HelpDataChild getChild(int groupPosition, int childPosititon) {

        /*// This will return the child
        return this.child.get(header.get(groupPosition)).get(
                childPosititon);*/

        return child.get(groupPosition).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

       /* // Getting child text
        final String childText = (String) getChild(groupPosition, childPosition);*/

        HelpDataChild childAnswer = getChild(groupPosition, childPosition);

        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.help_custom_item_child, parent, false);
        }

        TextView child_text = (TextView) convertView.findViewById(R.id.child);

        child_text.setText(childAnswer.getAnswer());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        // return children count
        return child.get(groupPosition).size();
    }

    @Override
    public HelpData getGroup(int groupPosition) {

        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        // Get header size
        return this.header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

       /* // Getting header title
        String headerTitle = (String) getGroup(groupPosition);*/

        HelpData questionData = (HelpData) getGroup(groupPosition);

        // Inflating header layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.help_custom_item_header, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.header);

        header_text.setText(questionData.getQuestion());

        // If group is expanded then change the text into bold and change the
        // icon
        if (isExpanded) {
            header_text.setTypeface(null, Typeface.BOLD);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.up_arrow, 0);
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon

            header_text.setTypeface(null, Typeface.NORMAL);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.down_arrow, 0);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    header = mFilteredList;
                } else {

                    ArrayList<HelpData> filteredList = new ArrayList<>();

                    for (HelpData androidVersion : mFilteredList) {

                        if (androidVersion.getQuestion().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    header = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = header;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                header = (ArrayList<HelpData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    /*public void filterData(String query)
    {
        query = query.toLowerCase();
        Log.v("MyListAdapter", String.valueOf(header.size()));
        header.clear();

        if(query.isEmpty())
        {
            header.addAll(originalList);
        } else {
            for(HelpData continent: originalList)
            {
//                ArrayList<HelpData> countryList = continent.getQuestion();
                ArrayList<HelpData> countryList = new ArrayList<>();

                continent.getQuestion();

                countryList.add(continent);

                ArrayList<HelpData> newList = new ArrayList<HelpData>();
                for(HelpData country: countryList)
                {
                    if(country.getQuestion().toLowerCase().contains(query))
                    {
                        newList.add(country);
                    }
                }
                if(newList.size() > 0)
                {
                    HelpData nContinent = new HelpData();
                    nContinent.getQuestion();

                    newList.add(nContinent);

                    header.add(nContinent);
                }
            }
        }

        Log.v("MyListAdapter", String.valueOf(header.size()));
        notifyDataSetChanged();
    }*/



}
