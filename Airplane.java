package shoot;
import java.util.Random;
public class Airplane extends FlyingObject implements Enemy{
	private int speed = 2;
	public Airplane(){					//子类继承所有父类成员
		image = ShootGame.airplane;		//airplane为静态变量，故用类名引用
		width = image.getWidth();
		height= image.getHeight();
		Random ran = new Random();
		x = ran.nextInt(ShootGame.WIDTH-this.width);
		y = -height;
		
	}
	public int getScore(){
		return 5;
	}
	public void step(){
		y+=speed;
	}
	public boolean outOfBounds(){
		return y>=ShootGame.HEIGHT;
	}
}
