package cn.xpu.hcp.entity;

import java.util.Random;
import cn.xpu.hcp.game.GameFrame;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.PlaySound;

public class CreateEnemyThread extends Thread{
	private GameFrame gf;
	private static Random r = new Random();
	private static int count=0;

	public CreateEnemyThread(GameFrame gf){
		this.gf = gf;
		System.out.println("check...");
	}

	@Override
	public void run() {
		while(true){
			System.out.println((System.currentTimeMillis()-gf.start)/1000);
			if((System.currentTimeMillis()-gf.start)/1000>=10){//一分半后，待打完所有普通敌人，boss开始出现
				if(gf.es.size()==0){
					
					if(gf.success==false){
						new PlaySound("bosscoming.mp3", false);
						gf.pbg.stop();
						gf.pbg = new PlaySound("bossing.mp3", true);
						gf.pbg.start();
						Plane boss = new Plane(r.nextInt(500),50,5,false,true,gf);//boss敌机
						boss.setLife(100);//boss生命值为1000
						gf.es.add(boss);
					}else{
						count++;
						if(count==4){
							System.out.println("count="+count);
							count=0;
						}
						gf.success = false;
						gf.pbg.stop();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						gf.boss = gf.temp;
						gf.gameBg = GameImage.getImage("resources/background"+(count+1)+".bmp");
						gf.start = System.currentTimeMillis();
						gf.myplane.setLife(100);
						gf.pbg = new PlaySound("bgmusic.mp3", true);
						gf.pbg.start();
//						count++;
						
					}
					
				}
			}else{
				if(gf.es.size()<6){//使敌机数量保持在6架
					Plane ePlane = new Plane(r.nextInt(500),-r.nextInt(500),5,false,false,gf);//敌机
					ePlane.setBoss(false);
					gf.es.add(ePlane);
				}
			}
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//使CPU歇会
		}
	}

}
