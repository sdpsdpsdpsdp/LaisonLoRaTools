package com.laisontech.laisonloratools.customerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.laisontech.commonuilibrary.utils.DisplayUtil;
import com.laisontech.laisonloratools.R;

/**
 * Created by SDP on 2018/5/2.
 * 地图的点击小控件
 */

public class MapStyleButton extends FrameLayout {
    private int mFlWidthAndHeight = 38;//frameLayout的宽和高
    private int mImageWidthAndHeight = 25;
    private int mImageResId = R.drawable.default_image;

    public MapStyleButton(@NonNull Context context) {
        this(context, null);
    }

    public MapStyleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MapStyleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MapStyleButton);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.MapStyleButton_centerImage) {
                mImageResId = ta.getResourceId(index, mImageResId);
            } else if (index == R.styleable.MapStyleButton_imageWH) {
                mImageWidthAndHeight = (int) ta.getDimension(index, DisplayUtil.dip2px(context, mImageWidthAndHeight));
            } else if (index == R.styleable.MapStyleButton_flWH) {
                mFlWidthAndHeight = (int) ta.getDimension(index, DisplayUtil.dip2px(context, mFlWidthAndHeight));
            }
        }
        ta.recycle();

        View view = View.inflate(context, R.layout.map_style_button_layout, this);

        View viewOutside = view.findViewById(R.id.fl_touch);
        ViewGroup.LayoutParams layoutParamsOutSide = viewOutside.getLayoutParams();
        layoutParamsOutSide.width = mFlWidthAndHeight;
        layoutParamsOutSide.height = mFlWidthAndHeight;
        viewOutside.setLayoutParams(layoutParamsOutSide);

        ImageView mIv = (ImageView) view.findViewById(R.id.iv_image);
        ViewGroup.LayoutParams layoutParamsIv = mIv.getLayoutParams();
        layoutParamsIv.height = mImageWidthAndHeight;
        layoutParamsIv.width = mImageWidthAndHeight;
        mIv.setLayoutParams(layoutParamsIv);

        mIv.setImageResource(mImageResId);
    }

}
