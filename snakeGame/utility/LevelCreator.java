package snakeGame.utility;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LevelCreator
{
  private Rectangle rectangleArray[];
  
  public LevelCreator(String levelName)
  {
    InputStream inputStream = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    
    rectangleArray = new Rectangle[0];
    try
    {
      inputStream = getClass().getResourceAsStream(levelName);
      inputStreamReader = new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(inputStreamReader);
      String line;
      rectangleArray = new Rectangle[Integer.parseInt(line = bufferedReader
          .readLine())];
      Scanner scanner;
      int index = 0;
      while ((line = bufferedReader.readLine()) != null)
      {
        scanner = new Scanner(line);
        rectangleArray[index++] = new Rectangle(scanner.nextInt(),
            scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
      }
      
      bufferedReader.close();
      inputStreamReader.close();
      inputStream.close();
    }
    catch (Exception e)
    {
    }
  }
  
  public Rectangle[] getBorders()
  {
    return rectangleArray;
  }
}