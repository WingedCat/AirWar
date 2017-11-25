package cn.xpu.hcp.entity;

import java.awt.Graphics;
import java.awt.Image;

import cn.xpu.hcp.game.GameFrame;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;

//使用于boss敌机的导弹
public class SuperMissile2 extends SuperMissile {

	Plane p;
	public int speed;
	public double Sinthita;
	public double Costhita;
	boolean flag=false;
	int type;
	Image img = GameImage.getImage("resources/em1.png");
	
	SuperMissile2(GameFrame gf, int x, int y) {
		super(gf, x, y);
	}

	public SuperMissile2(GameFrame gf, int x, int y, boolean good) {
		super(gf, x, y, good);
	}
	
	public SuperMissile2(GameFrame gf, int x, int y, boolean good,Plane p) {
		super(gf, x, y, good);
		this.p = p;
		this.Costhita = this.getSinthita(p);
		this.Sinthita = this.getCosthita(p);
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
    
	@Override
	public void draw(Graphics g, Plane p) {
		if(!live){
    		gf.supermissiles.remove(this);
    		return;
    	}
		g.drawImage(img, (int)x, (int)y,null);
		move();
	}

	public void move() {
		if(flag==false){
			if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)>10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)>10){
	    		type=1;
	    		x = (x + speed*this.Sinthita);
	    		y = (y + speed*this.Costhita);
	    	}
	    	else if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)<-10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)>10){
	    		type=2;
	    		x = (x - speed*this.Sinthita);
	    		y = (y + speed*this.Costhita);
	    	}
	    	else if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)<-10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)<-10){
	    		type=3;
	    		x = (x -speed*this.Sinthita);
	    		y = (y - speed*this.Costhita);
	    	}
	    	else if((p.x+p.WIDTH/2-this.x-this.WIDTH/2)>10&&(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)<-10){
	    		type=4;
	    		x = (x + speed*this.Sinthita);
	    		y = (y - speed*this.Costhita);
	    	}
	    	else if(Math.abs(p.x+p.WIDTH/2-this.x-this.WIDTH/2)<30&&(p.y+p.HEIGHT/2)<(this.y+this.HEIGHT/2)){
	    		type=5;
	    		y -= speed;
	    	}
	    	else if(Math.abs(p.x+p.WIDTH/2-this.x-this.WIDTH/2)<30&&(p.y+p.HEIGHT/2)>(this.y+this.HEIGHT/2)){
	    		type=6;
	    		y += speed;
	    	}
	    	else if((p.x+p.WIDTH/2)<(this.x+this.WIDTH/2)&&Math.abs(p.y+p.HEIGHT/2 - this.y-this.HEIGHT/2)<30){
	    		type=7;
	    		x -= speed;
	    	}
	    	else if((p.x+p.WIDTH/2)>(this.x+this.WIDTH/2)&&Math.abs(p.y+p.HEIGHT/2-this.y-this.HEIGHT/2)<30){
	    		type=8;
	    		x += speed;
	    	}
			flag = true;
		}else{
			switch (type) {
			case 1:
				x = (x + speed*Sinthita);
				y = (y + speed*Costhita);
				break;
			case 2:
				x = (x - speed*Sinthita);
				y = (y + speed*Costhita);
				break;
			case 3:
				x = (x -speed*Sinthita);
				y = (y - speed*Costhita);
				break;
			case 4:
				x = (x + speed*Sinthita);
				y = (y - speed*Costhita);
				break;
			case 5:
				y -= speed;
				break;
			case 6:
				y += speed;
				break;
			case 7:
				x -= speed;
				break;
			case 8:
				x += speed;
				break;

			default:
				break;
			}
		}
		
//    	if((System.currentTimeMillis()-start)/1000>=10){
//    		System.out.println("时间到");
//    		this.live =false;
//    	}
		if(x < 0 || y < 0 || x > Constant.GAME_WIDTH || y > Constant.GAME_HEIGHT) {  
            this.live = false;//出界就设置为false  
        }
	}
	
	@Override
	public boolean hitTank(Plane temp){
    	if(this.live && this.getRect().intersects(temp.getRect()) && temp.getAlive() && this.good != temp.isgood()) {//不是己方子弹并且发生了碰撞  
	    	this.live = false;//子弹应该消失 
	    	//除了boss机型，其他敌机对我的子弹是杀伤力100%
	        if(this.good&&temp.isBoss()==false){
	        	temp.setAlive(false);
	        }
	        temp.setLife(temp.getLife()-5);
	        if(temp.getLife()<=0||temp.getAlive()==false){
	        	if(temp.isgood()==false){
	        		temp.setAlive(false);//飞机死亡  
	        		Explode e = new Explode((int)x,(int) y, gf);  
	        		gf.explodes.add(e);//添加爆炸
	        		return true;
	        	}else{
	        		if(temp.lifeNum==0){
	        			temp.setAlive(false);//飞机死亡  
		        		Explode e = new Explode((int)x,(int) y, gf);  
		        		gf.explodes.add(e);//添加爆炸
		        		return true;
	        		}else{
	        			temp.lifeNum--;
	        			temp.setLife(100);
	        			temp.setAlive(true);//飞机死亡  
		        		return true;
	        		}
	        	}
	        }
	    }  
		return false;
	}
	
	
	
	
	
	

}
