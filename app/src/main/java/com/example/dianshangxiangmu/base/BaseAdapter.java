package com.example.dianshangxiangmu.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {
    protected List<T> list;
    protected Context context;
    protected OnClickItem onClickItem;

    public BaseAdapter(List<T> list) {
        this.list = list;
        //设置局部刷新
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(getLayout(),parent,false);
        final BaseViewHolder baseViewHolder = new BaseViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItem!=null){
                    onClickItem.itenClick(baseViewHolder, baseViewHolder.getLayoutPosition());
                }
            }
        });
        return baseViewHolder;
    }

    protected abstract int getLayout();

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        T t = list.get(position);
        bindData(holder,position,t);
    }

    protected abstract void bindData(BaseViewHolder holder, int position, T t);

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 刷新数据
     * @param list
     */
    public void refresh(List<T> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     *
     * 分页加载数据的刷新
     * @param list
     */
    public void addData(List<T> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据（局部刷新，重写getItemId方法，同时setHasStableIds(true)）
     * @param datas
     */
    public void addDataWithoutAnim(List<T> datas){
        if(datas == null) return;
        int size = datas.size();
        this.list.addAll(datas);
        notifyItemChanged(size,datas.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{
        private SparseArray<View> myViewSparseArray;
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            myViewSparseArray = new SparseArray<>();
        }
        public View getView(int id){
            View view = myViewSparseArray.get(id);
            if(view == null){
                view = itemView.findViewById(id);
                myViewSparseArray.put(id,view);
            }
            return view;
        }
    }

    public interface OnClickItem{
        void itenClick(BaseViewHolder v, int pos);
    }

    public void setOnItemClick(OnClickItem onItemClick){
        this.onClickItem = onItemClick;
    }

}
