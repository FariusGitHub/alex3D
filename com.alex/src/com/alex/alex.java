package com.alex;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.alex.R;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class alex extends Activity{
    private GLSurfaceView surface;
    
    public static String selection = "ALL BODY MOVEABLE";
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	surface = new cube(this);
        setContentView(surface);
    }
    public void onResume() {
        super.onResume();
        surface.onResume();
    }
    public void onPause() {
        super.onPause();
        surface.onPause();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.main, menu);
    	return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.armonly:
            selection = "bouger les bras";
        	Toast.makeText(alex.this, selection, Toast.LENGTH_SHORT).show();
            return true;

        case R.id.legonly:
            selection = "écarter les jambons";
        	Toast.makeText(alex.this, selection, Toast.LENGTH_SHORT).show();
            return true;
        
        case R.id.headonly:
            selection = "pivoter la tête";
        	Toast.makeText(alex.this, selection, Toast.LENGTH_SHORT).show();
            return true;

        case R.id.armleg:
            selection = "balançoire de membre";
        	Toast.makeText(alex.this, selection, Toast.LENGTH_SHORT).show();
            return true;    

        case R.id.armhead:
            selection = "tête et bras ensemble";
        	Toast.makeText(alex.this, selection, Toast.LENGTH_SHORT).show();
            return true;
        
        case R.id.leghead:
            selection = "tête/jambon ensemble";
        	Toast.makeText(alex.this, selection, Toast.LENGTH_SHORT).show();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }    
}
class cube extends GLSurfaceView implements Renderer{
	private IntBuffer ver;
    private IntBuffer col;
    private IntBuffer nor;
    

    float X=0;
    float Z=9f;
    float ZA=1f;
    float ZL=1f;
    float ZH=1f;
    float ZZ=0f;
    float XOLD;
    
    private IntBuffer menuVer;
    private IntBuffer menuTxt;
	private int yrot;
    private int xrot;
    private int yOld, xOld;     
    private int height;
    private int width;
    private boolean light=true;
    private boolean blend=false;

	public cube(Context context) {
		super(context);
        setRenderer(this);
		//this.setRenderMode(RENDERMODE_WHEN_DIRTY);
	}
    public boolean onTouchEvent(MotionEvent event) {
    	int x=(int) event.getX();
    	int y=(int) event.getY();
    	
    	prepareCube();
    	//prepareCube2();
    	//prepareCube3();
    	
    	if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	xrot-=(yOld-y);
        	yrot+=(x-xOld);
        } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	if( 	 y<=(height*0.5) && y>=(height*0.0) && x<=width*1.0 && x>=width*0.0){
        		if (X>-Z) {X--; Log.e("X=", String.valueOf(X));
        		}
        		else if(X<=-Z-1f) {X=-Z;
        		}
        	}else if(y<=(height*1.0) && y>=(height*0.5) && x<=width*1.0 && x>=width*0.0){
        		if (X<Z) {X++; Log.e("X=", String.valueOf(X));
        		} 
        		else if(X>=Z+1f) {X=Z;
        		}
        	}
        	
        }
        xOld=x;
        yOld=y;
        this.requestRender();
        return true;
	}
    
    
    private IntBuffer getIntBuffer(int[] data){
    	ByteBuffer byteBuf = ByteBuffer.allocateDirect(data.length * 4);
    	byteBuf.order(ByteOrder.nativeOrder());
    	IntBuffer buf = byteBuf.asIntBuffer();
    	buf.put(data);
    	buf.position(0);
    	return buf;
    }
	
	private int toInt(float num){
    	return (int)(num*65536);
    }
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	height=this.getHeight();
		width=this.getWidth();

		prepareCube();
    	//prepareCube2();
    	//prepareCube3();
    	prepareButtons();
    
    	int _I=toInt(0.1f);
    	int _V=toInt(0.5f);
    	int I=toInt(1.0f);
    	int II=toInt(2.0f);
    	gl.glLightxv( GL10.GL_LIGHT0, GL10.GL_AMBIENT, getIntBuffer( new int[]{_I,_I,_I,I} ) );
		gl.glLightxv( GL10.GL_LIGHT0, GL10.GL_DIFFUSE, getIntBuffer( new int[]{I,I,_V,I} ) );
		gl.glLightxv( GL10.GL_LIGHT0, GL10.GL_POSITION, getIntBuffer( new int[]{II,I,0,I} ) );
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
		
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		gl.glFrontFace(GL10.GL_CW);
		gl.glClientActiveTexture(GL10.GL_TEXTURE0);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

    }
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 90.0f, ((float)w)/h, 0.1f, 30.0f);
	}
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	if(light) {
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			gl.glDisable(GL10.GL_LIGHTING);
		}
    	if(blend) {
    		gl.glEnable(GL10.GL_BLEND);
    		gl.glDisable(GL10.GL_DEPTH_TEST);
    	}else {
			gl.glDisable(GL10.GL_BLEND);
			gl.glEnable(GL10.GL_DEPTH_TEST);

		}
    	
    	
    	gl.glMatrixMode(GL10.GL_MODELVIEW);
    	gl.glPushMatrix();
	    	gl.glLoadIdentity();
	    	gl.glTranslatef(0,0,-5f);
	        gl.glRotatef(xrot,1,0,0); 
	    	gl.glRotatef(yrot,0,1,0);
			drawCube(gl);
		gl.glPopMatrix();
		
		gl.glMatrixMode (GL10.GL_PROJECTION);
		gl.glPushMatrix(); 
			gl.glLoadIdentity (); 
			gl.glOrthof (0, width, height, 0, -1, 1);
		
			gl.glVertexPointer(3, GL10.GL_FIXED, 0, menuVer);
			gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, menuTxt);

		gl.glPopMatrix();
	}
	

	private void drawCube(GL10 gl){ 
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, ver);
		gl.glNormalPointer(GL10.GL_FIXED, 0, nor);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, col);
		
		//ARMS
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 3, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 6, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 9, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 15, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 18, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 21, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 24, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 27, 3);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 30, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 33, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 36, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 39, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 42, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 45, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 48, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 51, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 54, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 57, 3);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 60, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 63, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 66, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 69, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 72, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 75, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 78, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 81, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 84, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 87, 3);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 90, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 93, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 96, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 99, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 102, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 105, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 108, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 111, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 114, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 117, 3);

		//LEG
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 120, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 123, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 126, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 129, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 132, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 135, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 138, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 141, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 144, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 147, 3);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 150, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 153, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 156, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 159, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 162, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 165, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 168, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 171, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 174, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 177, 3);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 180, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 183, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 186, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 189, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 192, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 195, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 198, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 201, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 204, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 207, 3);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 210, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 213, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 216, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 219, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 222, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 225, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 228, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 231, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 234, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 237, 3);

		//HEAD
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 240, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 243, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 246, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 249, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 252, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 255, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 258, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 261, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 264, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 267, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 270, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 273, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 276, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 279, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 282, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 285, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 288, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 291, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 294, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 297, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 300, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 303, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 306, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 309, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 312, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 315, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 318, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 321, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 324, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 327, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 330, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 333, 3);
		
		//BODY
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 336, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 339, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 342, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 345, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 348, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 351, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 354, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 357, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 360, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 363, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 366, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 369, 3);

		//OTHER HEAD
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 372, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 375, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 378, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 381, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 384, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 387, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 390, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 393, 3);

		//FACE RED
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 396, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 399, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 402, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 405, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 408, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 411, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 414, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 417, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 420, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 423, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 426, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 429, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 432, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 435, 3);
		
		//FACE YELLOW
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 438, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 441, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 444, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 447, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 450, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 453, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 456, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 459, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 462, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 465, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 468, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 471, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 474, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 477, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 480, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 483, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 486, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 489, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 492, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 495, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 498, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 501, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 504, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 507, 3);
		
		//FACE BLUE
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 510, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 513, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 516, 3);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 519, 3);

	}
    
	private void prepareButtons(){
       /*************\
	   |0---2	4---6|
	   ||   |	|   ||
	   |1---3	5---7|
       \*************/
    	int dimH=toInt(height);
    	int dimW=toInt(width);
    	int dimS=toInt(64.0f);
    	int one=toInt(1.0f);
    	int[] vertices = {
    		0,dimH,0, 0,dimH-dimS,0, dimS,dimH,0, dimS,dimH-dimS,0,
    		dimW-dimS,dimH,0, dimW-dimS,dimH-dimS,0, dimW,dimH,0, dimW,dimH-dimS,0
    	};
    	int[] texture = {
    		one,one, one,0, 0,one, 0,0,
    		one,one, one,0, 0,one, 0,0
    	};
		menuVer=getIntBuffer(vertices);
		menuTxt=getIntBuffer(texture);
    }
