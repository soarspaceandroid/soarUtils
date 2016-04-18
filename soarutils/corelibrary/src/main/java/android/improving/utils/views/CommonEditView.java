package android.improving.utils.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.improving.utils.R;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author: guoyazhou
 * @date: 2016-04-12 16:44
 */

public class CommonEditView extends LinearLayout {

    private Context context;
    private LayoutInflater inflater;
    private ImageView imgDelete, imgScan;
    private EditText editText;
    private boolean isPwdInput;
    private String hint;
    private float textSize;
    private int textColor, hintTextColor, inputType;
    OnTextChangeListener listener;
    OnFocusCommonChangeListener focusChangeListener;
    Resources.Theme theme;

    public CommonEditView(Context context) {
        this(context, null);
    }

    public CommonEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonEditView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        this.context = context;
        theme = context.getTheme();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonEdittext);

        hint = array.getString(R.styleable.CommonEdittext_hint);
        textSize = array.getInteger(R.styleable.CommonEdittext_myTextSize, 15);
        textColor = array.getColor(R.styleable.CommonEdittext_myTextColor, context.getResources().getColor(R.color.black));
        hintTextColor = array.getColor(R.styleable.CommonEdittext_myTextHintColor, context.getResources().getColor(R.color.black));
        inputType = array.getInt(R.styleable.CommonEdittext_myInputType, 0);

        array.recycle();

        initView();

    }

    private void initView() {

        inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.common_editview, this);

        editText = (EditText) rootView.findViewById(R.id.custom_edit);
        imgDelete = (ImageView) rootView.findViewById(R.id.img_delete);
        imgScan = (ImageView) rootView.findViewById(R.id.img_scan);

        editText.setHint(hint);
        editText.setTextSize(textSize);
        editText.setTextColor(textColor);
        editText.setHintTextColor(hintTextColor);

        switch (inputType) {
            case 0:
                //NumberSigned
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                isPwdInput = false;
                break;
            case 1:
//                number
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                isPwdInput = false;
                break;
            case 2:
                //numberPwd
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
                isPwdInput = true;
                break;
            case 3:
//                textPwd
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPwdInput = true;
                break;
            case 4:
//                text
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                isPwdInput = false;
                break;
            case 5:
//                numberDecimal
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                isPwdInput = false;
                break;
            default:
//                text
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                isPwdInput = false;
                break;
        }

        if (isPwdInput) {
            // 设置EditText文本为隐藏的
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgScan.setVisibility(VISIBLE);
        } else {
            imgScan.setVisibility(GONE);
        }

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

//                EditText _v = (EditText) v;
//                if (!hasFocus) {// 失去焦点
//                    _v.setHint(_v.getTag().toString());
//                } else {
//                    String hint = _v.getHint().toString();
//                    _v.setTag(hint);
//                    _v.setHint("");
//                }

                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });


        imgDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                editText.setText("");

            }
        });

        imgScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (isPwdInput) {
                    // 设置EditText文本为可见的
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgScan.setBackgroundResource(R.mipmap.img_scan_press);
                } else {
                    // 设置EditText文本为隐藏的
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgScan.setBackgroundResource(R.mipmap.img_scan_normal);
                }

                CharSequence charSequence = editText.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }

                isPwdInput = !isPwdInput;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                if (arg0.length() > 0) {
                    imgDelete.setVisibility(View.VISIBLE);
                } else {
                    imgDelete.setVisibility(View.INVISIBLE);
                }
                if (listener != null) {
                    listener.onTextChanged(arg0, arg1, arg2, arg3);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                if (listener != null) {
                    listener.beforeTextChanged(arg0, arg1, arg2, arg3);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (listener != null) {
                    listener.afterTextChanged(arg0);
                }
            }
        });


    }

    public Editable getText() {
        return editText.getText();
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    public void setOnFocusCommonChangeListener(OnFocusCommonChangeListener listener) {
        this.focusChangeListener = listener;
    }

    public interface OnTextChangeListener {
        void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3);

        void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3);

        void afterTextChanged(Editable arg0);
    }

    public interface OnFocusCommonChangeListener {
        public void onFocusChange(View v, boolean hasFocus);
    }

}