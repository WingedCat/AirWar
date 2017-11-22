package cn.xpu.hcp.tools;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyFrame extends Frame{
	
	private static final long serialVersionUID = 2017618L;
	
	public void launchFrame() {//加载窗体
		 setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//设置大小
		 setLocation(400,0);//设置位置
		 setVisible(true);//设置窗体可见
		 setResizable(false);//大小不可以改变
		 new PaintTread().start();//启动重画线程
		 addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);//关闭窗口
			} 
		});
	}
	
	//使图片动起来,定义一个重画窗口的线程类
	class PaintTread extends Thread{
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}//使CPU歇会
			}
		}
	}
	
	private Image offScreenImage = null;//利用双缓冲技术消除屏幕闪烁
	public void update(Graphics g){
		if(offScreenImage == null){
			offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
}
