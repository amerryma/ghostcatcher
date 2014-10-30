package com.wurbo.ghostcatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.PictureDrawable;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private MainThread mThread;
	private long height, width;
	public static final String TAG = GameSurfaceView.class.getSimpleName();
	PictureDrawable ghostBase;
	PictureDrawable background;
	private float cursorX = 0;
	private float cursorY = 0;
	Ghost you;
	Paint redPaint;
	Paint goldPaint;
	Paint greenPaint;
	Paint bgPaint;
	Paint smallBgPaint;
	Paint scorePaint;
	List<Ghost> ghosts = new ArrayList<Ghost>();
	int numGhosts = 50;
	private long timeDiff;
	private long lastTime;
	private float minSize = 50;
	private float maxSize = 2000;
	private boolean shieldOn = false;
	private int shieldTime = 0;
	private double shieldStopTime = 0;
	private int score = 0;
	Typeface tf;
	private boolean paused = false;
	
	//Highscore related things
	boolean isGuest = true;
	String username = "";
	private int highscore = 0;
	SharedPreferences sharedPreferences;
	private long lastUpdate = 0;
	//GamesClient mGamesClient;
	String LEADERBOARD_ID;

	public GameSurfaceView(Context context, String username) {
		super(context);
	    getHolder().addCallback(this);
		
		//Username/highscore stuff:
	    sharedPreferences = context.getSharedPreferences("userrecords", Context.MODE_PRIVATE);
		LEADERBOARD_ID = context.getString(R.string.leaderboard_largest_size);
	    
		if (username.trim().equals("")) {
			isGuest = true;
			highscore = sharedPreferences.getInt("highscore", 0);
		} else {
			isGuest = false;
			this.username = username;
			try {
				highscore = HttpHelper.getHighscore(username);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	    //Load font
        tf = Typeface.createFromAsset(context.getAssets(),"gooddog.otf");
        
        //Load SVGS
	    SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.ghostbasic);
	    ghostBase = svg.createPictureDrawable();
	    
	    //Get window height/width
	    DisplayMetrics displaymetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    Display display = windowManager.getDefaultDisplay();
	    width = display.getWidth();
	    height = display.getHeight();
	    
	    //Define some paints
	    redPaint = new Paint();
	    redPaint.setARGB(100, 255, 0, 0);
	    redPaint.setTextSize((int)(height * .2));
	    redPaint.setTypeface(tf);
	    
	    goldPaint = new Paint();
	    goldPaint.setARGB(100, 255, 204, 0);
	    goldPaint.setTextSize((int)(height * .2));
	    goldPaint.setTypeface(tf);
	    
	    bgPaint = new Paint();
	    bgPaint.setARGB(100, 0, 0, 0);
	    bgPaint.setTextSize((int)(height * .2));
	    bgPaint.setTypeface(tf);
	    
	    greenPaint = new Paint();
	    greenPaint.setARGB(100, 0, 255, 0);
	    greenPaint.setTextSize((int)(height * .05));
	    greenPaint.setTypeface(tf);
	    
	    smallBgPaint = new Paint();
	    smallBgPaint.setARGB(100, 0, 0, 0);
	    smallBgPaint.setTextSize((int)(height * .05));
	    smallBgPaint.setTypeface(tf);
	    
	    you = new Ghost(100, (int)width, (int)height); //Create the player
	    you.setY((float)(height*.8));
	    you.setSize(100);
	    
	    for (int i = 0; i < numGhosts; i++) {
		    Ghost ghost = new Ghost(50, (int)width, (int)height);
		    ghost.random((int)width, (int)height);
		    ghost.setFlipped(true);
		    ghosts.add(ghost);
	    }
	    
	    mThread = new MainThread(getHolder(), this);

	    setFocusable(true);
	}

	public void update() {
		// TODO Auto-generated method stub
	}

	@SuppressLint("WrongCall")
	public void render(Canvas canvas) {
	    onDraw(canvas);
	}

	public void surfaceCreated(SurfaceHolder holder) {
	    mThread.setRunning(true);
	    mThread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	    Log.d(TAG, "Surface is being destroyed");
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
	    boolean retry = true;
	    mThread.setRunning(false);
	    while (retry) {
	        try {
	        	mThread.join();
	            retry = false;
	        } 
	        catch (InterruptedException e) {
	        }
	    }
	    Log.d(TAG, "Thread was shut down cleanly");
	}
	
	int i = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (canvas == null) return;
		canvas.drawColor(Color.WHITE);
		
		//Determine what paint to use
		scorePaint = redPaint;
		
		if (!paused) {
			timeDiff = SystemClock.elapsedRealtime()-lastTime;
			lastTime = SystemClock.elapsedRealtime();
			
			if (shieldStopTime < Math.ceil(SystemClock.elapsedRealtime()/1000)) {
				if (!shieldOn) {
					you.setShield(false);
				}
				shieldOn = false;
			} else {
				if (shieldOn) {
					you.setShield(true);
					shieldTime = (int) (shieldStopTime - Math.ceil(SystemClock.elapsedRealtime()/1000));
				}
				shieldOn = true;
			}
			
			for (Ghost ghost : ghosts) {
				ghost.moveY(ghost.getSpeed()*timeDiff);
				drawGhost(canvas, ghost);
				if (ghost.getY() > canvas.getHeight()) {
					ghost.random(canvas.getHeight(), canvas.getWidth());
				}
				float dx = you.getX() - ghost.getX();
				float dy = (you.getY()+(you.getSize()/2)) - (ghost.getY()+(ghost.getSize()/2));
				float radii = (float)((you.getSize() + ghost.getSize())/2);
				
				if ((dx * dx) + (dy * dy) < radii*radii) {
					switch (ghost.getType()) {
					case WHITE: //normal
						changeSize("+1");
						break;
					case RED: //red
						if (!shieldOn ) {
							changeSize("-10");
						}
						break;
					case GOLD: //gold
						changeSize("+30");
						break;
					case BLACK: //black
						changeSize("/2");
						break;
					case BLUE: //orange
						changeSize("*1.1");
						break;
					case GREEN: //green
						shieldTime  += 5;
						shieldStopTime  = (int)(Math.floor(SystemClock.elapsedRealtime()/1000) + shieldTime);
						break;
					default:
						break;
					}
					ghost.random(canvas.getHeight(), canvas.getWidth());
				}
			}
			you.moveX((cursorX-you.getX())/2);
			drawGhost(canvas, you);
			
			score = (int) you.getUnscaledSize();
			
			//Show gold paint if score is current or better than highscore
			if (score >= highscore) {
				scorePaint = goldPaint;
			}
			
			//Only update the shared preferences if the highscore is actually larger
			if (score > highscore) {
				highscore = score;
				
				//Because we can't update the highscore as quick as they can 
			    //achieve them. We will only update as fast as 1 time per 5 seconds
				//But, we'll always update the shared preference and when the 
				//application is closed.
				if (isGuest) {
					Editor editor = sharedPreferences.edit();
					editor.putInt("highscore", highscore);
					editor.commit();
				} else {
					if (lastTime - lastUpdate > 5000) {
		    			try {
		    				updateHighscore();
		    	    		lastUpdate = SystemClock.elapsedRealtime();
		    			} catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
					}
				}
			}
		} else {
			for (Ghost ghost : ghosts) {
				drawGhost(canvas, ghost);
			}
			
			drawGhost(canvas, you);
			
			canvas.drawText("Paused", (float) (canvas.getWidth() * .1) + 10, (float) (canvas.getHeight() * .5)+10, bgPaint);
			canvas.drawText("Paused", (float) (canvas.getWidth() * .1), (float) (canvas.getHeight() * .5), redPaint);
		}
			
		String scoreText = "" + score;
		String highscoreText = "High Score: " + highscore;
		String shieldText = "Shield Time: " + shieldTime;

		canvas.drawText(scoreText, 10, (float) (canvas.getHeight() * .1)+10, bgPaint);
		canvas.drawText(scoreText, 0, (float) (canvas.getHeight() * .1), scorePaint);
		canvas.drawText(highscoreText, 2, (float) (canvas.getHeight())+2, smallBgPaint);
		canvas.drawText(highscoreText, 0, (float) (canvas.getHeight()), greenPaint);
		canvas.drawText(shieldText, (float) (canvas.getWidth() * .5)+2, (float) (canvas.getHeight())+2, smallBgPaint);
		canvas.drawText(shieldText, (float) (canvas.getWidth() * .5), (float) (canvas.getHeight()), greenPaint);
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
		
		//reset timer
		lastTime = SystemClock.elapsedRealtime();
		
		if (shieldOn) {
			shieldStopTime = (lastTime/1000) + shieldTime;
		}
	}

	private void changeSize(String string) {
		float tempSize;
		
		float newNum = Float.parseFloat(string.substring(1));
		float unscaledSize = you.getUnscaledSize();
		switch ((char)string.substring(0,1).charAt(0)) {
			case '+':
				tempSize = unscaledSize+newNum;
				break;
			case '-':
				tempSize = unscaledSize-newNum;
				break;
			case '*':
				tempSize = unscaledSize*newNum;
				break;
			case '/':
				tempSize = (float)(unscaledSize/newNum);
				break;
			default: //no operator, perform set
				tempSize = newNum;
				break;
		}
		if (tempSize < minSize ) {
			tempSize = minSize;
		} else if (tempSize > maxSize) {
			tempSize = maxSize;
		}
		you.setSize(tempSize);
	}

	private void drawGhost(Canvas canvas, Ghost ghost) {
		canvas.drawPicture(ghostBase.getPicture(), ghost);
		canvas.drawCircle(ghost.getX(), ghost.getY()+ghost.getOffset(), ghost.getOffset(), ghost.getPaint());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isPaused()) {
			setPaused(false);
		}
		// TODO Auto-generated method stub

		int action = event.getAction();
		cursorX = event.getX();
		cursorY = event.getY();

		if (action == MotionEvent.ACTION_MOVE) {

		}

		if (action == MotionEvent.ACTION_UP) {
			//release = true;
		}
		return true;
	}

	public void updateHighscore() throws IOException {
		if (!isGuest) {
			HttpHelper.setHighscore(username, highscore);
			//mGamesClient.submitScore(LEADERBOARD_ID, highscore);
		}
	}
}
