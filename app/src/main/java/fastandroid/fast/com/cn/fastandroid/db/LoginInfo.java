package fastandroid.fast.com.cn.fastandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoginInfo {

    private DatabaseHelper dbhelper = null;

    public LoginInfo(Context context) {
        dbhelper = new DatabaseHelper(context);
    }

    private int mID;

    public int GetID() {
        return mID;
    }

    private String mUserName;

    public String GetUserName() {
        return mUserName;
    }

    private String mPassword;

    public String GetPassword() {
        return mPassword;
    }

    private String mEncryptPW;

    public String GetEncryptPassword() {
        return mEncryptPW;
    }

    private String mOnlyCode;

    public String GetOnlyCode() {
        return mOnlyCode;
    }

    //获取登录信息
    public void GetLoginInfo() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(DatabaseHelper.TABLE_Login, null, null, null, null, null, null);
            if (cursor.getCount() > 0)//存在历史登录
            {
                cursor.moveToFirst();//第一条信息即可，指只对该条信息进行处理
                mID = cursor.getInt(0);
                mUserName = cursor.getString(1);
                mPassword = cursor.getString(2);
                mEncryptPW = cursor.getString(3);
                mOnlyCode = cursor.getString(4);
            } else {
                System.out.println("没有找到数据，需要用户输入");
            }
            cursor.close();//释放游标资源
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
    }

    //保存登录信息
    public void Save(String sUserName, String sPassword, String sEncryptPW, String sOnlyCode) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            Cursor cursor = db.query(DatabaseHelper.TABLE_Login, null, null, null, null, null, null);
            if (cursor.getCount() > 0)//存在历史登录,直接修改该条数据即可
            {
                cursor.moveToFirst();
                ContentValues cv = new ContentValues();
                cv.put("UserName", sUserName);
                cv.put("PassWord", sPassword);
                cv.put("EncryptPW", sEncryptPW);
                cv.put("OnlyCode", sOnlyCode);
                String[] parms = {String.valueOf(cursor.getInt(0))};
                db.update(DatabaseHelper.TABLE_Login, cv, "ID=?", parms);
                cursor.close();//释放游标资源
            } else//空表格上则新建一条记录
            {
                ContentValues cv = new ContentValues();
                cv.put("UserName", sUserName);
                cv.put("PassWord", sPassword);
                cv.put("EncryptPW", sEncryptPW);
                cv.put("OnlyCode", sOnlyCode);
                db.insert(DatabaseHelper.TABLE_Login, null, cv);
                cursor.close();//释放游标资源
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                // finally中关闭数据库
                db.close();
            }
        }
    }


}
