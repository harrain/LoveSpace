package com.example.lovespace.common.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.lovespace.R;
import com.netease.nim.uikit.ImageLoaderKit;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.nos.model.NosThumbParam;
import com.netease.nimlib.sdk.nos.util.NosThumbImageUtil;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by stephen on 2017/5/22.
 */

public class LoadBitmapUtil {

    public static final int DEFAULT_AVATAR_THUMB_SIZE = (int) NimUIKit.getContext().getResources().getDimension(R.dimen.avatar_max_size);

    public static void loadBuddyAvatar(String account,ImageView imageView){
        loadBuddyAvatar(account,DEFAULT_AVATAR_THUMB_SIZE,imageView);
    }

    /**
     * 加载用户头像（指定缩略大小）
     *
     * @param account
     * @param thumbSize 缩略图的宽、高
     */
    public static void loadBuddyAvatar(final String account, final int thumbSize, ImageView imageView) {
        // 先显示默认头像
        imageView.setImageResource(NimUIKit.getUserInfoProvider().getDefaultIconResId());

        // 判断是否需要ImageLoader加载
        final UserInfoProvider.UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        boolean needLoad = userInfo != null && ImageLoaderKit.isImageUriValid(userInfo.getAvatar());

        doLoadImage(imageView,needLoad, account, userInfo != null ? userInfo.getAvatar() : null, thumbSize);
    }

    /**
     * ImageLoader异步加载
     */
    public static void doLoadImage(final ImageView imageView, final boolean needLoad, final String tag, final String url, final int thumbSize) {
        if (needLoad) {
            imageView.setTag(tag); // 解决ViewHolder复用问题
            /**
             * 若使用网易云信云存储，这里可以设置下载图片的压缩尺寸，生成下载URL
             * 如果图片来源是非网易云信云存储，请不要使用NosThumbImageUtil
             */
            final String thumbUrl = makeAvatarThumbNosUrl(url, thumbSize);

            // 异步从cache or NOS加载图片
            ImageLoader.getInstance().displayImage(thumbUrl, new NonViewAware(new ImageSize(thumbSize, thumbSize),
                    ViewScaleType.CROP), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (imageView.getTag() != null && imageView.getTag().equals(tag)) {
                        imageView.setImageBitmap(loadedImage);
                    }
                }
            });
        } else {
            imageView.setTag(null);
        }
    }

    /**
     * 生成头像缩略图NOS URL地址（用作ImageLoader缓存的key）
     */
    private static String makeAvatarThumbNosUrl(final String url, final int thumbSize) {
        return thumbSize > 0 ? NosThumbImageUtil.makeImageThumbUrl(url, NosThumbParam.ThumbType.Crop, thumbSize, thumbSize) : url;
    }

    private static DisplayImageOptions options = createImageOptions();

    private static final DisplayImageOptions createImageOptions() {
        int defaultIcon = NimUIKit.getUserInfoProvider().getDefaultIconResId();
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultIcon)
                .showImageOnFail(defaultIcon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
