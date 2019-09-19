package com.sangu.apptongji.main.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.CompanyRegistActivity;
import com.sangu.apptongji.main.utils.BitmapUtils;

/**
 * Created by Administrator on 2016-12-27.
 */

public class ContactQiyeFragment extends Fragment{
    private Button btn_create=null;
    private ImageView iv1,iv2,iv3;
    String qiyeId="",biaoshi;
    View v=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contact_qiye, container, false);
        iv1 = (ImageView) v.findViewById(R.id.iv1);
        iv2 = (ImageView) v.findViewById(R.id.iv2);
        iv3 = (ImageView) v.findViewById(R.id.iv3);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        biaoshi = "0";
        btn_create = (Button) v.findViewById(R.id.btn_create_company);
        Bitmap bm1 = BitmapUtils.readBitMap(getActivity(),R.drawable.qiye_one);
        iv1.setImageBitmap(bm1);
        Bitmap bm2 = BitmapUtils.readBitMap(getActivity(),R.drawable.qiye_two);
        iv2.setImageBitmap(bm2);
        Bitmap bm3 = BitmapUtils.readBitMap(getActivity(),R.drawable.qiye_three);
        iv3.setImageBitmap(bm3);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CompanyRegistActivity.class).putExtra("qiyeId",qiyeId).putExtra("biaoshi",biaoshi),0);
            }
        });
    }

    @Override
    public void onResume() {
        qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        Log.e("contactqiye,",qiyeId);
        if (qiyeId!=null&&!"".equals(qiyeId)) {
            biaoshi = "1";
        }
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
