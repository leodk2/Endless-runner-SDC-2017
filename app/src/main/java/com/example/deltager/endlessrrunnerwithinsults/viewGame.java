package com.example.deltager.endlessrrunnerwithinsults;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by deltager on 06-07-17.
 */

public class viewGame extends View implements View.OnTouchListener
{
    ArrayList<obstacles> obstacle;
    Game game;
    int width, height;
    Paint obstColour, playerColour, textColour;
    Bitmap background;
    Timer timing;
    Bitmap træ1;
    boolean init;

    InsultGenerator insultGenerator = new InsultGenerator(getContext());

    public viewGame(Context context)
    {
        super(context);
        setup(context);
    }

    public viewGame(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setup(context);
    }

    public viewGame(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    public void setup(Context context){
        game = new Game();
        game.newObstacle();
        game.newObstacle();
        init = true;

        //TODO: Hent alt den grafik I skal bruge ind i feltvariabler
        træ1 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.traeer_stor_web);
        //TODO: Brug den her som constructor for viewGame
        postInvalidate();

        timing = new Timer();
        timing.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO: Tegn alt I jeres spil med canvas.drawBitMap(), canvas.drawRect() etc.
        //TODO: I kan få fat i jeres obstacles og spiller med game.getPlayer() og game.getObstacles()

        //Width and height
        height = canvas.getHeight();
        width = canvas.getWidth();
        if (init)   {
            init = false;

            //Player pos
            game.getPlayer().setyPos((float) (0.875*height));
            game.getPlayer().setxPos((float) (width/2));

            //Image scaling
            træ1 = Bitmap.createScaledBitmap(træ1, (int)(0.3*width)+1, (int)(0.3*width)+1, true);

        }
        //Background colour

        Paint backColor = new Paint();
        backColor.setColor(Color.GREEN);

        //Background
        canvas.drawRect(0, 0, width, height, backColor);

        //Colour of the obstacle
        obstColour = new Paint();
        obstColour.setColor(Color.BLUE);

        //Arraylist of the current obstacles
        ArrayList<obstacles> obstacles = game.getObstacles();

        //Draw obstacle for ones which are currently in use
        for(obstacles o : obstacles)    {
            float p = (float) (0.05*width + o.getPath()*0.3*width);
            o.setxPos(p);
            float temp = (float) (0.3*width);
            //canvas.drawBitmap(p, o.getyPos(), p + temp, o.getyPos() + temp, null);
            canvas.drawBitmap(træ1, o.getxPos(), o.getyPos(), null);
            //canvas.drawBitmap(træ1, 0, height - træ1.getHeight(), null);


        }

        //canvas.drawBitmap(træ1, 0, height - træ1.getHeight(), null);


        playerColour = new Paint();
        playerColour.setColor(Color.RED);

//        canvas.drawCircle(width/2, game.getPlayer().getyPos(), (float) (.1*width), playerColour);
        canvas.drawRect((float) (game.getPlayer().getxPos() - .1*width), (float) (game.getPlayer().getyPos() - .1*width), (float) (game.getPlayer().getxPos() + .1*width), (float)(game.getPlayer().getyPos() + .1*width), playerColour);

        textColour = new Paint();
        textColour.setColor(Color.BLACK);
        textColour.setTextSize(30);

        if(!game.getIsAlive()){
            //canvas.drawText(insultGenerator.insult(1), 50, 50, textColour);
            insultGenerator.insult(1);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();
        float action = motionEvent.getAction();
        float koord  = motionEvent.ACTION_DOWN;
        //motionEvent.ACTION_MOVE når der swipes. Koordinaterne er hvor swipet slutter
        //motionEvent.ACTION_UP når man ikke længere rører fladen



        return true;
    }




    class Timer extends Thread{
        @Override
        public void run() {
            while (game.getIsAlive()) {
                try {
                    Thread.sleep(1000 / 60);
                    //60fps
                } catch (InterruptedException e) {
                    //Do nothing here
                }
                for (obstacles o : game.getObstacles()) {
                    o.setyPos(o.getyPos() + 20);
                    game.coll(o.getxPos(), o.getyPos(), width);
                }
                game.checkObstacles(height);

                postInvalidate();
            }

        }
    }
}

