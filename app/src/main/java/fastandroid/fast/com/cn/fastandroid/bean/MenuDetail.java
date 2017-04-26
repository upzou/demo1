package fastandroid.fast.com.cn.fastandroid.bean;

import java.io.Serializable;

/**
 * Created by zzs on 2017/3/6
 */
public class MenuDetail implements Serializable {
    private String name;//":"新闻",
    private String url;//":"http://10.0.0.2/News"

    public MenuDetail() {
    }

    public MenuDetail(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Menus{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
