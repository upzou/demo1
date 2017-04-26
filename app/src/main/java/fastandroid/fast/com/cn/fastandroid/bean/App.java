package fastandroid.fast.com.cn.fastandroid.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzs on 2017/3/6
 */
public class App {
    private int appid;//": 1,
    private String icon;//": "http://www.easyicon.net/api/resizeApi.php?id=1063843&size=128",
    private String name;//": "新闻公告",
    List<MenuDetail> menu = new ArrayList<>();//":

    public App() {
    }

    public App(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public App(int appid, String icon, String name, List<MenuDetail> menu) {
        this.appid = appid;
        this.icon = icon;
        this.name = name;
        this.menu = menu;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuDetail> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuDetail> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "App{" +
                "appid=" + appid +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }
}
