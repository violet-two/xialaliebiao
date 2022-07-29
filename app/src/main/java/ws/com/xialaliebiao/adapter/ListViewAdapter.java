package ws.com.xialaliebiao.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ws.com.xialaliebiao.R;
import ws.com.xialaliebiao.bean.ItemBean;

public abstract class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<ItemBean> mData;
    private OnItemClickListener mOnItemClickListener;

    protected ListViewAdapter(List<ItemBean> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        return new InnerHolder(view);
    }

    protected abstract View getSubView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((InnerHolder) holder).setData(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    //设置点击监听接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        //设置监听
    }

    public interface OnItemClickListener{
        void onItemClick(int position, String mTitle);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mTitle;
        private int mPosition;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mIcon = itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(mPosition,mTitle.getText().toString());
                    }
                }
            });
        }

        public void setData(ItemBean itemBean, int position) {
            this.mPosition = position;
            mIcon.setImageResource(itemBean.icon);
            mTitle.setText(itemBean.title);
        }
    }
}
