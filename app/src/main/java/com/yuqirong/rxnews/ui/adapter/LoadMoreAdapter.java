package com.yuqirong.rxnews.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 尾view的类型
    private static final int TYPE_FOOTER = 1001;
    // 头view的类型
    private static final int TYPE_HEADER = 1002;

    private final RecyclerView.LayoutManager layoutManager;
    // 头ViewHolder
    protected FooterViewHolder mFooterViewHolder;
    // 尾ViewHolder
    protected HeaderViewHolder mHeaderViewHolder;
    /**
     * 头view
     */
    protected View mHeaderView;
    /**
     * 尾view
     */
    protected View mFooterView;

    public List<T> getList() {
        return list;
    }

    protected List<T> list = new ArrayList<>();

    /**
     * 设置头view
     * @param view
     */
    public void setHeaderView(View view) {
        this.mHeaderView = view;
    }

    /**
     * 设置尾view
     * @param view
     */
    public void setFooterView(View view) {
        this.mFooterView = view;
    }

    public LoadMoreAdapter(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        mFooterView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.layout_footer, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER && mHeaderView != null) {
            mHeaderViewHolder = new HeaderViewHolder(mHeaderView);
            return mHeaderViewHolder;
        } else if (viewType == TYPE_FOOTER && mFooterView != null) {
            mFooterViewHolder = new FooterViewHolder(mFooterView);
            return mFooterViewHolder;
        } else {
            return createCustomViewHolder(parent, viewType);
        }
    }

    public abstract RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType);

    public abstract void bindCustomViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mHeaderView != null && holder.getItemViewType() == TYPE_HEADER) {
            return;
        } else if (mFooterView != null && holder.getItemViewType() == TYPE_FOOTER) {
            final FooterViewHolder viewHolder = (FooterViewHolder) holder;
            if (layoutManager != null) {
                // 如果LayoutManager是StaggeredGridLayoutManager，则把footerViewHolder设置为fullspan
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    if (((StaggeredGridLayoutManager) layoutManager).getSpanCount() > 1) {
                        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                        if(layoutParams!=null){
                            layoutParams.setFullSpan(true);
                        }
                    }
                }
            } else if (layoutManager instanceof GridLayoutManager) {
                if (((GridLayoutManager) layoutManager)
                        .getSpanCount() != 1 && ((GridLayoutManager) layoutManager)
                        .getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                    throw new RuntimeException("网格布局列数大于1时应该继承SpanSizeLookup时处理底部加载时布局占满一行。");
                }
            }
        } else {
            if (mHeaderView != null) {
                position--;
            }
            bindCustomViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        int exViewNum = 0;
        //如果有头View
        if (mHeaderView != null) {
            exViewNum++;
        }
        //如果有尾View
        if (mFooterView != null) {
            exViewNum++;
        }
        return list.size() + exViewNum;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.itemView.getAnimation() != null && holder.itemView
                .getAnimation().hasStarted()) {
            holder.itemView.clearAnimation();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        } else if (mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            if (mHeaderView != null) {
                return super.getItemViewType(position - 1);
            }
            return super.getItemViewType(position);
        }
    }

    /**
     * 增加一条数据
     *
     * @param position
     * @param t
     */
    public void addData(int position, T t) {
        list.add(position, t);
        notifyItemInserted(position);
    }

    /**
     * 删除一条数据
     *
     * @param position
     */
    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清除recyclerview数据
     */
    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    //footer viewholder
    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

    }

    //header viewhoder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}
