package com.jakemadethis.pinball.game;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.jakemadethis.pinball.AssetLoadingTask;

public class BackgroundRenderer extends AssetLoadingTask<Texture> {
	final String vertexShader = 
  	"attribute vec4 a_position;    \n" + 
  	"void main()                  \n" + 
  	"{                            \n" + 
  	"   gl_Position = a_position;  \n" + 
  	"}                            \n";
  final String fragmentShader = 
  	"#ifdef GL_ES\n" + 
  	"precision mediump float;\n" + 
  	"#endif\n"+ 
		"uniform vec2 u_mid;\n" +
		"uniform float u_radius;\n" +
		"uniform float u_thickness;\n" +
  	"void main()\n" + 
  	"{"+ 
  	"  float x = gl_FragCoord.x;\n" +
		"  float len = length(gl_FragCoord.xy - u_mid);\n" +
		//"  vec4 a = vec4 ( 0.0, 0.0, 0.0, 1.0 );\n" +
		//"  vec4 b = vec4 ( 0.2, 0.2, 0.2, 1.0 );\n" +
		"  vec4 a = vec4 ( 0.6, 0.2, 0.4, 1.0 );\n" +
		"  vec4 b = vec4 ( 0.8, 0.4, 0.6, 1.0 );\n" +
		"  vec4 color;\n" +
		//"  if (round(mod(x + gl_FragCoord.y, 5.0)) > 0.0) {\n"+
		"  if (floor(mod(x + gl_FragCoord.y, u_thickness)) > 2.0) {" +
		"    float d = (1.0 - len / u_radius);\n" +
		"    if (d < 0.0) d = 0.0;\n" +
		"    color = a + (b-a) * d*d;\n" +
		//"    color = b;" +
		"  } else {\n" +
		"    color = a;\n" +
		"  }\n" +
		"  gl_FragColor = color;\n" +
  	"}";
	private ShaderProgram bgShader;
	private Mesh mesh;
	private FrameBuffer fbo;
	private final float midx;
	private final float midy;
	private final float radius;
	private final float thickness;
	private final int width;
	private final int height;
  
  public BackgroundRenderer(int width, int height, float midx, float midy, float radius, float thickness) {
		this.width = width;
		this.height = height;
		this.midx = midx;
		this.midy = midy;
		this.radius = radius;
		this.thickness = thickness;
		
	  
  }
  
	@Override
	public Texture syncLoad() {

		bgShader = new ShaderProgram(vertexShader, fragmentShader);
	
		if(!bgShader.isCompiled())
			throw new RuntimeException(bgShader.getLog());
		
		mesh = new Mesh(true, 6, 0, new VertexAttribute( Usage.Position, 3, "a_position" ));
		
		mesh.setVertices(new float[] { 
			-1f, -1f, 0,
			-1f, 1f, 0,
			1f, -1f, 0,
			
			1f, 1f, 0,
			-1f, 1f, 0,
			1f, -1f, 0, } );	
		
		fbo = new FrameBuffer(Format.RGB565, width, height, false);
		
		fbo.begin();
		  bgShader.begin();
				bgShader.setUniformf("u_mid", midx, midy);
				bgShader.setUniformf("u_radius", radius);
				bgShader.setUniformf("u_thickness", thickness);
			  mesh.render(bgShader, GL10.GL_TRIANGLES);
		  bgShader.end();
	  fbo.end();
	  
		return fbo.getColorBufferTexture();
	}
}
