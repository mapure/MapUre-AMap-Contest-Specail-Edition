package com.mapure.amap.contest.ui.fagment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.ui.SNSEditActivity;
import com.tencent.mm.sdk.openapi.*;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.io.ByteArrayOutputStream;

/**
 * @author Izzy
 */

// 對應“分享至”的Fragment
public class SharingFragment extends DialogFragment {
    public static final String WX_APP_ID = "wx8d774f076c7c46a6";
    private static final int MIN_TL_SDK = 0x21020001;

    private IWXAPI api;

    public static SharingFragment getInstance() {
        return new SharingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        regToWX();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("分享到?");

        View view = inflater.inflate(R.layout.dialog_share_app, null);

        ImageView mWB = (ImageView) view.findViewById(R.id.weibo_icon);
        mWB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent();
                intent.setClass(getActivity(), SNSEditActivity.class);
                startActivity(intent);
            }
        });

        ImageView mWX = (ImageView) view.findViewById(R.id.wechat_icon);
        mWX.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (checkWX()) {
                    case -1:
                        dismiss();
                        Crouton.makeText(getActivity(),
                                "请安装微信", Style.ALERT).show();
                        break;
                    case -2:
                        dismiss();
                        Crouton.makeText(getActivity(),
                                "请更新微信", Style.ALERT).show();
                        break;
                    default:
                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = "http://wdxq.cnhubei.com/";

                        WXMediaMessage msg = new WXMediaMessage(webpage);
                        msg.title = getString(R.string.wx_msg_title);
                        msg.description = getString(R.string.wx_msg_description);

                        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_launcher);
                        msg.thumbData = bmpToByteArray(thumb, true);

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneSession;

                        api.sendReq(req);

                        dismiss();
                }

            }
        });

        ImageView mMM = (ImageView) view.findViewById(R.id.moments_icon);
        mMM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (checkWX()) {
                    case -1:
                        dismiss();
                        Crouton.makeText(getActivity(),
                                "请安装微信", Style.ALERT).show();
                        break;
                    case 0:
                    case -2:
                        dismiss();
                        Crouton.makeText(getActivity(),
                                "请更新微信", Style.ALERT).show();
                        break;
                    default:
                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = "http://wdxq.cnhubei.com/";

                        WXMediaMessage msg = new WXMediaMessage(webpage);
                        msg.title = getString(R.string.mm_msg_title);
                        msg.title = getString(R.string.wx_msg_title);
                        msg.description = getString(R.string.wx_msg_description);

                        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_launcher);
                        msg.thumbData = bmpToByteArray(thumb, true);

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;

                        api.sendReq(req);

                        dismiss();
                }

            }
        });

        return view;
    }

    private void regToWX() {
        api = WXAPIFactory.createWXAPI(getActivity(), WX_APP_ID, false);
        api.registerApp(WX_APP_ID);
    }

    private int checkWX() {
        if (!api.isWXAppInstalled()) {
            return -1;
        } else if (!api.isWXAppSupportAPI()) {
            return -2;
        } else if (api.getWXAppSupportAPI() < MIN_TL_SDK) {
            return 0;
        }

        return 1;
    }

    private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
