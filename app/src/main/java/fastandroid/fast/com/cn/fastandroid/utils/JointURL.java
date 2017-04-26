package fastandroid.fast.com.cn.fastandroid.utils;

/**
 * Created by zzs on 2017/4/14
 */

public class JointURL {

    private static final String TAG = "JointURL";
    private static String mURLEncoder;

//    public static String JointURL( String url) {
//        SharedPreferences sp =this.getSharedPreferences("config", Context.MODE_PRIVATE);
//        String webURL = sp.getString("webURL", "");
//
//        //URL转码(特殊字符)
//        try {
//            mURLEncoder = URLEncoder.encode(webURL + url, "utf-8");
//            Log.d(TAG, "mURLEncoder: " + mURLEncoder);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        String detail_url = webURL + "Helper/Redirect?" +
//                "echostr=" + "DDEC5AC9C2A7CBDA9009F3E424885042BD541672AD5858048D7A52B1F95B1D9AEAD4E4E3266CDA9C79BD7624BB5C5B39" +
//                "&uri=" + mURLEncoder;
//        return detail_url;
//    }
}
