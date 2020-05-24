package com.example.mytestapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestapp.R;
import com.example.mytestapp.entity.BaseItemEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<BaseItemEntity> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData(datas);
        initView();
    }

    public abstract void initData(List<BaseItemEntity> datas);

    public abstract  void onClickItem(int position, String value);

    public void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseListActivity.HomeRecycelAdapter homeRecycelAdapter = new BaseListActivity.HomeRecycelAdapter(datas);
        mRecyclerView.setAdapter(homeRecycelAdapter);
    }

    private class HomeRecycelAdapter extends RecyclerView.Adapter<BaseListActivity.HomeRecycelAdapter.HomeViewHolder> {

        private List<BaseItemEntity> mData;

        public HomeRecycelAdapter(List<BaseItemEntity> data) {
            mData = data;
        }

        @NonNull
        @Override
        public BaseListActivity.HomeRecycelAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View holderView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_home_recycel, parent, false);
            return new BaseListActivity.HomeRecycelAdapter.HomeViewHolder(holderView);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseListActivity.HomeRecycelAdapter.HomeViewHolder holder, int position) {
            holder.itemView.setOnClickListener(v -> {
                BaseItemEntity baseItemEntity = mData.get(position);
                onClickItem(position, baseItemEntity.getValue());
            });
            holder.titleView.setText(getItemData(position).getTitile());
            holder.titleView.setBackgroundResource(position % 2 == 0 ? R.color.colorPrimaryDark : R.color.colorPrimary);
        }

        private BaseItemEntity getItemData(int position) {
            return mData.get(position);
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        private class HomeViewHolder extends RecyclerView.ViewHolder {

            public TextView titleView;

            public HomeViewHolder(@NonNull View itemView) {
                super(itemView);
                titleView = itemView.findViewById(R.id.tvTitle);
            }
        }
    }
}
