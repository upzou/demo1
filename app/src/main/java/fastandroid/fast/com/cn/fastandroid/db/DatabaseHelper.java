package fastandroid.fast.com.cn.fastandroid.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//数据库访问辅助类
public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static String TAG				= "DatabaseHelper";
	private static int 					version 		= 1;
	private static String db_name 		= "fast.db";
	
	public static String TABLE_Login 	= "fast_loginInfo";
	public static String TABLE_Setting 	= "fast_setting";
	public static String TABLE_News		= "fast_news";
	
	//public DatabaseHelper(Context context, String name, CursorFactory cursorFactory, int version) 
	public DatabaseHelper(Context context)
	{     
		super(context, db_name, null, version);     
	}     
	     
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作     
		String sql1 = "CREATE TABLE IF NOT EXISTS " + TABLE_Login
				+ " (ID INTEGER PRIMARY KEY, UserName TEXT, PassWord TEXT, EncryptPW TEXT, OnlyCode TEXT);";
		String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_Setting
				+ " (ID INTEGER PRIMARY KEY, SettingName TEXT, Value TEXT);"; 
		String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_News
				+ " (ID INTEGER PRIMARY KEY, NewsID INTEGER, NewsTitle TEXT, "
				+ "NewsSenduser TEXT, NewsTime TEXT, NewsContent TEXT, BriefContent TEXT, "
				+ "isRead INTEGER, ThumbnailURL TEXT);";
		try {
			db.beginTransaction(); 
			
			db.execSQL(sql1);
			db.execSQL(sql2); 
			db.execSQL(sql3); 
			
			db.setTransactionSuccessful(); 
			Log.i(TAG,"数据表成功创建");
		} catch (SQLException ex) {
			Log.i(TAG,"数据表创建错误:" + ex.getMessage());
		}
		finally
		{
			db.endTransaction();  
	        Log.i(TAG,"Create finish()!!");
		}
	}     
	     
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DatabaseHelper","oldVersion="+oldVersion+" ,newVersion="+newVersion);
		int upgradeVersion  = oldVersion;
		// TODO 更改数据库版本的操作     
		
//		if(1 == upgradeVersion)
//		{
//			Log.i(TAG,"upgrade Version from 1 to 2");
//			try
//			{
//				db.beginTransaction(); 
//				// Create table fast_setting 
//				String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_Setting
//						+ " (ID INTEGER PRIMARY KEY, SettingName TEXT, Value TEXT);";
//		        db.execSQL(sql);  
//		        upgradeVersion = 2; 
//		        
//		        db.setTransactionSuccessful(); 
//		        
//			} 
//		    catch (Exception e)  
//		    {  
//		    	Log.i(TAG,"Upgrade Version 1 to 2 Fail!!");
//		        e.printStackTrace();  
//		    }  
//		    finally  
//		    {  
//		        db.endTransaction();  
//		        Log.i(TAG,"upgradeVersion 1 to 2 OK!!\n");
//		    } 
//		}
		
		if(upgradeVersion != newVersion)
		{
			//升级失败，初始化数据库
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Login);  
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Setting);  
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_News);  
	        
			onCreate(db);
		}
	}     
	     
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);       
		// TODO 每次成功打开数据库后首先被执行     
	}  

}
