package com.dhavalsoneji.rssfeedreader.ui.listener;

import com.dhavalsoneji.rssfeedreader.model.Entry;

import java.util.List;

/**
 * Created by Dhaval Soneji Riontech on 2/12/16.
 */

public interface LoadCompleteListener {
    void display(List<Entry> list);
}
