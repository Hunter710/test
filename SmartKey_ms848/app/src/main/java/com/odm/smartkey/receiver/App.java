package com.odm.smartkey.receiver;

import android.app.Application;

/**
 * 
 * @author yy
 * @Description:
 * 
 * @date 2015-8-25 5:34:16
 */
public class App extends Application {

	public int PIN_PANG = 0; // ƹ�Ҽ�
	public boolean ITEM = true;
	private static App app;

	public static App getInstance() {
		if (app == null) {
			app = new App();
		}
		return app;
	}

	public boolean get_intoItem() {
		return ITEM;
	}

	/**
	 * 
	 * @Description: ��������˵���������ѡ������
	 * @param item
	 */
	public void set_intoItem(boolean item) {
		this.ITEM = item;
	}
}
