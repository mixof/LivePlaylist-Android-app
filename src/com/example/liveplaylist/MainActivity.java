package com.example.liveplaylist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;

public class MainActivity extends FragmentActivity  {

	Login_fragment logfrag;
	FragmentTransaction fTrans;
	DbHelper database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		database = DbHelper.getInstance(this);
		database.open();

		logfrag = new Login_fragment();

		fTrans = getSupportFragmentManager().beginTransaction();

		fTrans.replace(R.id.fragments_container, logfrag);
		fTrans.commit();

	}

	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			// заголовок
			adb.setTitle("Подтверждение");
			// сообщение
			adb.setMessage("Хотите услышать этот трек?");
			// иконка
			adb.setIcon(android.R.drawable.ic_dialog_info);
			// кнопка положительного ответа
			adb.setPositiveButton("Да", myClickListener);
			// кнопка отрицательного ответа
			adb.setNegativeButton("Не", myClickListener);
			// создаем диалог
			return adb.create();
		}
		return super.onCreateDialog(id);
	}

	OnClickListener myClickListener = new OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			// положительная кнопка
			case Dialog.BUTTON_POSITIVE:
				Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
				break;
			// негаитвная кнопка
			case Dialog.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};

	

}
