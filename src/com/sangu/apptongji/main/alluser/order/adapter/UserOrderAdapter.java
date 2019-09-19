package com.sangu.apptongji.main.alluser.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public class UserOrderAdapter extends BaseAdapter{
    private List<OrderInfo> data=new ArrayList<>();
    private Context context;
    private LayoutInflater inflater=null;

    public UserOrderAdapter(List<OrderInfo> data, Context context) {
        this.data = data;
        this.context = context;
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (data!=null) {
            return data.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.list_item_consume,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_title_name);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tvOrderBody = (TextView) convertView.findViewById(R.id.tv__xiangmu_name);
            holder.tvOrderState = (TextView) convertView.findViewById(R.id.tv_dingdanzhuangtai);
            holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_jiaoyishijian);
            holder.tvTotalAmt = (TextView) convertView.findViewById(R.id.tv_balance);
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final OrderInfo orderInfo = (OrderInfo) getItem(position);
        String remark = orderInfo.getRemark();
        String merId = orderInfo.getMerId();
        if (merId.length()>12) {
            String url = FXConstant.URL_Get_QiyeInfo+merId;
            final ViewHolder finalHolder = holder;
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONObject jsonObject = object.getJSONObject("companyInfo");
                        QiYeInfo user2 = JSONParser.parseQiye(jsonObject);
                        String companyName = user2.getCompanyName();
                        String comImage = user2.getComImage();
                        final String companyId = user2.getCompanyId();
                        companyName = URLDecoder.decode(companyName, "UTF-8");
                        finalHolder.tvName.setText(companyName);
                        finalHolder.tvOrderBody.setText(TextUtils.isEmpty(orderInfo.getOrderBody()) ? "" : orderInfo.getOrderBody());
                        String orderTime = TextUtils.isEmpty(orderInfo.getOrderTime()) ? "" : orderInfo.getOrderTime();
                        orderTime = orderTime.substring(0, 4) + "-" + orderTime.substring(4, 6) + "-" + orderTime.substring(6, 8) + " "
                                + orderTime.substring(8, 10) + ":" + orderTime.substring(10, 12);
                        finalHolder.tvOrderTime.setText(orderTime);
                        String totalAmt = TextUtils.isEmpty(orderInfo.getTotalAmt()) ? "" : orderInfo.getTotalAmt();
                        if (totalAmt!=null&&!"".equals(totalAmt)) {
                            double once = Double.parseDouble(totalAmt);
                            String prices = String.format("%.2f", once);
                            finalHolder.tvTotalAmt.setText(prices+" 元");
                        }

                        String finalSum = TextUtils.isEmpty(orderInfo.getFinalSum()) ? "" : orderInfo.getFinalSum();
                        if (finalSum!=null&&!"".equals(finalSum)) {
                            double once = Double.parseDouble(finalSum);
                            String prices = String.format("%.2f", once);
                            finalHolder.tvTotalAmt.setText(prices+" 元");
                        }

                        String state = orderInfo.getOrderState();
                        String resv3 = orderInfo.getResv3();
                        if (resv3.equals("01")) {
                            if (state.equals("01")) {
                                finalHolder.tvOrderState.setText("待编辑");
                            } else if (state.equals("02")) {
                                finalHolder.tvOrderState.setText("待验资");
                            } else if (state.equals("03")) {
                                finalHolder.tvOrderState.setText("已验资");
                            } else if (state.equals("04")) {
                                finalHolder.tvOrderState.setText("待签收");
                            } else if (state.equals("05")) {
                                finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                finalHolder.tvOrderState.setText("交易成功");
                            } else if (state.equals("06")) {
                                finalHolder.tvOrderState.setText("拒绝签收");
                            } else if (state.equals("07")) {
                                finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                finalHolder.tvOrderState.setText("交易结束");
                            } else if (state.equals("08")) {
                                finalHolder.tvOrderState.setText("有争议");
                            } else if (state.equals("09")) {
                                finalHolder.tvOrderState.setText("平台介入中");
                            }else if (state.equals("10")){
                                finalHolder.tvOrderState.setText("申请售后");
                            }else if (state.equals("11")){
                                finalHolder.tvOrderState.setText("售后成功");
                                finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                            }
                        } else {
                            if (state.equals("03")) {
                                finalHolder.tvOrderState.setText("交易中");
                            } else if (state.equals("04")) {
                                finalHolder.tvOrderState.setText("待签收");
                            } else if (state.equals("05")) {
                                finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                finalHolder.tvOrderState.setText("交易成功");
                            } else if (state.equals("06")) {
                                finalHolder.tvOrderState.setText("拒绝签收");
                            } else if (state.equals("07")) {
                                finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                finalHolder.tvOrderState.setText("交易结束");
                            } else if (state.equals("08")) {
                                finalHolder.tvOrderState.setText("有争议");
                            } else if (state.equals("09")) {
                                finalHolder.tvOrderState.setText("平台介入中");
                            }else if (state.equals("10")){
                                finalHolder.tvOrderState.setText("申请售后");
                            }else if (state.equals("11")){
                                finalHolder.tvOrderState.setText("售后成功");
                                finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                            }
                        }
                        String head = TextUtils.isEmpty(comImage) ? "" : comImage;
                        if (head.length()>0) {
                            finalHolder.ivHead.setVisibility(View.VISIBLE);
                            finalHolder.tvTitleA.setVisibility(View.INVISIBLE);
                            String[] orderProjectArray = head.split("\\|");
                            head = orderProjectArray[0];
                            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + head,finalHolder.ivHead, DemoApplication.mOptions);
                            finalHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, QiYeDetailsActivity.class);
                                    intent.putExtra("qiyeId", companyId);
                                    context.startActivity(intent);
                                }
                            });
                        } else {
                            finalHolder.ivHead.setVisibility(View.INVISIBLE);
                            finalHolder.tvTitleA.setVisibility(View.VISIBLE);
                            finalHolder.tvTitleA.setText(companyName);
                            finalHolder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, QiYeDetailsActivity.class);
                                    intent.putExtra("qiyeId", companyId);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("userOrderAdapter",volleyError+"");
                }
            });
            MySingleton.getInstance(context).addToRequestQueue(request);
        }else {
            if ("".equals(remark)) {
                holder.tvName.setText(TextUtils.isEmpty(orderInfo.getM_name()) ? orderInfo.getMerId() : orderInfo.getM_name());
                holder.tvOrderBody.setText(TextUtils.isEmpty(orderInfo.getOrderBody()) ? "" : orderInfo.getOrderBody());
                String orderTime = TextUtils.isEmpty(orderInfo.getOrderTime()) ? "" : orderInfo.getOrderTime();
                orderTime = orderTime.substring(0, 4) + "-" + orderTime.substring(4, 6) + "-" + orderTime.substring(6, 8) + " "
                        + orderTime.substring(8, 10) + ":" + orderTime.substring(10, 12);
                holder.tvOrderTime.setText(orderTime);
                String totalAmt = TextUtils.isEmpty(orderInfo.getTotalAmt()) ? "" : orderInfo.getTotalAmt();
                if (totalAmt!=null&&!"".equals(totalAmt)) {
                    double once = Double.parseDouble(totalAmt);
                    String prices = String.format("%.2f", once);
                    holder.tvTotalAmt.setText(prices+" 元");
                }
                String finalSum = TextUtils.isEmpty(orderInfo.getFinalSum()) ? "" : orderInfo.getFinalSum();
                if (finalSum!=null&&!"".equals(finalSum)) {
                    double once = Double.parseDouble(finalSum);
                    String prices = String.format("%.2f", once);
                    holder.tvTotalAmt.setText(prices+" 元");
                }

                String state = orderInfo.getOrderState();
                String resv3 = orderInfo.getResv3();
                if (resv3.equals("01")) {
                    if (state.equals("01")) {
                        holder.tvOrderState.setText("待编辑");
                    } else if (state.equals("02")||state.equals("12")) {
                        holder.tvOrderState.setText("待验资");
                    } else if (state.equals("03")) {
                        holder.tvOrderState.setText("交易中");
                    } else if (state.equals("04")) {
                        holder.tvOrderState.setText("待签收");
                    } else if (state.equals("05")) {
                        holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                        holder.tvOrderState.setText("交易成功");
                    } else if (state.equals("06")) {
                        holder.tvOrderState.setText("申请退款");
                    } else if (state.equals("07")) {
                        holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                        holder.tvOrderState.setText("交易结束");
                    } else if (state.equals("08")) {
                        holder.tvOrderState.setText("有争议");
                    } else if (state.equals("09")) {
                        holder.tvOrderState.setText("平台介入中");
                    }else if (state.equals("10")){
                        holder.tvOrderState.setText("申请售后");
                    }else if (state.equals("11")){
                        holder.tvOrderState.setText("售后成功");
                        holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                    }
                } else {
                    if (state.equals("03")) {
                        holder.tvOrderState.setText("交易中");
                    } else if (state.equals("04")) {
                        holder.tvOrderState.setText("待签收");
                    } else if (state.equals("05")) {
                        holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                        holder.tvOrderState.setText("交易成功");
                    } else if (state.equals("06")) {
                        holder.tvOrderState.setText("申请退款");
                    } else if (state.equals("07")) {
                        holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                        holder.tvOrderState.setText("交易成功");
                    } else if (state.equals("08")) {
                        holder.tvOrderState.setText("拒绝退款");
                    } else if (state.equals("09")) {
                        holder.tvOrderState.setText("平台介入中");
                    }else if (state.equals("10")){
                        holder.tvOrderState.setText("申请售后");
                    }else if (state.equals("11")){
                        holder.tvOrderState.setText("售后成功");
                        holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                    }
                }
                String head = TextUtils.isEmpty(orderInfo.getM_uImage()) ? "" : orderInfo.getM_uImage();
                final String hxid = orderInfo.getMerId();
                if (head!=null&&!"".equals(head)) {
                    holder.ivHead.setVisibility(View.VISIBLE);
                    holder.tvTitleA.setVisibility(View.INVISIBLE);
                    String[] orderProjectArray = head.split("\\|");
                    head = orderProjectArray[0];
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head,holder.ivHead, DemoApplication.mOptions);
                    holder.ivHead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, UserDetailsActivity.class);
                            intent.putExtra(FXConstant.JSON_KEY_HXID, hxid);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.ivHead.setVisibility(View.INVISIBLE);
                    holder.tvTitleA.setVisibility(View.VISIBLE);
                    holder.tvTitleA.setText(TextUtils.isEmpty(orderInfo.getM_name()) ? orderInfo.getMerId() : orderInfo.getM_name());
                    holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, UserDetailsActivity.class);
                            intent.putExtra(FXConstant.JSON_KEY_HXID, hxid);
                            context.startActivity(intent);
                        }
                    });
                }
            }else {
                String url = FXConstant.URL_Get_QiyeInfo+remark;
                final ViewHolder finalHolder = holder;
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONObject jsonObject = object.getJSONObject("companyInfo");
                            QiYeInfo user2 = JSONParser.parseQiye(jsonObject);
                            String companyName = user2.getCompanyName();
                            String comImage = user2.getComImage();
                            final String companyId = user2.getCompanyId();
                            companyName = URLDecoder.decode(companyName, "UTF-8");
                            finalHolder.tvName.setText(companyName);
                            finalHolder.tvOrderBody.setText(TextUtils.isEmpty(orderInfo.getOrderBody()) ? "" : orderInfo.getOrderBody());
                            String orderTime = TextUtils.isEmpty(orderInfo.getOrderTime()) ? "" : orderInfo.getOrderTime();
                            orderTime = orderTime.substring(0, 4) + "-" + orderTime.substring(4, 6) + "-" + orderTime.substring(6, 8) + " "
                                    + orderTime.substring(8, 10) + ":" + orderTime.substring(10, 12);
                            finalHolder.tvOrderTime.setText(orderTime);
                            String totalAmt = TextUtils.isEmpty(orderInfo.getTotalAmt()) ? "" : orderInfo.getTotalAmt();
                            if (!(totalAmt == "")) {
                                totalAmt = totalAmt + " 元";
                            }
                            finalHolder.tvTotalAmt.setText(totalAmt);
                            String state = orderInfo.getOrderState();
                            String resv3 = orderInfo.getResv3();
                            if (resv3.equals("01")) {
                                if (state.equals("01")) {
                                    finalHolder.tvOrderState.setText("待编辑");
                                } else if (state.equals("02")) {
                                    finalHolder.tvOrderState.setText("待验资");
                                } else if (state.equals("03")) {
                                    finalHolder.tvOrderState.setText("交易中");
                                } else if (state.equals("04")) {
                                    finalHolder.tvOrderState.setText("待签收");
                                } else if (state.equals("05")) {
                                    finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                    finalHolder.tvOrderState.setText("交易成功");
                                } else if (state.equals("06")) {
                                    finalHolder.tvOrderState.setText("申请退款");
                                } else if (state.equals("07")) {
                                    finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                    finalHolder.tvOrderState.setText("交易结束");
                                } else if (state.equals("08")) {
                                    finalHolder.tvOrderState.setText("有争议");
                                } else if (state.equals("09")) {
                                    finalHolder.tvOrderState.setText("平台介入中");
                                }else if (state.equals("10")){
                                    finalHolder.tvOrderState.setText("申请售后");
                                }else if (state.equals("11")){
                                    finalHolder.tvOrderState.setText("售后成功");
                                    finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                }
                            } else {
                                if (state.equals("03")) {
                                    finalHolder.tvOrderState.setText("已验资");
                                } else if (state.equals("04")) {
                                    finalHolder.tvOrderState.setText("待签收");
                                } else if (state.equals("05")) {
                                    finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                    finalHolder.tvOrderState.setText("交易成功");
                                } else if (state.equals("06")) {
                                    finalHolder.tvOrderState.setText("申请退款");
                                } else if (state.equals("07")) {
                                    finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                    finalHolder.tvOrderState.setText("交易成功");
                                } else if (state.equals("08")) {
                                    finalHolder.tvOrderState.setText("拒绝退款");
                                } else if (state.equals("09")) {
                                    finalHolder.tvOrderState.setText("平台介入中");
                                }else if (state.equals("10")){
                                    finalHolder.tvOrderState.setText("申请售后");
                                }else if (state.equals("11")){
                                    finalHolder.tvOrderState.setText("售后成功");
                                    finalHolder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
                                }
                            }
                            String head = TextUtils.isEmpty(comImage) ? "" : comImage;
                            if (head.length()>0) {
                                finalHolder.ivHead.setVisibility(View.VISIBLE);
                                finalHolder.tvTitleA.setVisibility(View.INVISIBLE);
                                String[] orderProjectArray = head.split("\\|");
                                head = orderProjectArray[0];
                                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + head,finalHolder.ivHead, DemoApplication.mOptions);
                                finalHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, QiYeDetailsActivity.class);
                                        intent.putExtra("qiyeId", companyId);
                                        context.startActivity(intent);
                                    }
                                });
                            } else {
                                finalHolder.ivHead.setVisibility(View.INVISIBLE);
                                finalHolder.tvTitleA.setVisibility(View.VISIBLE);
                                finalHolder.tvTitleA.setText(companyName);
                                finalHolder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, QiYeDetailsActivity.class);
                                        intent.putExtra("qiyeId", companyId);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("userOrderAdapter",volleyError+"");
                    }
                });
                MySingleton.getInstance(context).addToRequestQueue(request);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName,tvTitleA;
        TextView tvOrderBody;
        TextView tvTotalAmt;
        TextView tvOrderTime;
        TextView tvOrderState;
        CircleImageView ivHead;
    }

}
