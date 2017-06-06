package shoot;

public class Bullet extends FlyingObject{
	private int speed = 3;
	public Bullet(int x,int y){
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}
	public void step(){
		y-=speed;
	}
	public boolean outOfBounds(){
		return y<=-this.height;
	}
	public boolean hiten(FlyingObject f){
		return this.x>=f.x && this.x<= f.x+f.width
				           &&
			   this.y >= f.y&&this.y<=f.y+f.height;
	}

}
