package fastandroid.fast.com.cn.fastandroid.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fastandroid.fast.com.cn.fastandroid.R;


public class AboutActivity extends Activity {

	private TextView mMID_title;
	private View mBackView;
	private ImageView mImageLeft;
	private TextView mLeft_title;
	private TextView mAboutTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initView();
	}
	
	private void initView()
	{
		mMID_title = (TextView)findViewById(R.id.midle_title);
		mMID_title.setText("关于");
		
		mImageLeft = (ImageView)findViewById(R.id.left_image);
		mImageLeft.setImageResource(R.drawable.angle_left);
		
		mLeft_title = (TextView)findViewById(R.id.left_title);
		mLeft_title.setText("返回");
		
		mBackView = findViewById(R.id.view_left); 
		mBackView.setOnClickListener(new View.OnClickListener() {
			//点击返回
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
		
		mAboutTxt = (TextView)findViewById(R.id.tv_about);
		mAboutTxt.setText("版本号:V2.0");
		
	}
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
	    try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	        String version = info.versionName;
	        return this.getString(R.string.version_name) + version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return this.getString(R.string.no_found_version_name);
	    }
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.about, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
