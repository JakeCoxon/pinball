package com.jakemadethis.pinballeditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jakemadethis.net.Client;
import com.jakemadethis.pinball.Entity;
import com.jakemadethis.pinball.IDrawable;
import com.jakemadethis.pinball.entities.WallPath;
import com.jakemadethis.pinballeditor.net.EditorClient;

public class WallPathTool extends Tool {

	private EditorView view;
	private EditorModel model;
	private SpriteBatch world;
	private OrthographicCamera worldCamera;

	private Color orange = new Color(1f, 0.7f, 0f, 1f);
	private Color white = new Color(1f, 1f, 1f, 1f);
	
	public PathSelector hoverSelection = new PathSelector(null, -1);
	public PathSelector newPointSelection = new PathSelector(null, -1);
	public Vector2 newPoint;
	
	private PathSelector dragSelection = new PathSelector(null, -1);
	private Vector3 worldMouse = new Vector3(0,0,0);
	private Client client;
	

	public WallPathTool(Client client, EditorView view, EditorModel model) {
		this.client = client;
		this.view = view;
		this.model = model;
		this.world = view.world;
		this.worldCamera = view.worldCamera;
	}
	@Override
	public Entity getEntity() {
		return null;
	}
	
	

	@Override
	public void draw() {
		for (WallPath path : model.walls) {
			ArrayList<Vector2> points = path.getPoints();
			
			world.setColor(white);
			for (int i = 0; i < points.size(); i++) {			
				view.drawRect(world, points.get(i).x, points.get(i).y, 6f * worldCamera.zoom, 6f * worldCamera.zoom);
			}
			
			world.setColor(orange);
			if (newPointSelection.valid()) {
				view.drawRect(world, newPoint.x, newPoint.y, 6f * worldCamera.zoom, 6f * worldCamera.zoom);
				
				Vector2 p = newPointSelection.getPath().getPoint(newPointSelection.getId());
				view.drawRect(world, p.x, p.y, 6f * worldCamera.zoom, 6f * worldCamera.zoom);
				
				p = newPointSelection.getPath().getPoint(newPointSelection.getId() + 1);
				view.drawRect(world, p.x, p.y, 6f * worldCamera.zoom, 6f * worldCamera.zoom);
			}
			else if (hoverSelection.valid()) {
				view.drawRect(world, hoverSelection.getPosition().x, 
						hoverSelection.getPosition().y, 
						6f * worldCamera.zoom, 6f * worldCamera.zoom);
			}
		}
	}
	

	private boolean isHover(Vector2 point) {
		final float hoverRadius = 10 * view.worldCamera.zoom;
		return worldMouse.x > point.x - hoverRadius && worldMouse.x < point.x + hoverRadius &&
			   worldMouse.y > point.y - hoverRadius && worldMouse.y < point.y + hoverRadius;
	}
	private boolean isHoverLine(Vector2 point1, Vector2 point2) {
		final float radius = 10 * view.worldCamera.zoom;
		float min_x = Math.min(point1.x, point2.x) - radius;
		float min_y = Math.min(point1.y, point2.y) - radius;
		float max_x = Math.max(point1.x, point2.x) + radius;
		float max_y = Math.max(point1.y, point2.y) + radius;
		float x = worldMouse.x, y = worldMouse.y;
		if (x < min_x || x > max_x || y < min_y || y > max_y) return false;
		
		float a = Math.abs((point2.x-point1.x)*(point1.y-y)-(point1.x-x)*(point2.y-point1.y));
		float b = (float) Math.sqrt((point2.x-point1.x)*(point2.x-point1.x) + (point2.y-point1.y)*(point2.y-point1.y));
		float d = a/b;
		return d < radius;
	}
	
	private Vector2 closestPoint(Vector2 a, Vector2 b, Vector2 p) {
		a = a.cpy();
		b = b.cpy();
		Vector2 ap = p.sub(a);
		Vector2 ab = b.sub(a);
		float ab2 = ab.x*ab.x + ab.y*ab.y;
		float ap_ab = ap.x*ab.x + ap.y*ab.y;
		float t = ap_ab / ab2;
		if (t < 0f) t = 0f;
		else if (t > 1f) t = 1f;
		return a.add(ab.mul(t));
	}
	
	public void think(float delta) {
		
		worldMouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		view.worldCamera.unproject(worldMouse);
		
		if (hoverSelection.valid() && isHover(hoverSelection.getPosition())) return;
		
		for (WallPath path : model.walls) {
			ArrayList<Vector2> points = path.getPoints();
			for (int i = 0; i < points.size(); i++) {
				if (isHover(points.get(i))) {
					hoverSelection.set(path, i);
					newPointSelection.clear();
					return;
				}
			}
		}
		hoverSelection.clear();

		for (WallPath path : model.walls) {
			ArrayList<Vector2> points = path.getPoints();
			for (int i = 0; i < points.size()-1; i++) {
				if (isHoverLine(points.get(i), points.get(i+1))) {
					newPoint = closestPoint(points.get(i), points.get(i+1), new Vector2(worldMouse.x, worldMouse.y));
					newPointSelection.set(path, i);
					return;
				}
			}
		}
		newPointSelection.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.FORWARD_DEL && dragSelection.valid()) {
			WallPath path = dragSelection.getPath();
			path.getPoints().remove(dragSelection.getId());
			if (path.getPoints().size() == 1) {
				model.remove(path);
			}
			dragSelection.clear();
			hoverSelection.clear();
			return true;
		}
		else if (keycode == Keys.ENTER) {
			//for (WallPath path : walls) {
			//	System.out.println(path.xmlString());
			//}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (newPointSelection.valid()) {
			int id = newPointSelection.getPath().addPoint(newPointSelection.getId()+1, newPoint);
			dragSelection.set(newPointSelection.getPath(), id);
			
			EditorClient.sendAddWallPoint(client, dragSelection.getPath().ID, id, newPoint.x, newPoint.y);
			return true;
		}
		if (hoverSelection.valid()) {
			dragSelection.set(hoverSelection);
			return true;
		}
		
		float mouse_x = view.snap(worldMouse.x);
		float mouse_y = view.snap(worldMouse.y);
		WallPath path = model.addWall(mouse_x*100f, mouse_y*100f, mouse_x*100f+1, mouse_y*100f+1, 1f);
		dragSelection.set(path, 1);
	
		//EditorClient.sendAddWall(client, mouse_x, mouse_y);
		
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (dragSelection.valid()) {
			float mouse_x = view.snap(worldMouse.x);
			float mouse_y = view.snap(worldMouse.y);
			dragSelection.getPosition().x = mouse_x;
			dragSelection.getPosition().y = mouse_y;
			
			EditorClient.sendMoveWall(client, dragSelection.getPath().ID, dragSelection.getId(), mouse_x, mouse_y);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
