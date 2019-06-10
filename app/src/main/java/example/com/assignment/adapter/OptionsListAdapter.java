package example.com.assignment.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.assignment.R;
import example.com.assignment.dto.ExclusionDto;
import example.com.assignment.dto.FacilitiesDto;
import example.com.assignment.listener.OptionListener;

public class OptionsListAdapter extends RecyclerView.Adapter<OptionsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FacilitiesDto> dataList;
    private int selectedPosition = -1;
    private ArrayList<ArrayList<ExclusionDto>> result;
    private OptionListener optionListener;

    public OptionsListAdapter(@NonNull Context context, ArrayList<FacilitiesDto> dataList, ArrayList<ArrayList<ExclusionDto>> result, OptionListener optionListener) {

        this.context = context;
        this.dataList = dataList;
        this.result = result;
        this.optionListener = optionListener;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.options_list_item_tmpl, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        FacilitiesDto rowItem = dataList.get(position);
        viewHolder.tvOption.setText(rowItem.getOptionName());

        if (position == selectedPosition) {
            viewHolder.radioButton.setChecked(true);
            Log.d("", "checkbox CHECKED at pos: " + position);
        } else {
            viewHolder.radioButton.setChecked(false);
            Log.d("", "checkbox UNCHECKED at pos: " + position);
        }

        if (result != null && !result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < result.get(i).size(); j++)
                    if (rowItem.getOptionId() == result.get(i).get(j).getOptionId()) {
                        viewHolder.radioButton.setEnabled(false);
                    }
            }
        }

        if (rowItem.getIconName().equalsIgnoreCase("no-room")) {
            rowItem.setIconName("no_room");
        }

        String uri = "@drawable/" + rowItem.getIconName();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        viewHolder.ivIcon.setImageDrawable(res);

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacilitiesDto rowItem = dataList.get(position);
                selectedPosition = position;
                optionListener.selectItem(rowItem);
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_option)
        TextView tvOption;

        @Bind(R.id.rb_item)
        RadioButton radioButton;

        @Bind(R.id.iv_icon)
        ImageView ivIcon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
