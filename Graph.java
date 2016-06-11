import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.Random;
 
public class Graph extends Application {


  class Point {
    double x;
    double y;
    Point(double xx, double yy) {
      x = xx;
      y = yy;
    }
  }
 
  public static void main(String[] args) {
      launch(args);
  }
 
  @Override
  public void start(Stage primaryStage) {
      primaryStage.setTitle("User Input Graph");
      Group root = new Group();
      int canvasWidth = 800;
      int canvasHeight = 800;
      Canvas canvas = new Canvas(canvasWidth, canvasHeight);
 
      drawGraph(canvas);
 
      root.getChildren().add(canvas);
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
  }
 
  public void drawGraph(Canvas canvas) {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setFill(Color.BLACK);
    gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Point> nodePoints = new ArrayList<Point>();

    canvas.setFocusTraversable(true);

    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
            points.add(new Point(e.getX(), e.getY())); 
            int numConnections = 10;
            if(points.size() != 0) {
              if(points.size() < numConnections) {
                numConnections = points.size();
              }
              Point newlyAddedPoint = points.get(points.size()-1);
              for(int i = points.size()-1; i > points.size()-numConnections; --i) {
                gc.setStroke(Color.BLUE);
                gc.strokeLine(newlyAddedPoint.x, newlyAddedPoint.y, points.get(i-1).x, points.get(i-1).y);
              }
            }
          }
      });

    canvas.addEventHandler(KeyEvent.KEY_PRESSED, 
      new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
          if(e.getCode().toString().equals("SPACE")) {
            Random randomGenerator = new Random();
            int radius = 5;
            double theta = Math.PI/4.0;
            for(Point p : points) {
              int rand = randomGenerator.nextInt(100); 
              if(rand < 15 || rand > 85) {
                gc.setFill(Color.WHITE);
                Point p1 = rotatePoint(p.x+radius, p.y+radius, theta,p);
                Point p2 = rotatePoint(p.x+radius, p.y-radius, theta,p);
                Point p3 = rotatePoint(p.x-radius, p.y-radius, theta,p);
                Point p4 = rotatePoint(p.x-radius, p.y+radius, theta,p);
                
                double[] xs = new double[] {p1.x, p2.x, p3.x, p4.x};
                double[] ys = new double[] {p1.y, p2.y, p3.y, p4.y};
                gc.fillPolygon(xs, ys, 4);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokePolygon(xs, ys, 4);
                gc.setLineWidth(1);
              }
            }
          }
          if(e.getCode().toString().equals("C")) {
            clearBoard(gc, canvas.getWidth(), canvas.getHeight());
            points.clear();
          }
          if(e.getCode().toString().equals("S")) {
            double r = 10;
            double step = 12*Math.PI / 200;
            for(double t = 0; t < 12*Math.PI; t += step) {
              double x = r * Math.cos(t) + canvas.getWidth() / 2.0;
              double y = r * Math.sin(t) + canvas.getHeight() / 2.0;
              r += 2;
              points.add(new Point(x, y));
              int numConnections = 10;
              if(points.size() != 0) {
                if(points.size() < numConnections) {
                  numConnections = points.size();
                }
                Point newlyAddedPoint = points.get(points.size()-1);
                for(int i = points.size()-1; i > points.size()-numConnections; --i) {
                  gc.setStroke(Color.BLUE);
                  gc.strokeLine(newlyAddedPoint.x, newlyAddedPoint.y, points.get(i-1).x, points.get(i-1).y);
                }
              }
            }
          }
        }
      });


  }

  public Point rotatePoint(double x, double y, double theta, Point center) {
    double rotatedX = x-center.x;
    double rotatedY = y-center.y;
    return new Point(rotatedX*Math.cos(theta) - rotatedY*Math.sin(theta) + center.x, rotatedX*Math.sin(theta) + rotatedY*Math.cos(theta) + center.y);
  }

  public void clearBoard(GraphicsContext gc, double canvasWidth, double canvasHeight) {
    gc.setFill(Color.BLACK);
    gc.fillRect(0,0,canvasWidth, canvasHeight);
  }
}
