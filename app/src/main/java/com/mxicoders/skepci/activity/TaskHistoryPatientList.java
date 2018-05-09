package com.mxicoders.skepci.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.TaskHistoryAdapter;
import com.mxicoders.skepci.model.ToDoData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mxicoders on 29/8/17.
 */

public class TaskHistoryPatientList  extends AppCompatActivity implements View.OnClickListener {


    private static final String tag = "MyCalendarActivity";

    private MyDynamicCalendar myCalendar;
    RecyclerView mRecyclerViewLastWeek, mRecyclerViewThisWeek;

    TaskHistoryAdapter mAdapterLastWeek;
    TaskHistoryAdapter mAdapterThisWeek;

    ArrayList<ToDoData> listLastWeek, listThisWeek, weekList;

    ProgressDialog pDialog;
    CommanClass cc;

    String pid;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne, imgToolTwo, imgToolBack;

    LinearLayout ln_schedule_task;

    public static String date_month_year;


    private TextView currentMonth;
    //	private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    Date oneWayTripDate;
    Date firstdate;
    Date checkdate;

    Calendar c;

    String preDate;

    static String result = "";

    int weekNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.task_his_patientlist_menu);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        pid = cc.loadPrefString("patient_id_main");

        Log.i("patient id",pid);

        listThisWeek = new ArrayList<ToDoData>();
        listLastWeek = new ArrayList<ToDoData>();

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                + year);


		/*selectedDayMonthYearButton = (Button) this
                .findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");*/

        prevMonth = (ImageView) findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(TaskHistoryPatientList.this,
                R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);


        ln_schedule_task = (LinearLayout) findViewById(R.id.ln_schedule_task);

        toolbar = (RelativeLayout) findViewById(R.id.toolbarr);
        tooName = (TextView) findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView) findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView) findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView) findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.task_history));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        ln_schedule_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sleep = new Intent(TaskHistoryPatientList.this, SleepPatientlistActivity.class);
                startActivity(sleep);

            }
        });


        mRecyclerViewLastWeek = (RecyclerView) findViewById(R.id.rc_two);
        mRecyclerViewThisWeek = (RecyclerView) findViewById(R.id.rc_one);

        mRecyclerViewLastWeek.setLayoutManager(new LinearLayoutManager(TaskHistoryPatientList.this));
        mRecyclerViewLastWeek.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewThisWeek.setLayoutManager(new LinearLayoutManager(TaskHistoryPatientList.this));
        mRecyclerViewThisWeek.setItemAnimator(new DefaultItemAnimator());

        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(TaskHistoryPatientList.this,getString(R.string.no_internet));
        } else {

            getDataForFirstTime();
        }

    }


    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(TaskHistoryPatientList.this,
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }

    }

    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"1", "2", "3",
                "4", "5", "6", "7", "8", "9",
                "10", "11", "12"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        public int currentWeekDay;
        private TextView gridcell;
        private RelativeLayout dateGrid;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
            dateGrid = (RelativeLayout) row.findViewById(R.id.date_grid_select);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row
                            .findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources()
                        .getColor(R.color.lightgray));
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(
                        R.color.lightgray02));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.orrange));
            }
            return row;
        }


        @Override
        public void onClick(View view) {
            date_month_year = (String) view.getTag();

            dateGrid.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_date_round));

            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");

            try {
                oneWayTripDate = input.parse(date_month_year);
                date_month_year = output.format(oneWayTripDate);

                Log.i("DateTobeList", date_month_year);
                clear();
                getTaskHistoryList(date_month_year);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;

            weekNumber = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

    private void getTaskHistoryList(final String date_month_year) {

        pDialog = new ProgressDialog(TaskHistoryPatientList.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_TASK_HISTORY_PSYCHOLOGIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:taskhis data", response);

                        int current_week_number = 0;
                        int requested_week_number = 0;

                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(c.getTime());

                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            firstdate = input.parse(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(firstdate);
                        int week = cal.get(Calendar.WEEK_OF_MONTH);

                        Log.i("WeekNumberCurent", String.valueOf(week));

                        current_week_number = week;


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("patient_task_history");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);
                                    cc.showToast(jsonObject.getString("message"));

                                    String assign_date = jsonObject1.getString("assign_date");
                                    SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd");

                                    try {
                                        checkdate = input1.parse(assign_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    Calendar cal1 = Calendar.getInstance();
                                    cal1.setTime(checkdate);

                                    int week1 = cal1.get(Calendar.WEEK_OF_MONTH);

                                    Log.i("WeekNumber", String.valueOf(week1));

                                    requested_week_number = week1;

                                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;
                                    try {
                                        date = inFormat.parse(jsonObject1.getString("assign_date"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
                                    SimpleDateFormat outFormat_date = new SimpleDateFormat("dd");
                                    String d_day = outFormat.format(date);
                                    String d_date= outFormat_date.format(date);

                                    if (jsonObject1.getString("task_type").equals("Postais")){

                                    }else{

                                        if (requested_week_number == current_week_number) {


                                            ToDoData model = new ToDoData();

                                            model.setName(jsonObject1.getString("task_type"));
                                            model.setTask_his_id(jsonObject1.getString("id"));
                                            model.settiming(jsonObject1.getString("timing"));
                                            model.setD_date(d_date);
                                            model.setD_day(d_day);

                                            listThisWeek.add(model);

                                        } else if (requested_week_number < current_week_number) {

                                            ToDoData model = new ToDoData();

                                            model.setName(jsonObject1.getString("task_type"));
                                            model.setTask_his_id(jsonObject1.getString("id"));
                                            model.settiming(jsonObject1.getString("timing"));
                                            model.setD_date(d_date);
                                            model.setD_day(d_day);

                                            listLastWeek.add(model);
                                        }

                                    }

                                }

                                Log.e("@@ThisWeek", listThisWeek.size() + "");
                                Log.e("@@LastWeek", listLastWeek.size() + "");

                                if (listThisWeek.size() > 0) {
                                    mAdapterThisWeek = new TaskHistoryAdapter(listThisWeek, R.layout.todo_yesterday_rc_item, TaskHistoryPatientList.this);
                                    mRecyclerViewThisWeek.setAdapter(mAdapterThisWeek);
                                }

                                if (listLastWeek.size() > 0) {
                                    mAdapterLastWeek = new TaskHistoryAdapter(listLastWeek, R.layout.todo_yesterday_rc_item, TaskHistoryPatientList.this);
                                    mRecyclerViewLastWeek.setAdapter(mAdapterLastWeek);
                                }


                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", cc.loadPrefString("patient_id_main"));
                params.put("task_date", date_month_year);
                Log.e("request taskhis data", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    public void clear() {
        int size = this.listThisWeek.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listThisWeek.remove(0);
            }

            mAdapterThisWeek.notifyItemRangeRemoved(0, size);
        }

        int size1 = this.listLastWeek.size();
        if (size1 > 0) {
            for (int i = 0; i < size1; i++) {
                this.listLastWeek.remove(0);
            }
            mAdapterLastWeek.notifyItemRangeRemoved(0, size1);
        }
    }



    public void getDataForFirstTime(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date_month_year= df.format(c.getTime());

        getTaskHistoryList(date_month_year);


    }

    @Override
    protected void onResume() {
//        getDataForFirstTime();
        super.onResume();

        if (cc.loadPrefBoolean("isInactiveDevice") || cc.loadPrefBoolean("isDialogOpen") ){


            Log.e("IsLoginDevice","Psychologist...If");

            cc.savePrefBoolean("isInactiveDevice",false);

        }else {

            if (cc.loadPrefBoolean("Inactive")) {

                cc.savePrefBoolean("Inactive",false);

                cc.savePrefBoolean("isDialogOpen",true);

                cc.showInactiveDialog();
            }


            Log.e("IsLoginDevice","Psychologist...Else");
        }

    }
}


