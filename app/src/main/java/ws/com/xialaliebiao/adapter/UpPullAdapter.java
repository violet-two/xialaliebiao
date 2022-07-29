package ws.com.xialaliebiao.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ws.com.xialaliebiao.R;
import ws.com.xialaliebiao.bean.ItemBean;

public class UpPullAdapter extends ListViewAdapter{
    //普通的类型
    public static final int TYPE_NORMAL = 0;
    //加载更多
    public static final int TYPE_LOADER_MODE = 1;

    private OnRefreshListener  mUpPullRefreshListener;
    public boolean first ;

    public UpPullAdapter(List<ItemBean> mData,Boolean first) {
        super(mData);
        this.mData = mData;
        this.first = first;
    }
    public void IsFirst(Boolean first){
        this.first = first;
    }


    @Override
    protected View getSubView(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_NORMAL) {
            view = View.inflate(parent.getContext(), R.layout.item_list_view, null);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_list_load_more, null);
        }
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_NORMAL&&holder instanceof InnerHolder){
            ((InnerHolder) holder).setData(mData.get(position), position);
        }else if(getItemViewType(position)==TYPE_LOADER_MODE&&holder instanceof loaderMoreHolder){
            ((loaderMoreHolder) holder).update(loaderMoreHolder.LOADER_SATE_LOADING);
        }

    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        if (viewType == TYPE_NORMAL) {
            return new InnerHolder(view);
        } else {
            loaderMoreHolder loaderMoreHolder = new loaderMoreHolder(view);
            loaderMoreHolder.update(UpPullAdapter.loaderMoreHolder.LOADER_SATE_LOADING);
            return loaderMoreHolder;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1) {
            return TYPE_LOADER_MODE;
        }
        return TYPE_NORMAL;
    }


    //设置刷新监听接口
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mUpPullRefreshListener = listener;
    }

    // 定义接口
    public interface OnRefreshListener{
        void onPullRefresh(loaderMoreHolder loaderMoreHolder);
    }


    public class loaderMoreHolder extends RecyclerView.ViewHolder {

        public static final int LOADER_SATE_LOADING = 0;
        public static final int LOADER_SATE_RELOAD = 1;
        public static final int LOADER_SATE_NORMAL = 2;

        private final LinearLayout loding;
        private final TextView reload;

        public loaderMoreHolder(@NonNull View itemView) {
            super(itemView);
            loding = itemView.findViewById(R.id.loading);
            reload = itemView.findViewById(R.id.reloading);
            reload.setOnClickListener(view -> {
                update(LOADER_SATE_LOADING);
            });
        }

        public void update(int state) {
            //重置2个控件
            loding.setVisibility(View.GONE);
            reload.setVisibility(View.GONE);
            switch (state) {
                case LOADER_SATE_LOADING:
                    loding.setVisibility(View.VISIBLE);
                    startLoaderMore();
                    break;
                case LOADER_SATE_RELOAD:
                    reload.setVisibility(View.VISIBLE);
                    break;
                case LOADER_SATE_NORMAL:
                    loding.setVisibility(View.GONE);
                    reload.setVisibility(View.GONE);
                    break;
            }
        }

        private void startLoaderMore() {
            if(mUpPullRefreshListener!=null){
                mUpPullRefreshListener.onPullRefresh(this);
            }
        }
    }
}
