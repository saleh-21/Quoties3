package com.goldenkey.quoties;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by sonu on 28/03/17.
 */

public class FloatingWidgetService extends Service implements View.OnClickListener {
    private WindowManager mWindowManager;
    private View mFloatingWidgetView, collapsedView, expandedView;
    private ImageView remove_image_view;
    private Point szWindow = new Point();
    private View removeFloatingWidgetView;
    TextView title;
    Map<String, String> map;
    ArrayList<String> words;
    int ind = 0;
    int i = 0;


    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;

    //Variable to check if the Floating widget view is on left side or in right side
    // initially we are displaying Floating widget view to Left side so set it to true
    private boolean isLeft = true;
    private int times;

    public FloatingWidgetService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //init WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        getWindowManagerDefaultDisplay();

        //Init LayoutInflater
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        addFloatingWidgetView(inflater);

        implementClickListeners();
        implementTouchListenerToFloatingWidgetView();
        words = new ArrayList<>();
        words.add("Don't cry becouse it end, smile becouse it happend ");
        words.add(" be happy ");
        words.add("sohel ");
        words.add("also sohel ");
        words.add("not sohel ");
        words.add(" bang sohel ");
        words.add("laaaaaaaaaaaaaaaaaaaaaaaaaaaarg sohel ");
        String word = words.get(0);
        title.setText(word);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                expandedView.setVisibility(View.GONE);
                wisam();
            }
        }.start();


    }

    void wisam() {
        if (i == 0)
            new CountDownTimer(1000*60*15, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    updateTitle();
                    expandedView.setVisibility(View.VISIBLE);
                    i = 1;
                    wisam();
                }
            }.start();
        else
            new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    expandedView.setVisibility(View.GONE);
                    i = 0;
                    wisam();
                }
            }.start();


    }


    private void updateWhenSohelWant(long called) {
        while (Calendar.getInstance().getTimeInMillis() - called < 3000) {
        }
        expandedView.setVisibility(View.VISIBLE);
    }


    /*  Add Remove View to Window Manager  */
    private View addRemoveView(LayoutInflater inflater) {
        //Inflate the removing view layout we created


        //Add the view to the window.
        WindowManager.LayoutParams paramRemove = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        paramRemove.gravity = Gravity.TOP | Gravity.LEFT;

        //Initially the Removing widget view is not visible, so set visibility to GONE
        removeFloatingWidgetView.setVisibility(View.GONE);


        //Add the view to the window
        mWindowManager.addView(removeFloatingWidgetView, paramRemove);
        return remove_image_view;
    }

    /*  Add Floating Widget View to Window Manager  */
    private void addFloatingWidgetView(LayoutInflater inflater) {
        //Inflate the floating view layout we created
        mFloatingWidgetView = inflater.inflate(R.layout.floating_widget_layout, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;

        //Initially view will be added to top-left corner, you change x-y coordinates according to your need
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager.addView(mFloatingWidgetView, params);

        //find id of collapsed view layout

        //find id of the expanded view layout
        expandedView = mFloatingWidgetView.findViewById(R.id.expanded_container);
        title = (TextView) mFloatingWidgetView.findViewById(R.id.floating_widget_title_label);
        ;
    }

    private void getWindowManagerDefaultDisplay() {
        mWindowManager.getDefaultDisplay().getSize(szWindow);
    }

    /*  Implement Touch Listener to Floating Widget Root View  */
    private void implementTouchListenerToFloatingWidgetView() {
        //Drag and move floating view using user's touch action.
        mFloatingWidgetView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {

            long time_start = 0, time_end = 0;

            boolean isLongClick = false;//variable to judge if user click long press
            boolean inBounded = false;//variable to judge if floating view is bounded to remove view
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {
                @Override
                public void run() {

                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                expandedView.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private void implementClickListeners() {
        mFloatingWidgetView.findViewById(R.id.sharebtn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sharebtn:
                Toast.makeText(this, "sharing fuck", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*  on Floating Widget Long Click, increase the size of remove view as it look like taking focus
    private void onFloatingWidgetLongClick() {
        //Get remove Floating view params
        WindowManager.LayoutParams removeParams = (WindowManager.LayoutParams) removeFloatingWidgetView.getLayoutParams();

        //get x and y coordinates of remove view
        int x_cord = (szWindow.x - removeFloatingWidgetView.getWidth()) / 2;
        int y_cord = szWindow.y - (removeFloatingWidgetView.getHeight() + getStatusBarHeight());


        removeParams.x = x_cord;
        removeParams.y = y_cord;

        //Update Remove view params
        mWindowManager.updateViewLayout(removeFloatingWidgetView, removeParams);
    }*/

    /*  Reset position of Floating Widget view on dragging  */
    private void resetPosition(int x_cord_now) {
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(x_cord_now);
        } else {
            isLeft = false;
            moveToRight(x_cord_now);
        }

    }


    /*  Method to move the Floating widget view to Left  */
    private void moveToLeft(final int current_x_cord) {
        final int x = szWindow.x - current_x_cord;

        new CountDownTimer(500, 5) {
            //get params of Floating Widget view
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;

                mParams.x = 0 - (int) (current_x_cord * current_x_cord * step);

                //If you want bounce effect uncomment below line and comment above line
                // mParams.x = 0 - (int) (double) bounceValue(step, x);


                //Update window manager for Floating Widget
                mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);
            }

            public void onFinish() {
                mParams.x = 0;

                //Update window manager for Floating Widget
                mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);
            }
        }.start();
    }

    /*  Method to move the Floating widget view to Right  */
    private void moveToRight(final int current_x_cord) {

        new CountDownTimer(500, 5) {
            //get params of Floating Widget view
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;

                mParams.x = (int) (szWindow.x + (current_x_cord * current_x_cord * step) - mFloatingWidgetView.getWidth());

                //If you want bounce effect uncomment below line and comment above line
                //  mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - mFloatingWidgetView.getWidth();

                //Update window manager for Floating Widget
                mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);
            }

            public void onFinish() {
                mParams.x = szWindow.x - mFloatingWidgetView.getWidth();

                //Update window manager for Floating Widget
                mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);
            }
        }.start();
    }

    /*  Get Bounce value if you want to make bounce effect to your Floating Widget */
    private double bounceValue(long step, long scale) {
        double value = scale * java.lang.Math.exp(-0.055 * step) * java.lang.Math.cos(0.08 * step);
        return value;
    }


    /*  Detect if the floating view is collapsed or expanded */
    private boolean isViewCollapsed() {
        return mFloatingWidgetView == null;
    }


    /*  return status bar height on basis of device display metrics  */
    private int getStatusBarHeight() {
        return (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
    }


    /*  Update Floating Widget view coordinates on Configuration change  */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getWindowManagerDefaultDisplay();

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


            if (layoutParams.y + (mFloatingWidgetView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y = szWindow.y - (mFloatingWidgetView.getHeight() + getStatusBarHeight());
                mWindowManager.updateViewLayout(mFloatingWidgetView, layoutParams);
            }

            if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                resetPosition(szWindow.x);
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (layoutParams.x > szWindow.x) {
                resetPosition(szWindow.x);
            }

        }

    }

    /*  on Floating widget click show expanded view  */
    private void onFloatingWidgetClick() {
        if (isViewCollapsed()) {
            //When user clicks on the image view of the collapsed layout,
            //visibility of the collapsed layout will be changed to "View.GONE"
            //and expanded view will become visible.
            /*
            collapsedView.setVisibility(View.GONE);
            expandedView.setVisibility(View.VISIBLE);
            */
            updateTitle();
        }
    }

    private void updateTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            title.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        int random=(int)(Math.random()*100)%7;
        title.setText(words.get(random));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*  on destroy remove both view from window manager */

        if (mFloatingWidgetView != null)
            mWindowManager.removeView(mFloatingWidgetView);

        if (removeFloatingWidgetView != null)
            mWindowManager.removeView(removeFloatingWidgetView);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


}
