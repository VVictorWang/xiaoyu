package com.example.franklin.myclient.view.Contact;

import com.example.franklin.myclient.view.Contact.sortlist.SortModel;

import java.util.Comparator;

/**
 * Created by victor on 2017/4/23.
 */

public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}

