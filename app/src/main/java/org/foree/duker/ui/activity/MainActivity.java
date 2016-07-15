package org.foree.duker.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.foree.duker.base.BaseActivity;
import org.foree.duker.base.MyApplication;
import org.foree.duker.db.RssDao;
import org.foree.duker.rssinfo.RssItemInfo;
import org.foree.duker.ui.fragment.NavigationDrawerFragment;
import org.foree.duker.utils.ColorUtils;
import org.foree.duker.utils.LogUtils;
import org.foree.duker.xmlparse.XmlParseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.rssreader.foree.rssreader.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, XmlParseHandler.ParseHandlerCallbacks {
    private static final String TAG = "MainActivity";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     * fragment管理navigation drawer的行为，交互和描述
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     * 通常用来存储最后一个屏幕的标题
     */
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //使navigationDrawer覆盖actionBar
        moveDrawToTop();

        //应用启动初始化环境变量
        myApplication = new MyApplication(this);
        myApplication.mainInitEnv();

        //drawerLayout
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        // 启动drawer，将drawerLayout的布局加载到位于navigationDrawer上的fragment中
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout);
        if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onCreate");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //if darkTheme setting changed, reload activity
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (isDarkTheme != sp.getBoolean(SettingsActivity.KEY_DARK_THEME, false)) {
            reload();
            isDarkTheme = !isDarkTheme;
        } else if (themeColor != sp.getInt(SettingsActivity.KEY_CHANGE_THEME, ColorUtils.AQUAMARINE)) {
            reload();
            themeColor = sp.getInt(SettingsActivity.KEY_CHANGE_THEME, ColorUtils.AQUAMARINE);
        }
    }

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    //将drawer移动覆盖actionBar
    public void moveDrawToTop() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) layoutInflater.inflate(R.layout.decor_actionbar, null);

        //获取根视图,并移除actionBar
        ViewGroup rootGroup = (ViewGroup) getWindow().getDecorView();
        View child = rootGroup.getChildAt(0);
        rootGroup.removeView(child);

        //将actionBar加回主视图(非drawer视图)
        LinearLayout linearLayout = (LinearLayout) mDrawerLayout.findViewById(R.id.container);
        linearLayout.addView(child, 0);

        //设置drawerLayout占据全屏
        mDrawerLayout.findViewById(R.id.navigation_drawer).setPadding(0, 0, 0, 0);

        //将设置好的view添加到根视图
        rootGroup.addView(mDrawerLayout);
    }

    //在navigation被选中的时候
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the decor_actionBar content by replacing fragments
        // 同过替换fragment来更新主界面(主界面使用android.R.id.content来获取,可用hierarchyViewer查看)
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, PlaceholderFragment.newInstance(position + 1))
                .commit();
        if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onNavigationDrawerItemSelected");
    }

    //在section被选中的时候设置标题
    public void onSectionAttached(int number) {
        if (!NavigationDrawerFragment.FeedInfos.isEmpty())
            mTitle = NavigationDrawerFragment.FeedInfos.get(number - 1);
        else
            mTitle = MyApplication.myApplicationName;
    }


    //重新设置actionBar的相关信息
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                final Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.action_about:
                String message = "新特性：" + "\n"
                        + "1.添加启动画面" + "\n"
                        + "2.美化图标" + "\n"
                        + "3.优化字体调节算法" + "\n"
                        + "4.增加清除缓存功能";
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(MyApplication.myVersionName);
                builder.setMessage(message);
                builder.setNeutralButton("返回", null);
                builder.show();
                break;
            case R.id.action_refresh:
                reload();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifyUiUpdate() {

    }

    //声明一个接口
    public interface myTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    //保存listener的接口
    private List<myTouchListener> myTouchListeners = new ArrayList<>();

    //注册的函数
    public void registerMyTouchListener(myTouchListener listener) {
        myTouchListeners.add(listener);
    }

    //取消注册的函数
    public void unregisterMyTouchListener(myTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    //事件分发的函数
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (myTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * A placeholder fragment containing a simple view.
     * 一个位置存储fragment包含一个简单的视图
     */
    public static class PlaceholderFragment extends Fragment implements XmlParseHandler.ParseHandlerCallbacks {
        private static final String TAG = "PlaceholderFragment";
        private GestureDetectorCompat gestureDetectorCompat;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private MainActivity mainActivity;
        ProgressBar mProgressBar;
        TextView mTextView;
        private String URLString;
        private String FeedLink;
        private String FeedName;
        private boolean isSearchAvailable = false;
        private boolean isSettingAvailable = false;
        private boolean openSearch = false;
        private boolean openSetting = false;

        public ListView getItemlist() {
            return listView;
        }

        public String getpostionUrl() {
            return URLString;
        }

        public String getFeedLink() {
            return FeedLink;
        }

        public String getpostionFeedName() {
            return FeedName;
        }

        public ProgressBar getmProgressBar() {
            return mProgressBar;
        }

        public TextView getmTextView() {
            return mTextView;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         * 根据给定的section number返回一个新的例子,并带有相应的参数
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        //在创建fragment之后，将要填充的布局文件转换为view控件
        //在此生命周期后，布局中的控件才可以被找到
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //初始化fragment中的控件
            listView = (ListView) rootView.findViewById(R.id.articel_listview);
            //找到fb,并绑定到listview中
            FloatingActionButton fb = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fb.attachToListView(listView, new ScrollDirectionListener() {
                @Override
                public void onScrollDown() {
                    if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onScrollDown");
                }

                @Override
                public void onScrollUp() {
                    if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onScrollUp");

                }
            });
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(mainActivity, FeedListActivity.class);
                    startActivity(intent2);
                }
            });
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mTextView = (TextView) rootView.findViewById(R.id.nonetwork);
            listView.setAdapter(mainActivity.getmRssAdapter());
            //设置滑动监听
            listView.setOnScrollListener(mainActivity);
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onCreateView");
            //获取当前所选中的fragment,并解析对应的url
            int id = getArguments().getInt(ARG_SECTION_NUMBER);
            /**
             * 获取左侧Drawer相对应位置的FeedName,来查找数据库中对应的url
             */
            //用户数据为空时,提示用户添加
            if (NavigationDrawerFragment.FeedInfos.isEmpty()) {
                mTextView.setText("你还没有添加任何RSS源,请点击搜索添加");
                mProgressBar.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                return rootView;
            }
            FeedName = NavigationDrawerFragment.FeedInfos.get(id - 1);
            RssDao rssDao = new RssDao(mainActivity);
            //取得对应名称的url链接
            URLString = rssDao.findUrl(FeedName);
            FeedLink = rssDao.findLink(FeedName);
            if (URLString != null) {
                //解析对应url获取数据
                doRSS(URLString);
                //绑定listview点击事件
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent mintent = new Intent(mainActivity, DescriptionActivity.class);

                        //绑定数据
                        Bundle bundle = new Bundle();
                        bundle.putString("title", mainActivity.getRssItemInfos().get(position).getTitle());
                        bundle.putString("description", mainActivity.getRssItemInfos().get(position).getDescription());
                        bundle.putString("link", mainActivity.getRssItemInfos().get(position).getLink());
                        bundle.putString("pubdate", mainActivity.getRssItemInfos().get(position).getpubDate());

                        mintent.putExtra("com.rssinfo.foree.rssreader", bundle);
                        mainActivity.startActivity(mintent);
                    }
                });
            }
            //注册touch事件
            mainActivity.registerMyTouchListener(touchListener);

            //监听手势
            //手势实现
            gestureDetectorCompat = new GestureDetectorCompat(mainActivity, new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (LogUtils.isCompilerLog)
                        LogUtils.v(TAG, "x = " + e2.getX() + " y = " + e2.getY());
                    if (e2.getY() - e1.getY() > 400) {
                        isSearchAvailable = true;
                        LogUtils.d(TAG, "hhhhhhh");
                    } else if (e2.getY() - e1.getY() < -400) {
                        isSearchAvailable = false;
                        isSettingAvailable = true;
                    } else {
                        isSearchAvailable = false;
                        isSettingAvailable = false;
                    }
                    if (isSearchAvailable) {
                        if (e2.getX() - e1.getX() > 400) {
                            //   if (distanceY > 20)
                            LogUtils.d(TAG, "&&&&&&&&&&&&&&&&&&&&&&&");
                            openSearch = true;
                        }
                    }
                    if (isSettingAvailable) {
                        if (e2.getX() - e1.getX() > 400) {
                            LogUtils.d(TAG, "**********************");
                            openSetting = true;
                        }
                    }
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    // if (Math.abs(e1.getRawX() - e2.getRawX()) > 250) {
                    // // System.out.println("水平方向移动距离过大");
                    // return true;
                    // }
                   /* if (Math.abs(velocityY) < 100) {
                        // System.out.println("手指移动的太慢了");
                        return true;
                    }*/

                    //Log.v(TAG, openSearch + "");
                    if (openSearch) {
                        Toast.makeText(mainActivity, "打开搜索", Toast.LENGTH_SHORT).show();
                        Intent searchIntent = new Intent(mainActivity, FeedListActivity.class);
                        startActivity(searchIntent);
                        openSearch = false;
                    }
                    if (openSetting) {
                        Toast.makeText(mainActivity, "打开设置", Toast.LENGTH_SHORT).show();
                        Intent searchIntent = new Intent(mainActivity, SettingsActivity.class);
                        startActivity(searchIntent);
                        openSetting = false;
                    }
                    /*// 手势向下 down
                    if ((e2.getRawY() - e1.getRawY()) > 200) {
                        Toast.makeText(mainActivity,"打开搜索", Toast.LENGTH_SHORT).show();
                        Intent searchIntent = new Intent(mainActivity, FeedListActivity.class);
                        startActivity(searchIntent);
                        return true;
                    }
                    // 手势向上 up
                    if ((e1.getRawY() - e2.getRawY()) > 200) {
                        return true;
                    }*/
                    return true;
                }
            });
            return rootView;
        }

        private myTouchListener touchListener = new myTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                gestureDetectorCompat.onTouchEvent(event);
            }
        };

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
            //初始化mainactivity实例，用于与主界面通信
            mainActivity = (MainActivity) activity;
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "OnAttach");

        }

        //在onStart方法中对listview进行动态的数据添加
        @Override
        public void onStart() {
            super.onStart();
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onStart");

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            //获取RssItemInfo的数量
            int count = mainActivity.getmRssAdapter().getCount();

            //使用charsequence 列表保存数据
            ArrayList<CharSequence> strings = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                RssItemInfo rssItemInfo = mainActivity.getRssItemInfos().get(i);
                strings.add(rssItemInfo.getFeedTitle());
                strings.add(rssItemInfo.getTitle());
            }
            outState.putSerializable("RSSITEMIFO_KEY", strings);

            if (listView.hasFocus()) {
                outState.putInt("SELECTION_KEY", Integer.valueOf(listView.getSelectedItemPosition()));
            }

            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onSavedInstanceState");
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (savedInstanceState == null) return;

            List<CharSequence> strings = (ArrayList<CharSequence>) savedInstanceState.getSerializable("RSSITEMIFO_KEY");
            List<RssItemInfo> items = new ArrayList<RssItemInfo>();
            for (int i = 0; i < strings.size(); i += 2) {
                items.add(new RssItemInfo(strings.get(i).toString(), strings.get(i + 1).toString()));
            }

            // Reset the list view to show this data.
            // mainActivity.resetUI(listView, items);

            // Restore selection
            if (savedInstanceState.containsKey("SELECTION_KEY")) {
                listView.requestFocus(View.FOCUS_FORWARD);
                // todo: is above right? needed it to work
                listView.setSelection(savedInstanceState.getInt("SELECTION_KEY"));
            }
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onCreate");
        }

        @Override
        public void onPause() {
            super.onPause();
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onPause");
        }

        @Override
        public void onStop() {
            super.onStop();
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onStop");

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onDestroy");

        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (LogUtils.isCompilerLog) LogUtils.v(TAG, "onDetach");
            mainActivity.resetUI();
            mainActivity.unregisterMyTouchListener(touchListener);

        }

        private void doRSS(String rssUrl) {
            RssParse worker = new RssParse(mainActivity, rssUrl);
            worker.start();
        }

        //第一个解析出来之后,更新ProgressBar,并显示listview
        @Override
        public void notifyUiUpdate() {
            mainActivity.getmHandler().post(new updateUi());
        }

        public class updateUi implements Runnable {
            @Override
            public void run() {
                //设置进度条不可见
                mProgressBar.setVisibility(View.GONE);
                //设置listview可见
                listView.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.INVISIBLE);

            }
        }
    }

}
