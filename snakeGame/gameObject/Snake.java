package snakeGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import snakeGame.SnakePanel;
import snakeGame.utility.Direction;

public class Snake
{
  private static final int MAX_SIZE = 200;
  private static int bodyX[] = new int[MAX_SIZE];
  private static int bodyY[] = new int[MAX_SIZE];
  
  private static int currentSize = 4;
  
  private static final Color HEAD_COLOR = new Color(200, 40, 40);
  private static final Color BODY_COLOR = new Color(50, 50, 50);
  
  private static final int BODY_SIZE = 10;
  
  private static Direction currentDirection;
  
  private Border border;
  
  public Snake(Border border)
  {
    this.border = border;
    currentDirection = Direction.NORTH;
    
    for (int bodyIndex = 0; bodyIndex < currentSize; bodyIndex++)
    {
      bodyX[bodyIndex] = (SnakePanel.WIDTH - BODY_SIZE) / 2;
      bodyY[bodyIndex] = (SnakePanel.HEIGHT * 4 / 5) + (bodyIndex * BODY_SIZE);
    }
    for (int bodyIndex = currentSize; bodyIndex < MAX_SIZE; bodyIndex++)
    {
      bodyX[bodyIndex] = -BODY_SIZE;
      bodyY[bodyIndex] = -BODY_SIZE;
      
    }
  }
  
  public void addBody()
  {
    if (currentSize < MAX_SIZE)
      currentSize++;
    
  }
  
  public void draw(Graphics g)
  {
    g.setColor(BODY_COLOR);
    for (int bodyIndex = 1; bodyIndex < currentSize; bodyIndex++)
    {
      g.fillOval(bodyX[bodyIndex], bodyY[bodyIndex], BODY_SIZE, BODY_SIZE);
    }
    g.setColor(HEAD_COLOR);
    g.fillOval(bodyX[0], bodyY[0], BODY_SIZE, BODY_SIZE);
  }
  
  public void setDirection(Direction direction)
  {
    if (!((currentDirection == direction) || (currentDirection == getOppositeOf(direction))))
    {
      currentDirection = direction;
    }
  }
  
  private Direction getOppositeOf(Direction d)
  {
    switch (d)
    {
      case NORTH:
        return Direction.SOUTH;
      case SOUTH:
        return Direction.NORTH;
      case WEST:
        return Direction.EAST;
      case EAST:
        return Direction.WEST;
      default:
        return d;
    }
  }
  
  public synchronized void move()
  {
    for (int bodyIndex = currentSize - 1; bodyIndex >= 1; bodyIndex--)
    {
      bodyX[bodyIndex] = bodyX[bodyIndex - 1];
      bodyY[bodyIndex] = bodyY[bodyIndex - 1];
    }
    
    switch (currentDirection)
    {
      case NORTH:
        bodyY[0] -= BODY_SIZE;
        break;
      case SOUTH:
        bodyY[0] += BODY_SIZE;
        break;
      case WEST:
        bodyX[0] -= BODY_SIZE;
        break;
      case EAST:
        bodyX[0] += BODY_SIZE;
        break;
    }
  }
  
  public synchronized boolean isCollidingWithItself()
  {
    for (int bodyIndex = 1; bodyIndex < currentSize; bodyIndex++)
    {
      if (getBodyBoundsAt(0).intersects(getBodyBoundsAt(bodyIndex)))
        return true;
    }
    return false;
  }
  
  public synchronized boolean isCollidingWithBug(Rectangle bugBounds)
  {
    if (getBodyBoundsAt(0).intersects(bugBounds))
      return true;
    return false;
  }
  
  public synchronized boolean isCollidingWithBorder()
  {
    Rectangle head = getBodyBoundsAt(0);
    for (Rectangle rect : border.getBorders())
    {
      if (head.intersects(rect))
        return true;
    }
    return false;
  }
  
  public synchronized void manageEnds()
  {
    if (bodyX[0] < 0)
      bodyX[0] = SnakePanel.WIDTH;
    else if (bodyX[0] >= SnakePanel.WIDTH)
      bodyX[0] = 0;
    
    if (bodyY[0] < 0)
      bodyY[0] = SnakePanel.HEIGHT;
    else if (bodyY[0] >= SnakePanel.HEIGHT)
      bodyY[0] = 0;
  }
  
  public int getSize()
  {
    return currentSize;
  }
  
  public Rectangle getBodyBoundsAt(int index)
  {
    return new Rectangle(bodyX[index] + 1, bodyY[index] + 1, BODY_SIZE - 1,
        BODY_SIZE - 1);
  }
}