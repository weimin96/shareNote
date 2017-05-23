package com.aoliao.notebook.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import com.aoliao.notebook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 你的奥利奥 on 2017/1/26.
 */

public class RichEditText extends EditText implements TextWatcher {
    private int bulletColor = 0;          //项目符号颜色
    private int bulletRadius = 0;         //项目符号缩进距离
    private int bulletGapWidth = 0;       //文本与项目符号距离
    private boolean underLine = true;
    private int linkColor = 0;
    private int quoteColor = 0;           //引用竖线
    private int quoteStripeWidth = 0;
    private int quoteGapWidth = 0;


    public static final int FORMAT_BOLD = 0x01;
    public static final int FORMAT_ITALIC = 0x02;
    public static final int FORMAT_UNDERLINE = 0x03;
    public static final int FORMAT_QUOTE = 0x04;
    public static final int FORMAT_LINK = 0x05;
    public static final int FORMAT_BULLET = 0x06;
    public static final int FORMAT_STRIKETHROUGH = 0x07;


    private SpannableStringBuilder inputBefore;

    public RichEditText(Context context) {
        super(context);
        init(null);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("NewApi")
    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RichEditText);
        bulletColor = array.getColor(R.styleable.RichEditText_bulletColor, 0);
        bulletRadius = array.getDimensionPixelSize(R.styleable.RichEditText_bulletRadius, 0);
        bulletGapWidth = array.getDimensionPixelSize(R.styleable.RichEditText_bulletGapWidth, 0);
        underLine = array.getBoolean(R.styleable.RichEditText_underLine, true);
        linkColor = array.getColor(R.styleable.RichEditText_linkColor, 0);
        quoteColor = array.getColor(R.styleable.RichEditText_quoteColor, 0);
        quoteStripeWidth = array.getDimensionPixelSize(R.styleable.RichEditText_quoteStripeWidth, 0);
        quoteGapWidth = array.getDimensionPixelSize(R.styleable.RichEditText_quoteGapWith, 0);
        array.recycle();//TypedArray通常最后调用recycle()方法，为了保持以后使用该属性一致性
    }

    /**
     * 在第一次onDraw前调用，也就是在view没有绘制出来时调用，但只调用一次
     * <p>
     * 是view本身回调，用于初始化一些东西相当于onstart
     * add
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //继承于editText的文本监听器，必须重写三个方法beforeTextChanged、onTextChanged、afterTextChanged
        addTextChangedListener(this);
    }

    /**
     * 销毁view时调用
     * <p>
     * 是view本身回调，用于销毁一些东西相当于onstop
     * remove
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {
        //相对于String,SpannableStringBuilder可以对字符串添加额外的样式信息
        inputBefore = new SpannableStringBuilder(text);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public void afterTextChanged(Editable text) {
        inputBefore = new SpannableStringBuilder(text);
    }

    //**************************粗体、斜体***********************************************************

    /**
     * 分两种情况
     * 1.还没有使用对应的格式
     * 2.已经使用对应格式效果的文本
     *
     * @param valid
     */
    public void bold(boolean valid) {
        if (valid) {
            styleValid(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
        } else {
            styleInValid(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
        }
    }

    public void italic(boolean valid) {
        if (valid) {
            styleValid(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
        } else {
            styleInValid(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
        }
    }

    /**
     * 如果与传入style的对应就设置对应的文本格式
     */
    protected void styleValid(int style, int start, int end) {
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return;
        }
        if (start >= end) {
            return;
        }
        getEditableText().setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected void styleInValid(int style, int start, int end) {
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return;
        }
        if (start >= end) {
            return;
        }
        StyleSpan[] spans = getEditableText().getSpans(start, end, StyleSpan.class);
        List<EditSpanPart> list = new ArrayList<>();
        for (StyleSpan span : spans) {             //遍历选中文本的所有格式
            if (span.getStyle() == style) {        //匹配要取消的格式
                //将有这种格式文本的开始结尾封装起来放到list里，不限于是否选择这段文本
                list.add(new EditSpanPart(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
                //对所选的文本包括对应span里包含的所有文本取消这种的格式
                getEditableText().removeSpan(span);
            }
        }
        //对之前多余的修改进行还原
        for (EditSpanPart part : list) {
            if (part.isValid()) {
                if (part.getStart() < start) {     //如果list里
                    styleValid(style, part.getStart(), start);
                }
                if (part.getEnd() > end) {
                    styleValid(style, end, part.getEnd());
                }
            }
        }
    }

    protected boolean containStyle(int style, int start, int end) {
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return false;
        }
        if (start > end) {
            return false;
        }
        //没选择文本时
        if (start == end) {
            //光标在最前和最后时
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                //before光标前面文本的格式
                StyleSpan[] before = getEditableText().getSpans(start - 1, start, StyleSpan.class);
                StyleSpan[] after = getEditableText().getSpans(start, start + 1, StyleSpan.class);
                //光标前后文本有格式并且。。。
                return before.length > 0 && after.length > 0 && before[0].getStyle() == style && after[0].getStyle() == style;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            // Make sure no duplicate characters be added
            for (int i = start; i < end; i++) {
                StyleSpan[] spans = getEditableText().getSpans(i, i + 1, StyleSpan.class);
                for (StyleSpan span : spans) {
                    if (span.getStyle() == style) {
                        builder.append(getEditableText().subSequence(i, i + 1).toString());
                        break;
                    }
                }
            }
            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    //**************************下划线***************************************************************

    public void underLine(boolean valid) {
        if (valid) {
            underLineValid(getSelectionStart(), getSelectionEnd());
        } else {
            underLineInValid(getSelectionStart(), getSelectionEnd());
        }
    }

    protected void underLineValid(int start, int end) {
        if (start >= end) {
            return;
        }
        getEditableText().setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected void underLineInValid(int start, int end) {
        if (start >= end) {
            return;
        }
        UnderlineSpan[] spans = getEditableText().getSpans(start, end, UnderlineSpan.class);
        List<EditSpanPart> list = new ArrayList<>();
        for (UnderlineSpan span : spans) {             //遍历选中文本的所有格式
            //将有这种格式文本的开始结尾封装起来放到list里，不限于是否选择这段文本
            list.add(new EditSpanPart(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            //对所选的文本包括对应span里包含的所有文本取消这种的格式
            getEditableText().removeSpan(span);
        }
        //对之前多余的修改进行还原
        for (EditSpanPart part : list) {
            if (part.isValid()) {
                if (part.getStart() < start) {     //如果list里
                    underLineValid(part.getStart(), start);
                }
                if (part.getEnd() > end) {
                    underLineValid(end, part.getEnd());
                }
            }
        }
    }

    protected boolean containUnderline(int start, int end) {
        if (start > end) {
            return false;
        }
        //没选择文本时
        if (start == end) {
            //光标在最前和最后时
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                //before光标前面文本的格式
                UnderlineSpan[] before = getEditableText().getSpans(start - 1, start, UnderlineSpan.class);
                UnderlineSpan[] after = getEditableText().getSpans(start, start + 1, UnderlineSpan.class);
                //光标前后文本有格式并且。。。
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            // Make sure no duplicate characters be added
            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, UnderlineSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }

            }
            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    //**********************删除线*******************************************************************
    public void strikeThrough(boolean valid) {
        if (valid) {
            strikeThroughValid(getSelectionStart(), getSelectionEnd());
        } else {
            strikeThroughInvalid(getSelectionStart(), getSelectionEnd());
        }
    }

    protected void strikeThroughValid(int start, int end) {
        if (start >= end) {
            return;
        }

        getEditableText().setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected void strikeThroughInvalid(int start, int end) {
        if (start >= end) {
            return;
        }

        StrikethroughSpan[] spans = getEditableText().getSpans(start, end, StrikethroughSpan.class);
        List<EditSpanPart> list = new ArrayList<>();

        for (StrikethroughSpan span : spans) {
            list.add(new EditSpanPart(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (EditSpanPart part : list) {
            if (part.isValid()) {
                if (part.getStart() < start) {
                    strikeThroughValid(part.getStart(), start);
                }

                if (part.getEnd() > end) {
                    strikeThroughValid(end, part.getEnd());
                }
            }
        }
    }

    protected boolean containStrikethrough(int start, int end) {
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                StrikethroughSpan[] before = getEditableText().getSpans(start - 1, start, StrikethroughSpan.class);
                StrikethroughSpan[] after = getEditableText().getSpans(start, start + 1, StrikethroughSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();

            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, StrikethroughSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }

            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    // BulletSpan ==================================================================================

    public void bullet(boolean valid) {
        if (valid) {
            bulletValid();
        } else {
            bulletInvalid();
        }
    }

    protected void bulletValid() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (containBullet(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1; // \n
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            // Find selection area inside
            int bulletStart = 0;
            int bulletEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            }

            if (bulletStart < bulletEnd) {
                getEditableText().setSpan(new EditBulletSpan(bulletColor, bulletRadius, bulletGapWidth), bulletStart, bulletEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    protected void bulletInvalid() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (!containBullet(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            int bulletStart = 0;
            int bulletEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            }

            if (bulletStart < bulletEnd) {
                BulletSpan[] spans = getEditableText().getSpans(bulletStart, bulletEnd, BulletSpan.class);
                for (BulletSpan span : spans) {
                    getEditableText().removeSpan(span);
                }
            }
        }
    }

    protected boolean containBullet() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                list.add(i);
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                list.add(i);
            }
        }

        for (Integer i : list) {
            if (!containBullet(i)) {
                return false;
            }
        }

        return true;
    }

    protected boolean containBullet(int index) {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        if (index < 0 || index >= lines.length) {
            return false;
        }

        int start = 0;
        for (int i = 0; i < index; i++) {
            start = start + lines[i].length() + 1;
        }

        int end = start + lines[index].length();
        if (start >= end) {
            return false;
        }

        BulletSpan[] spans = getEditableText().getSpans(start, end, BulletSpan.class);
        return spans.length > 0;
    }

    // QuoteSpan ===================================================================================

    public void quote(boolean valid) {
        if (valid) {
            quoteValid();
        } else {
            quoteInvalid();
        }
    }

    protected void quoteValid() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (containQuote(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1; // \n
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            int quoteStart = 0;
            int quoteEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            }

            if (quoteStart < quoteEnd) {
                getEditableText().setSpan(new EditQuoteSpan(quoteColor, quoteStripeWidth, quoteGapWidth), quoteStart, quoteEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    protected void quoteInvalid() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (!containQuote(i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            int quoteStart = 0;
            int quoteEnd = 0;
            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            }

            if (quoteStart < quoteEnd) {
                QuoteSpan[] spans = getEditableText().getSpans(quoteStart, quoteEnd, QuoteSpan.class);
                for (QuoteSpan span : spans) {
                    getEditableText().removeSpan(span);
                }
            }
        }
    }

    protected boolean containQuote() {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart = lineStart + lines[j].length() + 1;
            }

            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }

            if (lineStart <= getSelectionStart() && getSelectionEnd() <= lineEnd) {
                list.add(i);
            } else if (getSelectionStart() <= lineStart && lineEnd <= getSelectionEnd()) {
                list.add(i);
            }
        }

        for (Integer i : list) {
            if (!containQuote(i)) {
                return false;
            }
        }

        return true;
    }

    protected boolean containQuote(int index) {
        String[] lines = TextUtils.split(getEditableText().toString(), "\n");
        if (index < 0 || index >= lines.length) {
            return false;
        }

        int start = 0;
        for (int i = 0; i < index; i++) {
            start = start + lines[i].length() + 1;
        }

        int end = start + lines[index].length();
        if (start >= end) {
            return false;
        }

        QuoteSpan[] spans = getEditableText().getSpans(start, end, QuoteSpan.class);
        return spans.length > 0;
    }

    // URLSpan =====================================================================================

    public void link(String link) {
        link(link, getSelectionStart(), getSelectionEnd());
    }

    // When KnifeText lose focus, use this method
    public void link(String link, int start, int end) {
        if (link != null && !TextUtils.isEmpty(link.trim())) {
            linkValid(link, start, end);
        } else {
            linkInvalid(start, end);
        }
    }

    protected void linkValid(String link, int start, int end) {
        if (start >= end) {
            return;
        }

        linkInvalid(start, end);
        getEditableText().setSpan(new EditURLSpan(link, linkColor, underLine), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    // Remove all span in selection, not like the boldInvalid()
    protected void linkInvalid(int start, int end) {
        if (start >= end) {
            return;
        }

        URLSpan[] spans = getEditableText().getSpans(start, end, URLSpan.class);
        for (URLSpan span : spans) {
            getEditableText().removeSpan(span);
        }
    }

    protected boolean containLink(int start, int end) {
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                URLSpan[] before = getEditableText().getSpans(start - 1, start, URLSpan.class);
                URLSpan[] after = getEditableText().getSpans(start, start + 1, URLSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();

            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, URLSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }

            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }


    public boolean contains(int format) {
        switch (format) {
            case FORMAT_BOLD:
                return containStyle(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
            case FORMAT_ITALIC:
                return containStyle(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
            default:
                return false;
        }
    }
}
