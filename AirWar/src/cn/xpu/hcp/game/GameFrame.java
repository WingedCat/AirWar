package cn.xpu.hcp.game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import cn.xpu.hcp.entity.Bullet;
import cn.xpu.hcp.entity.CreateEnemyThread;
import cn.xpu.hcp.entity.Explode;
import cn.xpu.hcp.entity.Plane;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.MyFrame;
import cn.xpu.hcp.tools.PlaySound;

public class GameFrame extends MyFrame {
	//取得游戏背景
	public static Image gameBg = GameImage.getImage("resources/background1.bmp");
	
	public  long start = System.currentTimeMillis();
	
	public boolean success = false;
	
	//获取血条
	Image bloodImg = GameImage.getImage("resources/blood.png");
	
	static boolean begin=true;//开始标志
	static int yPos = -1*(gameBg.getHeight(null)-Constant.GAME_HEIGHT)+1;
	static int yPos2 = yPos - gameBg.getHeight(null);
	
	static boolean useMouse = true;
	public static PlaySound pbg = new PlaySound("bgmusic.mp3", true);
	
	private static final long serialVersionUID = 1L;
	//取得开始背景
	Image beginBg = GameImage.getImage("resources/startbg1.jpg");
	
	//创建我方飞机
	public Plane myplane = new Plane(Constant.GAME_WIDTH/2,650,10,true,false,this);
	//创建敌方飞机集合
	public  List<Plane> es = new LinkedList<Plane>(); 
	//创建子弹集合
	public List<Bullet> bs = new LinkedList<Bullet>();
	//创建爆炸集合
	public List<Explode> explodes = new LinkedList<Explode>();
	public Plane boss = new Plane(-100,-100,0,false,true,this);
	public Plane temp = boss;
	
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
		g.drawImage(gameBg, 0, yPos2, null);//两张图片交替
//		g.drawImage(gameBg, 0, yPos - 2*gameBg.getHeight(null), null);
//		g.drawString("子弹数量："+bs.size(), 100, 100);
//		g.setColor(Color.red);
//		g.drawString("敌机数量："+es.size(), 100, 100); 
		
		myplane.draw(g);
		//绘制子弹
		for(int i=0; i<bs.size(); i++) {//将集合中的子弹都绘制出来  
	        Bullet b = bs.get(i);  
	        b.draw(g);  
	        b.hitPlanes(es);
	        b.hitPlane(myplane);
	    }
		//绘制敌机
		for(int i=0; i<es.size(); i++) {  
	        Plane p = es.get(i);
	        if(p.isBoss()){
	        	boss = p;
				g.setColor(Color.blue);
				g.fillRect(48, 30, (int)Math.round(214*(p.getLife()*0.001)), 10);
				g.setColor(Color.white);
				if(p.getLife()<=0){
					p.setLife(0);
				}
				g.drawString(p.getLife()+"", 155, 40);
			}
	        p.draw(g);  
	    }
		//绘制爆炸
		for(int i=0; i<explodes.size(); i++) {  
		    Explode e = explodes.get(i);  
		    e.draw(g);  
		    new PlaySound("BombSound_solider.mp3", false).start();
		}
		
		g.setColor(Color.RED);
		g.fillRect(48, 55, (int)Math.round(214*(myplane.getLife()*0.01)), 10);
		g.drawImage(bloodImg, 0, 30, null);
		g.setColor(Color.white);
		if(myplane.getLife()<=0){
			myplane.setLife(0);
		}
		g.drawString(myplane.getLife()+"", 155, 65);
		if(boss.getLife()==0){
			g.drawImage(GameImage.getImage("resources/win.png"), 210, 330, null);
			for(long i=0;i<=200000;i++){
				
			}
			success = true;//成功通过
		}
		
	}
	
	static class BgThread extends Thread{//创建BgThread类，专门用于改名yPos使背景图片滚动
		@Override
		public void run() {
			while(true){
				if(yPos>=Constant.GAME_HEIGHT){
					yPos = yPos2 -gameBg.getHeight(null);
				}else{
					if(yPos2>=Constant.GAME_HEIGHT){
						yPos2 = yPos - gameBg.getHeight(null);
					}else{
						if(begin==false){//真正进入游戏才开始滚动
							yPos += 2;
							yPos2 +=2;
						}
					}
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
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getModifiers()==16)
			 myplane.fire();
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
				if(e.getModifiers()==16){
					myplane.fire(0);
				}
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
		pbg.start();
		
		game.addKeyListener(game.new KeyMonitor());//添加键盘监听
		game.setCursor(null);
		game.addMouseListener(game.new MouseMonitor());
		game.addMouseMotionListener(game.new MouseMonitor());//添加鼠标监听
		new CreateEnemyThread(game).start();//开启检测线程，检测敌机数量
	}

}
