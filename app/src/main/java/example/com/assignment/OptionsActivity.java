package example.com.assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.assignment.adapter.OptionsListAdapter;
import example.com.assignment.dto.ExclusionDto;
import example.com.assignment.dto.FacilitiesDto;
import example.com.assignment.listener.OptionListener;
import example.com.assignment.networkutils.NetworkUtils;

public class OptionsActivity extends AppCompatActivity implements OptionListener {

    private Context context = OptionsActivity.this;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.lv_list)
    RecyclerView listView;

    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private FacilitiesDto facilitiesDto;
    private ArrayList<ArrayList<ExclusionDto>> exclusionList;
    private ArrayList<ArrayList<ExclusionDto>> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);

        initToolbar();
        getIntentValue();
        handleUiElement();
        getExclusionList();
    }

    private void initToolbar() {
        try {
            mToolbar.setTitle("Options");
            setSupportActionBar(mToolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(context, R.drawable.ic_action_bacl_black));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIntentValue() {
        try {
            ArrayList<FacilitiesDto> optionsList;

            Intent intent = getIntent();
            facilitiesDto = (FacilitiesDto) intent.getSerializableExtra("facilitiesDto");
            result = (ArrayList<ArrayList<ExclusionDto>>) intent.getSerializableExtra("result");

            optionsList = facilitiesDto.getOptionsList();
            populateUiElements(optionsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUiElement() {
        try {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result = new ArrayList<ArrayList<ExclusionDto>>();
                    result = getExclusionListArray();
                    handleResponse();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getExclusionList() {
        String url = "https://my-json-server.typicode.com/iranjith4/ad-assignment/db";
        try {
            if (NetworkUtils.isConnectedToInternet(context)) {
                StringRequest jsonObject = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject dataObject = new JSONObject(response);
                                    parsePropertyListResponse(dataObject);

                                    Log.e("response", "" + dataObject);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                requestQueue.add(jsonObject);
            } else {
                Toast.makeText(context, "Please connect to the internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parsePropertyListResponse(JSONObject dataObject) {
        try {
            exclusionList = new ArrayList<>();

            if (dataObject.has("exclusions") && !dataObject.isNull("exclusions")) {
                JSONArray exclusionsArray = dataObject.getJSONArray("exclusions");

                for (int i = 0; i < exclusionsArray.length(); i++) {

                    JSONArray jsonArray = exclusionsArray.getJSONArray(i);
                    ExclusionDto exclusionDto = new ExclusionDto();
                    ArrayList<ExclusionDto> exclusionChildArray = new ArrayList<>();

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);

                        ExclusionDto facilitiesDto = new ExclusionDto();

                        facilitiesDto.setFilterId(jsonObject.getInt("facility_id"));
                        facilitiesDto.setOptionId(jsonObject.getInt("options_id"));

                        exclusionChildArray.add(facilitiesDto);
                        exclusionDto.setChildExclusionList(exclusionChildArray);
                    }
                    Log.e("childList", "" + exclusionDto.getChildExclusionList());
                    exclusionList.add(exclusionChildArray);
                }
            }
            Log.e("arrayList", "" + exclusionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList getExclusionListArray() {

        int facilityId = facilitiesDto.getFacilityId();
        int optionId = facilitiesDto.getOptionId();
        ArrayList<ArrayList<ExclusionDto>> result = new ArrayList<>();

        for (int i = 0; i < exclusionList.size(); i++) {

            for (int j = 0; j < exclusionList.get(i).size(); j++) {

                int facId = exclusionList.get(i).get(j).getFilterId();
                int optId = exclusionList.get(i).get(j).getOptionId();
                if (facilityId == facId && optId == optionId) {

                    result.add(exclusionList.get(i));
                    Log.e("result", "" + result);
                }
            }
        }
        return result;
    }

    private void populateUiElements(ArrayList<FacilitiesDto> optionsList) {
        try {
            OptionsListAdapter optionsListAdapter = new OptionsListAdapter(context, optionsList, result, this);
            listView.setNestedScrollingEnabled(false);
            listView.setAdapter(optionsListAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            listView.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectItem(FacilitiesDto facilitiesDto) {
        this.facilitiesDto.setOptionName(facilitiesDto.getOptionName());
        this.facilitiesDto.setOptionId(facilitiesDto.getOptionId());
        this.facilitiesDto.setSelectedPosition(facilitiesDto.getSelectedPosition());
        this.facilitiesDto.setSelectedOption(facilitiesDto.getOptionName());
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void handleResponse() {
        try {
            Intent intent = new Intent();
            intent.putExtra("facilitiesDto", facilitiesDto);
            intent.putExtra("resultList", result);
            setResult(RESULT_OK, intent);

            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
