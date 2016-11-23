package es.usefulearnings.gui.animation;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.sqrt;

/**
 * Overwatch - Like loader animation JavaFx
 * @author Yago Rodriguez
 */
public class OverWatchLoader extends Control {
  private Node view;
  private AnimatedHexagon hexagonChain;

  private class Hexagon {
    double[] points;

    public double getCenter() {
      return center;
    }

    double center;

    Hexagon(double side) {
      center = getH(side);
      points = new double[12];
      //     X                          Y
      points[0] =  center;      points[1] =  0.0;
      points[2] =  center * 2;  points[3] =  side / 2;
      points[4] =  center * 2;  points[5] =  side + side / 2;
      points[6] =  center;      points[7] =  side * 2;
      points[8] =  0.0;         points[9] =  side + side / 2;
      points[10] = 0.0;         points[11] = side / 2;
    }

    private double getH(double side) {
      return ((sqrt(3) / 2) * side);
    }

    double[] getPoints() {
      return points;
    }
  }

  private class AnimatedHexagon {

    private Polygon hexagon;
    private ScaleTransition scaleTransition;
    private ParallelTransition parallelTransition;
    private FadeTransition fadeTransition;
    private boolean wasPaused;
    private AnimatedHexagon _successor;
    private Duration currentTime;

    AnimatedHexagon(double side, Color color) {
      wasPaused = false;
      hexagon = new Polygon(new Hexagon(side).getPoints());
      hexagon.setFill(color);

      Duration duration = Duration.millis(250d);
      scaleTransition = new ScaleTransition(duration, hexagon);
      scaleTransition.setToY(0f);
      scaleTransition.setToX(0f);
      scaleTransition.setCycleCount(2);
      scaleTransition.setAutoReverse(true);

      fadeTransition = new FadeTransition(duration, hexagon);
      fadeTransition.setFromValue(1.0f);
      fadeTransition.setToValue(0.0f);
      fadeTransition.setCycleCount(2);
      fadeTransition.setAutoReverse(true);

      parallelTransition = new ParallelTransition();
      parallelTransition.getChildren().addAll(
        scaleTransition,
        fadeTransition
      );
      parallelTransition.setCycleCount(Timeline.INDEFINITE);

      scaleTransition
          .currentRateProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (newValue.intValue() <= 0 && !wasPaused()) {
                  pause();
                  setWasPaused();

                  _successor.play();
                  _successor.resetWasPaused();
                }
              }
          );
    }

    private void saveTime(Duration currentTime) {
      this.currentTime = currentTime;
    }

    void setSuccessor(AnimatedHexagon successor){
      this._successor = successor;
    }

    Node getAnimatedHexagon() {
      return hexagon;
    }

    void play() {
        parallelTransition.play();
    }

    void pause() {
      parallelTransition.pause();
    }

    ScaleTransition getScaleTransition() {
      return scaleTransition;
    }

    boolean wasPaused() {
      return wasPaused;
    }

    void setWasPaused() {
      wasPaused = true;
    }

    void resetWasPaused() {
      wasPaused = false;
    }
  }

  /**
   * Creates a loader with a default 20.0 hexagonSize
   * @param color color to fill the hexagon
   */
  public OverWatchLoader(Color color){
    this(20.0, color);
  }

  /**
   * Creates a loader
   * @param hexagonSize size for each hexagon's side
   * @param color color to fill the hexagon
   */
  public OverWatchLoader(double hexagonSize, Color color){
    final int MAX_HEXAGONS = 7;

    Node[] animatedNodes = new Node[MAX_HEXAGONS];
    AnimatedHexagon [] animatedHexagons = new AnimatedHexagon[MAX_HEXAGONS];

    hexagonChain = new AnimatedHexagon(hexagonSize, color);
    animatedNodes[0] = hexagonChain.getAnimatedHexagon();
    animatedHexagons[0] = hexagonChain;

    for (int i = 1; i < MAX_HEXAGONS; i++) {
      AnimatedHexagon animatedHexagon = new AnimatedHexagon(hexagonSize, color);
      animatedNodes[i] = animatedHexagon.getAnimatedHexagon();
      animatedHexagons[i] = animatedHexagon;
      animatedHexagons[i-1].setSuccessor(animatedHexagon);
    }
    animatedHexagons[MAX_HEXAGONS-1].setSuccessor(hexagonChain);

    Polygon filler1 = new Polygon(new Hexagon(((sqrt(3) / 2) * hexagonSize / 2)).getPoints());
    filler1.setFill(Color.TRANSPARENT);

    Polygon filler2 = new Polygon(new Hexagon(((sqrt(3) / 2) * hexagonSize / 2)).getPoints());
    filler2.setFill(Color.TRANSPARENT);

    final double MAX_SPACING = hexagonSize / 2;

    final double spacing = MAX_SPACING * 0.3;
    HBox topHbox = new HBox(filler1, animatedNodes[0], animatedNodes[1]);
    topHbox.setSpacing(spacing);
    HBox midHbox = new HBox(animatedNodes[5], animatedNodes[6], animatedNodes[2]);
    midHbox.setSpacing(spacing);
    HBox botHbox = new HBox(filler2, animatedNodes[4], animatedNodes[3]);
    botHbox.setSpacing(spacing);

    VBox vbox = new VBox(topHbox, midHbox, botHbox);
    vbox.setSpacing(MAX_SPACING * -0.7);

    vbox.setAlignment(Pos.CENTER);
    vbox.setMaxSize(((sqrt(3) / 2) * hexagonSize)*6, ((sqrt(3) / 2) * hexagonSize)*6);
    view = vbox;
  }

  /**
   * start the animation
   * @return the node that contains the animation
   */
  public Node getLoader(){
    hexagonChain.play();
    return view;
  }
}