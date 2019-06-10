package example.com.assignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import example.com.assignment.adapter.FacilitiesListAdapter;
import example.com.assignment.dto.ExclusionDto;
import example.com.assignment.dto.FacilitiesDto;
import example.com.assignment.listener.OptionListener;
import example.com.assignment.networkutils.NetworkUtils;

public class PropertyActivity extends AppCompatActivity implements OptionListener {

    private Context context = PropertyActivity.this;
    public static final int INTENT_OPTION_SELECTED = 1001;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.lv_list)
    ListView listView;

    @Bind(R.id.ln_footer)
    LinearLayout lnFooter;

    @Bind(R.id.btn_save)
    Button btnSave;

    @Bind(R.id.root_view)
    LinearLayout rootView;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private ArrayList<FacilitiesDto> optionsList;
    private ArrayList<FacilitiesDto> facilitiesList;
    private FacilitiesListAdapter listAdapter;
    private FacilitiesDto facilitiesDto;
    private ArrayList<ArrayList<ExclusionDto>> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);

/*
        mRealm = Realm.getDefaultInstance();
*/
        initToolbar();
        getPropertyList();
/*
        handleUiElements();
*/
    }

    private void initToolbar() {
        try {
            toolbar.setTitle("Radius");
            setSupportActionBar(toolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPropertyList() {
        String url = "https://my-json-server.typicode.com/iranjith4/ad-assignment/db";
        try {
            if (NetworkUtils.isConnectedToInternet(context)) {
                showProgressBar();

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
            dismissProgressBar();
            facilitiesList = new ArrayList<>();
            if (dataObject.has("facilities") && !dataObject.isNull("facilities")) {
                JSONArray facilitiesArray = dataObject.getJSONArray("facilities");

                for (int i = 0; i < facilitiesArray.length(); i++) {

                    JSONObject facilityObject = facilitiesArray.getJSONObject(i);
                    FacilitiesDto facilitiesDto = new FacilitiesDto();

                    facilitiesDto.setFacilityId(facilityObject.getInt("facility_id"));
                    facilitiesDto.setFacilityName(facilityObject.getString("name"));


                    JSONArray optionsArray = facilityObject.getJSONArray("options");
                    optionsList = new ArrayList<>();

                    for (int j = 0; j < optionsArray.length(); j++) {
                        JSONObject optionsObject = optionsArray.getJSONObject(j);
                        FacilitiesDto optionsDto = new FacilitiesDto();

                        optionsDto.setOptionId(optionsObject.getInt("id"));
                        optionsDto.setOptionName(optionsObject.getString("name"));
                        optionsDto.setIconName(optionsObject.getString("icon"));

                        optionsList.add(optionsDto);
                    }
                    facilitiesDto.setOptionsList(optionsList);
                    facilitiesList.add(facilitiesDto);
                }
                populateUiElement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*
    private void handleUiElements() {
        try {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDataIntoDb();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

/*    private void addDataIntoDb() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {

                    } catch (RealmPrimaryKeyConstraintException e) {
                        e.printStackTrace();
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }*/


    private void populateUiElement() {
        try {
            listAdapter = new FacilitiesListAdapter(context, R.layout.facilities_list_tem_templ, facilitiesList, this);
            listView.setAdapter(listAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case INTENT_OPTION_SELECTED:
                    facilitiesDto = (FacilitiesDto) data.getSerializableExtra("facilitiesDto");
                    result = (ArrayList<ArrayList<ExclusionDto>>) data.getSerializableExtra("resultList");
                    showSelectedType();
            }
        }
    }

    private void showSelectedType() {
        try {
            String selectedType = facilitiesDto.getFacilityName();
            String selectedOption = facilitiesDto.getSelectedOption();
            String message = "You have selected " + selectedType + " - " + selectedOption;

            Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.md_orange_900));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(context.getResources().getColor(R.color.white));
            textView.setTextSize(14f);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

            snackbar.show();

            lnFooter.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectItem(FacilitiesDto facilitiesDto) {
        try {
            Intent intent = new Intent(context, OptionsActivity.class);
            intent.putExtra("facilitiesDto", facilitiesDto);
            intent.putExtra("result", result);
            startActivityForResult(intent, INTENT_OPTION_SELECTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressBar() {
        try {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                //  progressBar.setClickable(false);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressBar() {
        try {
            if (progressBar != null) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
