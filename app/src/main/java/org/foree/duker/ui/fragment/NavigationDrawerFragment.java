package org.foree.duker.ui.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.foree.duker.db.RssDao;
import org.foree.duker.ui.activity.SettingsActivityTmp;
import org.foree.duker.utils.LogUtils;
import org.foree.duker.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {

    /**
     * 记住所选择的位置
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * 在用户手动展开drawer之前,启动的时候保证drawer是开着的
     */

    /**
     * 指向当前回调函数的接口
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    //侧边栏的数据集合
    public static List<String> FeedInfos;

    //adapter数据控制器
    public static ArrayAdapter<String> adapter;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLinearLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //防止用户不知道drawer，在第一次打开应用时，打开drawer
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(SettingsActivityTmp.KEY_PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        selectItem(mCurrentSelectedPosition);

        //初始化侧边栏数组
        FeedInfos = new ArrayList<>();
        //初始化数据适配器
        adapter = new ArrayAdapter<>(
                getActionBar().getThemedContext(),
                R.layout.navigation_drawer_listview,
                R.id.navigation_item,
                FeedInfos
        );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView = (ListView) mLinearLayout.findViewById(R.id.drawer_listView);
        //set listview's ChoiceMode multiple
        mDrawerListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        //set listener
        mDrawerListView.setMultiChoiceModeListener(new MyCallBack());
        //if not in multiple mode, set clickListener
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        //为drawer侧边的listview设置数据适配器
        mDrawerListView.setAdapter(adapter);
        //设置高亮显示
        // mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mLinearLayout;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // 调用 onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                // 如果用户已经学会了打开drawer，将设置项写入配置文件，防止下次启动自动打开
                if (!mUserLearnedDrawer) {
                    //用户手动打开drawer,将其作为配置项保存起来
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(SettingsActivityTmp.KEY_PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        //如果用户还未学会如何打开drawer,就在启动的时候打开drawer
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    //在选中之后的动作
    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        /*if (mDrawerListView != null) {
                mDrawerListView.setItemChecked(position, true);
        }*/
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 当系统配置改变时调用DrawerToggle的改变配置方法（例如横竖屏切换会回调此方法）
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //在navigationDrawer界面显示菜单（当前只有settings）
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //当抽屉打开时显示应用全局的actionbar设置
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            //当drawer打开的时候，显示全局的标题而不是当前屏幕的标题（当前为应用名称)
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * 按照navigation drawer设计指导，更新action bar去显示全局app的内容，而不是当前屏幕是什么
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * 使用这个Navigation的所有的activity都得实现这个回调接口
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * 当Item选中的时候调用
         */
        void onNavigationDrawerItemSelected(int position);
    }

    /**
     * 进入多选状态时的回调方法(用于删除侧边栏的订阅)
     */
    private class MyCallBack implements ListView.MultiChoiceModeListener {
        private static final String TAG = "MyCallBack";
        private List<String> selections;
        private RssDao rssDao;

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            //Toast.makeText(getActivity(), "选中", Toast.LENGTH_SHORT).show();
            if (checked) {
                selections.add(NavigationDrawerFragment.FeedInfos.get(position));
                if (LogUtils.isCompilerLog)
                    LogUtils.v(TAG, "add position " + NavigationDrawerFragment.FeedInfos.get(position));
            } else {
                selections.remove(NavigationDrawerFragment.FeedInfos.get(position));
                if (LogUtils.isCompilerLog)
                    LogUtils.v(TAG, "delete position " + NavigationDrawerFragment.FeedInfos.get(position));
            }
        }

        //在此处创建多选选中时候的menu功能
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getActivity().getMenuInflater();
            //插入一个菜单按键,可以全选以及删除
            inflater.inflate(R.menu.menu_multiselection, menu);
            selections = new ArrayList<>();
            rssDao = new RssDao(getActivity());
            LogUtils.i(TAG, "onCreateActionMode");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            LogUtils.i(TAG, "onPrepareActionMode");
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    LogUtils.i(TAG, "delete!");
                    //delete rss data
                    for (int i = 0; i < selections.size(); i++) {
                        //  String DeleteName = NavigationDrawerFragment.FeedInfos.get(selections.get(i));
                        //delete data from FeedInfos, and notify
                        NavigationDrawerFragment.FeedInfos.remove(selections.get(i));
                        //selections.remove(i);
                        adapter.notifyDataSetChanged();
                        //delete data from userdatabase
                        rssDao.delete(selections.get(i));
                    }
                    //finish mode
                    mode.finish();
                    //refresh( later )
                    break;
            }
            LogUtils.i(TAG, "onActionItemClicked");
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            LogUtils.i(TAG, "onDestroyActionMode");
        }
    }
}
