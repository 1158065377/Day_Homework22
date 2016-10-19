package db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import bean.Bean;

/**
 * Created by Administrator on 2016/10/16.
 */

public class MyDbAs extends AsyncTaskLoader<ArrayList<Bean>> {
    private Context context;
    private String tablename;
    public MyDbAs(Context context,String tablename) {
        super(context);
        this.context=context;
        this.tablename=tablename;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public ArrayList<Bean> loadInBackground() {
        ArrayList<Bean> list = new ArrayList<>();
        DBManger manger = new DBManger(context, tablename);
        Cursor query = manger.query();
        while(query.moveToNext()){
            String id = query.getString(query.getColumnIndex("id"));
            String title = query.getString(query.getColumnIndex("title"));
            String img_url = query.getString(query.getColumnIndex("img_url"));
            Bean bean = new Bean();
            bean.setId(id);
            bean.setTitle(title);
            bean.setCover_url(img_url);
            list.add(bean);
        }
        return list;
    }
}
