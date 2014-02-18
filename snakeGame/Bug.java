package snakeGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Bug {
	private static int x = 0, y = 0;

	private static final int BUG_SIZE = 10;
	private static Random random = new Random();

	private static Color BUG_COLOR = new Color(223, 89, 0);
	private static boolean eatenState = false;

	private Snake snake;
	private Border border;

	public Bug(Snake snake, Border border) {
		this.snake = snake;
		this.border = border;
		randomizePosition();
	}

	public void randomizePosition() {
		x = y = 0;
		while (x < (BUG_SIZE * 3) || x > (SnakePanel.WIDTH - (BUG_SIZE * 3)))
			x = BUG_SIZE * random.nextInt(SnakePanel.WIDTH / BUG_SIZE)
					- BUG_SIZE / 2;
		while (y < (BUG_SIZE * 3) || y > (SnakePanel.HEIGHT - (BUG_SIZE * 3)))
			y = BUG_SIZE * random.nextInt(SnakePanel.HEIGHT / BUG_SIZE);

		for (int snakeIndex = 0; snakeIndex < snake.getSize(); snakeIndex++) {
			if (getBounds().intersects(snake.getBodyBoundsAt(snakeIndex)))
				randomizePosition();
		}

		for (Rectangle rect : border.getBorders()) {
			if (getBounds().intersects(rect))
				randomizePosition();
		}
	}

	public void draw(Graphics g) {
		if (!isEaten()) {
			g.setColor(BUG_COLOR);
			g.fillOval(x, y, BUG_SIZE, BUG_SIZE);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x + 1, y + 1, BUG_SIZE - 1, BUG_SIZE - 1);
	}

	public void setEaten(boolean b) {
		if (eatenState != b)
			eatenState = !eatenState;
	}

	public boolean isEaten() {
		return eatenState;
	}
}