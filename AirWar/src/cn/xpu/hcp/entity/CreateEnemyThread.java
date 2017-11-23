package cn.xpu.hcp.entity;

import java.util.List;
import java.util.Random;

import cn.xpu.hcp.game.GameFrame;

public class CreateEnemyThread extends Thread{
	private List<Plane> es = null;
	private GameFrame gf;
	private static Random r = new Random();
	
	public CreateEnemyThread(List<Plane> es,GameFrame gf){
		this.es = es;
		this.gf = gf;
	}
	@Override
	public void run() {
		if(es.size()<6){//使敌机数量保持在6架
				Plane ePlane = new Plane(r.nextInt(500),-r.nextInt(500),5,false,gf);//敌机
				es.add(ePlane);
		}
	}

}
