package ws.com.xialaliebiao.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;
import java.util.List;

import ws.com.xialaliebiao.MainActivity;
import ws.com.xialaliebiao.R;
import ws.com.xialaliebiao.adapter.ListViewAdapter;
import ws.com.xialaliebiao.adapter.UpPullAdapter;
import ws.com.xialaliebiao.bean.Datas;
import ws.com.xialaliebiao.bean.ItemBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bodyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bodyFragment extends Fragment {
    private RecyclerView recycler_view;
    private List<ItemBean> mData;
    private UpPullAdapter upPullAdapter;
    private SwipeRefreshLayout refresh_layout;
    private TabItem inbox;
    private TabItem outbox;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public bodyFragment() {
        // Required empty public constructor
    }
    public static bodyFragment newInstance(int sectionNumber) {
        bodyFragment fragment = new bodyFragment();
        Bundle args = new Bundle();
        args.putInt("section_number",sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        recycler_view = view.findViewById(R.id.recycler_view);
        refresh_layout = view.findViewById(R.id.refresh_layout);
        inbox = view.findViewById(R.id.Inbox);
        outbox = view.findViewById(R.id.Outbox);
        //准备静态数据
        initData();
        //下拉刷新
        handlerDownPullUpdate();
        //上拉加载
        handlerUpPullOnload();
        //初始化事件
        initEvent();
        return view;
    }
    private void initEvent() {
        upPullAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String mTitle) {
                Toast.makeText(getContext(),"这是第"+position+"个数据"+";"+mTitle,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlerUpPullOnload() {
        upPullAdapter.setOnRefreshListener(new UpPullAdapter.OnRefreshListener() {
            @Override
            public void onPullRefresh(UpPullAdapter.loaderMoreHolder loaderMoreHolder) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //这里加载数据，同样需要在子线程中完成
                        ItemBean data = new ItemBean();
                        data.title = "我是新添加的数据";
                        data.icon = R.mipmap.pic_11;
                        upPullAdapter.IsFirst(false);
                        mData.add(data);
                        //更新列表
                        upPullAdapter.notifyDataSetChanged();
                        //刷新停止
                        loaderMoreHolder.update(UpPullAdapter.loaderMoreHolder.LOADER_SATE_NORMAL);
                    }
                }, 1000);
            }
        });
    }

    private void handlerDownPullUpdate() {
        //设置为启动状态
        refresh_layout.setEnabled(true);
        //添加事件监听器
        initData();
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //更新列表
                        //更新列表
                        initData();
                        //停止刷新
                        refresh_layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
//        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                //执行刷新操作
////                ItemBean data = new ItemBean();
////                //添加静态数据
////                data.title = "我是新添加的数据";
////                data.icon = R.mipmap.pic_07;
////                mData.add(0, data);
//                //开启线程,postDelayed延时
//                List data = new ArrayList<>();
//                for (int i = 0; i < Datas.icons.length; i++) {
//                    ItemBean itemBean = new ItemBean();
//                    itemBean.title = "我是第" + i + "条数据";
//                    itemBean.icon = Datas.icons[i];
//                    data.add(itemBean);
//                }
//                mData = data;
//                upPullAdapter = new UpPullAdapter(mData);
//                recycler_view.setAdapter(upPullAdapter);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //更新列表
//                        //更新列表
//                        upPullAdapter.notifyDataSetChanged();
//                        //停止刷新
//                        refresh_layout.setRefreshing(false);
//                    }
//                }, 1000);
//            }
//        });
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < Datas.icons.length; i++) {
            ItemBean itemBean = new ItemBean();
            itemBean.title = "我是第" + i + "条数据";
            itemBean.icon = Datas.icons[i];
            mData.add(itemBean);
        }
        //编写适配器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(linearLayoutManager);
        upPullAdapter = new UpPullAdapter(mData,true);
        recycler_view.setAdapter(upPullAdapter);
        //上拉加载
        handlerUpPullOnload();
    }
}