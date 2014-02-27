package snakeGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import snakeGame.utility.LevelCreator;

public class Border
{
  private Color color = new Color(20, 20, 20);
  
  private LevelCreator levelCreator;
  
  public Border(LevelCreator levelCreator)
  {
    this.levelCreator = levelCreator;
  }
  
  public Rectangle[] getBorders()
  {
    return levelCreator.getBorders();
  }
  
  public void draw(Graphics g)
  {
    g.setColor(color);
    for (Rectangle rect : getBorders())
    {
      g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
  }
}