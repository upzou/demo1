package fastandroid.fast.com.cn.fastandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    private final static int VERSION = 3;
    //    private final static String DB_NAME = "testNewsss.db";

    private final static String CREATE_TBL_NEWS = "create table tesNews(_id integer primary key autoincrement, title text, content text,time text,appid text, url text,isRead text)";
    private SQLiteDatabase db;

    //SQLiteOpenHelper子类必须要的一个构造函数
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        //必须通过super 调用父类的构造函数
        super(context, name, factory, version);
    }

    //数据库的构造函数，传递三个参数的
    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    //数据库的构造函数，传递两个参数的
    public DBHelper(Context context, String DBName ) {
        this(context, DBName, null, VERSION);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
//    public DBHelper(Context context) {
//        this(context, DB_NAME, null, VERSION);
//    }

    // 回调函数，第一次创建时才会调用此函数，创建一个数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        Log.d(TAG, "Create Database");
        db.execSQL(CREATE_TBL_NEWS);
    }

    //回调函数，当你构造DBHelper的传递的Version与之前的Version调用此函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "update Database");
//        db.execSQL("ALTER TABLE testNews ADD COLUMN isRead text");

    }

    //插入方法
    public void insert(ContentValues values,String tableName) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        //插入数据库中
        db.insert(tableName, null, values);
        db.close();
    }

    //查询方法
    public Cursor query(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        //获取Cursor
        Cursor c = db.query(tableName, null, null, null, null, null, null, null);
        return c;

    }

    //根据唯一标识_id  来删除数据
    public void delete(int id,String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, "_id=?", new String[]{String.valueOf(id)});
    }


    //更新数据库的内容
    public void update(ContentValues values, String whereClause, String[] whereArgs,String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(tableName, values, whereClause, whereArgs);
    }

    //关闭数据库
    public void close() {
        if (db != null) {
            db.close();
        }
    }

}
