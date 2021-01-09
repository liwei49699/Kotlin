package com.liwei.kotlin.snapshot;


import com.liwei.kotlin.R;
import com.liwei.kotlin.snapshot.utils.ShareTypeEnum;

/**
 * # **********************************************************************************************
 * # ClassName:      ShareModel
 * # Description:    分享数据model，布局时使用
 * # Author:         dzzhao.summer
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    dzzhao.summer     2018/12/19 15:44
 * # Modifications:  initial
 * # **********************************************************************************************
 */

public class ShareModel {
    /**
     * 分享渠道名
     */
    public String name;
    /**
     * 分享渠道图片资源id
     */
    public int imageResId;
    /**
     * 分享类型
     */
    public ShareTypeEnum shareType;

    public ShareModel(ShareTypeEnum type) {
        this.shareType = type;
        switch (type) {
//            case SHARE_WECHAT:
//                name = "微信";
//                imageResId = R.drawable.adf_icon_share_weixin_friend;
//                break;
//            case SHARE_WECHAT_MOMENTS:
//                imageResId = R.drawable.adf_icon_share_weixin_moments;
//                name = "朋友圈";
//                break;
//            case SHARE_WECHAT_FAVORITE:
//                imageResId = R.drawable.adf_icon_share_weixin_favorite;
//                name = "微信收藏";
//                break;
//            case SHARE_SINA_WEIBO:
//                imageResId = R.drawable.adf_icon_share_weibo;
//                name = "微博";
//                break;
//            case SHARE_QQ:
//                imageResId = R.drawable.adf_icon_share_qq;
//                name = "QQ";
//                break;
//            case SHARE_QQ_ZONE:
//                imageResId = R.drawable.adf_icon_share_qqzone;
//                name = "QQ空间";
//                break;
//            case SHARE_EMAIL:
//                imageResId = R.drawable.adf_icon_share_email;
//                name = "邮件";
//                break;
//            case SHARE_MESSAGE:
//                imageResId = R.drawable.adf_icon_share_msg;
//                name = "短信";
//                break;
//            case SHARE_ALBUM:
//                imageResId = R.drawable.adf_icon_share_save;
//                name = "保存相册";
//                break;
//            case SHARE_MORE:
//                imageResId = R.drawable.adf_icon_share_more;
//                name = "更多";
//                break;
//            case SHARE_COPY_URL:
//                imageResId = R.drawable.adf_icon_copy_url;
//                name = "复制链接";
//                break;
//            case SHARE_REFRESH:
//                imageResId = R.drawable.adf_icon_share_refresh;
//                name = "刷新";
//                break;
            default:
                this.shareType = ShareTypeEnum.SHARE_TYPE_ERROR;
                break;
        }
    }

}
