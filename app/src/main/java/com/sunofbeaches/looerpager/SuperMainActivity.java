package com.sunofbeaches.looerpager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.skin2.SkinActivity;
import com.example.qqaplication.ILoginInterface;
import com.google.android.material.snackbar.Snackbar;
import com.sunofbeaches.looerpager.adapter.MyRecycicleViewAdapter;
import com.sunofbeaches.looerpager.manager.CompanyManager;
import com.sunofbeaches.looerpager.model.Company;
import com.sunofbeaches.looerpager.model.PagerItem;
import com.sunofbeaches.looerpager.views.SobLooperPager;
import com.sunofbeaches.looerpager.views.ToolBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SuperMainActivity extends SkinActivity implements MyRecycicleViewAdapter.AddDataListener, View.OnClickListener {

    private SobLooperPager mLooperPager;

    private List<PagerItem> mPagerItems = new ArrayList<>();
    private RecyclerView mRvMain;
    private MyRecycicleViewAdapter mRecycicleViewAdapter;
    private List<PagerItem> mDatas;
    private boolean isStartRemote; // 是否开启跨进程

    private ILoginInterface iLogin;
    private String mSkinPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_suer_main);
        initData();
        initView();
        initEvent();
//        initToolBar();
        initRecyclerView();

        initBindService();

        // 换肤包
        mSkinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "net163.skin";

        // 运行时权限申请(6.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }

    }

    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iLogin = ILoginInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑服务
        if (isStartRemote) {
            unbindService(mServiceConnection);
        }

    }

    private void initBindService() {
        Intent intent = new Intent();
        intent.setAction("Binder_LoginService");

        intent.setPackage("com.example.qqaplication");

        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        isStartRemote = true;
    }


    private void initRecyclerView() {
        mRvMain = (RecyclerView) findViewById(R.id.rv_main);

        // 设置布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvMain.setLayoutManager(linearLayoutManager);
        mRvMain.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this).build());

        initRecycicleViewAdapter();
    }

    private void initRecycicleViewAdapter() {
        mDatas = new ArrayList<>();
        CompanyManager companyManager = new CompanyManager(this);
        companyManager.query(new CompanyManager.CompanyListener() {
            @Override
            public void getCompany(List<Company> list) {
                mDatas.clear();
                list = list.subList(0, 6);
                for (Company company : list) {
                    mDatas.add(new PagerItem(company.getName(), ""));
                }

                if (mRecycicleViewAdapter == null) {
                    mRecycicleViewAdapter = new MyRecycicleViewAdapter(mDatas, true);
                    mRvMain.setAdapter(mRecycicleViewAdapter);
                    mRecycicleViewAdapter.setAddDataListener(SuperMainActivity.this);
                }

            }
        });
    }


    private void initToolBar() {
        ToolBar toolBar = findViewById(R.id.toolBar);
        toolBar.setBarClickListener(new ToolBar.BarCLickListener() {
            @Override
            public void leftClick(View view) {
                Toast.makeText(SuperMainActivity.this, "向左", Toast.LENGTH_SHORT);
            }

            @Override
            public void rightClick(View view) {
                Toast.makeText(SuperMainActivity.this, "向右", Toast.LENGTH_SHORT);
            }
        });
    }


    private void initEvent() {
        if (mLooperPager != null) {
            mLooperPager.setOnItemClickListener(new SobLooperPager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(SuperMainActivity.this, "点击了第" + (position + 1) + "个item", Toast.LENGTH_SHORT).show();
                    //todo:根据交互业务去实现具体逻辑
                }
            });
        }


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello Snackbar", Snackbar.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.testRecyclerViewBtn).setOnClickListener(this);
        findViewById(R.id.qqLogin).setOnClickListener(this);
        findViewById(R.id.updateSkin).setOnClickListener(this);
        findViewById(R.id.print).setOnClickListener(this);
    }

    private void initData() {
        mPagerItems.add(new PagerItem("第一个图片", R.mipmap.pic0));
        mPagerItems.add(new PagerItem("第二个图片", R.mipmap.pic1));
        mPagerItems.add(new PagerItem("第三个图片", R.mipmap.pic2));
        mPagerItems.add(new PagerItem("第四个图片", R.mipmap.pic3));
        mPagerItems.add(new PagerItem("第五个图片", "http://imgstore04.cdn.sogou.com/app/a/100520024/877e990117d6a7ebc68f46c5e76fc47a"));
    }

    private void initView() {
        mLooperPager = this.findViewById(R.id.sob_looper_pager);
        mLooperPager.setData(mPagerItems, new SobLooperPager.BindTitleListener() {
            @Override
            public String getTitle(int position) {
                return mPagerItems.get(position).getTitle();
            }
        });

    }

    @Override
    public void addData(View v) {
        // 显示所有未被关注的公司的列表的Dialog
//        PagerItem pagerItem = new PagerItem("测试添加", R.mipmap.pic0);
//        mRecycicleViewAdapter.addDataAtLast(pagerItem);

        startActivity(ViewAllCompanayActivity.class);
    }


    public void testLongClickList(View view) {
        startActivity(LongClickTestActivity.class);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.testRecyclerViewBtn:
                startActivity(RecyclerViewTestActivity.class);
                break;

            case R.id.qqLogin:
                callQQLoginMethod();
                break;

            case R.id.updateSkin:
                // 调用lib中的换肤方法
                dayOrNight(v);
                break;

            case R.id.skinDynamic:
                skinDynamic(v);
                break;

            case R.id.print:
                startActivity(PrintActivity.class);
                break;
        }
    }

    // 动态换肤
    private void skinDynamic(View v) {

    }

    /**
     * 调用第三方QQ登录界面
     */
    private void callQQLoginMethod() {
        if (iLogin != null) {
            try {
                iLogin.login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "请先安装QQ应用", Toast.LENGTH_SHORT);
        }
    }


    private void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public boolean openChangeSkin() {
        return true;
    }
}
