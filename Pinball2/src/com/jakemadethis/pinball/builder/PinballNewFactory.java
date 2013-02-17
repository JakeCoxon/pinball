/* 
Generated from:
ruby script/codegen.rb ./src/com/jakemadethis/pinball/builder/PinballNewFactory.java 
*/
package com.jakemadethis.pinball.builder;
import com.jakemadethis.pinball.game.GameModel;
import com.jakemadethis.pinball.LevelException;
import com.jakemadethis.pinball.Attachable;
import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.jakemadethis.pinball.level.Level;
import com.jakemadethis.pinball.level.LightGroup;
import com.jakemadethis.pinball.level.Wall;
import com.jakemadethis.pinball.level.Light;
import com.jakemadethis.pinball.level.Bumper;
import com.jakemadethis.pinball.level.Flipper;
import com.jakemadethis.pinball.level.Frame;
public class PinballNewFactory implements BuilderFactory<Object> {
  private GameModel model;
  private static float SCALE = 100f;
  public PinballNewFactory(GameModel model) {
    this.model = model;
  }
  public Object create(BuilderNode node) {
    Attachable rel_to = null;
    if ("level".equals(node.getNodeName()))
      create_level(node, rel_to);
    else 
      throw new LevelException("Invalid node in root: "+node.getNodeName());
    return null;
  }
  private void create_level(BuilderNode node, Attachable rel_to) {
    String name = node.attExpected("name");
    float[] size = FactoryUtil.toSize(node.attExpected("size"), SCALE);
    float w = size[0];
    float h = size[1];
    rel_to = new Level(name, w, h); model.setSize(w, h);
    for(BuilderNode child : node.getChilds()) {
      if ("ball-start".equals(child.getNodeName()))
        create_level_ball_start(child, rel_to);
      else if ("bumper".equals(child.getNodeName()))
        create_level_bumper(child, rel_to);
      else if ("left-flipper".equals(child.getNodeName()))
        create_level_left_flipper(child, rel_to);
      else if ("right-flipper".equals(child.getNodeName()))
        create_level_right_flipper(child, rel_to);
      else if ("light".equals(child.getNodeName()))
        create_level_light(child, rel_to);
      else if ("light-group".equals(child.getNodeName()))
        create_level_light_group(child, rel_to);
      else if ("wall".equals(child.getNodeName()))
        create_level_wall(child, rel_to);
      else if ("group".equals(child.getNodeName()))
        create_level_group(child, rel_to);
      else if ("frame".equals(child.getNodeName()))
        create_level_frame(child, rel_to);
      else 
        throw new LevelException("Invalid node in level: "+child.getNodeName());
    }
  }
  private void create_level_ball_start(BuilderNode node, Attachable rel_to) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    model.addBallPos(x, y);
  }
  private void create_level_bumper(BuilderNode node, Attachable rel_to) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    float radius = FactoryUtil.toLength(node.attOptional("radius", "20"), SCALE);
    model.add(new Bumper(model, x, y, radius));
  }
  private void create_level_left_flipper(BuilderNode node, Attachable rel_to) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    float length = FactoryUtil.toLength(node.attExpected("length"), SCALE);
    model.add(new Flipper(model, x, y, length, Flipper.Type.LEFT));
  }
  private void create_level_right_flipper(BuilderNode node, Attachable rel_to) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    float length = FactoryUtil.toLength(node.attExpected("length"), SCALE);
    model.add(new Flipper(model, x, y, length, Flipper.Type.RIGHT));
  }
  private void create_level_light(BuilderNode node, Attachable rel_to) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    float radius = FactoryUtil.toLength(node.attOptional("radius", "20"), SCALE);
    Color color = Color.RED;
    model.add(new Light(model, x, y, radius, color));
  }
  private void create_level_light_group(BuilderNode node, Attachable rel_to) {
    int score = FactoryUtil.toInteger(node.attExpected("score"));
    ArrayList<Vector2> vectors = new ArrayList<Vector2>();
    for(BuilderNode child : node.getChilds()) {
      if ("light".equals(child.getNodeName()))
        create_level_light_group_light(child, rel_to, vectors);
      else 
        throw new LevelException("Invalid node in light_group: "+child.getNodeName());
    }
    model.add(new LightGroup(model, vectors, score));
  }
  private void create_level_light_group_light(BuilderNode node, Attachable rel_to, ArrayList<Vector2> vectors) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    vectors.add(new Vector2(x, y));
  }
  private void create_level_wall(BuilderNode node, Attachable rel_to) {
    Wall.Builder builder = new Wall.Builder(false);
    for(BuilderNode child : node.getChilds()) {
      if ("point".equals(child.getNodeName()))
        create_level_wall_point(child, rel_to, builder);
      else if ("arc".equals(child.getNodeName()))
        create_level_wall_arc(child, rel_to, builder);
      else 
        throw new LevelException("Invalid node in wall: "+child.getNodeName());
    }
    model.add(builder.constructWall(model));
  }
  private void create_level_wall_point(BuilderNode node, Attachable rel_to, Wall.Builder builder) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    builder.addPoint(x, y);
  }
  private void create_level_wall_arc(BuilderNode node, Attachable rel_to, Wall.Builder builder) {
    float[] at = FactoryUtil.toPosition(node.attExpected("at"), rel_to, SCALE);
    float x = at[0];
    float y = at[1];
    float radius = FactoryUtil.toLength(node.attExpected("radius"), SCALE);
    builder.addArc(x, y, radius);
  }
  private void create_level_group(BuilderNode node, Attachable rel_to) {
    rel_to = new Frame(rel_to);
    for(BuilderNode child : node.getChilds()) {
      if ("ball-start".equals(child.getNodeName()))
        create_level_ball_start(child, rel_to);
      else if ("bumper".equals(child.getNodeName()))
        create_level_bumper(child, rel_to);
      else if ("left-flipper".equals(child.getNodeName()))
        create_level_left_flipper(child, rel_to);
      else if ("right-flipper".equals(child.getNodeName()))
        create_level_right_flipper(child, rel_to);
      else if ("light".equals(child.getNodeName()))
        create_level_light(child, rel_to);
      else if ("light-group".equals(child.getNodeName()))
        create_level_light_group(child, rel_to);
      else if ("wall".equals(child.getNodeName()))
        create_level_wall(child, rel_to);
      else if ("group".equals(child.getNodeName()))
        create_level_group(child, rel_to);
      else if ("frame".equals(child.getNodeName()))
        create_level_frame(child, rel_to);
      else 
        throw new LevelException("Invalid node in group: "+child.getNodeName());
    }
  }
  private void create_level_frame(BuilderNode node, Attachable rel_to) {
    float[] from = FactoryUtil.toPosition(node.attExpected("from"), rel_to, SCALE);
    float x1 = from[0];
    float y1 = from[1];
    float[] to = FactoryUtil.toPosition(node.attExpected("to"), rel_to, SCALE);
    float x2 = to[0];
    float y2 = to[1];
    rel_to = new Frame(x1, y1, x2, y2);
    for(BuilderNode child : node.getChilds()) {
      if ("ball-start".equals(child.getNodeName()))
        create_level_ball_start(child, rel_to);
      else if ("bumper".equals(child.getNodeName()))
        create_level_bumper(child, rel_to);
      else if ("left-flipper".equals(child.getNodeName()))
        create_level_left_flipper(child, rel_to);
      else if ("right-flipper".equals(child.getNodeName()))
        create_level_right_flipper(child, rel_to);
      else if ("light".equals(child.getNodeName()))
        create_level_light(child, rel_to);
      else if ("light-group".equals(child.getNodeName()))
        create_level_light_group(child, rel_to);
      else if ("wall".equals(child.getNodeName()))
        create_level_wall(child, rel_to);
      else if ("group".equals(child.getNodeName()))
        create_level_group(child, rel_to);
      else if ("frame".equals(child.getNodeName()))
        create_level_frame(child, rel_to);
      else 
        throw new LevelException("Invalid node in frame: "+child.getNodeName());
    }
  }
}
