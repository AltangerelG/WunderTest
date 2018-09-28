package io.github.altangerelg.wundertest;

import android.view.View;

/**
 * Created by Altangerel on 2017-01-23.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
