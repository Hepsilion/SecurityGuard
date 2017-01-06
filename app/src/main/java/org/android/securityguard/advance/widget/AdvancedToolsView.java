package org.android.securityguard.advance.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.android.securityguard.R;

/**
 * Created by Hepsilion on 2017/1/5.
 */
public class AdvancedToolsView extends RelativeLayout {
    private String desc="";
    private Drawable drawable;

    private ImageView mLeftImgV;
    private TextView mDescriptionTV;

    public AdvancedToolsView(Context context){
        super(context);
        init(context);
    }

    public AdvancedToolsView(Context context, AttributeSet attrs){
        super(context, attrs);

        TypedArray mTypeArray=context.obtainStyledAttributes(attrs, R.styleable.AdvancedToolsView); //获取到属性对象
        desc=mTypeArray.getString(R.styleable.AdvancedToolsView_desc);        //获取到desc属性，与attrs.xml中定义的desc属性绑定
        drawable=mTypeArray.getDrawable(R.styleable.AdvancedToolsView_android_src);  //获取到android:src属性，与attrs.xml中定义的android:src属性绑定
        mTypeArray.recycle(); //回收属性对象

        init(context);
    }

    public AdvancedToolsView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 控件初始化
     * @param context
     */
    private void init(Context context){
        //将资源转化为View对象显示在自己身上
        View view=View.inflate(context, R.layout.ui_advancedtools_view, null);
        this.addView(view);

        mLeftImgV= (ImageView) findViewById(R.id.imgv_left);
        if(drawable!=null){
            mLeftImgV.setImageDrawable(drawable);
        }

        mDescriptionTV= (TextView) findViewById(R.id.tv_description);
        mDescriptionTV.setText(desc);
    }
}