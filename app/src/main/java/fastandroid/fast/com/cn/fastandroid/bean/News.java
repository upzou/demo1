package fastandroid.fast.com.cn.fastandroid.bean;

/**
 * Created by zzs on 2017/3/13
 */
public class News {
    private String title;
    private String content;
    private String url;
    private String time;
    private String appid;
    private boolean isRead;

    public News() {

    }

    public News(String title, String content, String url, String time, String appid, boolean isRead) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.time = time;
        this.appid = appid;
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", appid='" + appid + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
