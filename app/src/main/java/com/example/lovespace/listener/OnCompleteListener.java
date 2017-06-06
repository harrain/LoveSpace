package com.example.lovespace.listener;

/**
 * Created by stephen on 2017/6/3.
 */

public interface OnCompleteListener<T> {

     void onSuccess(T data);
     void onFailure(Exception e);
}
