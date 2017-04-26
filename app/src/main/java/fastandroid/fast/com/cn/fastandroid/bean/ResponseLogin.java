package fastandroid.fast.com.cn.fastandroid.bean;

/**
 * Created by zzs on 2017/2/22
 */

public class ResponseLogin {
    private String code;
    private String errmsg;
    private String user_code;
    private String user_token;

    public ResponseLogin() {
    }

    public ResponseLogin(String code, String errmsg, String user_code, String user_token) {
        this.code = code;
        this.errmsg = errmsg;
        this.user_code = user_code;
        this.user_token = user_token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "code='" + code + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", user_code='" + user_code + '\'' +
                ", user_token='" + user_token + '\'' +
                '}';
    }
}
