package fastandroid.fast.com.cn.fastandroid.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzs on 2017/3/6
 */
public class ResponseHome {
    private int version;
    List<App> appList = new ArrayList<App>();

    public ResponseHome() {
    }

    public ResponseHome(int version, List<App> appList) {
        this.version = version;
        this.appList = appList;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<App> getAppList() {
        return appList;
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
    }

    @Override
    public String toString() {
        return "ResponseHome{" +
                "version=" + version +
                ", appList=" + appList +
                '}';
    }
}
