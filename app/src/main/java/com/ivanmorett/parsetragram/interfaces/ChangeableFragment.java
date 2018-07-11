package com.ivanmorett.parsetragram.interfaces;

import com.parse.ParseUser;

public interface ChangeableFragment {
    void changeToUserFragment(ParseUser user);
    void changeToFeedFragment();
}
