package com.example.criminal.criminalgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by criminal on 20/06/15.
 */
public class GameView extends SurfaceView {


    private final Bitmap bmp;
    private final SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Sprite sprite;

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                //Get canvas object
//                Canvas c = holder.lockCanvas(null);
//                onDraw(c);
//                holder.unlockCanvasAndPost(c);
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

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bad1);
            sprite = new Sprite(this,bmp);
    }

    @Override
    protected void onDraw(Canvas canvas){


        canvas.drawColor(Color.BLACK);
        sprite.onDraw(canvas);

    }


}
