package shoot;
import java.awt.image.BufferedImage;
public class Hero extends FlyingObject{
	private int doubleFire;
	private int life;
	private BufferedImage[] images;
	private int index;
	
	public Hero(){
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
		life = 3;
		images =new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		index = 0;
	}
	public void step(){
		index++;
		int a= index/10;		//每100毫秒切换一次
		int b=a%2;
		image =images[b];
	}
	public Bullet[] shoot(){
		int x = this.width/4;
		int y =20;
		if(doubleFire>0){
			Bullet[] bs = new Bullet[2];
			bs[0]=new Bullet(this.x+x,this.y-y);
			bs[1]=new Bullet(this.x+3*x,this.y-y);
			doubleFire-=2;
			return bs;
			}else{
			Bullet[] bs =new Bullet[1];
			bs[0]= new Bullet(this.x+2*x,this.y-y);
				return bs;
			}
		
	}
	public boolean outOfBounds(){
		return false;
	}
	public int getLife(){
		return life;
	}
	public void addLife(){
		life++;
	}
	public void subLife(){
		life--;
	}
	public void addFire(){
		doubleFire+=20;
	}
	public void cleanFire(){
		doubleFire =0;
	}
	public boolean hit(FlyingObject f){
		return f.x+f.width/2>=this.x-f.width/2 &&  f.x+f.width/2<=this.x+this.width+f.width/2
				                               &&
			   f.y+f.height/2>=this.y-f.height/2 && f.y+f.height/2<=this.y+this.height+f.height/2;
	}
	
	public void moveTo(int x,int y){
		this.x = x- this.width/2;
		this.y = y- this.height/2;
	}
	

}

