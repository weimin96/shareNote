package com.aoliao.notebook.richeditor;

/**
 * Created by 你的奥利奥 on 2017/1/26.
 */

public class EditSpanPart {
    private int start;
    private int end;

    EditSpanPart(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isValid() {
        return start < end;
    }
}
