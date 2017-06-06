package shoot;
import java.util.Random;
public class Bee extends FlyingObject implements Award{
	private int xSpeed =1;
	private int ySpeed =1;
	private int awardType;
	public Bee(){
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random ran = new Random();
		x = ran.nextInt(ShootGame.WIDTH-this.width);
		y = -height;
		awardType =ran.nextInt(2);
	}
	public int getType(){
		return awardType;
	}
	public void step(){
		y+=ySpeed;
		x+=xSpeed;
		if(this.x>=ShootGame.WIDTH-this.width){
			xSpeed = -1;
		}
		if(x<0){
			xSpeed =1;
		}
		
	}
	public boolean outOfBounds(){
		return this.y>ShootGame.HEIGHT;
	}

}
