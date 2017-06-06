package shoot;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class ShootGame extends JPanel{	
	protected static final int WIDTH = 400;
	protected static final int HEIGHT = 654;
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage bullet;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	static {
		try{
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane= ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int state = START;
	
	
	Hero hero =new Hero();
	private Bullet[] bullets ={};
	private FlyingObject[] flyings = {};
	
	public FlyingObject nextOne(){		//创建敌人对象，及蜜蜂和敌机概率
		Random ran = new Random();
		int type = ran.nextInt(20);
		if(type>5){
			return new Bee();
		}else{
			return new Airplane();
		}
	}
	
	int enterIndex = 0;
	public void enterAction(){
		enterIndex++;
		if(enterIndex%40==0){
			FlyingObject f =nextOne();
			flyings =Arrays.copyOf(flyings, flyings.length+1);
			flyings[flyings.length-1] = f;
		}
	}
	
	public void stepAction(){
		hero.step();
		for(int i=0;i<flyings.length;i++){
			flyings[i].step();
		}
		for(int i=0;i<bullets.length;i++){
			bullets[i].step();
		}
	}
	int shootIndex=0;
	public void shootAction(){
		shootIndex++;
		if(shootIndex%30==0){
			Bullet[] bs =hero.shoot();
			bullets =Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length,bs.length);
		}
	}
	public void outOfBoundsAction(){
		int index = 0;		//不越界敌人下标，敌人个数
		FlyingObject[] flylives = new FlyingObject[flyings.length];		//创建不越界敌人数组
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){			//判定未越界的敌人并添加至不越界的数组当中
				flylives[index] = f;
				index++;
			}
		}
		flyings = Arrays.copyOf(flylives, index);		//将未越界数组复制到敌人数组当中
		index = 0;
		Bullet[] bulves = new Bullet[bullets.length];
		for(int j=0;j<bullets.length;j++){
			Bullet b = bullets[j];
			if(!b.outOfBounds()){
				bulves[index]= b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulves,index);
		
		
	}
	public void hitenAction(){
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			for(int j=0;j<bullets.length;j++){
				if(bullets[i].hiten(f)){
					Bullet b = bullets[i];
					bullets[i] = bullets[bullets.length-1];
					bullets[bullets.length-1] = b;
					bullets = Arrays.copyOf(bullets, bullets.length-1);
				}
			}
		}
	}
	public void bangAction(){
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];
			bang(b);
			
			
		}
	}
	int score =0;
	public void bang(Bullet b){
		int index =-1;
		for(int i=0;i<flyings.length;i++){
			if(flyings[i].shootBy(b)){		//遍历所有敌人，判定是否相撞
				index = i;
				break;
			}
		}
		if(index!=-1){
			FlyingObject one = flyings[index];
			if(one instanceof Enemy){
				Enemy enemy = (Enemy)one;
				score+=enemy.getScore();
			}
			if(one instanceof Award){
				Award award = (Award)one;
				int type = award.getType();
				switch(type){
				case Award.LIFE:
					hero.addLife();
					break;
				case Award.DOUBLE_FIRE:
					hero.addFire();
					break;
				}
			}
			FlyingObject t = one;
			one = flyings[flyings.length-1];
			flyings[flyings.length -1]=t;
			flyings = Arrays.copyOf(flyings, flyings.length-1);
		}
				
	}
    
	public void hitAction(){
		for(int i=0;i<flyings.length;i++){
			if(hero.hit(flyings[i])){
				hero.subLife();
				hero.cleanFire();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length-1];
				flyings[flyings.length-1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
						
			}
		}
	}
	public void checkOverAction(){
		if(hero.getLife()<=0){
			state = GAME_OVER;
		}
	}
	
	
	public void action(){
		MouseAdapter l = new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				if(state == RUNNING){
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			
			public void mouseClicked(MouseEvent e){
				switch(state){
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
					}
				}
			public void mouseExited(MouseEvent e){
				if(state == RUNNING){
					state = PAUSE;
				}
			}
			public void mouseEntered(MouseEvent e){
				if(state == PAUSE){
					state = RUNNING;
				}
			}
		};
		
		this.addMouseListener(l); //处理鼠标操作事件
		this.addMouseMotionListener(l);
		
		
		
		Timer timer = new Timer();
		int intervel =10;
		timer.schedule(new TimerTask(){		//匿名内部类，重写TimerTask内run（）方法，方法内为定时做的事情，创建定时器
			public void run(){
				if(state == RUNNING){
					enterAction();
					stepAction();
					shootAction();
					hitenAction();
					outOfBoundsAction();
					bangAction();
					hitAction();
					checkOverAction();
				}
				repaint();
			}
		}, intervel,intervel);
		
	}
	public void paint(Graphics g){
		g.drawImage(background, 0,0,null);
		paintHero(g);
		paintFlyingObjects(g);
		paintBullets(g);
		paintScoreAndLife(g);
		paintState(g);
		
	}
	public void paintHero(Graphics g){
		g.drawImage(hero.image, hero.x, hero.y,null);
	}
	public void paintFlyingObjects(Graphics g){
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}
	public void paintBullets(Graphics g){
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}
	public void paintScoreAndLife(Graphics g){
		g.setColor(new Color(0xFF0000));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		g.drawString("SCORE:"+score, 10, 25);
		g.drawString("LIFE:"+hero.getLife(),10,45);
		
	}
	public void paintState(Graphics g){
		switch(state){
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	

	public static void main(String[] args) {
		JFrame frame = new JFrame("FLY");
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH,HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.action();;

	}

}
