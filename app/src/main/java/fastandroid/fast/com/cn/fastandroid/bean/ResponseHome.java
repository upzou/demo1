package fastandroid.fast.com.cn.fastandroid.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzs on 2017/3/6
 */
public class ResponseHome {
    private int menuver;
    private List<App> appList = new ArrayList<App>();
    private Appver appver;

    public ResponseHome() {
    }

    public ResponseHome(int menuver, List<App> appList, Appver appver) {
        this.menuver = menuver;
        this.appList = appList;
        this.appver = appver;
    }

    public int getMenuver() {
        return menuver;
    }

    public void setMenuver(int menuver) {
        this.menuver = menuver;
    }

    public List<App> getAppList() {
        return appList;
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
    }

    public Appver getAppver() {
        return appver;
    }

    public void setAppver(Appver appver) {
        this.appver = appver;
    }

    @Override
    public String toString() {
        return "ResponseHome{" +
                "menuver=" + menuver +
                ", appList=" + appList +
                ", appver=" + appver +
                '}';
    }
}
