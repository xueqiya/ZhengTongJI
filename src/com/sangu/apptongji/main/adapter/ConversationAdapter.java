package com.sangu.apptongji.main.adapter;

/**
 * Created by huangfangyi on 2016/7/13.\
 * QQ:84543217
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanxin.easeui.domain.EaseUser;
import com.fanxin.easeui.utils.EaseUserUtils;
import com.fanxin.easeui.widget.EaseConversationList;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.widget.CircleImageView;
import com.xiaomi.mimc.cipher.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * conversation list adapter
 */
public class ConversationAdapter extends BaseAdapter {
    private static final String TAG = "ChatAllHistoryAdapter";
    private List<JSONObject> conversationList;
    private List<EMConversation> copyConversationList;
    private List<Userful> users;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;
    private Context context;
    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;
    private static final int TYPE_0 = 0;
    private static final int TYPE_1 = 1;
    private static final int TYPE_2 = 2;
    private static final int TYPE_3 = 3;
    private static final int TYPE_4 = 4;
    private static final int TYPE_5 = 5;
    private static final int TYPE_6 = 6;

    public ConversationAdapter(Context context,int resource,List<JSONObject> objects,List<Userful> userfuls) {
       // super(context, resource);
        this.context = context;
        users = userfuls;
        conversationList = objects;

       // copyConversationList = new ArrayList<EMConversation>();
       // copyConversationList.addAll(objects);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Object getItem(int i) {
        return conversationList.get(i);
    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (position>=users.size()){
            return 0;
        }
        String type = users.get(position).getType();
        if (type==null||"".equals(type)||"0".equals(type)) {
            return 0;
        }else {
            if (type.equals("单聊")){
                return 0;
            }else if (type.equals("企业")) {
                return 1;
            }else {
                String imgUrl = users.get(position).getImage();
                int size = imgUrl.split("\\|").length;
                if (size<3){
                    return 2;
                }else {
                    return size;
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (position>=users.size()){
//            return null;
//        }
        int type=getItemViewType(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getViewByType(type, parent);
            if(type!=0){
                switch (type) {
                    case TYPE_1:
                        holder.iv_avatar1 = (CircleImageView) convertView.findViewById(R.id.iv_avatar1);
                        break;
                    case TYPE_2:
                        holder.iv_avatar1 = (CircleImageView) convertView.findViewById(R.id.iv_avatar1);
                        break;
                    case TYPE_3:
                        holder.iv_avatar1 = (CircleImageView) convertView.findViewById(R.id.iv_avatar1);
                        holder.iv_avatar2 = (CircleImageView) convertView.findViewById(R.id.iv_avatar2);
                    case TYPE_4:
                        holder.iv_avatar1 = (CircleImageView) convertView.findViewById(R.id.iv_avatar1);
                        holder.iv_avatar2 = (CircleImageView) convertView.findViewById(R.id.iv_avatar2);
                        holder.iv_avatar3 = (CircleImageView) convertView.findViewById(R.id.iv_avatar3);
                    case TYPE_5:
                        holder.iv_avatar1 = (CircleImageView) convertView.findViewById(R.id.iv_avatar1);
                        holder.iv_avatar2 = (CircleImageView) convertView.findViewById(R.id.iv_avatar2);
                        holder.iv_avatar3 = (CircleImageView) convertView.findViewById(R.id.iv_avatar3);
                        holder.iv_avatar4 = (CircleImageView) convertView.findViewById(R.id.iv_avatar4);
                    case TYPE_6:
                        holder.iv_avatar1 = (CircleImageView) convertView.findViewById(R.id.iv_avatar1);
                        holder.iv_avatar2 = (CircleImageView) convertView.findViewById(R.id.iv_avatar2);
                        holder.iv_avatar3 = (CircleImageView) convertView.findViewById(R.id.iv_avatar3);
                        holder.iv_avatar4 = (CircleImageView) convertView.findViewById(R.id.iv_avatar4);
                        holder.iv_avatar5 = (CircleImageView) convertView.findViewById(R.id.iv_avatar5);
                        break;
                }
            }else{
                holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            }
            holder.name = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.message);
            holder.time = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.time);
            holder.msgState = convertView.findViewById(com.hyphenate.easeui.R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(com.hyphenate.easeui.R.id.list_itease_layout);
            holder.motioned = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.mentioned);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();

        }*/
        holder.list_itease_layout.setBackgroundResource(com.hyphenate.easeui.R.drawable.ease_mm_listitem);
        // get conversation
     //   EMConversation conversation = getItem(position);


        /*
        // get username or group id
        Userful userful = null;
        final String username = conversation.conversationId();
        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            String groupId = conversation.conversationId();
            if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {
                holder.motioned.setVisibility(View.VISIBLE);
            } else {
                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar

            // holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            if (holder.iv_avatar1 != null) {
                holder.iv_avatar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, QiYeDetailsActivity.class).putExtra("qiyeId",username));
                    }
                });
            }

            //  holder.name.setText(group != null ? group.getGroupName() : username);
        } else if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
//            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
        } else {
//            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);

        }
*/
        JSONObject jsonObject = (JSONObject)getItem(position);

        try {

            JSONObject jsonObject1  = jsonObject.getJSONObject("lastMessage");

            String nameId = jsonObject.getString("name");

            String payload = jsonObject1.getString("payload");

            String str2 = new String(Base64.decode(payload));

            JSONObject object2 = new JSONObject(str2);

            JSONObject json = object2.getJSONObject("ext");
            String content = new String(Base64.decode(object2.getString("content")));

            String userInfo = json.getString(jsonObject.getString("name"));

            String[] userinfoStrs = userInfo.split("\\|");

            final String username = jsonObject.getString("name");
            EaseUserUtils.setUserNick(username, holder.name);
            if (holder.avatar != null) {
                holder.avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,username));
                    }
                });
            }


            String titleName = userinfoStrs[1];
