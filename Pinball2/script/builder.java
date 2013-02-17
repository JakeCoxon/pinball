interface 

class ScopeBuilder {

  public void node_ball_start(Position pos) {
    model.addBallPos(pos.x, pos.y);
  }

  public void node_bumper(Position at, Length radius) {
    model.add(new Bumper(model, at.x, at.y, radius.get()));
  }

  public void node_left_flipper(Position at, Length length) {
    model.add(new Flipper(model, at.x, at.y, length.get(), Flipper.Type.LEFT));
  }
  public void node_right_flipper(Position at, Length length) {
    model.add(new Flipper(model, at.x, at.y, length.get(), Flipper.Type.RIGHT));
  }

  public void node_light(Position at, Length radius) {
    model.add(new Light(model, at.x, at.y, radius.getOrDefault(20)), Color.RED);
  }

  public void node_light_group(LightGroupBuilder b) {
    
  }
}

class GroupBuilder extends ScopeBuilder {

}

class LevelBulder extends GroupBuilder {

  final private Level level;

  public LevelBulder(String name, Size size) {
    level = new Level(name, size.w, size.h);
  }

  public Level finish() {
    return level;
  }
}
