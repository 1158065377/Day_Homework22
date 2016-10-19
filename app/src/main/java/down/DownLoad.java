package down;

import java.io.BufferedOutputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import tool.BitmapCompress;

public class DownLoad {
    private static Handler handler = new Handler();
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static  String data=null;
    public interface OnjsonDownLoad {
        void getJson(String data);
    }
    public static void JsonDownLoad(final String path,
                                    final OnjsonDownLoad download) {
        pool.execute(new Runnable() {

            @Override
            public void run() {
                InputStream is = null;
                BufferedOutputStream bos = null;

                try {

                    URL url = new URL(path);

                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        is = conn.getInputStream();
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        bos = new BufferedOutputStream(os);
                        byte[] b = new byte[1024];
                        int len = 0;
                        while ((len = is.read(b)) != -1) {
                            bos.write(b, 0, len);
                        }
                        bos.flush();
                        byte[] bs = os.toByteArray();
                        data = new String(bs, 0, bs.length);
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            download.getJson(data);
                        }
                    });
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    public static void ImageDownLoad(final ImageView iv, final String path) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        final Bitmap smallBitmap = BitmapCompress.getSmallBitmap(bitmap, iv);
                        BitmapCompress.newIntence().putBitmap(path,smallBitmap);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String tag = (String) iv.getTag();
                                if (tag != null && tag.equals(path)) {
                                    iv.setImageBitmap(smallBitmap);
                                }
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}

