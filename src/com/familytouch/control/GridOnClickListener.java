package com.familytouch.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.familytouch.HomePage;
import com.familytouch.R;
import com.familytouch.data.ServiceJsonObject;
import com.familytouch.util.JsonUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GridOnClickListener implements OnItemClickListener {
	private Activity activity;

	JsonUtil jsonUtil;
	final String FILE_NAME = "test.json";
	List<ServiceJsonObject> serviceList;
	ArrayList<ServiceJsonObject> serviceAddList = new ArrayList<ServiceJsonObject>();
	AlertDialog.Builder dialogBuilder = null;

	public GridOnClickListener(Activity act) {
		activity = act;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowid) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		HashMap<String, Object> itemMap = (HashMap<String, Object>) parent
				.getItemAtPosition(position);
		int image = (Integer) itemMap.get(HomePage.ICOKEY);
		switch (image) {
		case R.drawable.ic_addnewservice:

			jsonUtil = new JsonUtil(activity);
			if (!jsonUtil.isJsonExist(FILE_NAME)) {
				jsonUtil.connnectAndWriteJson(
						"http://202.120.40.111:8080/smartcity-server/rest/service/list",
						FILE_NAME);
			}
			serviceList = jsonUtil.readJson(FILE_NAME);
			boolean[] initBool = new boolean[serviceList.size()];

			String[] serviceTitle = new String[serviceList.size()];
			for (int i = 0; i < serviceList.size(); i++) {
				serviceTitle[i] = serviceList.get(i).getName();
			}

			if (dialogBuilder == null) {
				dialogBuilder = new AlertDialog.Builder(activity);
				dialogBuilder.setMultiChoiceItems(serviceTitle, initBool,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub
								if (isChecked) {
									serviceAddList.add(serviceList.get(which));
								} else {
									serviceAddList.remove(serviceList
											.get(which));
								}
							}
						});
				dialogBuilder.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								((HomePage) activity)
										.updateView(serviceAddList);
							}
						});
				dialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				dialogBuilder.setTitle("Add new service");
			}
			dialogBuilder.create().show();

			break;

		default:
			break;
		}
	}

}
