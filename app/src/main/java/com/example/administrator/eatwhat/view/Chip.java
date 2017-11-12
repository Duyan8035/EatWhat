package com.example.administrator.eatwhat.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.administrator.eatwhat.R;
import com.robertlevonyan.views.chip.OnChipClickListener;
import com.robertlevonyan.views.chip.OnCloseClickListener;
import com.robertlevonyan.views.chip.OnIconClickListener;
import com.robertlevonyan.views.chip.OnSelectClickListener;

import static com.example.administrator.eatwhat.view.ChipUtils.IMAGE_ID;
import static com.example.administrator.eatwhat.view.ChipUtils.TEXT_ID;
import static com.example.administrator.eatwhat.view.ChipUtils.generateText;
import static com.example.administrator.eatwhat.view.ChipUtils.getCircleBitmap;
import static com.example.administrator.eatwhat.view.ChipUtils.getCircleBitmapWithText;
import static com.example.administrator.eatwhat.view.ChipUtils.getScaledBitmap;
import static com.example.administrator.eatwhat.view.ChipUtils.getSquareBitmap;
import static com.example.administrator.eatwhat.view.ChipUtils.setIconColor;

/**
 * Created by robert on 3/1/17.
 */

public class Chip extends RelativeLayout {

    private String chipText;
    private boolean hasIcon;
    private Drawable chipIcon;
    private Bitmap chipIconBitmap;
    private String chipIconUrl;
    private boolean closable;
    private boolean selectable;
    private int backgroundColor;
    private int selectedBackgroundColor;
    private int textColor;
    private int selectedTextColor;
    private int closeColor;
    private int selectedCloseColor;
    private int cornerRadius;
    private int strokeSize;
    private int strokeColor;
    private String iconText;
    private int iconTextColor;
    private int iconTextBackgroundColor;

    private ImageView closeIcon;
    private ImageView selectIcon;
    private ImageView iconImageView;

    private boolean clicked;
    private boolean selected;
    private boolean isCreated;

    private OnCloseClickListener onCloseClickListener;
    private OnSelectClickListener onSelectClickListener;
    private OnChipClickListener onChipClickListener;
    private OnIconClickListener onIconClickListener;

    public Chip(Context context) {
        this(context, null, 0);
    }

    public Chip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTypedArray(attrs);

