package tool;

/**
 * Created by Administrator on 2016/10/15.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/10/12.
 */

public class BitmapCompress {
    private LruCache<String, Bitmap> lruCache;
    private HashMap<String, SoftReference<Bitmap>> map;

    private BitmapCompress() {
        Runtime runtime = Runtime.getRuntime();
        //手机最大运行内存
        long l = runtime.maxMemory();
//        ViewGroup.LayoutParams params = iv.getLayoutParams();
//        int width=params.width;
//        int height=params.height;
//        int compresswidth= (int) (l/width);
//        int compressheight= (int) (l/height);
        lruCache = new LruCache<>((int) (l / 8));
        map = new LinkedHashMap<>();
    }

    private static BitmapCompress bitmapCompress;

    public static BitmapCompress newIntence() {
        if (bitmapCompress == null) {
            bitmapCompress = new BitmapCompress();
        }
        return bitmapCompress;
    }
    public Bitmap getBitmap(String path){
        Bitmap bitmap=null;
        if(path!=null){
            bitmap= lruCache.get(path);
            if(bitmap==null){
                SoftReference<Bitmap> soft = map.get(path);
                if(soft!=null){
                    bitmap = soft.get();
                    if(bitmap!=null){
                        lruCache.put(path,bitmap);
                    }
                }
            }
        }
        return bitmap;
    }
    public void putBitmap(String path, Bitmap bitmap) {
        lruCache.put(path, bitmap);
        SoftReference<Bitmap> soft = new SoftReference<>(bitmap);
        map.put(path, soft);
    }
    /**
     * 二次采样
     */

    public static Bitmap getSmallBitmap(Bitmap bitmap, ImageView iv){
        Bitmap smllbitmap=null;
        int ivw = iv.getWidth();
        int ivh = iv.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        int wmin= (ivw/ivw);
        int hmin= (ivh/ivh);
        options.inSampleSize=Math.min(wmin,hmin);
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        /**
         * 对图片进行压缩
         */
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        if(compress){
            byte[] bytes = bos.toByteArray();
            smllbitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            return smllbitmap;
        }
        return null;
    }

}

