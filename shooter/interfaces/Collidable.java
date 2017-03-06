package interfaces;

import javafx.scene.shape.Rectangle;

public interface Collidable {

	Rectangle getBounds();
	boolean collides(Collidable object);
}
