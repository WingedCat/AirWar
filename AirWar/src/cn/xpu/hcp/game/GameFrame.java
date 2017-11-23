package cn.xpu.hcp.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import cn.xpu.hcp.entity.Plane;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.MyFrame;
import cn.xpu.hcp.tools.PlaySound;

public class GameFrame extends MyFrame {
	static boolean begin=true;//开始标志
	static int yPos = -646;
	
	private static final long serialVersionUID = 1L;
	//取得开始背景
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	//取得游戏背景
	Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	//创建我方飞机
	Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,this);
	
	public void paint(Graphics g){
		if(begin){
			g.drawImage(beginBg, 0, 0, null);
			try {
				Thread.sleep(3000);
				begin = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		g.drawImage(gameBg, 0, yPos, null);
		g.drawImage(gameBg, 0, yPos-1411, null);//两张图片交替
		
		myplane.draw(g);
	}
	
	static class BgThread extends Thread{//创建BgThread类，专门用于改名yPos使背景图片滚动
		@Override
		public void run() {
			while(true){
				if(yPos==764){
					yPos = -646;
				}else{
					if(begin==false)//真正进入游戏才开始滚动
						yPos += 2;
				}
				try {
					Thread.sleep(50);//滚动速度的设定
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//键盘监听类  
	//定义为内部类，方便使用外部类的普通属性  
	private class KeyMonitor extends KeyAdapter{  
	    @Override  
	    public void keyPressed(KeyEvent e) {  
	        myplane.Press(e);//在Plane类中创建方法，响应键盘按下时的操作  
	    }  
	  
	    @Override  
	    public void keyReleased(KeyEvent e) {  
	        myplane.Release(e);//在Plane类中创建方法，响应键盘释放时的操作  
	    }  
	      
	}  
	public static void main(String[] args) {
		GameFrame game = new GameFrame();
		game.launchFrame();
		new BgThread().start();
		new PlaySound("bgmusic.mp3", true).start();
		game.addKeyListener(game.new KeyMonitor());//添加键盘监听
	}

}
