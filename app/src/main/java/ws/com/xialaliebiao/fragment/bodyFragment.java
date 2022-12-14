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
        //??????????????????
        initData();
        //????????????
        handlerDownPullUpdate();
        //????????????
        handlerUpPullOnload();
        return view;
    }
    private void initEvent() {
        upPullAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String mTitle) {
                Toast.makeText(getContext(),"?????????"+position+"?????????"+";"+mTitle,Toast.LENGTH_SHORT).show();
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
                        //??????????????????????????????????????????????????????
                        ItemBean data = new ItemBean();
                        data.title = "????????????????????????";
                        data.icon = R.mipmap.pic_11;
                        upPullAdapter.IsFirst(false);
                        mData.add(data);
                        //????????????
                        upPullAdapter.notifyDataSetChanged();
                        //????????????
                        loaderMoreHolder.update(UpPullAdapter.loaderMoreHolder.LOADER_SATE_NORMAL);
                    }
                }, 1000);
            }
        });
    }

    private void handlerDownPullUpdate() {
        //?????????????????????
        refresh_layout.setEnabled(true);
        //?????????????????????
        initData();
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //????????????
                        //????????????
                        initData();
                        //????????????
                        refresh_layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
//        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                //??????????????????
////                ItemBean data = new ItemBean();
////                //??????????????????
////                data.title = "????????????????????????";
////                data.icon = R.mipmap.pic_07;
////                mData.add(0, data);
//                //????????????,postDelayed??????
//                List data = new ArrayList<>();
//                for (int i = 0; i < Datas.icons.length; i++) {
//                    ItemBean itemBean = new ItemBean();
//                    itemBean.title = "?????????" + i + "?????????";
//                    itemBean.icon = Datas.icons[i];
//                    data.add(itemBean);
//                }
//                mData = data;
//                upPullAdapter = new UpPullAdapter(mData);
//                recycler_view.setAdapter(upPullAdapter);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //????????????
//                        //????????????
//                        upPullAdapter.notifyDataSetChanged();
//                        //????????????
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
            itemBean.title = "?????????" + i + "?????????";
            itemBean.icon = Datas.icons[i];
            mData.add(itemBean);
        }
        //???????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(linearLayoutManager);
        upPullAdapter = new UpPullAdapter(mData,true);
        recycler_view.setAdapter(upPullAdapter);
        //????????????
        initEvent();
        handlerUpPullOnload();
    }
}