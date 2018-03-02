package com.alonelegion.workproject01.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.alonelegion.workproject01.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by develop2 on 26.02.2018.
 */

public class WorkOrderListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public WorkOrderListAdapter(Context _context, List<String> _listDataHeader,
                                HashMap<String, List<String>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_work_orders_list, null);
        }

        TextView id = (TextView) convertView.findViewById(R.id.nz_id_work);
        id.setText(childText);
        TextView data = (TextView) convertView.findViewById(R.id.data_work);
        data.setText(childText);
        TextView t_begin = (TextView) convertView.findViewById(R.id.t_begin_work);
        t_begin.setText(childText);
        TextView how = (TextView) convertView.findViewById(R.id.how_work);
        how.setText(childText);
        TextView filial = (TextView) convertView.findViewById(R.id.filial_work);
        filial.setText(childText);
        TextView city = (TextView) convertView.findViewById(R.id.city_work);
        city.setText(childText);
        TextView street = (TextView) convertView.findViewById(R.id.street_work);
        street.setText(childText);
        TextView fio = (TextView) convertView.findViewById(R.id.fio_work);
        fio.setText(childText);
        TextView tip_nz = (TextView) convertView.findViewById(R.id.tip_nz_work);
        tip_nz.setText(childText);
        TextView stat = (TextView) convertView.findViewById(R.id.stat_work);
        stat.setText(childText);
        TextView nz_id = (TextView) convertView.findViewById(R.id.nz_id_part_work);
        nz_id.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_work_orders, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.groupHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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
}