private void prepareCube(){
    	
    	if ( alex.selection == "bouger les bras") {ZA=1f; ZL=0f; ZH=0f;};
    	if ( alex.selection == "écarter les jambons") {ZA=0f; ZL=1f; ZH=0f;};
    	if ( alex.selection == "balançoire de membre") {ZA=1f; ZL=1f; ZH=0f;};
    	if ( alex.selection == "tête et bras ensemble") {ZA=1f; ZL=0f; ZH=1f;};
    	if ( alex.selection == "tête/jambon ensemble") {ZA=0f; ZL=1f; ZH=1f;};
    	if ( alex.selection == "pivoter la tête") {ZA=0f; ZL=0f; ZH=1f;};
    	
    	//ARM PARAMETERS
    	
    	float AAX=-2.000f, AAY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), AAZ=+(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	float ABX=-2.000f, ABY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), ABZ=-(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));
    	float ACX=-1.000f, ACY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), ACZ=+(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	float ADX=-1.000f, ADY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), ADZ=-(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));

    	float AEX=-2.000f, AEY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), AEZ=+(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));
    	float AFX=-2.000f, AFY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), AFZ=-(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	float AGX=-1.000f, AGY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), AGZ=+(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));
    	float AHX=-1.000f, AHY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), AHZ=-(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	
    	float AIX=-2.000f, AIY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZA))), AIZ=+(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZA)));
    	float AJX=-2.000f, AJY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZA))), AJZ=-(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZA)));
    	float AKX=-1.000f, AKY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZA))), AKZ=+(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZA)));
    	float ALX=-1.000f, ALY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZA))), ALZ=-(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZA)));

    	float BAX=+1.000f, BAY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), BAZ=+(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));
    	float BBX=+1.000f, BBY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), BBZ=-(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	float BCX=+2.000f, BCY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), BCZ=+(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));
    	float BDX=+2.000f, BDY=+1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), BDZ=-(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));

    	float BEX=+1.000f, BEY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), BEZ=+(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	float BFX=+1.000f, BFY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), BFZ=-(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));
    	float BGX=+2.000f, BGY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZA))), BGZ=+(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZA)));
    	float BHX=+2.000f, BHY=+1.5f-(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZA))), BHZ=-(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZA)));

    	float BIX=+1.000f, BIY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZA))), BIZ=+(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZA)));
    	float BJX=+1.000f, BJY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZA))), BJZ=-(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZA)));
    	float BKX=+2.000f, BKY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZA))), BKZ=+(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZA)));
    	float BLX=+2.000f, BLY=+1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZA))), BLZ=-(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZA)));

    	
    	//LEG PARAMETERS
    	float CAX=-1.000f, CAY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZL))), CAZ=+(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZL)));
    	float CBX=-1.000f, CBY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZL))), CBZ=-(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZL)));
    	float CCX=+0.000f, CCY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZL))), CCZ=+(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZL)));
    	float CDX=+0.000f, CDY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZL))), CDZ=-(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZL)));

    	float CEX=-1.000f, CEY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04+10*X*ZL))), CEZ=+(float)(2.062*Math.sin(Math.toRadians(14.04+10*X*ZL)));
    	float CFX=-1.000f, CFY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04-10*X*ZL))), CFZ=-(float)(2.062*Math.sin(Math.toRadians(14.04-10*X*ZL)));
    	float CGX=+0.000f, CGY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04+10*X*ZL))), CGZ=+(float)(2.062*Math.sin(Math.toRadians(14.04+10*X*ZL)));
    	float CHX=+0.000f, CHY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04-10*X*ZL))), CHZ=-(float)(2.062*Math.sin(Math.toRadians(14.04-10*X*ZL)));

    	float CIX=-1.000f, CIY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZL))), CIZ=+(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZL)));
    	float CJX=-1.000f, CJY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZL))), CJZ=-(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZL)));
    	float CKX=+0.000f, CKY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZL))), CKZ=+(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZL)));
    	float CLX=+0.000f, CLY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZL))), CLZ=-(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZL)));

    	float DAX=+0.000f, DAY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZL))), DAZ=+(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZL)));
    	float DBX=+0.000f, DBY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZL))), DBZ=-(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZL)));
    	float DCX=+1.000f, DCY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00+10*X*ZL))), DCZ=+(float)(0.707*Math.sin(Math.toRadians(45.00+10*X*ZL)));
    	float DDX=+1.000f, DDY=-1.5f+(float)(0.707*Math.cos(Math.toRadians(45.00-10*X*ZL))), DDZ=-(float)(0.707*Math.sin(Math.toRadians(45.00-10*X*ZL)));

    	float DEX=+0.000f, DEY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04-10*X*ZL))), DEZ=+(float)(2.062*Math.sin(Math.toRadians(14.04-10*X*ZL)));
    	float DFX=+0.000f, DFY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04+10*X*ZL))), DFZ=-(float)(2.062*Math.sin(Math.toRadians(14.04+10*X*ZL)));
    	float DGX=+1.000f, DGY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04-10*X*ZL))), DGZ=+(float)(2.062*Math.sin(Math.toRadians(14.04-10*X*ZL)));
    	float DHX=+1.000f, DHY=-1.5f-(float)(2.062*Math.cos(Math.toRadians(14.04+10*X*ZL))), DHZ=-(float)(2.062*Math.sin(Math.toRadians(14.04+10*X*ZL)));

    	float DIX=+0.000f, DIY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZL))), DIZ=+(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZL)));
    	float DJX=+0.000f, DJY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZL))), DJZ=-(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZL)));
    	float DKX=+1.000f, DKY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31-10*X*ZL))), DKZ=+(float)(2.550*Math.sin(Math.toRadians(11.31-10*X*ZL)));
    	float DLX=+1.000f, DLY=-1.5f-(float)(2.550*Math.cos(Math.toRadians(11.31+10*X*ZL))), DLZ=-(float)(2.550*Math.sin(Math.toRadians(11.31+10*X*ZL)));

        //HEAD 
    	float EAX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), EAY=4.000f, EAZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH))); 
    	float EBX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), EBY=2.500f, EBZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH))); 
    	float ECX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), ECY=2.000f, ECZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	
    	float EDX=-(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), EDY=4.000f, EDZ=+(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	float EEX=-(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), EEY=3.000f, EEZ=+(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	float EFX=-(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), EFY=2.500f, EFZ=+(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	float EGX=-(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), EGY=2.000f, EGZ=+(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	
    	float EHX=-(float)(1.000*Math.cos(Math.toRadians(00.00+10*X*ZH))), EHY=4.000f, EHZ=+(float)(1.000*Math.sin(Math.toRadians(00.00+10*X*ZH))); 
    	float EIX=-(float)(1.000*Math.cos(Math.toRadians(00.00+10*X*ZH))), EIY=3.000f, EIZ=+(float)(1.000*Math.sin(Math.toRadians(00.00+10*X*ZH)));
    	float EJX=-(float)(1.000*Math.cos(Math.toRadians(00.00+10*X*ZH))), EJY=2.500f, EJZ=+(float)(1.000*Math.sin(Math.toRadians(00.00+10*X*ZH)));
    	float EKX=-(float)(1.000*Math.cos(Math.toRadians(00.00+10*X*ZH))), EKY=2.000f, EKZ=+(float)(1.000*Math.sin(Math.toRadians(00.00+10*X*ZH)));
    	
    	float ELX=-(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), ELY=4.000f, ELZ=-(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	float EMX=-(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), EMY=2.500f, EMZ=-(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	float ENX=-(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), ENY=2.250f, ENZ=-(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	float EOX=-(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), EOY=2.000f, EOZ=-(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	
    	float EPX=-(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), EPY=4.000f, EPZ=-(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float EQX=-(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), EQY=2.250f, EQZ=-(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));
    	float ERX=-(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), ERY=2.000f, ERZ=-(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));

    	
    	float FAX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), FAY=4.000f, FAZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float FBX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), FBY=2.500f, FBZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float FCX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), FCY=2.000f, FCZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));
    	
    	float FDX=+(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), FDY=4.000f, FDZ=+(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	float FEX=+(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), FEY=3.000f, FEZ=+(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	float FFX=+(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), FFY=2.500f, FFZ=+(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	float FGX=+(float)(1.118*Math.cos(Math.toRadians(26.57-10*X*ZH))), FGY=2.000f, FGZ=+(float)(1.118*Math.sin(Math.toRadians(26.57-10*X*ZH)));
    	
    	float FHX=+(float)(1.000*Math.cos(Math.toRadians(00.00-10*X*ZH))), FHY=4.000f, FHZ=+(float)(1.000*Math.sin(Math.toRadians(00.00-10*X*ZH))); 
    	float FIX=+(float)(1.000*Math.cos(Math.toRadians(00.00-10*X*ZH))), FIY=3.000f, FIZ=+(float)(1.000*Math.sin(Math.toRadians(00.00-10*X*ZH)));
    	float FJX=+(float)(1.000*Math.cos(Math.toRadians(00.00-10*X*ZH))), FJY=2.500f, FJZ=+(float)(1.000*Math.sin(Math.toRadians(00.00-10*X*ZH)));
    	float FKX=+(float)(1.000*Math.cos(Math.toRadians(00.00-10*X*ZH))), FKY=2.000f, FKZ=+(float)(1.000*Math.sin(Math.toRadians(00.00-10*X*ZH)));
    	
    	float FLX=+(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), FLY=4.000f, FLZ=-(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	float FMX=+(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), FMY=2.500f, FMZ=-(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	float FNX=+(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), FNY=2.250f, FNZ=-(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	float FOX=+(float)(1.118*Math.cos(Math.toRadians(26.57+10*X*ZH))), FOY=2.000f, FOZ=-(float)(1.118*Math.sin(Math.toRadians(26.57+10*X*ZH)));
    	
    	float FPX=+(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), FPY=4.000f, FPZ=-(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH))); 
    	float FQX=+(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), FQY=2.250f, FQZ=-(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	float FRX=+(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), FRY=2.000f, FRZ=-(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	
    	//BODY	
    	float AX= -1.0f,  AY= +2.0f,  AZ= +0.5f;
    	float BX= +1.0f,  BY= +2.0f,  BZ= +0.5f;
    	float CX= +1.0f,  CY= -1.0f,  CZ= +0.5f;
    	float DX= -1.0f,  DY= -1.0f,  DZ= +0.5f;
    	float EX= -1.0f,  EY= +2.0f,  EZ= -0.5f;
    	float FX= +1.0f,  FY= +2.0f,  FZ= -0.5f;
    	float GX= +1.0f,  GY= -1.0f,  GZ= -0.5f;
    	float HX= -1.0f,  HY= -1.0f,  HZ= -0.5f;

    	//HEAD OTHER
    	float IAX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), IAY=4.000f, IAZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	float IIX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), IIY=2.000f, IIZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));

    	float ICX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), ICY=4.000f, ICZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));
    	float IJX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), IJY=2.000f, IJZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));

    	float IBX=-(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), IBY=4.000f, IBZ=-(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));
    	float IEX=-(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), IEY=2.250f, IEZ=-(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));
    	float IGX=-(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), IGY=2.000f, IGZ=-(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH)));

    	float IDX=+(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), IDY=4.000f, IDZ=-(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	float IFX=+(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), IFY=2.250f, IFZ=-(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	float IHX=+(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), IHY=2.000f, IHZ=-(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));

    	//FACE
    	float HAX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), HAY=4.000f, HAZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH))); 
    	float HBX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), HBY=3.250f, HBZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	float HCX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), HCY=3.000f, HCZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH)));
    	float HDX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), HDY=2.695f, HDZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH))); 
    	float HEX=-(float)(1.414*Math.cos(Math.toRadians(45.00+10*X*ZH))), HEY=2.000f, HEZ=+(float)(1.414*Math.sin(Math.toRadians(45.00+10*X*ZH))); 

    	float HFX=-(float)(1.250*Math.cos(Math.toRadians(53.13+10*X*ZH))), HFY=3.000f, HFZ=+(float)(1.250*Math.sin(Math.toRadians(53.13+10*X*ZH))); 
    	float HGX=-(float)(1.250*Math.cos(Math.toRadians(53.13+10*X*ZH))), HGY=2.695f, HGZ=+(float)(1.250*Math.sin(Math.toRadians(53.13+10*X*ZH)));

    	float HHX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HHY=4.000f, HHZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH))); 
    	float HIX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HIY=3.625f, HIZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));
    	float HJX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HJY=3.250f, HJZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));
    	float HKX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HKY=3.000f, HKZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));
    	float HLX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HLY=2.695f, HLZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));
    	float HMX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HMY=2.300f, HMZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));
    	float HNX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HNY=2.184f, HNZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));
    	float HOX=-(float)(1.118*Math.cos(Math.toRadians(63.43+10*X*ZH))), HOY=2.000f, HOZ=+(float)(1.118*Math.sin(Math.toRadians(63.43+10*X*ZH)));

    	float HPX=-(float)(1.031*Math.cos(Math.toRadians(75.96+10*X*ZH))), HPY=3.000f, HPZ=+(float)(1.031*Math.sin(Math.toRadians(75.96+10*X*ZH))); 
    	float HQX=-(float)(1.031*Math.cos(Math.toRadians(75.96+10*X*ZH))), HQY=2.695f, HQZ=+(float)(1.031*Math.sin(Math.toRadians(75.96+10*X*ZH)));
    	float HRX=-(float)(1.031*Math.cos(Math.toRadians(75.96+10*X*ZH))), HRY=2.500f, HRZ=+(float)(1.031*Math.sin(Math.toRadians(75.96+10*X*ZH)));
    	float HSX=-(float)(1.031*Math.cos(Math.toRadians(75.96+10*X*ZH))), HSY=2.300f, HSZ=+(float)(1.031*Math.sin(Math.toRadians(75.96+10*X*ZH)));

    	float GPX=+(float)(1.031*Math.cos(Math.toRadians(75.96-10*X*ZH))), GPY=3.000f, GPZ=+(float)(1.031*Math.sin(Math.toRadians(75.96-10*X*ZH))); 
    	float GQX=+(float)(1.031*Math.cos(Math.toRadians(75.96-10*X*ZH))), GQY=2.695f, GQZ=+(float)(1.031*Math.sin(Math.toRadians(75.96-10*X*ZH)));
    	float GRX=+(float)(1.031*Math.cos(Math.toRadians(75.96-10*X*ZH))), GRY=2.500f, GRZ=+(float)(1.031*Math.sin(Math.toRadians(75.96-10*X*ZH)));
    	float GSX=+(float)(1.031*Math.cos(Math.toRadians(75.96-10*X*ZH))), GSY=2.300f, GSZ=+(float)(1.031*Math.sin(Math.toRadians(75.96-10*X*ZH)));

    	float GHX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GHY=4.000f, GHZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH)));
    	float GIX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GIY=3.625f, GIZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH)));
    	float GJX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GJY=3.250f, GJZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH)));
    	float GKX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GKY=3.000f, GKZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH))); 
    	float GLX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GLY=2.695f, GLZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH))); 
    	float GMX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GMY=2.300f, GMZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH)));
    	float GNX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GNY=2.184f, GNZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH))); 
    	float GOX=+(float)(1.118*Math.cos(Math.toRadians(63.43-10*X*ZH))), GOY=2.000f, GOZ=+(float)(1.118*Math.sin(Math.toRadians(63.43-10*X*ZH))); 

    	float GFX=+(float)(1.250*Math.cos(Math.toRadians(53.13-10*X*ZH))), GFY=3.000f, GFZ=+(float)(1.250*Math.sin(Math.toRadians(53.13-10*X*ZH))); 
    	float GGX=+(float)(1.250*Math.cos(Math.toRadians(53.13-10*X*ZH))), GGY=2.695f, GGZ=+(float)(1.250*Math.sin(Math.toRadians(53.13-10*X*ZH))); 

    	float GAX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), GAY=4.000f, GAZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float GBX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), GBY=3.250f, GBZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float GCX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), GCY=3.000f, GCZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float GDX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), GDY=2.695f, GDZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 
    	float GEX=+(float)(1.414*Math.cos(Math.toRadians(45.00-10*X*ZH))), GEY=2.000f, GEZ=+(float)(1.414*Math.sin(Math.toRadians(45.00-10*X*ZH))); 

    	int[] vertices = {
				//ARMS

	    		toInt(AAX), toInt(AAY), toInt(AAZ),    toInt(AEX), toInt(AEY), toInt(AEZ),    toInt(AFX), toInt(AFY), toInt(AFZ),    
	    		toInt(AAX), toInt(AAY), toInt(AAZ),    toInt(AFX), toInt(AFY), toInt(AFZ),    toInt(ABX), toInt(ABY), toInt(ABZ),    
	    		toInt(ACX), toInt(ACY), toInt(ACZ),    toInt(AGX), toInt(AGY), toInt(AGZ),    toInt(AEX), toInt(AEY), toInt(AEZ),    
	    		toInt(ACX), toInt(ACY), toInt(ACZ),    toInt(AEX), toInt(AEY), toInt(AEZ),    toInt(AAX), toInt(AAY), toInt(AAZ),    
	    		toInt(ADX), toInt(ADY), toInt(ADZ),    toInt(AHX), toInt(AHY), toInt(AHZ),    toInt(AGX), toInt(AGY), toInt(AGZ),    
	    		toInt(ADX), toInt(ADY), toInt(ADZ),    toInt(AGX), toInt(AGY), toInt(AGZ),    toInt(ACX), toInt(ACY), toInt(ACZ),    
	    		toInt(ABX), toInt(ABY), toInt(ABZ),    toInt(AFX), toInt(AFY), toInt(AFZ),    toInt(AHX), toInt(AHY), toInt(AHZ),    
	    		toInt(ABX), toInt(ABY), toInt(ABZ),    toInt(AHX), toInt(AHY), toInt(AHZ),    toInt(ADX), toInt(ADY), toInt(ADZ),    
	    		toInt(AAX), toInt(AAY), toInt(AAZ),    toInt(ABX), toInt(ABY), toInt(ABZ),    toInt(ADX), toInt(ADY), toInt(ADZ),    
	    		toInt(AAX), toInt(AAY), toInt(AAZ),    toInt(ADX), toInt(ADY), toInt(ADZ),    toInt(ACX), toInt(ACY), toInt(ACZ),    
	    		
	    		toInt(AEX), toInt(AEY), toInt(AEZ),    toInt(AIX), toInt(AIY), toInt(AIZ),    toInt(AJX), toInt(AJY), toInt(AJZ),    
	    		toInt(AEX), toInt(AEY), toInt(AEZ),    toInt(AJX), toInt(AJY), toInt(AJZ),    toInt(AFX), toInt(AFY), toInt(AFZ),    
	    		toInt(AGX), toInt(AGY), toInt(AGZ),    toInt(AKX), toInt(AKY), toInt(AKZ),    toInt(AIX), toInt(AIY), toInt(AIZ),    
	    		toInt(AGX), toInt(AGY), toInt(AGZ),    toInt(AIX), toInt(AIY), toInt(AIZ),    toInt(AEX), toInt(AEY), toInt(AEZ),    
	    		toInt(AHX), toInt(AHY), toInt(AHZ),    toInt(ALX), toInt(ALY), toInt(ALZ),    toInt(AKX), toInt(AKY), toInt(AKZ),    
	    		toInt(AHX), toInt(AHY), toInt(AHZ),    toInt(AKX), toInt(AKY), toInt(AKZ),    toInt(AGX), toInt(AGY), toInt(AGZ),    
	    		toInt(AFX), toInt(AFY), toInt(AFZ),    toInt(AJX), toInt(AJY), toInt(AJZ),    toInt(ALX), toInt(ALY), toInt(ALZ),    
	    		toInt(AFX), toInt(AFY), toInt(AFZ),    toInt(ALX), toInt(ALY), toInt(ALZ),    toInt(AHX), toInt(AHY), toInt(AHZ),    
	    		toInt(ALX), toInt(ALY), toInt(ALZ),    toInt(AJX), toInt(AJY), toInt(AJZ),    toInt(AIX), toInt(AIY), toInt(AIZ),    
	    		toInt(ALX), toInt(ALY), toInt(ALZ),    toInt(AIX), toInt(AIY), toInt(AIZ),    toInt(AKX), toInt(AKY), toInt(AKZ),    

	    		toInt(BAX), toInt(BAY), toInt(BAZ),    toInt(BEX), toInt(BEY), toInt(BEZ),    toInt(BFX), toInt(BFY), toInt(BFZ),    
	    		toInt(BAX), toInt(BAY), toInt(BAZ),    toInt(BFX), toInt(BFY), toInt(BFZ),    toInt(BBX), toInt(BBY), toInt(BBZ),    
	    		toInt(BCX), toInt(BCY), toInt(BCZ),    toInt(BGX), toInt(BGY), toInt(BGZ),    toInt(BEX), toInt(BEY), toInt(BEZ),    
	    		toInt(BCX), toInt(BCY), toInt(BCZ),    toInt(BEX), toInt(BEY), toInt(BEZ),    toInt(BAX), toInt(BAY), toInt(BAZ),    
	    		toInt(BDX), toInt(BDY), toInt(BDZ),    toInt(BHX), toInt(BHY), toInt(BHZ),    toInt(BGX), toInt(BGY), toInt(BGZ),    
	    		toInt(BDX), toInt(BDY), toInt(BDZ),    toInt(BGX), toInt(BGY), toInt(BGZ),    toInt(BCX), toInt(BCY), toInt(BCZ),    
	    		toInt(BBX), toInt(BBY), toInt(BBZ),    toInt(BFX), toInt(BFY), toInt(BFZ),    toInt(BHX), toInt(BHY), toInt(BHZ),    
	    		toInt(BBX), toInt(BBY), toInt(BBZ),    toInt(BHX), toInt(BHY), toInt(BHZ),    toInt(BDX), toInt(BDY), toInt(BDZ),    
	    		toInt(BAX), toInt(BAY), toInt(BAZ),    toInt(BBX), toInt(BBY), toInt(BBZ),    toInt(BDX), toInt(BDY), toInt(BDZ),    
	    		toInt(BAX), toInt(BAY), toInt(BAZ),    toInt(BDX), toInt(BDY), toInt(BDZ),    toInt(BCX), toInt(BCY), toInt(BCZ),    
	    		
	    		toInt(BEX), toInt(BEY), toInt(BEZ),    toInt(BIX), toInt(BIY), toInt(BIZ),    toInt(BJX), toInt(BJY), toInt(BJZ),    
	    		toInt(BEX), toInt(BEY), toInt(BEZ),    toInt(BJX), toInt(BJY), toInt(BJZ),    toInt(BFX), toInt(BFY), toInt(BFZ),    
	    		toInt(BGX), toInt(BGY), toInt(BGZ),    toInt(BKX), toInt(BKY), toInt(BKZ),    toInt(BIX), toInt(BIY), toInt(BIZ),    
	    		toInt(BGX), toInt(BGY), toInt(BGZ),    toInt(BIX), toInt(BIY), toInt(BIZ),    toInt(BEX), toInt(BEY), toInt(BEZ),    
	    		toInt(BHX), toInt(BHY), toInt(BHZ),    toInt(BLX), toInt(BLY), toInt(BLZ),    toInt(BKX), toInt(BKY), toInt(BKZ),    
	    		toInt(BHX), toInt(BHY), toInt(BHZ),    toInt(BKX), toInt(BKY), toInt(BKZ),    toInt(BGX), toInt(BGY), toInt(BGZ),    
	    		toInt(BFX), toInt(BFY), toInt(BFZ),    toInt(BJX), toInt(BJY), toInt(BJZ),    toInt(BLX), toInt(BLY), toInt(BLZ),    
	    		toInt(BFX), toInt(BFY), toInt(BFZ),    toInt(BLX), toInt(BLY), toInt(BLZ),    toInt(BHX), toInt(BHY), toInt(BHZ),    
	    		toInt(BLX), toInt(BLY), toInt(BLZ),    toInt(BJX), toInt(BJY), toInt(BJZ),    toInt(BIX), toInt(BIY), toInt(BIZ),    
	    		toInt(BLX), toInt(BLY), toInt(BLZ),    toInt(BIX), toInt(BIY), toInt(BIZ),    toInt(BKX), toInt(BKY), toInt(BKZ),    


				//LEGS
				
	    		toInt(CAX), toInt(CAY), toInt(CAZ),    toInt(CEX), toInt(CEY), toInt(CEZ),    toInt(CFX), toInt(CFY), toInt(CFZ),    
	    		toInt(CAX), toInt(CAY), toInt(CAZ),    toInt(CFX), toInt(CFY), toInt(CFZ),    toInt(CBX), toInt(CBY), toInt(CBZ),    
	    		toInt(CCX), toInt(CCY), toInt(CCZ),    toInt(CGX), toInt(CGY), toInt(CGZ),    toInt(CEX), toInt(CEY), toInt(CEZ),    
	    		toInt(CCX), toInt(CCY), toInt(CCZ),    toInt(CEX), toInt(CEY), toInt(CEZ),    toInt(CAX), toInt(CAY), toInt(CAZ),    
	    		toInt(CDX), toInt(CDY), toInt(CDZ),    toInt(CHX), toInt(CHY), toInt(CHZ),    toInt(CGX), toInt(CGY), toInt(CGZ),    
	    		toInt(CDX), toInt(CDY), toInt(CDZ),    toInt(CGX), toInt(CGY), toInt(CGZ),    toInt(CCX), toInt(CCY), toInt(CCZ),    
	    		toInt(CBX), toInt(CBY), toInt(CBZ),    toInt(CFX), toInt(CFY), toInt(CFZ),    toInt(CHX), toInt(CHY), toInt(CHZ),    
	    		toInt(CBX), toInt(CBY), toInt(CBZ),    toInt(CHX), toInt(CHY), toInt(CHZ),    toInt(CDX), toInt(CDY), toInt(CDZ),    
	    		toInt(CAX), toInt(CAY), toInt(CAZ),    toInt(CBX), toInt(CBY), toInt(CBZ),    toInt(CDX), toInt(CDY), toInt(CDZ),    
	    		toInt(CAX), toInt(CAY), toInt(CAZ),    toInt(CDX), toInt(CDY), toInt(CDZ),    toInt(CCX), toInt(CCY), toInt(CCZ),    
	    		
	    		toInt(CEX), toInt(CEY), toInt(CEZ),    toInt(CIX), toInt(CIY), toInt(CIZ),    toInt(CJX), toInt(CJY), toInt(CJZ),    
	    		toInt(CEX), toInt(CEY), toInt(CEZ),    toInt(CJX), toInt(CJY), toInt(CJZ),    toInt(CFX), toInt(CFY), toInt(CFZ),    
	    		toInt(CGX), toInt(CGY), toInt(CGZ),    toInt(CKX), toInt(CKY), toInt(CKZ),    toInt(CIX), toInt(CIY), toInt(CIZ),    
	    		toInt(CGX), toInt(CGY), toInt(CGZ),    toInt(CIX), toInt(CIY), toInt(CIZ),    toInt(CEX), toInt(CEY), toInt(CEZ),    
	    		toInt(CHX), toInt(CHY), toInt(CHZ),    toInt(CLX), toInt(CLY), toInt(CLZ),    toInt(CKX), toInt(CKY), toInt(CKZ),    
	    		toInt(CHX), toInt(CHY), toInt(CHZ),    toInt(CKX), toInt(CKY), toInt(CKZ),    toInt(CGX), toInt(CGY), toInt(CGZ),    
	    		toInt(CFX), toInt(CFY), toInt(CFZ),    toInt(CJX), toInt(CJY), toInt(CJZ),    toInt(CLX), toInt(CLY), toInt(CLZ),    
	    		toInt(CFX), toInt(CFY), toInt(CFZ),    toInt(CLX), toInt(CLY), toInt(CLZ),    toInt(CHX), toInt(CHY), toInt(CHZ),    
	    		toInt(CLX), toInt(CLY), toInt(CLZ),    toInt(CJX), toInt(CJY), toInt(CJZ),    toInt(CIX), toInt(CIY), toInt(CIZ),    
	    		toInt(CLX), toInt(CLY), toInt(CLZ),    toInt(CIX), toInt(CIY), toInt(CIZ),    toInt(CKX), toInt(CKY), toInt(CKZ),    

	    		toInt(DAX), toInt(DAY), toInt(DAZ),    toInt(DEX), toInt(DEY), toInt(DEZ),    toInt(DFX), toInt(DFY), toInt(DFZ),    
	    		toInt(DAX), toInt(DAY), toInt(DAZ),    toInt(DFX), toInt(DFY), toInt(DFZ),    toInt(DBX), toInt(DBY), toInt(DBZ),    
	    		toInt(DCX), toInt(DCY), toInt(DCZ),    toInt(DGX), toInt(DGY), toInt(DGZ),    toInt(DEX), toInt(DEY), toInt(DEZ),    
	    		toInt(DCX), toInt(DCY), toInt(DCZ),    toInt(DEX), toInt(DEY), toInt(DEZ),    toInt(DAX), toInt(DAY), toInt(DAZ),    
	    		toInt(DDX), toInt(DDY), toInt(DDZ),    toInt(DHX), toInt(DHY), toInt(DHZ),    toInt(DGX), toInt(DGY), toInt(DGZ),    
	    		toInt(DDX), toInt(DDY), toInt(DDZ),    toInt(DGX), toInt(DGY), toInt(DGZ),    toInt(DCX), toInt(DCY), toInt(DCZ),    
	    		toInt(DBX), toInt(DBY), toInt(DBZ),    toInt(DFX), toInt(DFY), toInt(DFZ),    toInt(DHX), toInt(DHY), toInt(DHZ),    
	    		toInt(DBX), toInt(DBY), toInt(DBZ),    toInt(DHX), toInt(DHY), toInt(DHZ),    toInt(DDX), toInt(DDY), toInt(DDZ),    
	    		toInt(DAX), toInt(DAY), toInt(DAZ),    toInt(DBX), toInt(DBY), toInt(DBZ),    toInt(DDX), toInt(DDY), toInt(DDZ),    
	    		toInt(DAX), toInt(DAY), toInt(DAZ),    toInt(DDX), toInt(DDY), toInt(DDZ),    toInt(DCX), toInt(DCY), toInt(DCZ),    
	    		
	    		toInt(DEX), toInt(DEY), toInt(DEZ),    toInt(DIX), toInt(DIY), toInt(DIZ),    toInt(DJX), toInt(DJY), toInt(DJZ),    
	    		toInt(DEX), toInt(DEY), toInt(DEZ),    toInt(DJX), toInt(DJY), toInt(DJZ),    toInt(DFX), toInt(DFY), toInt(DFZ),    
	    		toInt(DGX), toInt(DGY), toInt(DGZ),    toInt(DKX), toInt(DKY), toInt(DKZ),    toInt(DIX), toInt(DIY), toInt(DIZ),    
	    		toInt(DGX), toInt(DGY), toInt(DGZ),    toInt(DIX), toInt(DIY), toInt(DIZ),    toInt(DEX), toInt(DEY), toInt(DEZ),    
	    		toInt(DHX), toInt(DHY), toInt(DHZ),    toInt(DLX), toInt(DLY), toInt(DLZ),    toInt(DKX), toInt(DKY), toInt(DKZ),    
	    		toInt(DHX), toInt(DHY), toInt(DHZ),    toInt(DKX), toInt(DKY), toInt(DKZ),    toInt(DGX), toInt(DGY), toInt(DGZ),    
	    		toInt(DFX), toInt(DFY), toInt(DFZ),    toInt(DJX), toInt(DJY), toInt(DJZ),    toInt(DLX), toInt(DLY), toInt(DLZ),    
	    		toInt(DFX), toInt(DFY), toInt(DFZ),    toInt(DLX), toInt(DLY), toInt(DLZ),    toInt(DHX), toInt(DHY), toInt(DHZ),    
	    		toInt(DLX), toInt(DLY), toInt(DLZ),    toInt(DJX), toInt(DJY), toInt(DJZ),    toInt(DIX), toInt(DIY), toInt(DIZ),    
	    		toInt(DLX), toInt(DLY), toInt(DLZ),    toInt(DIX), toInt(DIY), toInt(DIZ),    toInt(DKX), toInt(DKY), toInt(DKZ),    
	    		

				//HEAD 
	    		toInt(EAX), toInt(EAY), toInt(EAZ),    toInt(EBX), toInt(EBY), toInt(EBZ),    toInt(EFX), toInt(EFY), toInt(EFZ),    
	    		toInt(EAX), toInt(EAY), toInt(EAZ),    toInt(EFX), toInt(EFY), toInt(EFZ),    toInt(EDX), toInt(EDY), toInt(EDZ),    
	    		toInt(EDX), toInt(EDY), toInt(EDZ),    toInt(EEX), toInt(EEY), toInt(EEZ),    toInt(EIX), toInt(EIY), toInt(EIZ),    
	    		toInt(EDX), toInt(EDY), toInt(EDZ),    toInt(EIX), toInt(EIY), toInt(EIZ),    toInt(EHX), toInt(EHY), toInt(EHZ),    
	    		toInt(EHX), toInt(EHY), toInt(EHZ),    toInt(EJX), toInt(EJY), toInt(EJZ),    toInt(EMX), toInt(EMY), toInt(EMZ),    
	    		toInt(EHX), toInt(EHY), toInt(EHZ),    toInt(EMX), toInt(EMY), toInt(EMZ),    toInt(ELX), toInt(ELY), toInt(ELZ),    
	    		toInt(ELX), toInt(ELY), toInt(ELZ),    toInt(ENX), toInt(ENY), toInt(EOZ),    toInt(EQX), toInt(EQY), toInt(EQZ),    
	    		toInt(ELX), toInt(ELY), toInt(ELZ),    toInt(EQX), toInt(EQY), toInt(EQZ),    toInt(EPX), toInt(EPY), toInt(EPZ),    

	    		toInt(EBX), toInt(EBY), toInt(EBZ),    toInt(ECX), toInt(ECY), toInt(ECZ),    toInt(EGX), toInt(EGY), toInt(EGZ),    
	    		toInt(EBX), toInt(EBY), toInt(EBZ),    toInt(EGX), toInt(EGY), toInt(EGZ),    toInt(EFX), toInt(EFY), toInt(EFZ),    
	    		toInt(EEX), toInt(EEY), toInt(EEZ),    toInt(EGX), toInt(EGY), toInt(EGZ),    toInt(EKX), toInt(EKY), toInt(EKZ),    
	    		toInt(EEX), toInt(EEY), toInt(EEZ),    toInt(EKX), toInt(EKY), toInt(EKZ),    toInt(EIX), toInt(EIY), toInt(EIZ),    
	    		toInt(EJX), toInt(EJY), toInt(EJZ),    toInt(EKX), toInt(EKY), toInt(EKZ),    toInt(EOX), toInt(EOY), toInt(EOZ),    
	    		toInt(EJX), toInt(EJY), toInt(EJZ),    toInt(EOX), toInt(EOY), toInt(EOZ),    toInt(EMX), toInt(EMY), toInt(EMZ),    
	    		toInt(ENX), toInt(ENY), toInt(ENZ),    toInt(EOX), toInt(EOY), toInt(EOZ),    toInt(ERX), toInt(ERY), toInt(ERZ),    
	    		toInt(ENX), toInt(ENY), toInt(ENZ),    toInt(ERX), toInt(ERY), toInt(ERZ),    toInt(EQX), toInt(EQY), toInt(EQZ),    

	    		toInt(FFX), toInt(FFY), toInt(FFZ),    toInt(FBX), toInt(FBY), toInt(FBZ),    toInt(FAX), toInt(FAY), toInt(FAZ),
	    		toInt(FFX), toInt(FFY), toInt(FFZ),    toInt(FAX), toInt(FAY), toInt(FAZ),    toInt(FDX), toInt(FDY), toInt(FDZ),
	    		toInt(FIX), toInt(FIY), toInt(FIZ),    toInt(FEX), toInt(FEY), toInt(FEZ),    toInt(FDX), toInt(FDY), toInt(FDZ),
	    		toInt(FIX), toInt(FIY), toInt(FIZ),    toInt(FDX), toInt(FDY), toInt(FDZ),    toInt(FHX), toInt(FHY), toInt(FHZ),
	    		toInt(FMX), toInt(FMY), toInt(FMZ),    toInt(FJX), toInt(FJY), toInt(FJZ),    toInt(FHX), toInt(FHY), toInt(FHZ),
	    		toInt(FMX), toInt(FMY), toInt(FMZ),    toInt(FHX), toInt(FHY), toInt(FHZ),    toInt(FLX), toInt(FLY), toInt(FLZ),
	    		toInt(FQX), toInt(FQY), toInt(FQZ),    toInt(FNX), toInt(FNY), toInt(FOZ),    toInt(FLX), toInt(FLY), toInt(FLZ),
	    		toInt(FQX), toInt(FQY), toInt(FQZ),    toInt(FLX), toInt(FLY), toInt(FLZ),    toInt(FPX), toInt(FPY), toInt(FPZ),

	    		toInt(FGX), toInt(FGY), toInt(FGZ),    toInt(FCX), toInt(FCY), toInt(FCZ),    toInt(FBX), toInt(FBY), toInt(FBZ),
	    		toInt(FGX), toInt(FGY), toInt(FGZ),    toInt(FBX), toInt(FBY), toInt(FBZ),    toInt(FFX), toInt(FFY), toInt(FFZ),
	    		toInt(FKX), toInt(FKY), toInt(FKZ),    toInt(FGX), toInt(FGY), toInt(FGZ),    toInt(FEX), toInt(FEY), toInt(FEZ),
	    		toInt(FKX), toInt(FKY), toInt(FKZ),    toInt(FEX), toInt(FEY), toInt(FEZ),    toInt(FIX), toInt(FIY), toInt(FIZ),
	    		toInt(FOX), toInt(FOY), toInt(FOZ),    toInt(FKX), toInt(FKY), toInt(FKZ),    toInt(FJX), toInt(FJY), toInt(FJZ),
	    		toInt(FOX), toInt(FOY), toInt(FOZ),    toInt(FJX), toInt(FJY), toInt(FJZ),    toInt(FMX), toInt(FMY), toInt(FMZ),
	    		toInt(FRX), toInt(FRY), toInt(FRZ),    toInt(FOX), toInt(FOY), toInt(FOZ),    toInt(FNX), toInt(FNY), toInt(FNZ),
	    		toInt(FRX), toInt(FRY), toInt(FRZ),    toInt(FNX), toInt(FNY), toInt(FNZ),    toInt(FQX), toInt(FQY), toInt(FQZ),

				//BODY
				toInt(AX), toInt(AY), toInt(AZ),       toInt(BX), toInt(BY), toInt(BZ),       toInt(CX), toInt(CY), toInt(CZ),    
				toInt(AX), toInt(AY), toInt(AZ),       toInt(CX), toInt(CY), toInt(CZ),       toInt(DX), toInt(DY), toInt(DZ),    
				toInt(BX), toInt(BY), toInt(BZ),       toInt(FX), toInt(FY), toInt(FZ),       toInt(GX), toInt(GY), toInt(GZ),    
				toInt(BX), toInt(BY), toInt(BZ),       toInt(GX), toInt(GY), toInt(GZ),       toInt(CX), toInt(CY), toInt(CZ),    
				toInt(EX), toInt(EY), toInt(EZ),       toInt(HX), toInt(HY), toInt(HZ),       toInt(GX), toInt(GY), toInt(GZ),    
				toInt(EX), toInt(EY), toInt(EZ),       toInt(GX), toInt(GY), toInt(GZ),       toInt(FX), toInt(FY), toInt(FZ),    
				toInt(AX), toInt(AY), toInt(AZ),       toInt(DX), toInt(DY), toInt(DZ),       toInt(HX), toInt(HY), toInt(HZ),    
				toInt(AX), toInt(AY), toInt(AZ),       toInt(HX), toInt(HY), toInt(HZ),       toInt(EX), toInt(EY), toInt(EZ),    
				toInt(BX), toInt(BY), toInt(BZ),       toInt(AX), toInt(AY), toInt(AZ),       toInt(EX), toInt(EY), toInt(EZ),    
				toInt(BX), toInt(BY), toInt(BZ),       toInt(EX), toInt(EY), toInt(EZ),       toInt(FX), toInt(FY), toInt(FZ),    
				toInt(HX), toInt(HY), toInt(HZ),       toInt(DX), toInt(DY), toInt(DZ),       toInt(CX), toInt(CY), toInt(CZ),    
				toInt(HX), toInt(HY), toInt(HZ),       toInt(CX), toInt(CY), toInt(CZ),       toInt(GX), toInt(GY), toInt(GZ),    
				
				//OTHER HEAD
	    		toInt(IAX), toInt(IAY), toInt(IAZ),    toInt(IBX), toInt(IBY), toInt(IBZ),    toInt(IDX), toInt(IDY), toInt(IDZ),    
	    		toInt(IAX), toInt(IAY), toInt(IAZ),    toInt(IDX), toInt(IDY), toInt(IDZ),    toInt(ICX), toInt(ICY), toInt(ICZ),    
	    		toInt(IBX), toInt(IBY), toInt(IBZ),    toInt(IEX), toInt(IEY), toInt(IEZ),    toInt(IFX), toInt(IFY), toInt(IFZ),    
	    		toInt(IBX), toInt(IBY), toInt(IBZ),    toInt(IFX), toInt(IFY), toInt(IFZ),    toInt(IDX), toInt(IDY), toInt(IDZ),    
	    		toInt(IEX), toInt(IEY), toInt(IEZ),    toInt(IGX), toInt(IGY), toInt(IGZ),    toInt(IHX), toInt(IHY), toInt(IHZ),    
	    		toInt(IEX), toInt(IEY), toInt(IEZ),    toInt(IHX), toInt(IHY), toInt(IHZ),    toInt(IFX), toInt(IFY), toInt(IFZ),    
	    		toInt(IGX), toInt(IGY), toInt(IGZ),    toInt(IIX), toInt(IIY), toInt(IIZ),    toInt(IJX), toInt(IJY), toInt(IJZ),    
	    		toInt(IGX), toInt(IGY), toInt(IGZ),    toInt(IJX), toInt(IJY), toInt(IJZ),    toInt(IHX), toInt(IHY), toInt(IHZ),    

	    		//FACE RED
	    		toInt(HAX), toInt(HAY), toInt(HAZ),    toInt(HHX), toInt(HHY), toInt(HHZ),    toInt(HJX), toInt(HJY), toInt(HJZ),    
	    		toInt(HAX), toInt(HAY), toInt(HAZ),    toInt(HJX), toInt(HJY), toInt(HJZ),    toInt(HBX), toInt(HBY), toInt(HBZ),    
	    		toInt(HHX), toInt(HHY), toInt(HHZ),    toInt(GHX), toInt(GHY), toInt(GHZ),    toInt(GIX), toInt(GIY), toInt(GIZ),    
	    		toInt(HHX), toInt(HHY), toInt(HHZ),    toInt(GIX), toInt(GIY), toInt(GIZ),    toInt(HIX), toInt(HIY), toInt(HIZ),    
	    		toInt(GHX), toInt(GHY), toInt(GHZ),    toInt(GAX), toInt(GAY), toInt(GAZ),    toInt(GBX), toInt(GBY), toInt(GBZ),    
	    		toInt(GHX), toInt(GHY), toInt(GHZ),    toInt(GBX), toInt(GBY), toInt(GBZ),    toInt(GJX), toInt(GJY), toInt(GJZ),    
	    		toInt(HKX), toInt(HKY), toInt(HKZ),    toInt(HPX), toInt(HPY), toInt(HPZ),    toInt(HQX), toInt(HQY), toInt(HQZ),    
	    		toInt(HKX), toInt(HKY), toInt(HKZ),    toInt(HQX), toInt(HQY), toInt(HQZ),    toInt(HLX), toInt(HLY), toInt(HLZ),    
	    		toInt(GPX), toInt(GPY), toInt(GPZ),    toInt(GKX), toInt(GKY), toInt(GKZ),    toInt(GLX), toInt(GLY), toInt(GLZ),    
	    		toInt(GPX), toInt(GPY), toInt(GPZ),    toInt(GLX), toInt(GLY), toInt(GLZ),    toInt(GQX), toInt(GQY), toInt(GQZ),    
	    		toInt(HRX), toInt(HRY), toInt(HRZ),    toInt(GRX), toInt(GRY), toInt(GRZ),    toInt(GSX), toInt(GSY), toInt(GSZ),    
	    		toInt(HRX), toInt(HRY), toInt(HRZ),    toInt(GSX), toInt(GSY), toInt(GSZ),    toInt(HSX), toInt(HSY), toInt(HSZ),    
	    		toInt(HMX), toInt(HMY), toInt(HMZ),    toInt(GMX), toInt(GMY), toInt(GMZ),    toInt(GNX), toInt(GNY), toInt(GNZ),    
	    		toInt(HMX), toInt(HMY), toInt(HMZ),    toInt(GNX), toInt(GNY), toInt(GNZ),    toInt(HNX), toInt(HNY), toInt(HNZ),    

	    		//FACE YELLOW
	    		toInt(HLX), toInt(HLY), toInt(HLZ),    toInt(HQX), toInt(HQY), toInt(HQZ),    toInt(HSX), toInt(HSY), toInt(HSZ),    
	    		toInt(HLX), toInt(HLY), toInt(HLZ),    toInt(HSX), toInt(HSY), toInt(HSZ),    toInt(HMX), toInt(HMY), toInt(HMZ),    
	    		toInt(HQX), toInt(HQY), toInt(HQZ),    toInt(GQX), toInt(GQY), toInt(GQZ),    toInt(GRX), toInt(GRY), toInt(GRZ),    
	    		toInt(HQX), toInt(HQY), toInt(HQZ),    toInt(GRX), toInt(GRY), toInt(GRZ),    toInt(HRX), toInt(HRY), toInt(HRZ),    
	    		toInt(GQX), toInt(GQY), toInt(GQZ),    toInt(GLX), toInt(GLY), toInt(GLZ),    toInt(GMX), toInt(GMY), toInt(GMZ),    
	    		toInt(GQX), toInt(GQY), toInt(GQZ),    toInt(GMX), toInt(GMY), toInt(GMZ),    toInt(GSX), toInt(GSY), toInt(GSZ),    
	    		toInt(HNX), toInt(HNY), toInt(HNZ),    toInt(GNX), toInt(GNY), toInt(GNZ),    toInt(GOX), toInt(GOY), toInt(GOZ),    
	    		toInt(HNX), toInt(HNY), toInt(HNZ),    toInt(GOX), toInt(GOY), toInt(GOZ),    toInt(HOX), toInt(HOY), toInt(HOZ),    
	    		toInt(GLX), toInt(GLY), toInt(GLZ),    toInt(GDX), toInt(GDY), toInt(GDZ),    toInt(GEX), toInt(GEY), toInt(GEZ),    
	    		toInt(GLX), toInt(GLY), toInt(GLZ),    toInt(GEX), toInt(GEY), toInt(GEZ),    toInt(GOX), toInt(GOY), toInt(GOZ),    
	    		toInt(HBX), toInt(HBY), toInt(HBZ),    toInt(HJX), toInt(HJY), toInt(HJZ),    toInt(HKX), toInt(HKY), toInt(HKZ),    
	    		toInt(HBX), toInt(HBY), toInt(HBZ),    toInt(HKX), toInt(HKY), toInt(HKZ),    toInt(HCX), toInt(HCY), toInt(HCZ),    
	    		toInt(HIX), toInt(HIY), toInt(HIZ),    toInt(GIX), toInt(GIY), toInt(GIZ),    toInt(GKX), toInt(GKY), toInt(GKZ),    
	    		toInt(HIX), toInt(HIY), toInt(HIZ),    toInt(GKX), toInt(GKY), toInt(GKZ),    toInt(HKX), toInt(HKY), toInt(HKZ),    
	    		toInt(GJX), toInt(GJY), toInt(GJZ),    toInt(GBX), toInt(GBY), toInt(GBZ),    toInt(GCX), toInt(GCY), toInt(GCZ),    
	    		toInt(GJX), toInt(GJY), toInt(GJZ),    toInt(GCX), toInt(GCY), toInt(GCZ),    toInt(GKX), toInt(GKY), toInt(GKZ),    
	    		toInt(HCX), toInt(HCY), toInt(HCZ),    toInt(HFX), toInt(HFY), toInt(HFZ),    toInt(HGX), toInt(HGY), toInt(HGZ),    
	    		toInt(HCX), toInt(HCY), toInt(HCZ),    toInt(HGX), toInt(HGY), toInt(HGZ),    toInt(HDX), toInt(HDY), toInt(HDZ),    
	    		toInt(HPX), toInt(HPY), toInt(HPZ),    toInt(GPX), toInt(GPY), toInt(GPZ),    toInt(GQX), toInt(GQY), toInt(GQZ),    
	    		toInt(HPX), toInt(HPY), toInt(HPZ),    toInt(GQX), toInt(GQY), toInt(GQZ),    toInt(HQX), toInt(HQY), toInt(HQZ),    
	    		toInt(GFX), toInt(GFY), toInt(GFZ),    toInt(GCX), toInt(GCY), toInt(GCZ),    toInt(GDX), toInt(GDY), toInt(GDZ),    
	    		toInt(GFX), toInt(GFY), toInt(GFZ),    toInt(GDX), toInt(GDY), toInt(GDZ),    toInt(GGX), toInt(GGY), toInt(GGZ),    
	    		toInt(HDX), toInt(HDY), toInt(HDZ),    toInt(HLX), toInt(HLY), toInt(HLZ),    toInt(HOX), toInt(HOY), toInt(HOZ),    
	    		toInt(HDX), toInt(HDY), toInt(HDZ),    toInt(HOX), toInt(HOY), toInt(HOZ),    toInt(HEX), toInt(HEY), toInt(HEZ),    

	    		//FACE BLUE
	    		toInt(HFX), toInt(HFY), toInt(HFZ),    toInt(HKX), toInt(HKY), toInt(HKZ),    toInt(HLX), toInt(HLY), toInt(HLZ),    
	    		toInt(HFX), toInt(HFY), toInt(HFZ),    toInt(HLX), toInt(HLY), toInt(HLZ),    toInt(HGX), toInt(HGY), toInt(HGZ),    
	    		toInt(GKX), toInt(GKY), toInt(GKZ),    toInt(GFX), toInt(GFY), toInt(GFZ),    toInt(GGX), toInt(GGY), toInt(GGZ),    
	    		toInt(GKX), toInt(GKY), toInt(GKZ),    toInt(GGX), toInt(GGY), toInt(GGZ),    toInt(GLX), toInt(GLY), toInt(GLZ),    

	    };
    
    	int[] normals = {
    			//ARMS
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),

    			//LEGS

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			
    			//HEAD
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),

    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),

    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),

    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),

    			//BODY
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),	toInt(+1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),	toInt(-1), toInt(+0), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),

    			//OTHER HEAD
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),	toInt(+0), toInt(+1), toInt(+0),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),	toInt(+0), toInt(+0), toInt(-1),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),	toInt(+0), toInt(-1), toInt(+0),
    			
    			//FACE RED
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
       			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
       			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
       			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
       			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
       			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
       			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),

    			//FACE YELLOW
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),

    			//FACE BLUE
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),
    			toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),	toInt(+0), toInt(+0), toInt(+1),

    	};
    	int colors[] = {
    			//ARMS
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,

    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,	toInt(+0f), toInt(+1f), toInt(+0),0,
    			
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,
    			toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,	toInt(+1f), toInt(+1f), toInt(+0f),0,

    			//LEGS
    
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    					
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,

    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    					
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,

    			//HEAD 
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,

    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,
    			toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,	toInt(+1), toInt(+0f), toInt(+0f),0,

    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,
    			toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,	toInt(+1), toInt(+1f), toInt(+0f),0,

    			//BODY	
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,
    			toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,		toInt(+0), toInt(+1f), toInt(+0),0,

    			//OTHER HEAD
    			
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,	toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,	toInt(+1), toInt(+1), toInt(+0),0,
    			
    			//FACE RED
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,
    			toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,				toInt(+1), toInt(+0), toInt(+0),0,

    			//FACE YELLOW
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,				toInt(+1), toInt(+1), toInt(+0),0,
    			
    			//FACE BLUE
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,
    			toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,	toInt(+0), toInt(+0), toInt(+1),0,

    	};
		ver=getIntBuffer(vertices);
		nor=getIntBuffer(normals);
		col=getIntBuffer(colors);
    }


}
