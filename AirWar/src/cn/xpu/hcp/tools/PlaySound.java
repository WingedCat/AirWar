package cn.xpu.hcp.tools;

import java.io.InputStream;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * 
 * 必须使用多线程，播放音效
 *
 */
public class PlaySound extends Thread{
	
	private String mp3Url;
	
	private boolean isLoop;
	
	public PlaySound(String mp3Url, boolean isLoop) {
		super();
		this.mp3Url = mp3Url;
		this.isLoop = isLoop;
	}

	public void run() {
		
		do{
			
			//读取音频文件流
			InputStream mp3 = PlaySound.class.getClassLoader().getResourceAsStream("music/"+mp3Url);
			
			try {
			
				//创建播放器
				AdvancedPlayer adv = new AdvancedPlayer(mp3);
				
				//播放
				adv.play();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}while(isLoop);
	}
	
	public static void main(String[] args) {
		
		new PlaySound("bgmusic.mp3", true).start();
		
	}
}
