package cn.xpu.hcp.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import cn.xpu.hcp.game.GameFrame;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;

public class Treasure {
	private static Random r = new Random();
	int x, y, w, h;
	GameFrame gf; 
	
	public int type;//珍宝类型
	int step = 0;
	private boolean live = true;
	
	private static Image[] imgs = new Image[4];
	private Image ensureImg;
	
	private static int rx;
	private static int ry;
	static{
		//补血
		imgs[0] = GameImage.getImage("resources/t2.bmp");
		//子弹升级
		imgs[1] = GameImage.getImage("resources/t1.bmp");
		imgs[2] = GameImage.getImage("resources/t3.bmp");
		imgs[3] = GameImage.getImage("resources/t4.png");
		rx = r.nextInt(Constant.GAME_WIDTH-20);
		ry = r.nextInt(Constant.GAME_HEIGHT-20);
	}
	
	//指明血块运动的轨迹，由pos中各个点构成
	private int[][] pos = {
			          {rx, ry}, {rx+10, ry}, {rx+25, ry-25}, {rx+50, ry-100}, {rx+10, ry-30}, {rx+15, ry-10}, {rx-10, ry-20}
					  };
	
	public Treasure(GameFrame gf) {
		x = pos[0][0];
		y = pos[0][1];
		int randIndex = r.nextInt(3)%(3) + 1;
		ensureImg = imgs[randIndex];
		type=2;
		this.gf = gf;
		w = h = ensureImg.getWidth(null);
	}
	
	public Treasure(int randIndex,GameFrame gf) {
		x = pos[0][0];
		y = pos[0][1];
		randIndex = 0;
		this.gf = gf;
		ensureImg = imgs[randIndex];
		type=1;
		w = h = ensureImg.getWidth(null);
	}
	
	public void draw(Graphics g) {
		if(!live) {
			gf.ts.remove(this);
			return;
		}
		
		g.drawImage(ensureImg, x, y, null);
		
		move();
	}

	private void move() {
		step ++;
		if(step == pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w , h);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
