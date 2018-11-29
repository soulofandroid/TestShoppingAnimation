package com.testshoppinganimation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    setAnim(ball, startLocation);// 开始执行动画
                    break;
            }
        }
    };
    private GridView gridview_zc;
    private List<Shopping> list=new ArrayList<>();
    private MyAdapter adapter;

    private TextView tv_zhongcai_hong;

    int totalCount;

    //dong动画:
    private  int[] startLocation;
    private ImageView ball;// 小圆点
    private ViewGroup anim_mask_layout;//动画层
    private RelativeLayout re_zhongcai_tanchu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initList();
    }

    private void initList() {
        for (int i = 0; i <10 ; i++) {
            Shopping shop=new Shopping();
            shop.setName("商品"+(i+1));
            shop.setPrice(100);
            shop.setCount(0);
            list.add(shop);
        }

        for (int i = 0; i <10 ; i++) {
            totalCount+=list.get(i).getCount();
        }

        adapter = new MyAdapter(list);
        adapter.notifyDataSetChanged();
        gridview_zc.setAdapter(adapter);
    }

    private void initView() {
        gridview_zc= (GridView) findViewById(R.id.gridview_zc);
        tv_zhongcai_hong= (TextView) findViewById(R.id.tv_zhongcai_hong);
        re_zhongcai_tanchu= (RelativeLayout) findViewById(R.id.re_zhongcai_tanchu);
    }

    class MyAdapter extends BaseAdapter {
        int a;
        int b;
        private List<Shopping> data;
        private ViewHolder viewholder;

        public MyAdapter(List<Shopping> data) {
            this.data = data;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null){
                viewholder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_zhongcai_gridview, null);
                viewholder.addbt = (TextView) convertView
                        .findViewById(R.id.addbt);
                viewholder.item_iv_muban= (ImageView) convertView.findViewById(R.id.item_iv_muban);
                viewholder.subbt = (TextView) convertView
                        .findViewById(R.id.subbt);
                viewholder.tv_shuzi = (TextView) convertView
                        .findViewById(R.id.tv_shuzi);
                viewholder.tv_show = (TextView) convertView
                        .findViewById(R.id.tv_show);
                convertView.setTag(viewholder);
            }else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (position%2==0){
                viewholder.item_iv_muban.setImageResource(R.mipmap.zhongcai_mubanzuo);
            }else {
                viewholder.item_iv_muban.setImageResource(R.mipmap.zhongcai_mubanyou);
            }
            viewholder.tv_shuzi.setText(data.get(position).getCount()+"");
            viewholder.tv_show.setText(list.get(position).getName());

            //减少
            viewholder.subbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //获得对应item在list中的总数
                   int count=data.get(position).getCount();

                    if (count > 0) {
                        data.get(position).setCount(count-1);
                        totalCount=totalCount-1;

                        if(totalCount==0){
                            tv_zhongcai_hong.setVisibility(View.INVISIBLE);
                        }else{
                            tv_zhongcai_hong.setVisibility(View.VISIBLE);
                        }
                        tv_zhongcai_hong.setText(totalCount + "");
                    }else if (count == 0) {
                        viewholder.subbt.setClickable(false);
                    } else {
                        Toast.makeText(getApplicationContext(),"不能为负数", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                }
            });

            // 增加
            viewholder.addbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                    view.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                    ball = new ImageView(MainActivity.this);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                    ball.setImageResource(R.mipmap.sign);// 设置buyImg的图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    }).start();

                    //获得对应item在list中的总数
                    int count=data.get(position).getCount();
                    data.get(position).setCount(count + 1);
                    totalCount=totalCount+1;
                    if (totalCount==0){
                        tv_zhongcai_hong.setVisibility(View.INVISIBLE);
                    }else{
                        tv_zhongcai_hong.setVisibility(View.VISIBLE);
                    }
                    tv_zhongcai_hong.setText(totalCount+"");
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
    public class ViewHolder {
        TextView tv_show;
        TextView subbt;
        TextView tv_shuzi;
        TextView addbt;
        ImageView item_iv_muban;
    }




    private void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v,
                startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        re_zhongcai_tanchu.getLocationInWindow(endLocation);// re_zhongcai_tanchu是那个抛物线最后掉落的控件

        // 计算位移
        int endX = 0 - startLocation[0] + 40;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        final AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
                //    Log.e("动画","asdasdasdasd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                set.cancel();
                animation.cancel();
            }
        });

    }

    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

}
