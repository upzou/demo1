package fastandroid.fast.com.cn.fastandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SettingInfo {
	// this is the log tag
	public static final String TAG = "SettingInfo";
		
	private DatabaseHelper dbhelper = null;
	
	public SettingInfo(Context context)
	{
		dbhelper = new DatabaseHelper(context);
	}
	
	//根据设置项名称查找值
	public String GetSettingValueByKey(String KeyName)
	{
		//Log.e(TAG,"KeyName = "+KeyName);
		String sValue = "";
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		try{
			Cursor cursor = db.rawQuery("SELECT * FROM fast_setting WHERE SettingName = ?", new String[]{KeyName});
			//Cursor cursor = db.query(DatabaseHelper.TABLE_Setting, null, null, null, null, null, null);  
			if(cursor.getCount() > 0)//存在记录
			{
				cursor.moveToFirst();//第一条信息即可，指只对该条信息进行处理

				sValue = cursor.getString(2);
				cursor.close();//释放游标资源
				//Log.e(TAG,"sValue = "+sValue);
			}
			else
			{
				Log.i(TAG,"没有找到数据");
			}
		}
		catch (Exception e) {
            e.printStackTrace();
        } 
		finally {
            if (db != null) {
                // finally中关闭数据库
            	db.close();
            }
        }
		return sValue;
	}
	
	/*
	 * 设置值
	 */
	public Boolean SetSettingValueByKey(String KeyName, String Value)
	{
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		try{
			Cursor cursor = db.rawQuery("SELECT * FROM fast_setting WHERE SettingName=?", new String[]{KeyName});
			ContentValues cv = new ContentValues();
	        cv.put("SettingName", KeyName);  
	        cv.put("Value", Value);
			
	        if(cursor.getCount() > 0)
			{
		        //插入ContentValues中的数据  
		        db.update(DatabaseHelper.TABLE_Setting, cv, "SettingName=?", new String[]{KeyName});
		        Log.i(TAG,"存在记录就更新");
			}
			else//不存在则插入
			{
				db.insert(DatabaseHelper.TABLE_Setting, null, cv);  
				Log.i(TAG,"不存在则插入");
			}
			cursor.close();//释放游标资源
		}
		catch (Exception e) {
            e.printStackTrace();
        } 
		finally {
            if (db != null) {
                // finally中关闭数据库
            	db.close();
            }
        }
		return true;
	}
}
