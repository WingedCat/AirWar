package cn.xpu.hcp.game;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import cn.xpu.hcp.entity.Plane;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.MyFrame;
import cn.xpu.hcp.tools.PlaySound;

public class GameFrame extends MyFrame {
	static boolean begin=true;//开始标志
	static int yPos = -646;
	static boolean useMouse = true;
	
	private static final long serialVersionUID = 1L;
	//取得开始背景
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	//取得游戏背景
	Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	//创建我方飞机
	Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,this);
	
	//创建子弹集合
	
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
	        if(e.getKeyCode()==e.VK_F2){
	        	useMouse = !useMouse;
	        }
	    }  
	  
	    @Override  
	    public void keyReleased(KeyEvent e) {  
	        myplane.Release(e);//在Plane类中创建方法，响应键盘释放时的操作  
	    }  
	      
	}  
	
	//鼠标监听类
	private class MouseMonitor extends MouseAdapter{
		
		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("鼠标按下");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("鼠标释放");
			//左键为16，右键为4
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(useMouse){
				myplane.setX(e.getX());
				myplane.setY(e.getY());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(useMouse){
				System.out.println(e.getModifiers());
				myplane.setX(e.getX());
				myplane.setY(e.getY());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if(useMouse){
				//鼠标进入设置鼠标消失
				//设置鼠标消失的方法就是将鼠标设置为一张透明的图片
				Toolkit  tk =Toolkit.getDefaultToolkit() ;
				Image img = GameImage.getImage("resources/cur.png");
				Cursor  ret =tk.createCustomCursor(img, new Point( 10,10) ,"mycur"); 
				game.setCursor(ret);
			}
		}
	}
	
	static GameFrame game = new GameFrame();
	
	public static void main(String[] args) {
		game.launchFrame();
		new BgThread().start();
		new PlaySound("bgmusic.mp3", true).start();
		game.addKeyListener(game.new KeyMonitor());//添加键盘监听
		game.setCursor(null);
		game.addMouseListener(game.new MouseMonitor());
		game.addMouseMotionListener(game.new MouseMonitor());//添加鼠标监听
	}

}
