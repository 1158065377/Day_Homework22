package com.liulong.day_homework;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bean.Bean;
import db.DBManger;
import db.MyDbAs;
import down.DownLoad;
import path.Path;
import tool.BitmapCompress;
import tool.MySp;

/**
 * Created by Administrator on 2016/10/15.
 */

public class MyFragment extends Fragment implements DownLoad.OnjsonDownLoad, PullToRefreshListView.OnRefreshListener, PullToRefreshBase.OnLastItemVisibleListener {
    private int a = 10;
    private MyAdapter adapter;
    private PullToRefreshListView lv;
    private ArrayList<Bean> list = new ArrayList<>();
    private int page = 1;
    private int count = 0;
    private ImageView fragment_iv_01, fragment_iv_02, fragment_iv_03;
    private int style;
    private String path1;
    private String path2;
    private ProgressDialog dialog;
    private String tablename;
    private DBManger dbManger;
    private Handler handler=new Handler();
    private Timer timer;
    private ViewPager fragment_vp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = (PullToRefreshListView) view.findViewById(R.id.lv);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在加载中");
        dialog.show();
        Bundle bundle = getArguments();
        style = bundle.getInt("style");
        path1 = bundle.getString("path1");
        path2 = bundle.getString("path2");
        tablename = bundle.getString("tablename");
        dbManger = new DBManger(getActivity(), tablename);
        DownLoad.JsonDownLoad(path1 + page + path2, this);
        adapter = new MyAdapter(getActivity(), style);
        if (style == 1) {
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.viewpager, null);
            fragment_iv_01 = (ImageView) view1.findViewById(R.id.fragment_iv_01);
            fragment_iv_02 = (ImageView) view1.findViewById(R.id.fragment_iv_02);
            fragment_iv_03 = (ImageView) view1.findViewById(R.id.fragment_iv_03);
            ArrayList<View> viewList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                View view2 = getActivity().getLayoutInflater().inflate(R.layout.fragment_vp_item, null);
                viewList.add(view2);
            }
            MyPagerAdapter adapter1 = new MyPagerAdapter(viewList);
            fragment_vp = (ViewPager) view1.findViewById(R.id.framgent_vp);
            fragment_vp.setAdapter(adapter1);
            lv.getRefreshableView().addHeaderView(view1);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public synchronized void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment_vp.setCurrentItem(count, true);
                            count++;
                            if (count == 3) {
                                count = 0;
                            }
                        }
                    });
                }
            }, 0, 2000);
            fragment_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (count) {
                        case 0:
                            fragment_iv_01.setImageResource(R.drawable.iv_yes);
                            fragment_iv_02.setImageResource(R.drawable.iv_no);
                            fragment_iv_03.setImageResource(R.drawable.iv_no);
                            break;
                        case 1:
                            fragment_iv_02.setImageResource(R.drawable.iv_yes);
                            fragment_iv_01.setImageResource(R.drawable.iv_no);
                            fragment_iv_03.setImageResource(R.drawable.iv_no);
                            break;
                        case 2:
                            fragment_iv_03.setImageResource(R.drawable.iv_yes);
                            fragment_iv_02.setImageResource(R.drawable.iv_no);
                            fragment_iv_01.setImageResource(R.drawable.iv_no);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        lv.setAdapter(adapter);
        lv.setOnRefreshListener(this);
        lv.setOnLastItemVisibleListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (style == 1) {
                    Bean bean = adapter.getItem(position - 2);
                    if (position > 1) {
                        Intent i = new Intent(getActivity(), WebActivity.class);
                        i.putExtra("web", Path.XIANGQING_PATH + bean.getId());
                        startActivity(i);
                    }
                } else {
                    if (position > 0) {
                        Bean bean = adapter.getItem(position - 1);
                        Intent i = new Intent(getActivity(), WebActivity.class);
                        i.putExtra("web", Path.XIANGQING_PATH + bean.getId());
                        startActivity(i);
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(timer!=null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public synchronized void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment_vp!=null) {
                            fragment_vp.setCurrentItem(count, true);
                        }
                        count++;
                        if (count == 3) {
                            count = 0;
                        }
                    }
                });
            }
        }, 0, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public static MyFragment newInstance(String path1, String path2, int style, String tablename) {
        Bundle args = new Bundle();
        args.putInt("style", style);
        args.putString("path1", path1);
        args.putString("path2", path2);
        args.putString("tablename", tablename);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void getJson(String data) {
        if (data != null && !"".equals(data)) {
            if (page == 1) {
                dbManger.delete();
            }
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o1 = array.getJSONObject(i);
                    String id = o1.optString("id");
                    String title = o1.optString("title");
                    String description = o1.optString("description");
                    String cover_url = o1.optString("cover_url");
                    Bean bean = new Bean(title, id, description, cover_url);
                    list.add(bean);
                    if (page == 1) {

                        dbManger.insert(id, title, cover_url);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
            dialog.dismiss();
            adapter.addData(list);
        } else {
            a = 11;
            Cursor cursor = dbManger.query();
            list.clear();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String img_url = cursor.getString(cursor.getColumnIndex("img_url"));
                Bean bean = new Bean();
                bean.setId(id);
                bean.setTitle(title);
                bean.setCover_url(img_url);
                list.add(bean);
            }
            dialog.cancel();
            dialog.dismiss();
            adapter.clean();
            adapter.addData(list);
        }
    }


    @Override
    public void onLastItemVisible() {
        page++;
        list.clear();
        DownLoad.JsonDownLoad(path1 + page + path2, this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        page = 1;
        a = 10;
        adapter.clean();
        list.clear();
        DownLoad.JsonDownLoad(path1 + page + path2, this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv.onRefreshComplete();
            }
        },2000);

    }

    class MyAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private ArrayList<Bean> mylist;
        private int type;

        public MyAdapter(Context context, int type) {
            inflater = LayoutInflater.from(context);
            mylist = new ArrayList<>();
            this.type = type;
        }

        public void addData(ArrayList<Bean> mylist) {
            this.mylist.addAll(mylist);
            notifyDataSetChanged();
        }

        public void clean() {
            this.mylist.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public Bean getItem(int position) {
            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (type == 1) {
                int types = getItemViewType(position);
                if (types == 1) {
                    ViewHolder holder = null;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = inflater.inflate(R.layout.itemtype1, null);
                        holder.tv = (TextView) convertView.findViewById(R.id.itemtype1_tv);
                        holder.iv = (ImageView) convertView.findViewById(R.id.itemtype1_iv);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    Bean bean = getItem(position);
                    holder.tv.setText(bean.getTitle());
                    String sp = MySp.newInstance(getActivity()).getSp();
                    if (sp != null && sp.equals("1")) {
                        Bitmap bitmap = BitmapCompress.newIntence().getBitmap(bean.getCover_url());
                        if (bitmap != null) {
                            holder.iv.setImageBitmap(bitmap);
                        } else {
                            Glide.with(MyFragment.this)
                                    .load(bean.getCover_url())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.mipmap.ic_launcher)
                                    .into(holder.iv);
                        }
                    }
                } else if (types == 2) {
                    ViewHolder holder = null;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = inflater.inflate(R.layout.itemtype2, null);
                        holder.tv = (TextView) convertView.findViewById(R.id.itemtype2_tv);
                        holder.iv = (ImageView) convertView.findViewById(R.id.itemtype2_iv);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    Bean bean = getItem(position);
                    holder.tv.setText(bean.getTitle());
                    String sp = MySp.newInstance(getActivity()).getSp();
                    if (sp != null && sp.equals("1")) {
                        Bitmap bitmap = BitmapCompress.newIntence().getBitmap(bean.getCover_url());
                        if (bitmap != null) {
                            holder.iv.setImageBitmap(bitmap);
                        } else {
                            Glide.with(MyFragment.this)
                                    .load(bean.getCover_url())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(android.R.drawable.ic_delete)
                                    .into(holder.iv);
                        }
                    }
                }
            }
            if (type == 2) {
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.itemtype1, null);
                    holder.tv = (TextView) convertView.findViewById(R.id.itemtype1_tv);
                    holder.iv = (ImageView) convertView.findViewById(R.id.itemtype1_iv);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                Bean bean = getItem(position);
                holder.tv.setText(bean.getTitle());
                String sp = MySp.newInstance(getActivity()).getSp();
                if (sp != null && sp.equals("1")) {
                    Bitmap bitmap = BitmapCompress.newIntence().getBitmap(bean.getCover_url());
                    if (bitmap != null) {
                        holder.iv.setImageBitmap(bitmap);
                    } else {
                        Glide.with(MyFragment.this)
                                .load(bean.getCover_url())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(holder.iv);
                    }
                }
            }
            return convertView;
        }
        class ViewHolder {
            ImageView iv;
            TextView tv;
        }
        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return 1;
            } else {
                return 2;
            }
        }
        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> list;

        public MyPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (timer != null)
              timer.cancel();
    }
}