//            if (username.length()>12){
//                try {
//                    titleName = URLDecoder.decode(titleName, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
            String avatar = userinfoStrs[0];
            holder.name.setText(titleName);
            if (type==0||type==1) {
                holder.name.setEllipsize(TextUtils.TruncateAt.END);
                if (username.length() > 12) {
                    if (avatar!=null&&!"".equals(avatar)) {
                        ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + avatar, holder.iv_avatar1, DemoApplication.mOptions);
                    }
                } else {
                    if (avatar!=null&&!"".equals(avatar)) {
                        avatar = avatar.split("\\|")[0];
                        ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, holder.avatar, DemoApplication.mOptions);
                    }
                }
            }else {
                holder.name.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                String myUserId = DemoHelper.getInstance().getCurrentUsernName();
                if (avatar!=null&&!"".equals(avatar)) {
                    String[] str = avatar.split("\\|");
                    List<String> urlList = new ArrayList<>();
                    switch (type) {
                        case 2:
                            if (str.length>0) {
                                urlList.add(str[0]);
                            }
                            if (str.length>1) {
                                urlList.add(str[1]);
                            }
                            for (int j=0;j<urlList.size();j++) {
                                if (urlList.get(j).indexOf(myUserId) != -1) {
                                    urlList.remove(j);
                                    break;
                                }
                            }
                            if (urlList.size() != 0) {
                                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), holder.iv_avatar1, DemoApplication.mOptions);
                            } else {

                            }

                            break;
                        case 3:
                            if (str.length>0) {
                                urlList.add(str[0]);
                            }
                            if (str.length>1) {
                                urlList.add(str[1]);
                            }
                            if (str.length>2) {
                                urlList.add(str[2]);
                            }
                            for (int j=0;j<urlList.size();j++){
                                if (urlList.get(j).indexOf(myUserId) != -1){
                                    urlList.remove(j);
                                    break;
                                }
                            }
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), holder.iv_avatar1, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), holder.iv_avatar2, DemoApplication.mOptions);
                            break;
                        case 4:
                            if (str.length>0) {
                                urlList.add(str[0]);
                            }
                            if (str.length>1) {
                                urlList.add(str[1]);
                            }
                            if (str.length>2) {
                                urlList.add(str[2]);
                            }
                            if (str.length>3) {
                                urlList.add(str[3]);
                            }
                            for (int j=0;j<urlList.size();j++){
                                if (urlList.get(j).indexOf(myUserId) != -1){
                                    urlList.remove(j);
                                    break;
                                }
                            }
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), holder.iv_avatar1, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), holder.iv_avatar2, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(2), holder.iv_avatar3, DemoApplication.mOptions);
                            break;
                        case 5:
                            if (str.length>0) {
                                urlList.add(str[0]);
                            }
                            if (str.length>1) {
                                urlList.add(str[1]);
                            }
                            if (str.length>2) {
                                urlList.add(str[2]);
                            }
                            if (str.length>3) {
                                urlList.add(str[3]);
                            }
                            if (str.length>4) {
                                urlList.add(str[4]);
                            }
                            for (int j=0;j<urlList.size();j++){
                                if (urlList.get(j).indexOf(myUserId) != -1){
                                    urlList.remove(j);
                                    break;
                                }
                            }
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), holder.iv_avatar1, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), holder.iv_avatar2, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(2), holder.iv_avatar3, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(3), holder.iv_avatar4, DemoApplication.mOptions);
                            break;
                        case 6:
                            if (str.length>0) {
                                urlList.add(str[0]);
                            }
                            if (str.length>1) {
                                urlList.add(str[1]);
                            }
                            if (str.length>2) {
                                urlList.add(str[2]);
                            }
                            if (str.length>3) {
                                urlList.add(str[3]);
                            }
                            if (str.length>4) {
                                urlList.add(str[4]);
                            }
                            if (str.length>5) {
                                urlList.add(str[5]);
                            }
                            for (int j=0;j<urlList.size();j++){
                                if (urlList.get(j).indexOf(myUserId) != -1){
                                    urlList.remove(j);
                                    break;
                                }
                            }
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), holder.iv_avatar1, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), holder.iv_avatar2, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(2), holder.iv_avatar3, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(3), holder.iv_avatar4, DemoApplication.mOptions);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(4), holder.iv_avatar5, DemoApplication.mOptions);
                            break;
                    }
                }
            }

            //内容
            holder.message.setText(content);

            SharedPreferences sp = context.getSharedPreferences("sangu_message_info", Context.MODE_PRIVATE);

            String count = sp.getString(username + "message","0");

            if (count.equals("0")){

                //无未读消息
                holder.unreadLabel.setVisibility(View.INVISIBLE);
                holder.unreadLabel.setText("");

            }else {

                //未读消息
                holder.unreadLabel.setVisibility(View.VISIBLE);
                holder.unreadLabel.setText(count);

            }


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date date = new Date(Long.valueOf(object2.getString("timestamp")));
            System.out.println(formatter.format(date));

            //时间
            holder.time.setText(formatter.format(date));

