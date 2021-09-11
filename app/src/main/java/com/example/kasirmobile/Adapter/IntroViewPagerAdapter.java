package com.example.kasirmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.kasirmobile.Model.ScreenItem;
import com.example.kasirmobile.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context context;
    List<ScreenItem> listScreen;

    public IntroViewPagerAdapter(Context context, List<ScreenItem> listScreen) {
        this.context = context;
        this.listScreen = listScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreenView = inflater.inflate(R.layout.layout_screen,null);

        ImageView ivImageSlide = layoutScreenView.findViewById(R.id.iv_intro);
        TextView tvTitle = layoutScreenView.findViewById(R.id.tv_intro_title);
        TextView tvDesc = layoutScreenView.findViewById(R.id.tv_intro_description);
        TextView tvSubDesc = layoutScreenView.findViewById(R.id.tv_intro_subdesc);

        tvTitle.setText(listScreen.get(position).getTitle());
        tvDesc.setText(listScreen.get(position).getDesc());
        tvSubDesc.setText(listScreen.get(position).getSubdesc());
        ivImageSlide.setImageResource(listScreen.get(position).getScreenImage());

        container.addView(layoutScreenView);

        return layoutScreenView;
    }

    @Override
    public int getCount() {
        return listScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