        initChipClick();
    }

    private void initChipClick() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChipClickListener != null) {
                    onChipClickListener.onChipClick(v);
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isCreated) {
            buildView();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams thisParams = getLayoutParams();

        thisParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        thisParams.height = (int) getResources().getDimension(R.dimen.chip_height);

        this.setLayoutParams(thisParams);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && !isCreated) {
            buildView();
        }
    }

    private void buildView() {
        isCreated = true;
        initBackgroundColor();
        initTextView();
        initImageIcon();
        initCloseIcon();
        initSelectIcon();
    }

    private void initSelectClick() {
        selectIcon.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        onSelectTouchDown();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        onSelectTouchUp(v);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void onSelectTouchDown() {
        clicked = true;
        initBackgroundColor();
        initTextView();
        selectIcon.setImageResource(R.drawable.ic_select);
        setIconColor(selectIcon, selectedCloseColor);
    }

    private void onSelectTouchUp(View v) {
        if (selected) {
            clicked = false;
            initBackgroundColor();
            initTextView();
            selectIcon.setImageResource(R.drawable.ic_select);
            setIconColor(selectIcon, closeColor);
        }
        selected = !selected;
        if (onSelectClickListener != null) {
            onSelectClickListener.onSelectClick(v, selected);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCloseClick() {
        closeIcon.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        onCloseTouchDown();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        onCloseTouchUp(v);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void onCloseTouchDown() {
        clicked = true;
        initBackgroundColor();
        initTextView();
        closeIcon.setImageResource(R.drawable.ic_close);
        setIconColor(closeIcon, selectedCloseColor);
    }

    private void onCloseTouchUp(View v) {
        clicked = false;
        initBackgroundColor();
        initTextView();
        closeIcon.setImageResource(R.drawable.ic_close);
        setIconColor(closeIcon, closeColor);

        if (onCloseClickListener != null) {
            onCloseClickListener.onCloseClick(v);
        }
    }

    private void initSelectIcon() {
        if (!selectable) {
            return;
        }

        selectIcon = new ImageView(Utils.getApp());


        LayoutParams selectIconParams = new LayoutParams((int) getResources().getDimension(R.dimen.chip_close_icon_size2), (int) getResources().getDimension(R.dimen.chip_close_icon_size2));
        selectIconParams.addRule(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? END_OF : RIGHT_OF, TEXT_ID);
        selectIconParams.addRule(CENTER_VERTICAL);
        selectIconParams.setMargins(
                (int) getResources().getDimension(R.dimen.chip_close_horizontal_margin),
                0,
                (int) getResources().getDimension(R.dimen.chip_close_horizontal_margin),
                0
        );

        selectIcon.setLayoutParams(selectIconParams);
        selectIcon.setScaleType(ImageView.ScaleType.CENTER);
        selectIcon.setImageResource(R.drawable.ic_select);
        setIconColor(selectIcon, closeColor);

        initSelectClick();

        addView(selectIcon);
    }

    private void initCloseIcon() {
        if (!closable) {
            return;
        }

        closeIcon = new ImageView(Utils.getApp());

        LayoutParams closeIconParams = new LayoutParams((int) getResources().getDimension(R.dimen.chip_close_icon_size2), (int) getResources().getDimension(R.dimen.chip_close_icon_size2));
        closeIconParams.addRule(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? END_OF : RIGHT_OF, TEXT_ID);
        closeIconParams.addRule(CENTER_VERTICAL);
        closeIconParams.setMargins(
                (int) getResources().getDimension(R.dimen.chip_close_horizontal_margin),
                0,
                (int) getResources().getDimension(R.dimen.chip_close_horizontal_margin),
                0
        );

        closeIcon.setLayoutParams(closeIconParams);
        closeIcon.setScaleType(ImageView.ScaleType.CENTER);
        closeIcon.setImageResource(R.drawable.ic_close);
        setIconColor(closeIcon, closeColor);

        initCloseClick();

        addView(closeIcon);
    }

    private void initImageIcon() {
//        if (!hasIcon) {
//            return;
//        }

        LogUtils.e("初始化ImageIcon");
        iconImageView = new ImageView(Utils.getApp());
        LayoutParams iconParams = new LayoutParams((int) getResources().getDimension(R.dimen.chip_height), (int) getResources().getDimension(R.dimen.chip_height));
        iconParams.addRule(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? ALIGN_PARENT_START : ALIGN_PARENT_LEFT);
        iconImageView.setLayoutParams(iconParams);
        iconImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iconImageView.setId(IMAGE_ID);

        if (chipIcon != null && ((BitmapDrawable) chipIcon).getBitmap() != null) {
            LogUtils.e("chipIcon不为空");
            Bitmap bitmap = ((BitmapDrawable) chipIcon).getBitmap();
            bitmap = getSquareBitmap(bitmap);
            bitmap = getScaledBitmap(Utils.getApp(), bitmap);
            iconImageView.setImageBitmap(getCircleBitmap(Utils.getApp(), bitmap));
        }
        if (chipIconBitmap != null) {
            chipIconBitmap = getSquareBitmap(chipIconBitmap);
            iconImageView.setImageBitmap(getCircleBitmap(Utils.getApp(), chipIconBitmap));
            iconImageView.bringToFront();
        }
        if (chipIconUrl != null) {
            Glide.with(Utils.getApp()).load(chipIconUrl).into(iconImageView);
            try {
                Glide.with(Utils.getApp()).asBitmap().load(chipIconUrl).into(new BitmapImageViewTarget(iconImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(Utils.getApp().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iconImageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (iconText != null && !iconText.equals("")) {
            Bitmap textBitmap = getCircleBitmapWithText(Utils.getApp(), iconText, iconTextColor, iconTextBackgroundColor);
            iconImageView.setImageBitmap(textBitmap);
        }

        iconImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIconClickListener != null) {
                    onIconClickListener.onIconClick(v);
                }
            }
        });

        addView(iconImageView);
    }

    private void initTextView() {
        TextView chipTextView = new TextView(Utils.getApp());

        LayoutParams chipTextParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (hasIcon || closable || selectable) {
            chipTextParams.addRule(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? END_OF : RIGHT_OF, IMAGE_ID);
            chipTextParams.addRule(CENTER_VERTICAL);
        } else {
            chipTextParams.addRule(CENTER_IN_PARENT);
        }

        @Px int startMargin = hasIcon ? (int) getResources().getDimension(R.dimen.chip_icon_horizontal_margin) : (int) getResources().getDimension(R.dimen.chip_horizontal_padding);
        @Px int endMargin = closable || selectable ? 0 : (int) getResources().getDimension(R.dimen.chip_horizontal_padding);
        chipTextParams.setMargins(
                startMargin,
                0,
                endMargin,
                0
        );

        chipTextView.setLayoutParams(chipTextParams);
        chipTextView.setTextColor(clicked ? selectedTextColor : textColor);
        chipTextView.setText(chipText);
        chipTextView.setId(TEXT_ID);

        this.addView(chipTextView);
    }

    private void initBackgroundColor() {
        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setShape(GradientDrawable.RECTANGLE);
        bgDrawable.setCornerRadii(new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                cornerRadius, cornerRadius, cornerRadius, cornerRadius});
        bgDrawable.setColor(clicked ? selectedBackgroundColor : backgroundColor);
        bgDrawable.setStroke(strokeSize, strokeColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(bgDrawable);
        } else {
            setBackgroundDrawable(bgDrawable);
        }
    }

    private void initTypedArray(AttributeSet attrs) {
        TypedArray ta = Utils.getApp().getTheme().obtainStyledAttributes(attrs, R.styleable.Chip, 0, 0);

        chipText = ta.getString(R.styleable.Chip_mcv_chipText);
        hasIcon = ta.getBoolean(R.styleable.Chip_mcv_hasIcon, false);
        chipIcon = ta.getDrawable(R.styleable.Chip_mcv_chipIcon);
//        chipIcon = Utils.getApp().getResources().getDrawable(R.drawable.icon_img_default);
        closable = ta.getBoolean(R.styleable.Chip_mcv_closable, false);
        selectable = ta.getBoolean(R.styleable.Chip_mcv_selectable, false);
        backgroundColor = ta.getColor(R.styleable.Chip_mcv_backgroundColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipBackground));
        selectedBackgroundColor = ta.getColor(R.styleable.Chip_mcv_selectedBackgroundColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipBackgroundClicked));
        textColor = ta.getColor(R.styleable.Chip_mcv_textColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipText));
        selectedTextColor = ta.getColor(R.styleable.Chip_mcv_selectedTextColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipTextClicked));
        closeColor = ta.getColor(R.styleable.Chip_mcv_closeColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipCloseInactive));
        selectedCloseColor = ta.getColor(R.styleable.Chip_mcv_selectedCloseColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipCloseClicked));
        cornerRadius = (int) ta.getDimension(R.styleable.Chip_mcv_cornerRadius, getResources().getDimension(R.dimen.chip_height) / 2);
        strokeSize = (int) ta.getDimension(R.styleable.Chip_mcv_strokeSize, 0);
        strokeColor = ta.getColor(R.styleable.Chip_mcv_strokeColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipCloseClicked));
        iconText = ta.getString(R.styleable.Chip_mcv_iconText);
        iconTextColor = ta.getColor(R.styleable.Chip_mcv_iconTextColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipBackgroundClicked));
        iconTextBackgroundColor = ta.getColor(R.styleable.Chip_mcv_iconTextColor, ContextCompat.getColor(Utils.getApp(), R.color.colorChipCloseClicked));

        ta.recycle();
    }

    public String getChipText() {
        return chipText;
    }

    public void setChipText(String chipText) {
        this.chipText = chipText;
    }

    public boolean isHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public Drawable getChipIcon() {
        return chipIcon;
    }

    public void setChipIcon(Drawable chipIcon) {
        this.chipIcon = chipIcon;
    }

    public void setChipIcon(Bitmap chipIcon) {
        this.chipIconBitmap = chipIcon;
    }


    public void setChipIcon(String url) {
        this.chipIconUrl = url;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
        this.selectable = false;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void changeBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public void changeSelectedBackgroundColor(int selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public int getCloseColor() {
        return closeColor;
    }

    public void setCloseColor(int closeColor) {
        this.closeColor = closeColor;
    }

    public int getSelectedCloseColor() {
        return selectedCloseColor;
    }

    public void setSelectedCloseColor(int selectedCloseColor) {
        this.selectedCloseColor = selectedCloseColor;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        this.closable = false;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public String getIconText() {
        return iconText;
    }

    public void setIconText(String iconText, int iconTextColor, int iconTextBackgroundColor) {
        this.iconText = generateText(iconText);
        this.iconTextColor = iconTextColor == 0 ? ContextCompat.getColor(Utils.getApp(), R.color.colorChipBackgroundClicked) : iconTextColor;
        this.iconTextBackgroundColor = iconTextBackgroundColor == 0 ? ContextCompat.getColor(Utils.getApp(), R.color.colorChipCloseClicked) : iconTextBackgroundColor;
    }

    public void setOnCloseClickListener(OnCloseClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public void setOnChipClickListener(OnChipClickListener onChipClickListener) {
        this.onChipClickListener = onChipClickListener;
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }

    public ImageView getIconImageView() {
        if (iconImageView == null)
            iconImageView = new ImageView(getContext());
        return iconImageView;
    }

    public void setIconImageView(ImageView iconImageView) {
        this.iconImageView = iconImageView;
    }
}