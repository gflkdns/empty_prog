package com.demo;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.com.empty.view.zoomimageview.MakeUpZoomImage;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private RecyclerView rv_list;
    private ImageView tv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MakeUpZoomImage.attach(this);

        rv_list = findViewById(R.id.rv_list);
        tv_image = findViewById(R.id.tv_image);
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MakeUpZoomImage.get().release();
    }

    private void initview() {
        Glide.with(this).load("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1560505693&di=44f52eb91a379d01f3567cd9f2fbc9d9&src=http://t-1.tuzhan.com/767f41fe2f0c/c-2/l/2013/08/06/17/70c0cc46f88f4768a7253bfcab5b049a.jpg").into(tv_image);
        List<String> data = new ArrayList<>();
        initdata(data);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setAdapter(new ImageAdapter(data, this));
    }

    private void initdata(List<String> data) {
        data.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2153937626,1074119156&fm=27&gp=0.jpg");
        data.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1519175636,579560806&fm=27&gp=0.jpg");
        data.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2033838781,461625559&fm=27&gp=0.jpg");
        data.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1457704519,3529830056&fm=27&gp=0.jpg");
        data.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1718395925,3485808025&fm=27&gp=0.jpg");
        data.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1483033257,4287748004&fm=27&gp=0.jpg");
        data.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2363037083,4182949652&fm=27&gp=0.jpg");
        data.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1413405362,1511835577&fm=27&gp=0.jpg");
        data.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2649246600,2667630149&fm=27&gp=0.jpg");
        data.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2204714399,1444377312&fm=27&gp=0.jpg");
    }
}
