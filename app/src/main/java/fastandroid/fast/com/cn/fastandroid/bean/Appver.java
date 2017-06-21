package fastandroid.fast.com.cn.fastandroid.bean;

/**
 * Created by zzs on 2017/5/12
 */

public class Appver {
    private String androidver;//": "1.0",
    private String androidurl;//": "/Config/设计院管理.apk",
    private String iosver;//": "1.0",
    private String iosurl;//": "/Config/设计院管理.apk"

    public Appver() {
    }

    public Appver(String androidver, String androidurl, String iosver, String iosurl) {
        this.androidver = androidver;
        this.androidurl = androidurl;
        this.iosver = iosver;
        this.iosurl = iosurl;
    }

    public String getAndroidver() {
        return androidver;
    }

    public void setAndroidver(String androidver) {
        this.androidver = androidver;
    }

    public String getAndroidurl() {
        return androidurl;
    }

    public void setAndroidurl(String androidurl) {
        this.androidurl = androidurl;
    }

    public String getIosver() {
        return iosver;
    }

    public void setIosver(String iosver) {
        this.iosver = iosver;
    }

    public String getIosurl() {
        return iosurl;
    }

    public void setIosurl(String iosurl) {
        this.iosurl = iosurl;
    }

    @Override
    public String toString() {
        return "Appver{" +
                "androidver='" + androidver + '\'' +
                ", androidurl='" + androidurl + '\'' +
                ", iosver='" + iosver + '\'' +
                ", iosurl='" + iosurl + '\'' +
                '}';
    }
}
