package game;

import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class SnakeFrame extends JFrame
{
  private static final long serialVersionUID = -1624735497099558420L;
  private SnakePanel snakePanel = new SnakePanel();
  
  public SnakeFrame()
  {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Snake Game");
    
    addWindowListener(new FrameListener());
    
    getContentPane().setLayout(new GridBagLayout());
    getContentPane().add(snakePanel);
    
    pack();
    setLocationRelativeTo(null);
    setResizable(false);
    setVisible(true);
  }
  
  public class FrameListener extends WindowAdapter
  {
    public void windowActivated(WindowEvent we)
    {
      snakePanel.setWindowPaused(false);
    }
    
    public void windowDeactivated(WindowEvent we)
    {
      snakePanel.setWindowPaused(true);
    }
    
    public void windowDeiconified(WindowEvent we)
    {
      snakePanel.setWindowPaused(false);
    }
    
    public void windowIconified(WindowEvent we)
    {
      snakePanel.setWindowPaused(true);
    }
    
    public void windowClosing(WindowEvent we)
    {
      snakePanel.stopGame();
    }
  }
  
  public static void main(String args[])
  {
    new SnakeFrame();
  }
}