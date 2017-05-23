package com.aoliao.notebook.richeditor;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by 你的奥利奥 on 2017/1/28.
 */

public class EditURLSpan extends URLSpan {
    private int linkColor = 0;
    private boolean linkUnderline = true;

    public EditURLSpan(String url, int linkColor, boolean linkUnderline) {
        super(url);
        this.linkColor = linkColor;
        this.linkUnderline = linkUnderline;
    }

    public EditURLSpan(Parcel src) {
        super(src);
        this.linkColor = src.readInt();
        this.linkUnderline = src.readInt() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(linkColor);
        dest.writeInt(linkUnderline ? 1 : 0);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(linkColor != 0 ? linkColor : ds.linkColor);
        ds.setUnderlineText(linkUnderline);
    }
}