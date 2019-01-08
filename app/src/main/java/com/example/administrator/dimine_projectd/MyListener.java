package com.example.administrator.dimine_projectd;

import android.os.Handler;
import android.os.Message;

public class MyListener implements PullToRefreshLayout.OnRefreshListener
{

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
	{
		// 下拉刷新操作
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				//刷新完毕
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 5000);
	}

}
