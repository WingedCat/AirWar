package cn.xpu.hcp.entity;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.Math;

import cn.xpu.hcp.game.GameFrame;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;

public class SuperMissile {
    public static int speed = 2;
//    private long start;
    public   int WIDTH ;
    public   int HEIGHT;
    
    public double thita;
    
    protected boolean good;
    public int power = 10;
    public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}
	GameFrame gf;
    public double x,y;
    public boolean live = true;
    private static Image[] superImage = null;

    static {
	    superImage = new Image[]{
	    		GameImage.getImage("resources/superM2.png"),GameImage.getImage("resources/superM2.png")
	    };
	    
    }
    
    SuperMissile(GameFrame gf,int x,int y){
    	this.gf =gf;
    	this.x = x;
    	this.y = y;
    	
    }
    SuperMissile(GameFrame gf,int x,int y,boolean good){
    	this(gf, x, y);
    	this.good = good;
    	WIDTH = superImage[0].getWidth(null);
    	HEIGHT = superImage[0].getHeight(null);
    }
    
    
    public void draw(Graphics g,Plane p){
    	if(!live){
    		gf.supermissiles.remove(this);
    		return;
    	}
		g.drawImage(superImage[0], (int)x, (int)y,null);
		move(p);
	}
	
	public double getSinthita(Plane p){
        double Sinthita ,l;
        l=Math.abs(((this.x+this.WIDTH/2)-(p.x+p.WIDTH/2))*((this.x+this.WIDTH/2)-(p.x+p.WIDTH/2))+(p.y+p.HEIGHT/2-(this.y+this.HEIGHT/2))*(p.y+p.HEIGHT/2-(this.y+this.HEIGHT/2)));
        Sinthita =Math.abs(p.y+p.HEIGHT/2-(this.y+this.HEIGHT/2))/(Math.sqrt(l));
        return Sinthita;
    }
    
    public double getCosthita(Plane p){
        double Costhita ,l;
        l=Math.abs((this.x+this.WIDTH/2-(p.x+p.WIDTH/2))*(this.x+this.WIDTH/2-(p.x+p.WIDTH/2))+(p.y+p.HEIGHT/2-(this.y+this.HEIGHT/2))*(p.y+p.HEIGHT/2-(this.y+this.HEIGHT/2)));
        Costhita =(Math.abs(this.x+this.WIDTH/2-(p.x+p.WIDTH/2)))/(Math.sqrt(l));
        return Costhita;
    }
    
    public void move(Plane p){
    	if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)>10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)>10){
//    		System.out.println("1");
    		x = (x + speed*this.getSinthita(p));
    		y = (y + speed*this.getCosthita(p));
    	}
    	else if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)<-10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)>10){
//    		System.out.println("2");
    		x = (x - speed*this.getSinthita(p));
    		y = (y + speed*this.getCosthita(p));
    	}
    	else if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)<-10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)<-10){
//    		System.out.println("3");
    		x = (x -speed*this.getSinthita(p));
    		y = (y - speed*this.getCosthita(p));
    	}
    	else if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)>10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)<-10){
//    		System.out.println("4");
    		x = (x + speed*this.getSinthita(p));
    		y = (y - speed*this.getCosthita(p));
    	}
    	else if(Math.abs(p.x+p.WIDTH/2-this.x-this.WIDTH/2)<30&&(p.y+p.HEIGHT/2)<(this.y+this.HEIGHT/2)){
//    		System.out.println("垂直");
    		y -= speed;
    	}
    	else if(Math.abs(p.x+p.WIDTH/2-this.x-this.WIDTH/2)<30&&(p.y+p.HEIGHT/2)>(this.y+this.HEIGHT/2)){
//    		System.out.println("垂直");
    		y += speed;
    	}
    	else if((p.x+p.WIDTH/2)<(this.x+this.WIDTH/2)&&Math.abs(p.y+p.HEIGHT/2 - this.y-this.HEIGHT/2)<30){
//    		System.out.println("水平");
    		x -= speed;
    	}
    	else if((p.x+p.WIDTH/2)>(this.x+this.WIDTH/2)&&Math.abs(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)<30){
//    		System.out.println("水平");
    		x += speed;
    	}
//    	if((System.currentTimeMillis()-start)/1000>=10){
//    		System.out.println("时间到");
//    		this.live =false;
//    	}
    	if(p.getAlive()==false){
    		this.live = false;
    		
    	}
    }
    	
    public Rectangle getRect(){
    	return new Rectangle((int)x,(int)y,WIDTH,HEIGHT);
    }
    
    public boolean hitTank(Plane p){
    	if(this.live && this.getRect().intersects(p.getRect()) && p.getAlive() && this.good != p.isgood()) {//不是己方子弹并且发生了碰撞  
	    	this.live = false;//子弹应该消失 
	    	//除了boss机型，其他敌机对我的子弹是杀伤力100%
	        if(this.good&&p.isBoss()==false){
	        	p.setAlive(false);
	        }
	    	p.setLife(p.getLife()-this.power);
	        if(p.getLife()<=0||p.getAlive()==false){
	        	if(p.isgood()==false){
	        		p.setAlive(false);//飞机死亡  
	        		Explode e = new Explode((int)x,(int) y, gf);  
	        		gf.explodes.add(e);//添加爆炸
	        		return true;
	        	}else{
	        		if(p.lifeNum==0){
	        			p.setAlive(false);//飞机死亡  
		        		Explode e = new Explode((int)x,(int) y, gf);  
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
	/*public boolean hitTanks(Tank t){       //只有引入包时，写到List，才能这样运用泛型
		
			if(hitTank(t)){
				v.start();
				return true;
				
			}
		
		return false;
	}*/
	
	public boolean hitTanks(List<Plane> planes){  
		for(int i = 0;i<planes.size();i++){
			if(hitTank(planes.get(i))){
				return true;
				
			}
		}
		return false;
	}
	
}
    









