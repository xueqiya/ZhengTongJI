package com.fanxin.easeui.widget.chatrow;

import java.io.File;

import com.fanxin.easeui.utils.EaseCommonUtils;
import com.fanxin.easeui.utils.EaseImageUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.R;
import com.fanxin.easeui.model.EaseImageCache;
import com.fanxin.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.exceptions.HyphenateException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EaseChatRowImage extends EaseChatRowFile{

    protected ImageView imageView;
    private EMImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }


    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody) message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageView.setImageResource(R.drawable.ease_default_image);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                String thumbPath = imgBody.thumbnailLocalPath();
                if (!new File(thumbPath).exists()) {
                    // to make it compatible with thumbnail received in previous version
                    thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                }
                showImageView(thumbPath, imageView, imgBody.getLocalUrl(), message);
            }
            return;
        }

        String filePath = imgBody.getLocalUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
        showImageView(thumbPath, imageView, filePath, message);
        handleSendMessage();
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        String type = null;
        try {
            type = message.getStringAttribute("types");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        if ((type==null||"".equals(type))) {
            Intent intent = new Intent(context, EaseShowBigImageActivity.class);
            File file = new File(imgBody.getLocalUrl());
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                intent.putExtra("uri", uri);
            } else {
                // The local full size pic does not exist yet.
                // ShowBigImage needs to download it from the server
                // first
                String msgId = message.getMsgId();
                intent.putExtra("messageId", msgId);
                intent.putExtra("localUrl", imgBody.getLocalUrl());
            }
            if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                    && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            context.startActivity(intent);
        }else {
            String mingPianId = null,flg=null,merId=null,userId=null;
            try {
                mingPianId = message.getStringAttribute("shareId");
                flg = message.getStringAttribute("flg");
                merId = message.getStringAttribute("merId");
                userId = message.getStringAttribute("userId");
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            if (mingPianId!=null){
                Intent intent = new Intent();
                intent.setAction("com.tiaozhuan");
                intent.putExtra("mingPianId",mingPianId);
                intent.putExtra("types",type);
                intent.putExtra("userId",userId);
                intent.putExtra("merId",merId);
                intent.putExtra("flg",flg);
                context.sendBroadcast(intent);
            }
        }
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @param
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath,final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
                    }
                    else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        EMClient.getInstance().chatManager().downloadThumbnail(message);
                                    }
                                }).start();
                            }
                        }
                    }
                }
            }.execute();

            return true;
        }
    }

}
