package fastandroid.fast.com.cn.fastandroid.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CacheDao implements CacheService{

	private static final String TAG = "CacheDao";
	private DatabaseHelper dbhelper = null;
	
	public CacheDao(Context context)
	{
		dbhelper = new DatabaseHelper(context);
	}
	@Override
	public boolean ClearCache() {
		boolean bFlag = false;
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		try
		{
			String sql = "DELETE FROM "+DatabaseHelper.TABLE_News;
			db.execSQL(sql);
			bFlag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (db != null) {
                // finally中关闭数据库
            	db.close();
            }
		}
		return bFlag;
	}

}
