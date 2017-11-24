package cn.xpu.hcp.entity;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import cn.xpu.hcp.game.GameFrame;
import cn.xpu.hcp.tools.Constant;
import cn.xpu.hcp.tools.GameImage;
import cn.xpu.hcp.tools.PlaySound;

public class CreateEnemyThread extends Thread{
	private GameFrame gf;
	private static Random r = new Random();
	private static AtomicInteger count = new AtomicInteger(1);

	public CreateEnemyThread(GameFrame gf){
		this.gf = gf;
		System.out.println("check...");
	}

	@Override
	public void run() {
		while(true){
			if((System.currentTimeMillis()-gf.myplane.eatStart)/1000>=10){
				gf.myplane.back();//超级炮弹使用10秒，还原
				gf.myplane.count = 0;
			}
			if(gf.myplane.getLife()<=10&&r.nextInt(40)>=38){
				Treasure t = new Treasure(0);//生命值小于10时系统随机刷出补血的珍宝
				gf.ts.add(t);
			}
			if((System.currentTimeMillis()-gf.start)/1000>=10){//一分半后，待打完所有普通敌人，boss开始出现
				
				if((System.currentTimeMillis()-gf.start)/1000%10==0&&r.nextInt(40)>=38){
					Treasure t = new Treasure();//三个时间节点，随机刷出珍宝
					gf.ts.add(t);
				}
				
				if(gf.es.size()==0){
//					System.out.println("没有普通敌机了...");
					if(gf.success.get()==false){
//						System.out.println("还没有通关，产生boss");
						new PlaySound("bosscoming.mp3", false);
						gf.pbg.stop();
						gf.pbg = new PlaySound("bossing.mp3", true);
						gf.pbg.start();
						Plane boss = new Plane(r.nextInt(500),50,5,false,true,gf);//boss敌机
						boss.setLife(100);//boss生命值为1000
						gf.es.add(boss);
					}else{
						gf.pbg.stop();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						count.getAndIncrement();
//						System.out.println("消灭了boss，下一关...\n下面是第"+count.get()+"关");
						if(count.get()==4){
							System.out.println("count="+count);
							count.set(1);
							System.out.println(count.get());
						}
						System.out.println("修改success=false");
						gf.success.set(false);;
						gf.boss = gf.temp;
						gf.gameBg = GameImage.getImage("resources/background"+count.get()+".bmp");
						gf.yPos = -1*(gf.gameBg.getHeight(null)-Constant.GAME_HEIGHT)+1;
						gf.yPos2 = gf.yPos - gf.gameBg.getHeight(null);
						gf.start = System.currentTimeMillis();
						gf.myplane.setLife(100);
						gf.pbg = new PlaySound("bgmusic.mp3", true);
						gf.pbg.start();
						
						
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
