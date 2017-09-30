package com.yang.node;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        new LoadUserList().execute("http://172.16.2.16:52273/user") ;
    }

    public void addUser(View view) {

    }

    class LoadUserList extends AsyncTask<String,String,String>{

        ProgressDialog dialog = new ProgressDialog(UserActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("사용자 목록 로딩 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            //JSON 파싱 -> ListViewe 에 출력

            try {
                JSONArray array = new JSONArray(s);
                ArrayList<String> strings = new ArrayList<String>();
                for (int i=0;i<array.length();i++) {
                    JSONObject obj = array.getJSONObject(i);
                    strings.add(obj.getString("name"));

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        UserActivity.this, android.R.layout.simple_list_item_1, strings);
                ListView listView = (ListView)findViewById(R.id.listview);
                listView.setAdapter (adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer output = new StringBuffer();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn!=null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                   // conn.setDoInput(true);
                   // conn.setDoOutput(true);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line==null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output.toString();

        }
    }
}