//        if (conversation.getUnreadMsgCount() > 0) {
//            // show unread message count
//            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
//            holder.unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            holder.unreadLabel.setVisibility(View.INVISIBLE);
//        }

        /*
        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            if (cvsListHelper != null) {
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
            try {
                String type1 = lastMessage.getStringAttribute("types");
                if ("名片".equals(type1)){
                    content = "[名片]";
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            holder.message.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                    TextView.BufferType.SPANNABLE);
            if (content != null) {
                holder.message.setText(content);
            }
            if (holder.message.getText().toString().startsWith("[Icon")||holder.message.getText().toString().startsWith("[示例")){
                holder.message.setText("[动态表情]");
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }
        */
            String shareRed = json.getString("shareRed");
            if ("有".equals(shareRed)){
                //消息列表聊天对象名字红色或者黑色显示 ，暂时先统一黑色  之前判断的字段是shareRed
                //holder.name.setTextColor(Color.rgb(255,99,71));
                holder.name.setTextColor(Color.rgb(53,53,53));
            }else {
                holder.name.setTextColor(Color.rgb(53,53,53));
            }
//        //set property
//        holder.name.setTextColor(primaryColor);
//        holder.message.setTextColor(secondaryColor);
//        holder.time.setTextColor(timeColor);
//        if (primarySize != 0)
//            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
//        if (secondarySize != 0)
//            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
//        if (timeSize != 0)
//            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyConversationList.clear();
           // copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

//    @Override
//    public Filter getFilter() {
//        if (conversationFilter == null) {
//            conversationFilter = new ConversationFilter(conversationList);
//        }
//        return conversationFilter;
//    }


    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }

    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.conversationId();

                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    if (group != null) {
                        username = group.getGroupName();
                    } else {
                        EaseUser user = EaseUserUtils.getUserInfo(username);
                        // TODO: not support Nick anymore
//                        if(user != null && user.getNick() != null)
//                            username = user.getNick();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            if (results.values != null) {
              //  conversationList.addAll((List<EMConversation>) results.values);
            }
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private EaseConversationList.EaseConversationListHelper cvsListHelper;

    public void setCvsListHelper(EaseConversationList.EaseConversationListHelper cvsListHelper) {
        this.cvsListHelper = cvsListHelper;
    }

    private static class ViewHolder {
        /**
         * who you chat with
         */
        TextView name;
        /**
         * unread message count
         */
        TextView unreadLabel;
        /**
         * content of last message
         */
        TextView message;
        /**
         * time of last message
         */
        TextView time;
        /**
         * avatar
         */
        CircleImageView avatar;
        /**
         * status of last message
         */
        View msgState;
        /**
         * layout
         */
        RelativeLayout list_itease_layout;
        TextView motioned;
        CircleImageView iv_avatar1;
        CircleImageView iv_avatar2;
        CircleImageView iv_avatar3;
        CircleImageView iv_avatar4;
        CircleImageView iv_avatar5;
    }

    private View getViewByType(int type, ViewGroup parent) {
        if (type == 0) {
            return LayoutInflater.from(context).inflate(R.layout.fx_item_conversation_single, null, false);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.fx_item_conversation_group, null, false);
            RelativeLayout avatarView = (RelativeLayout) view.findViewById(R.id.re_avatar);
            avatarView.addView(creatAvatarView(type));
            return view;
        }
    }

    private View creatAvatarView(int type) {
        switch (type) {
            case 1:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar1, null, false);
            case 2:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar1, null, false);
            case 3:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar2, null, false);
            case 4:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar3, null, false);
            case 5:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar4, null, false);
            case 6:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar5, null, false);
            default:
                return LayoutInflater.from(context).inflate(R.layout.fx_group_avatar1, null, false);
        }

    }
}