package cn.xpu.hcp.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import cn.xpu.hcp.game.GameFrame;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;

public class Bullet {
	private static Random r = new Random(); 
	int randIndex;
	private int x,y;//子弹的位置
	int WIDTH,HEIGHT;//子弹的宽和高

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	private boolean good;//子弹是我还是对方（敌机）发射的；我设置为true，敌机设置为false
	private boolean isAlive = true;//表明子弹是否应该消失，击中活着出界就设置为false
	private int power;//子弹的杀伤力
	private int speed;//子弹的移动速度
	private boolean isBoss;//是否为boss机型子弹
	private Direction dir;//子弹的方向，我是向上，敌方向下
	private GameFrame gf;
	
	private static Image[] myImgs=new Image[13];//存放我方子弹图片的数组
	private static Image[] enemyImgs = new Image[9];//存放敌方普通机型子弹的图片
	private static Image[] bossImgs = new Image[5];//存放敌方boss机型子弹的图片
	private Image ensureImg;//具体确定的图片
	
	static{
		//我方飞机子弹图片
		//低级子弹
		myImgs[0]=GameImage.getImage("resources/m5.png");  
	    myImgs[1]= GameImage.getImage("resources/m6.png");  
	    myImgs[2]= GameImage.getImage("resources/m7.png");  
	    //中级子弹
	    myImgs[3]= GameImage.getImage("resources/m1.png"); 
	    //高级子弹
	    myImgs[4]= GameImage.getImage("resources/m2.png");  
	    myImgs[5]= GameImage.getImage("resources/m3.png");  
	    myImgs[6]= GameImage.getImage("resources/m4.png"); 
	    myImgs[7]= GameImage.getImage("resources/m8.png");
	    myImgs[8]= GameImage.getImage("resources/m9.png");
	    //超级炮弹
	    myImgs[9]= GameImage.getImage("resources/m10.png");  
	    myImgs[10]= GameImage.getImage("resources/m11.png"); 
	    myImgs[11]= GameImage.getImage("resources/m12.png");
	    myImgs[12]= GameImage.getImage("resources/m13.png");
	    
	    //敌方普通飞机子弹图片
	    enemyImgs[0]=GameImage.getImage("resources/em2.png");  
	    enemyImgs[1]= GameImage.getImage("resources/em3.png");  
	    enemyImgs[2]= GameImage.getImage("resources/em4.png");  
	    enemyImgs[3]= GameImage.getImage("resources/em5.png");  
	    enemyImgs[4]= GameImage.getImage("resources/em6.png"); 
	    
	    enemyImgs[5]= GameImage.getImage("resources/em7.png");  
	    
	    enemyImgs[6]= GameImage.getImage("resources/em8.png");  
	    enemyImgs[7]= GameImage.getImage("resources/em9.png"); 
	    enemyImgs[8]= GameImage.getImage("resources/em1.png"); 
	    
	    //boss子弹图片
	    //低级子弹
	    bossImgs[0]=GameImage.getImage("resources/bm3.png");  
	    bossImgs[1]= GameImage.getImage("resources/bm5.png");
	    //高级子弹
	    bossImgs[2]= GameImage.getImage("resources/bm4.png");
	    //大招
	    bossImgs[3]= GameImage.getImage("resources/bm1.png");  
	    bossImgs[4]= GameImage.getImage("resources/bm2.png"); 
	}
	
	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public Bullet(int x,int y,int speed,int randIndex,boolean good,boolean boss,GameFrame gf){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.good = good;
		this.gf = gf;
		this.randIndex = randIndex;
		this.isBoss = boss;
		if(good==true){
			//开始只能是低级子弹，通过拾取宝贝升级子弹
			ensureImg = myImgs[randIndex];
			//根据不同等级的子弹设置不同等级的杀伤力
			if(randIndex>=0&&randIndex<=2){
				this.power = 10;
			}else{
				if(randIndex==3){
					this.power = 20;
				}else{
					if(randIndex>=4&&randIndex<=8){
						this.power = 50;
					}else{
						this.power = 100;
					}
				}
			}
			dir = Direction.U;
			WIDTH = ensureImg.getWidth(null);
			HEIGHT = ensureImg.getHeight(null);
		}else{
			dir = Direction.D;
			if(isBoss){
				//开始发射低级子弹，随机发射高级和大招
//				this.randIndex = r.nextInt(2);
				this.randIndex = randIndex;
				ensureImg = bossImgs[this.randIndex];
				WIDTH = ensureImg.getWidth(null);
				HEIGHT = ensureImg.getHeight(null);
				//根据不同等级的子弹设置不同等级的杀伤力
				if(randIndex>=0&&randIndex<=1){
					this.power = 10;
				}else{
					if(randIndex==2){
						this.power = 20;
					}else{
						if(randIndex>=3&&randIndex<=4){
							this.power = 50;
						}
					}
				}
			}else{
				ensureImg = enemyImgs[randIndex];
				WIDTH = ensureImg.getWidth(null);
				HEIGHT = ensureImg.getHeight(null);
				//根据不同等级的子弹设置不同等级的杀伤力
				if(randIndex>=0&&randIndex<=4){
					this.power = 2;
				}else{
					if(randIndex==5){
						this.power = 5;
					}else{
						if(randIndex>=6&&randIndex<=8){
							this.power = 15;
						}
					}
				}
			}
		}
		
	}
	
	public void draw(Graphics g){
		if(!isAlive){
			gf.bs.remove(this);//销毁子弹
			return;//子弹消失了，不画
		}
		g.drawImage(ensureImg,x,y,null);
		move();
	}
	
	private void move() {  
        switch(dir) {  
        case U:  
        	y -= speed;  
        	break; 
        case D:
        	y += speed;  
        	break;  
        }  
        if(x < 0 || y < 0 || x > Constant.GAME_WIDTH || y > Constant.GAME_HEIGHT) {  
            isAlive = false;//出界就设置为false  
        }
	}
	
	public boolean hitPlane(Plane p) {  
	    if(this.isAlive && this.getRect().intersects(p.getRect()) && p.getAlive() && this.good != p.isgood()) {//不是己方子弹并且发生了碰撞  
	    	this.isAlive = false;//子弹应该消失 
	    	//除了boss机型，其他敌机对我的子弹是杀伤力100%
	        if(this.good&&p.isBoss()==false){
	        	p.setAlive(false);
	        }
	    	p.setLife(p.getLife()-this.power);
	        if(p.getLife()<=0||p.getAlive()==false){
	        	if(p.isgood()==false){
	        		p.setAlive(false);//飞机死亡  
	        		Explode e = new Explode(x, y, gf);  
	        		gf.explodes.add(e);//添加爆炸
	        		return true;
	        	}else{
	        		if(p.lifeNum==0){
	        			p.setAlive(false);//飞机死亡  
		        		Explode e = new Explode(x, y, gf);  
		        		gf.explodes.add(e);//添加爆炸
		        		return true;
	        		}else{
	        			p.lifeNum--;
	        			p.setLife(100);
	        			p.setAlive(true);//飞机死亡  
		        		return true;
	        		}
	        	}
	        }
	    }  
	    return false;  
	}  
	      
	private Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitPlanes(List<Plane> planes) {  
	    for(int i=0; i<planes.size(); i++) {  
	        if(hitPlane(planes.get(i))) {  
	            return true;  
	        }  
	    }  
	    return false;  
	}
}
