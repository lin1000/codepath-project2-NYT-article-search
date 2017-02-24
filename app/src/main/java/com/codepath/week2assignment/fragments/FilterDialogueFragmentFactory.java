package com.codepath.week2assignment.fragments;

/**
 * Created by lin1000 on 2017/2/24.
 */

public class FilterDialogueFragmentFactory {

    private static FilterDialogueFragment instance;

    private FilterDialogueFragmentFactory(){}

    public static FilterDialogueFragment getInstance(){
        if(instance==null){
            synchronized (FilterDialogueFragmentFactory.class){
                if(instance==null){
                    instance = new FilterDialogueFragment();
                }
            }
        }
        return instance;
    }
}
