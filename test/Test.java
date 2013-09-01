import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Syd
 * Date: 01.09.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class Test {
  public static void main(String[] args) throws IOException, InterruptedException {
    Process p = Runtime.getRuntime().exec(args[0]);
    p.waitFor();
    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line = reader.readLine();
    StringBuilder builder = new StringBuilder();
    while (line != null) {
      line = reader.readLine();
      builder.append(line);
    }

    reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    line = reader.readLine();
    while (line != null) {
      line = reader.readLine();
      builder.append(line);
    }

    String result = builder.toString();
    System.out.println(result);
  }
}
