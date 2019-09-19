package com.sangu.apptongji.main.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-10-25.
 */

public class FriendCheckAdapter extends RecyclerView.Adapter<FriendCheckAdapter.ViewHolder> implements Filterable {
    private List<UserAll> data;
    private List<UserAll> selected = new ArrayList<>();
    private Context context;
    private PersonFilter filter;
    private Map<Integer, Boolean> map = new HashMap<>();
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;
    private String type = "";

    public FriendCheckAdapter(List<UserAll> data, Context context, String type) {
        super();
        this.data = data;
        this.context = context;
        this.type = type;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < data.size(); i++) {
            map.put(i, false);
        }
    }

    public List<UserAll> getDate(){
        return data;
    }

    public void appendData(List<UserAll> list, Context context) {//分页关键
        this.data.addAll(list);
        this.context = context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_fragment,parent,false),mItemClickListener,mItemLongClickListener);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }

    private class PersonFilter extends Filter {

        private List<UserAll> original;

        public PersonFilter(List<UserAll> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = original;
                results.count = original.size();
            } else {
                List<UserAll> mList = new ArrayList<UserAll>();
                for (UserAll p: original) {
                    if (p.getuName().toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || new String(p.getuName() + "").toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        mList.add(p);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            data = (List<UserAll>)results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(true);
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        final UserAll allUser = data.get(position);
        final String deviceType = allUser.getDeviceType();
        final String loginId = allUser.getuLoginId();
        holder.root.setTag(position);
        if (data.get(0).getMyType() != null && "16".equalsIgnoreCase(data.get(0).getMyType())) {

        } else {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (deviceType==null||"".equals(deviceType)){
                        holder.cb_select.setChecked(false);
                        showDxTzh(loginId);
                    }else {
                        String str=null;
                        if (deviceType.startsWith("iOS")) {
                            str = deviceType.substring(3, 8);
                        }else if (deviceType.startsWith("android")){
                            str = deviceType.substring(7, 12);
                        }
                        Log.e("friendchad,str",str);
                        if (!"3.0.1".equals(str)&&!"3.0.0".equals(str)) {
                            //用map集合保存
                            if (map.get(position)) {
                                holder.cb_select.setChecked(false);
                            } else {
                                holder.cb_select.setChecked(true);
                            }
                        } else {
                            holder.cb_select.setChecked(false);
                            showDxTzh(loginId);
                        }
                    }
                }
            });
        }

        holder.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (data.get(0).getMyType() != null && "16".equalsIgnoreCase(data.get(0).getMyType())) {
                    if (isChecked) {
                        selected.add(allUser);
                    } else {
                        selected.remove(allUser);
                    }
                    return;
                }
                if (deviceType==null||"".equals(deviceType)){
                    holder.cb_select.setChecked(false);
                    showDxTzh(loginId);
                }else {
                    String str=null;
                    if (deviceType.startsWith("iOS")) {
                        str = deviceType.substring(3, 8);
                    }else if (deviceType.startsWith("android")){
                        str = deviceType.substring(7, 12);
                    }
                    if (!"3.0.1".equals(str)&&!"3.0.0".equals(str)) {
                        //用map集合保存
                        map.put(position, isChecked);
                        if (isChecked) {
                            selected.add(allUser);
                        } else {
                            selected.remove(allUser);
                        }
                    } else {
                        holder.cb_select.setChecked(false);
                        showDxTzh(loginId);
                    }
                }
            }
        });
        holder.cb_select.setChecked(map.get(position));
        if (allUser == null || data.size() == 0) {
            data.remove(position);
            this.notifyDataSetChanged();
        }
        String resv4 = allUser.getResv4();
        holder.tvName.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
        String shareRed = allUser.getShareRed();
        String friendsNumber = allUser.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            holder.tvName.setTextColor(Color.RED);
        }else {
            holder.tvName.setTextColor(Color.BLACK);
        }
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
        holder.tvNianLing.setText(nianLing);
        if (allUser.getMyType() != null && "16".equalsIgnoreCase(allUser.getMyType())) {
            holder.tv_company_count.setVisibility(View.INVISIBLE);
            holder.tvCompany.setText("联系方式： " + allUser.getuId());
        } else {
            String company = TextUtils.isEmpty(allUser.getuCompany()) ? "暂未加入企业" : allUser.getuCompany();
            String uNation = allUser.getuNation();
            String resv5 = allUser.getResv5();
            String resv6 = allUser.getResv6();
            if (company == null || company.equals("")) {
                company = "暂未加入企业";
            }
            if ("00".equals(resv6)&&!"1".equals(uNation)){
                company = "暂未加入企业";
            }
            if (resv5==null||"".equals(resv5)){
                company = "暂未加入企业";
            }
            try {
                company = URLDecoder.decode(company, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String member = allUser.getMemberNumber();
            if (member==null||"".equals(member)){
                member = "0";
            }
            if (!company.equals("暂未加入企业")){
                holder.tv_company_count.setVisibility(View.VISIBLE);
            }else {
                holder.tv_company_count.setVisibility(View.INVISIBLE);
            }
            holder.tv_company_count.setText("("+member+"人"+")");
            holder.tvCompany.setText(company);
        }

        holder.tvProject1.setText(allUser.getpL1());
        holder.tvProject2.setText(allUser.getpL2());
        holder.tvProject3.setText(allUser.getpL3());
        holder.tvProject4.setText(allUser.getpL4());
        String image1 = allUser.getImage1();
        String image2 = allUser.getImage2();
        String image3 = allUser.getImage3();
        String image4 = allUser.getImage4();
        String margan1 = allUser.getMargen1();
        String margan2 = allUser.getMargen2();
        String margan3 = allUser.getMargen3();
        String margan4 = allUser.getMargen4();
        if (allUser.getpL1()==null||allUser.getpL1().equals("")){
            holder.ll_one.setVisibility(View.GONE);
        }else {
            holder.ll_one.setVisibility(View.VISIBLE);
        }
        if (allUser.getpL2()==null||allUser.getpL2().equals("")){
            holder.ll_two.setVisibility(View.GONE);
        }else {
            holder.ll_two.setVisibility(View.VISIBLE);
        }
        if (allUser.getpL3()==null||allUser.getpL3().equals("")){
            holder.ll_three.setVisibility(View.GONE);
        }else {
            holder.ll_three.setVisibility(View.VISIBLE);
        }
        if (allUser.getpL4()==null||allUser.getpL4().equals("")){
            holder.ll_four.setVisibility(View.GONE);
        }else {
            holder.ll_four.setVisibility(View.VISIBLE);
        }
        if (image1!=null&&!"".equals(image1)) {
            holder.ivZYTP1.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP1.setVisibility(View.INVISIBLE);
        }
        if (margan1!=null&&!"".equals(margan1) && Double.valueOf(margan1) > 0) {
            holder.tvBao1.setVisibility(View.VISIBLE);
        }else {
            holder.tvBao1.setVisibility(View.INVISIBLE);
        }
        if (image2!=null&&!"".equals(image2)) {
            holder.ivZYTP2.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP2.setVisibility(View.INVISIBLE);
        }
        if (margan2!=null&&!"".equals(margan2) && Double.valueOf(margan2) > 0) {
            holder.tvBao2.setVisibility(View.VISIBLE);
        }else {
            holder.tvBao2.setVisibility(View.INVISIBLE);
        }
        if (image3!=null&&!"".equals(image3)) {
            holder.ivZYTP3.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP3.setVisibility(View.INVISIBLE);
        }
        if (margan3!=null&&!"".equals(margan3) && Double.valueOf(margan3) > 0) {
            holder.tvBao3.setVisibility(View.VISIBLE);
        }else {
            holder.tvBao3.setVisibility(View.INVISIBLE);
        }
        if (image4!=null&&!"".equals(image4)) {
            holder.ivZYTP4.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP4.setVisibility(View.INVISIBLE);
        }
        if (margan4!=null&&!"".equals(margan4) && Double.valueOf(margan4) > 0) {
            holder.tvBao4.setVisibility(View.VISIBLE);
        }else {
            holder.tvBao4.setVisibility(View.INVISIBLE);
        }
        if ("00".equals(allUser.getuSex())) {
            holder.ivSex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            holder.tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            holder.ivSex.setImageResource(R.drawable.nan);
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        String head = TextUtils.isEmpty(allUser.getuImage()) ? "" : allUser.getuImage();
        if (head.length() > 40) {
            holder.tvTitleA.setVisibility(View.INVISIBLE);
            holder.ivHead.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if ("00".equals(resv4)){
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gray);
            holder.tvTitleA.setText("下线");
        }else {
            if (!(head == null || head.equals(""))) {
                holder.tvTitleA.setVisibility(View.INVISIBLE);
                holder.ivHead.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head, holder.ivHead, DemoApplication.mOptions);
            } else {
                holder.ivHead.setVisibility(View.INVISIBLE);
                holder.tvTitleA.setVisibility(View.VISIBLE);
                holder.tvTitleA.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
            }
        }
        String locationState = allUser.getLocationState();
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "34.762711" : DemoApplication.getInstance().getCurrentLat());
        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "113.744531" : DemoApplication.getInstance().getCurrentLng());
        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
            final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
            LatLng ll = new LatLng(latitude1,longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.2f",dou);//format 返回的是字符串
            if (str!=null&&dou>=10000){
                holder.tvDistance.setText("隐藏");
            }else {
                holder.tvDistance.setText(str + "km");
            }
        } else {
            holder.tvDistance.setText("3km之内");
        }
        String sign = allUser.getuSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        holder.tvQianming.setText(sign);
        if (locationState.equals("00")){
            holder.tvDistance.setText("隐藏");
        }

        if (allUser.getMyType() != null && "16".equalsIgnoreCase(allUser.getMyType())) {
            holder.ivHead.setVisibility(View.VISIBLE);
            holder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("chen", "我被点击了");
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,allUser.getuId()));
                }
            });
        }
    }

    private void showDxTzh(final String loginId) {
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = id;
        }
        LayoutInflater inflater1 = LayoutInflater.from(context);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(context,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("确定");
        btnCancel1.setText("取消");
        title_tv1.setText("该用户不是最新版本，无法加入群聊!");
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        final String finalName = name;
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
//                final String url = FXConstant.URL_DUANXIN_TONGZHI;
//                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        Toast.makeText(context,"通知成功，等待对方回复！",Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(context,"通知失败，网络错误！",Toast.LENGTH_SHORT).show();
//                    }
//                }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String,String> param = new HashMap<>();
//                        param.put("message","【正事多】通知：用户"+ finalName +"邀请您升级最新版本加入群组聊天,建议您及时更新");
//                        param.put("telNum", loginId);
//                        Log.e("friendch,p",param.toString());
//                        return param;
//                    }
//                };
//                MySingleton.getInstance(context).addToRequestQueue(request);
            }
        });
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PersonFilter(data);
        }
        return filter;
    }

    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    public List<UserAll> getselectedList(){
        return selected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        View root;
        RelativeLayout rl_sex;
        CheckBox cb_select;
        TextView tvName,tvNianLing;
        TextView tvCompany,tv_company_count;
        TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
        TextView tvDistance;
        ImageView ivSex;
        CircleImageView ivHead;
        TextView tvProject1,tvProject2,tvProject3,tvProject4;
        TextView tvQianming;
        LinearLayout ll_one,ll_two,ll_three,ll_four;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);
            this.root = convertView;
            cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
            if ("1".equalsIgnoreCase(type)) {
                cb_select.setVisibility(View.INVISIBLE);
            }
            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            ll_three = (LinearLayout) convertView.findViewById(R.id.ll_three);
            ll_four = (LinearLayout) convertView.findViewById(R.id.ll_four);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            tvBao1 = (TextView) convertView.findViewById(R.id.tv_zy1_bao);
            tvBao2 = (TextView) convertView.findViewById(R.id.tv_zy2_bao);
            tvBao3 = (TextView) convertView.findViewById(R.id.tv_zy3_bao);
            tvBao4 = (TextView) convertView.findViewById(R.id.tv_zy4_bao);
            ivZYTP1 = (TextView) convertView.findViewById(R.id.iv_zy1_tupian);
            ivZYTP2 = (TextView) convertView.findViewById(R.id.iv_zy2_tupian);
            ivZYTP3 = (TextView) convertView.findViewById(R.id.iv_zy3_tupian);
            ivZYTP4 = (TextView) convertView.findViewById(R.id.iv_zy4_tupian);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tv_company_count = (TextView) convertView.findViewById(R.id.tv_company_count);
            //holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
            ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            tvQianming = (TextView) convertView.findViewById(R.id.tv_qianming);
            tvProject1 = (TextView) convertView.findViewById(R.id.tv_project_one);
            tvProject2 = (TextView) convertView.findViewById(R.id.tv_project_two);
            tvProject3 = (TextView) convertView.findViewById(R.id.tv_project_three);
            tvProject4 = (TextView) convertView.findViewById(R.id.tv_project_four);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            this.mListener=listener;
            this.mLongClickListener=longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null){
                mLongClickListener.onItemLongClick(v,getPosition());
            }
            return true;
        }
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
