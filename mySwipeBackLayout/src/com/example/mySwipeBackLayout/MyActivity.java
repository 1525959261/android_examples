package com.example.mySwipeBackLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * �������� 2015/12/1 16:00
 * PS����AndroidManifest�У�Ҫʵ��android:allowBackup="true"������
 */
public class MyActivity extends SwipeBackActivity {

	 private SwipeBackLayout mSwipeBackLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mSwipeBackLayout = getSwipeBackLayout();

		// ���ÿ��Ի����������Ƽ�����Ļ���ص�һ����ָ��
        mSwipeBackLayout.setEdgeSize(200);

        // �趨�����رյķ���SwipeBackLayout.EDGE_ALL��ʾ���¡����һ������ɡ�EDGE_LEFT��EDGE_RIGHT��EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
        Button btn = (Button)findViewById(R.id.open_button);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyActivity.this, MyActivity.class));
			}
		});
	}


}
