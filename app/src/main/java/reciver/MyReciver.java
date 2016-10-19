package reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import com.liulong.day_homework.NetActivity;

import tool.MySp;

/**
 * Created by Administrator on 2016/10/15.
 */

public class MyReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager o = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = o.getActiveNetworkInfo();
        if(info!=null&&info.isConnected()){
            int type = info.getType();
            if(type==ConnectivityManager.TYPE_MOBILE){
                Intent intent1 = new Intent(context, NetActivity.class);
                context.startActivity(intent1);
                MySp.newInstance(context).putSp("0");
            }else if(type==ConnectivityManager.TYPE_WIFI){
                MySp.newInstance(context).putSp("1");
            }
        }else{
            Toast.makeText(context,"请检查网络",Toast.LENGTH_SHORT).show();
        }

    }
}
