package com.example.criminal.criminalgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by criminal on 20/06/15.
 */
public class GameView extends SurfaceView {

    private final SurfaceHolder holder;
    private final Bitmap bmpBlood;
    private GameLoopThread gameLoopThread;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private long lastClick;
    private List<TempSprite> temps = new ArrayList<TempSprite>();

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                boolean retry = true;
                gameLoopThread.setRunning(false);
                while(retry){
                    try{
                        gameLoopThread.join();
                        retry = false;

                    }catch(InterruptedException e){

                    }
                }

            }
        });
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);

    }

    private void createSprites(){
        sprites.add(createSprite(R.drawable.bad1));
        sprites.add(createSprite(R.drawable.bad2));
        sprites.add(createSprite(R.drawable.bad3));
        sprites.add(createSprite(R.drawable.bad4));
        sprites.add(createSprite(R.drawable.bad5));
        sprites.add(createSprite(R.drawable.bad6));
        sprites.add(createSprite(R.drawable.good1));
        sprites.add(createSprite(R.drawable.good2));
        sprites.add(createSprite(R.drawable.good3));
        sprites.add(createSprite(R.drawable.good4));
        sprites.add(createSprite(R.drawable.good5));
        sprites.add(createSprite(R.drawable.good6));
    }


    private Sprite createSprite(int resouce) {

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this,bmp);

    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.BLACK);
        //Blood image
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);

        }

        for (Sprite sprite : sprites) {

            sprite.onDraw(canvas);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() -lastClick >300){
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                //sound efect when touch
                MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.scream);
                float x=event.getX();
                float y = event.getY();
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(x, y)) {
                        //Play sound
                        mp.start();
                        sprites.remove(sprite);
                        //Blood after remove avatar

                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        break;
                    }
                }
            }
        }

        return true;

    }


}
