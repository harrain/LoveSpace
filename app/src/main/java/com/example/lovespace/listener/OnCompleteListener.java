package com.example.lovespace.listener;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by stephen on 2017/6/3.
 */

public interface OnCompleteListener<T> {

     void onSuccess(T data);
     void onFailure(BmobException e);
}
