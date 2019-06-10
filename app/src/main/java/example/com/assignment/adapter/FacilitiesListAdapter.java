package example.com.assignment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.assignment.listener.OptionListener;
import example.com.assignment.R;
import example.com.assignment.dto.FacilitiesDto;

public class FacilitiesListAdapter extends ArrayAdapter<FacilitiesDto> {

    private Context context;
    private ArrayList<FacilitiesDto> dataList;
    private OptionListener optionListener;
    private ViewHolder holder;

    public FacilitiesListAdapter(@NonNull Context context, int resource, ArrayList<FacilitiesDto> dataList, OptionListener optionListener) {
        super(context, resource);

        this.context = context;
        this.dataList = dataList;
        this.optionListener = optionListener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.facilities_list_tem_templ, null);

            holder = new ViewHolder();
            holder.tvFacilityType = convertView.findViewById(R.id.tv_facility_type);
            holder.lnMain = convertView.findViewById(R.id.ln_main);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FacilitiesDto rowItem = dataList.get(position);

        if (rowItem.getFacilityName() != null && !rowItem.getFacilityName().isEmpty()) {
            holder.lnMain.setVisibility(View.VISIBLE);
            holder.tvFacilityType.setText(rowItem.getFacilityName());
        }else {
            holder.lnMain.setVisibility(View.GONE);
        }
        holder.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacilitiesDto rowItem = dataList.get(position);
                optionListener.selectItem(rowItem);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private TextView tvFacilityType;
        private LinearLayout lnMain;
    }
}
