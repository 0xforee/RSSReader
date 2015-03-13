package com.rssreader.foree.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baseapplication.foree.rssreader.BaseActivity;
import com.baseapplication.foree.rssreader.MyApplication;
import com.xmlparse.foree.rssreader.ParseTask;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
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
    // private RssFeedInfo rssFeedInfo = null;
    ListView itemlist;
    ProgressBar mProgressBar;
    TextView mTextView;
    MyApplication myApplication;
    // private String pathXML= Environment.getExternalStorageDirectory().getAbsolutePath() + "/w3cschool.xml";

    public ListView getItemlist() {
        return itemlist;
    }

    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    public TextView getmTextView() {
        return mTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DayTheme);
        setContentView(R.layout.main);

        //应用启动初始化环境变量
        myApplication = new MyApplication(this);
        myApplication.initEnv();
        myApplication.initSettings(this);

        //搭建UI
        itemlist = new ListView(this);
        mProgressBar = new ProgressBar(this);
        mTextView = new TextView(this);

        itemlist = (ListView) findViewById(R.id.listview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.nonetwork);

        //drawerlayout
        Log.v(TAG, "onCreate");
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        // 启动drawer，将drawer的布局加载到位于navigationDrawer上的fragment中
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //使用asyncTask完成url解析操作
        ParseTask parseTask = new ParseTask(this);
        parseTask.execute(this);

    }

    //在navigation被选中的时候
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        // 同过替换fragment来更新主界面
        FragmentManager fragmentManager = getSupportFragmentManager();
        //
        PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1);

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        Log.v(TAG, "onNavigationDrawerItemSelected");
    }

    //在section被选中的时候设置标题
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.action_about:
                String message = "增加以下特性：" + "\n"
                        + "1.可以浏览新闻";
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("版本v0.2");
                builder.setMessage(message);
                builder.setNeutralButton("返回", null);
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     * 一个位置存储fragment包含一个简单的视图
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "PlaceholderFragment";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private TextView textView;

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
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Log.v(TAG, "onCreateView");
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.v(TAG, "onStart");
            textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("hhha");
        }
    }
}
