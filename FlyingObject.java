package shoot;
import java.awt.image.BufferedImage;
public abstract class FlyingObject {
	protected BufferedImage image;		//变量在子类中初始化，故不能设置为private
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	public abstract void step();
	public abstract boolean outOfBounds();
	
	public boolean shootBy(Bullet b){
		return b.x>this.x && b.x<this.x+this.width
						  &&
			   b.y>this.y && b.y<this.y+this.height;
	}

}
