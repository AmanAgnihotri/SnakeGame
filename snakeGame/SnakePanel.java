package snakeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import snakeGame.gameObject.Border;
import snakeGame.gameObject.Bug;
import snakeGame.gameObject.Snake;
import snakeGame.utility.Direction;
import snakeGame.utility.LevelCreator;

public class SnakePanel extends JPanel implements Runnable
{
  private static final long serialVersionUID = 6892533030374996243L;
  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;
  
  private Thread animator;
  private Thread timeThread;
  
  private volatile boolean running = false;
  private volatile boolean isGameOver = false;
  private volatile boolean isUserPaused = false;
  private volatile boolean isWindowPaused = false;
  
  private Graphics dbg;
  private Image dbImage = null;
  
  private static final int NO_DELAYS_PER_YIELD = 16;
  private static final int MAX_FRAME_SKIPS = 5;
  
  private static final Color backgroundColor = new Color(245, 245, 245);
  
  private static long fps = 30;
  private static long period = 1000000L * (long) 1000.0 / fps;
  
  private LevelCreator levelCreator = new LevelCreator("Level 1");
  private Border border = new Border(levelCreator);
  private Snake snake = new Snake(border);
  private Bug bug = new Bug(snake, border);
  
  private static volatile boolean isPainted = false;
  
  private long bugsEaten;
  private long timeSpentInSeconds;
  
  public SnakePanel()
  {
    setBackground(backgroundColor);
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    
    bugsEaten = 0;
    timeSpentInSeconds = 0;
    setFocusable(true);
    requestFocus();
    readyForPause();
    
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        int keyCode = e.getKeyCode();
        if ((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)
            && isPainted)
        {
          snake.setDirection(Direction.WEST);
          isPainted = false;
        }
        if ((keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)
            && isPainted)
        {
          snake.setDirection(Direction.EAST);
          isPainted = false;
        }
        if ((keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W)
            && isPainted)
        {
          snake.setDirection(Direction.NORTH);
          isPainted = false;
        }
        if ((keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)
            && isPainted)
        {
          snake.setDirection(Direction.SOUTH);
          isPainted = false;
        }
      }
    });
  }
  
  public void addNotify()
  {
    super.addNotify();
    startGame();
  }
  
  void startGame()
  {
    if (animator == null || !running)
    {
      animator = new Thread(this);
      animator.start();
      timeThread = new Thread(new TimeThread());
      timeThread.start();
    }
  }
  
  void stopGame()
  {
    running = false;
  }
  
  private void readyForPause()
  {
    addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        int keyCode = e.getKeyCode();
        if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q)
            || (keyCode == KeyEvent.VK_END) || (keyCode == KeyEvent.VK_P)
            || ((keyCode == KeyEvent.VK_C) && e.isControlDown()))
        {
          if (!isUserPaused)
            setUserPaused(true);
          else
            setUserPaused(false);
        }
      }
    });
  }
  
  public void run()
  {
    long beforeTime, afterTime, timeDiff, sleepTime;
    long overSleepTime = 0L;
    int noDelays = 0;
    long excess = 0L;
    
    beforeTime = System.nanoTime();
    
    running = true;
    
    while (running)
    {
      requestFocus();
      gameUpdate();
      gameRender();
      paintScreen();
      
      afterTime = System.nanoTime();
      
      timeDiff = afterTime - beforeTime;
      sleepTime = (period - timeDiff) - overSleepTime;
      
      if (sleepTime > 0)
      {
        try
        {
          Thread.sleep(sleepTime / 1000000L);
        }
        catch (InterruptedException e)
        {
        }
        
        overSleepTime = (System.nanoTime() - afterTime - sleepTime);
      }
      else
      {
        excess -= sleepTime;
        overSleepTime = 0L;
        
        if (++noDelays >= NO_DELAYS_PER_YIELD)
        {
          Thread.yield();
          noDelays = 0;
        }
      }
      
      beforeTime = System.nanoTime();
      
      int skips = 0;
      
      while ((excess > period) && (skips < MAX_FRAME_SKIPS))
      {
        excess -= period;
        gameUpdate();
        skips++;
      }
      
      isPainted = true;
    }
    System.exit(0);
  }
  
  private void gameUpdate()
  {
    if (!isUserPaused && !isWindowPaused && !isGameOver)
    {
      snake.move();
      if (snake.isCollidingWithItself())
        isGameOver = true;
      if (snake.isCollidingWithBug(bug.getBounds()))
      {
        snake.addBody();
        snake.addBody();
        bug.setEaten(true);
        bug.randomizePosition();
        bug.setEaten(false);
        bugsEaten++;
      }
      if (snake.isCollidingWithBorder())
        isGameOver = true;
      snake.manageEnds();
    }
  }
  
  private void gameRender()
  {
    if (dbImage == null)
    {
      dbImage = createImage(WIDTH, HEIGHT);
      if (dbImage == null)
      {
        System.out.println("Image is null.");
        return;
      }
      else
        dbg = dbImage.getGraphics();
    }
    
    dbg.setColor(backgroundColor);
    dbg.fillRect(0, 0, WIDTH, HEIGHT);
    
    snake.draw(dbg);
    bug.draw(dbg);
    border.draw(dbg);
    
    if (isGameOver)
      gameOverMessage(dbg);
  }
  
  private void gameOverMessage(Graphics g)
  {
    g.setColor(new Color(33, 33, 33));
    g.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
    g.drawString("Game Over!", 265, 250);
    g.setColor(new Color(0, 64, 0));
    g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
    g.drawString("Bugs Eaten : " + bugsEaten, 255, 300);
    g.drawString("Time Spent : " + timeSpentInSeconds + " secs", 200, 350);
  }
  
  private void paintScreen()
  {
    Graphics g;
    
    try
    {
      g = this.getGraphics();
      if ((g != null) && (dbImage != null))
        g.drawImage(dbImage, 0, 0, null);
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
    catch (Exception e)
    {
      System.out.println("Graphics context error : " + e);
    }
  }
  
  public void setWindowPaused(boolean isPaused)
  {
    isWindowPaused = isPaused;
  }
  
  public void setUserPaused(boolean isPaused)
  {
    isUserPaused = isPaused;
  }
  
  public class TimeThread implements Runnable
  {
    public void run()
    {
      while (!isUserPaused && !isWindowPaused && !isGameOver)
      {
        timeSpentInSeconds += 1;
        try
        {
          Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
        }
      }
    }
  }
}