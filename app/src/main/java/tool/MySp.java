package tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/10/15.
 */

public class MySp {
    private static MySp mysp;
    private SharedPreferences sp;

    private MySp(Context context) {
        sp = context.getSharedPreferences("netData", Context.MODE_PRIVATE);
    }
    public static MySp newInstance(Context context) {
        if(mysp==null){
            mysp=new MySp(context);
        }
        return mysp;
    }
    public void putSp(String data){
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("net",data);
        ed.commit();
    }
    public String getSp(){
        String net = sp.getString("net", "0");
        return net;
    }


}
