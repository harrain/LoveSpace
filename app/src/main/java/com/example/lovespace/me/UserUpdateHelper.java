package com.example.lovespace.me;/**
 * Created by stephen on 2017/5/13.
 */

import android.widget.Toast;

import com.example.lovespace.DemoCache;
import com.example.lovespace.R;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 包名： com.example.lovespace.me
 * 类名:  UserUpdateHelper
 * 类功能：
 * 创建者：  stephen
 * 创建日期: 2017/5/13
 */
public class UserUpdateHelper {

    private static final String TAG = UserUpdateHelper.class.getSimpleName();

    /**
     * 更新用户资料
     */
    public static void update(final UserInfoFieldEnum field, final Object value, RequestCallbackWrapper<Void> callback) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(field, value);
        update(fields, callback);
    }

    private static void update(final Map<UserInfoFieldEnum, Object> fields, final RequestCallbackWrapper<Void> callback) {
        NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {

                if (code == ResponseCode.RES_SUCCESS) {
                    LogUtil.i(TAG, "update userInfo success, update fields count=" + fields.size());
                } else {
                    if (exception != null) {
                        Toast.makeText(DemoCache.getContext(), R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                        LogUtil.i(TAG, "update userInfo failed, exception=" + exception.getMessage());
                    }
                }
                if (callback != null) {
                    callback.onResult(code, result, exception);
                }
            }
        });
    }
}
