package com.mxicoders.skepci.demolist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.HelpPatientMenuActivity;
import com.mxicoders.skepci.adapter.HelpExpandableAdapter;
import com.mxicoders.skepci.model.HelpData;
import com.mxicoders.skepci.model.HelpDataChild;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private SearchView search;
	private MyListAdapter listAdapter;
	private ExpandableListView myList;
	private ArrayList<Continent> continentList = new ArrayList<Continent>();

	CommanClass cc;

	ProgressDialog pDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);


		cc = new CommanClass(this);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.search);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);

		// display the list
		displayList();
		// expand all Groups
		expandAll();

	}


	// method to expand all groups
	private void expandAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			myList.expandGroup(i);
		}
	}

	// method to expand all groups
	private void displayList() {

		// display the list
		loadSomeData();

		// get reference to the ExpandableListView
		myList = (ExpandableListView) findViewById(R.id.expandableList);
		// create the adapter by passing your ArrayList data
		listAdapter = new MyListAdapter(MainActivity2.this, continentList);
		// attach the adapter to the list
		myList.setAdapter(listAdapter);



	}

	private void loadSomeData() {
		ArrayList<Country> countryList = new ArrayList<Country>();
		Country country = new Country("BMU", "Bermuda", 10000000);
		countryList.add(country);
		country = new Country("CAN", "Canada", 20000000);
		countryList.add(country);
		country = new Country("USA", "United States", 50000000);
		countryList.add(country);

		Continent continent = new Continent("North America", countryList);
		continentList.add(continent);

		countryList = new ArrayList<Country>();
		country = new Country("CHN", "China", 10000100);
		countryList.add(country);
		country = new Country("JPN", "Japan", 20000200);
		countryList.add(country);
		country = new Country("THA", "Thailand", 50000500);
		countryList.add(country);

		continent = new Continent("Asia", countryList);
		continentList.add(continent);
	}



	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		listAdapter.filterData("");
		expandAll();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		listAdapter.filterData(newText);
		expandAll();
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		listAdapter.filterData(query);
		expandAll();
		return false;
	}

}
