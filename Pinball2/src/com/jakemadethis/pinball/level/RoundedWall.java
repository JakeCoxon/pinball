package com.jakemadethis.pinball.level;

import static com.jakemadethis.pinball.builder.FactoryUtil.expected;
import static com.jakemadethis.pinball.builder.FactoryUtil.getAbsolutePosition;
import static com.jakemadethis.pinball.builder.FactoryUtil.optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.jakemadethis.pinball.BaseModel;
import com.jakemadethis.pinball.builder.BuilderNode;
import com.jakemadethis.pinball.builder.FactoryException;

public abstract class RoundedWall {
	
	public static Wall fromNode(BaseModel model, BuilderNode node) {
		HashMap<String, String> atts = node.getAttributes();
		
		float radius =      Float.valueOf( expected(atts, "radius") );
		float restitution = Float.valueOf( optional(atts, "restitution", "0.5") );
		int numSegments = Integer.valueOf( optional(atts, "numSegments", "8") );
		boolean looped =  Boolean.valueOf( optional(atts, "looped", "false") );
		String name =                      optional(atts, "name", "");


		ArrayList<Vector2> points = getPathPoints(node);
		

		ArrayList<Float> pathArray = makeWallPath(points, radius, numSegments, looped);
		
		// Covert into float array
		float[] path = new float[pathArray.size()];
		for (int i = 0; i < path.length; i++) {
			path[i] = pathArray.get(i);
		}
		

		Wall entity = model.addWallPath(path, restitution);
		model.setName(name, entity);
	
		return entity;
	}
	
	private static ArrayList<Float> makeWallPath(ArrayList<Vector2> points, float radius, int segs, boolean looped) {
		ArrayList<Float> pathList = new ArrayList<Float>();

		if (!looped) {
			pathList.add(points.get(0).x);
			pathList.add(points.get(0).y);
		}
		
		int size = points.size();
		for (int i = 1; i < size-1; i++) {
			addPathWithArc(pathList, points.get(i-1), points.get(i), points.get(i+1), radius, segs);
		}

		if (looped) {
			addPathWithArc(pathList, points.get(size-2), points.get(size-1), points.get(0), radius, segs);
			addPathWithArc(pathList, points.get(size-1), points.get(0), points.get(1), radius, segs);
			pathList.add(pathList.get(0));
			pathList.add(pathList.get(1));
		}
		else {
			pathList.add(points.get(size-1).x);
			pathList.add(points.get(size-1).y);
		}
		return pathList;
	}
	
	private static void addPathWithArc(List<Float> path, Vector2 p0,
			Vector2 p1, Vector2 p2, float r, int segs) {
		
		Vector2 p10 = p0.cpy().sub(p1).nor();
		Vector2 p12 = p2.cpy().sub(p1).nor();
		
		Vector2 mid = p12.cpy().add(p10).nor();
		
		
		float cross = p10.crs(mid);
		float h = r / Math.abs(cross);

		
		Vector2 c = mid.cpy().mul(h).add(p1);
		
		// Normals to the two vectors
		Vector2 p12normal = new Vector2(-p12.y, p12.x);
		Vector2 p10normal = new Vector2(p10.y, -p10.x);
		
		// Normals are backwards depending on the direction of the point
		if (cross < 0) {
			p12normal.mul(-1f);
			p10normal.mul(-1f);
		}
		Vector2 a = p10normal.cpy().mul(r).add(c);
		Vector2 b = p12normal.cpy().mul(r).add(c);
		
		path.add(a.x);
		path.add(a.y);
		addArc(path, c.x, c.y, r, p10normal, p12normal, segs);
		path.add(b.x);
		path.add(b.y);
		
		
	}

	private static ArrayList<Vector2> getPathPoints(BuilderNode node) {
		ArrayList<Vector2> points = new ArrayList<Vector2>();
		
		
		// Make all <point> tags
		for (BuilderNode child : node.getChilds()) {
			if (!child.getNodeName().equals("point")) 
				throw new FactoryException("Invalid "+child.getNodeName()+" here");
			
			float[] pos = getAbsolutePosition(node.getParent().getValue(), child.getAttributes());
			points.add(new Vector2(pos[0], pos[1]));
		}
		
		// Clear childs list so the factory doesn't try to build them
		node.getChilds().clear();
		
		return points;
	}
	
	private static void addArc(List<Float> path, float cx, float cy, float radius, 
			Vector2 startDir, Vector2 endDir, int numSegments) {
		Vector2 normal = startDir.cpy();
		
		float cross = startDir.crs(endDir); // sin theta
		float dot = startDir.dot(endDir);		// cos theta

		double angle =  (Math.acos(dot) / numSegments);
		if (cross < 0) angle = Math.PI*2 - angle;
		
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		
		for(int i=0; i < numSegments; i++) {

			float oldx = normal.x;
			float oldy = normal.y;
			normal.x = oldx * cos - oldy * sin;
			normal.y = oldx * sin + oldy * cos;
			normal.nor();

			float x = cx + normal.x * radius;
			float y = cy + normal.y * radius;
			
			path.add(x);
			path.add(y);
			
		}
	}
}
