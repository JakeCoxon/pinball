package com.jakemadethis.pinball.drawing;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class Layer {
	private ImmediateModeRenderer20 lineRenderer;
	private ImmediateModeRenderer20 triangleRenderer;
	private ImmediateModeRenderer20 textureRenderer;
	private ShaderProgram lineShader;
	private ShaderProgram triangleShader;
	private ShaderProgram textureShader;
	

	public Layer(ShaderProgram lineShader, ShaderProgram triangleShader, ShaderProgram textureShader) {
		this.lineShader = lineShader;
		this.triangleShader = triangleShader;
		this.textureShader = textureShader;
		this.lineRenderer = new ImmediateModeRenderer20(false, true, 0);
		this.triangleRenderer = new ImmediateModeRenderer20(false, true, 0);
		this.textureRenderer = new ImmediateModeRenderer20(false, true, 1);
	}
	public ImmediateModeRenderer20 getLineRenderer() { return lineRenderer; }
	public ImmediateModeRenderer20 getTriangleRenderer() { return triangleRenderer; }
	public ImmediateModeRenderer20 getTextureRenderer() { return textureRenderer; }
	
	public void fillCircle(float x, float y, float radius, float[] color) {
		fillSquare(x, y, radius, radius, color);
	}
	public void fillSquare(float x, float y, float w, float h, float[] color) {
		w /= 2;
		h /= 2;
		triangleRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		triangleRenderer.vertex(x-w, y-h, 0f);
		triangleRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		triangleRenderer.vertex(x+w, y-h, 0f);
		triangleRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		triangleRenderer.vertex(x-w, y+h, 0f);

		triangleRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		triangleRenderer.vertex(x+w, y-h, 0f);
		triangleRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		triangleRenderer.vertex(x+w, y+h,	0f);
		triangleRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		triangleRenderer.vertex(x-w, y+h, 0f);
	}

	public void drawTexture(float x, float y, float w, float h, TextureRegion texture, float[] color) {
		drawTexture(x, y, w, h, texture.getU(), texture.getV(), texture.getU2(), texture.getV2(), color);
	}
	public void drawTexture(float x, float y, float w, float h, float u, float v, float u2, float v2, float[] color) {
		w /= 2;
		h /= 2;
		textureRenderer.texCoord(u, v);
		textureRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		textureRenderer.vertex(x-w, y-h, 0f);
		textureRenderer.texCoord(u2, v);
		textureRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		textureRenderer.vertex(x+w, y-h, 0f);
		textureRenderer.texCoord(u, v2);
		textureRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		textureRenderer.vertex(x-w, y+h, 0f);

		textureRenderer.texCoord(u2, v);
		textureRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		textureRenderer.vertex(x+w, y-h, 0f);
		textureRenderer.texCoord(u2, v2);
		textureRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		textureRenderer.vertex(x+w, y+h,	0f);
		textureRenderer.texCoord(u, v2);
		textureRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		textureRenderer.vertex(x-w, y+h, 0f);
	}
	public void drawSquare(float x, float y, float w, float h, float[] color) {
		w /= 2;
		h /= 2;
		float[] coords = new float[] { x-w, y-h, x+w, y-h, x+w, y+h, x-w, y+h, x-w, y-h };
		
		for(int i = 0; i < coords.length - 2; i += 2) {
			lineRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
			lineRenderer.vertex(coords[i], coords[i+1], 0f);
			
			lineRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
			lineRenderer.vertex(coords[i+2], coords[i+3], 0f);
		}

	}
	public void drawLine(float x0, float y0, float x1, float y1, float[] color) {
		lineRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		lineRenderer.vertex(x0, y0, 0f);
		lineRenderer.color(color[0], color[1], color[2], color.length == 4 ? color[3] : 1f);
		lineRenderer.vertex(x1, y1, 0f);
	}
	
	public void render(Matrix4 projModelView) {
		
		lineShader.begin();
		lineShader.setUniformMatrix("u_projModelView", projModelView);
			lineRenderer.begin(lineShader, GL20.GL_LINES);
			lineRenderer.end();
		lineShader.end();
		
		triangleShader.begin();
		triangleShader.setUniformMatrix("u_projModelView", projModelView);
			triangleRenderer.begin(triangleShader, GL20.GL_TRIANGLES);
			triangleRenderer.end();
		triangleShader.end();
		
		if (textureRenderer.hasTexture(0)) {
			textureShader.begin();
			textureShader.setUniformMatrix("u_projModelView", projModelView);
			textureShader.setUniformi("u_sampler0", 0);
				textureRenderer.begin(textureShader, GL20.GL_TRIANGLES);
				textureRenderer.end();
			textureShader.end();
		}
		
	}


}
