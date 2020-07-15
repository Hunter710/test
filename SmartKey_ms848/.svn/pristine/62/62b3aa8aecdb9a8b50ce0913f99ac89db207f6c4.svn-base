package com.odm.smartkey.receiver;

/**
 * 
 * @author yy
 * @Description: �����˳���ʱ��
 * 
 * @date 2015-11-9 ����11:11:47
 */
public class Timer {
	public static Thread thread = null;

	/**
	 * 
	 * @Description: �����˳���ʱ��
	 * @param time1
	 *            ��һ�ε�ʱ��
	 * @param time2
	 *            ���ε�ʱ��
	 */
	public static void resume_time() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(15 * 1000); // 15��������ϲ������Զ��˳�����
					ActivityManager.getInstance().exit();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

	/**
	 * 
	 * @Description: ���״̬
	 */

	public static void clear() {
		thread.interrupt();
	}

}
