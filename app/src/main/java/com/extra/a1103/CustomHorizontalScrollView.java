//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.extra.a1103;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView<ScrollViewListenner> extends HorizontalScrollView {

    private final Context context;

    public CustomHorizontalScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        this.context = context;
    }


     @Override
     public void fling(int velocity) {
     super.fling(velocity / 10000);
     }

}

