package es.usefulearnings.utils;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.sqrt;

/**
 * Overwatch - Like loader animation JavaFx
 * @author Yago Rodriguez
 */
public class OverwatchLoader {
  private Node view;
  private AnimatedHexagon[] animatedHexagons;

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
    private boolean wasPaused;

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

      FadeTransition fadeTransition = new FadeTransition(duration, hexagon);
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
  public OverwatchLoader(Color color){
    this(20.0, color);
  }

  /**
   * Creates a loader
   * @param hexagonSize size for each hexagon's side
   * @param color color to fill the hexagon
   */
  public OverwatchLoader(double hexagonSize, Color color){
    final int MAX_HEXAGONS = 7;

    Node[] animatedNodes = new Node[MAX_HEXAGONS];
    animatedHexagons = new AnimatedHexagon[MAX_HEXAGONS];

    for (int i = 0; i < 7; i++) {
      AnimatedHexagon animatedHexagon = new AnimatedHexagon(hexagonSize, color);
      animatedNodes[i] = animatedHexagon.getAnimatedHexagon();
      animatedHexagons[i] = animatedHexagon;
    }

    final AtomicInteger i = new AtomicInteger(0);
    for (; i.get() < 7; i.getAndIncrement()) {
      final int myPos = i.get();
      animatedHexagons[i.get()]
        .getScaleTransition()
        .currentRateProperty()
        .addListener(
          (observable, oldValue, newValue) -> {
            if (newValue.intValue() <= 0 && !animatedHexagons[myPos].wasPaused()) {
              animatedHexagons[myPos].pause();
              animatedHexagons[myPos].setWasPaused();

              if (myPos == 6) {
                animatedHexagons[0].play();
                animatedHexagons[0].resetWasPaused();
              } else {
                animatedHexagons[myPos + 1].play();
                animatedHexagons[myPos + 1].resetWasPaused();
              }
            }
          }
        );
    }

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
    Pane pane = new Pane(vbox);
    pane.setMaxSize(hexagonSize * 6, hexagonSize * 6);
    view = pane;

  }

  /**
   * start the animation
   * @return the node that contains the animation
   */
  public Node getLoader(){
    animatedHexagons[0].play();
    return view;
  }
}