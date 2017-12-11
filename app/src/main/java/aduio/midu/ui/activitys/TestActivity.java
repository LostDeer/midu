package aduio.midu.ui.activitys;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import aduio.lib.ui.BaseActivity;
import aduio.midu.R;
import aduio.midu.ui.adapters.TestAdapter;
import aduio.midu.ui.fragments.BuyFragment;
import aduio.midu.ui.fragments.MeFragment;

/**
 * Created by ${LostDeer} on 2017/11/21.
 * Github:https://github.com/LostDeer
 */

public class TestActivity extends BaseActivity {
//    @BindView(R.id.vp)
//    ViewPager mVp;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.fragment_hover);
    }

    @Override
    protected void setListener() {

//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute();
//        myAsyncTask.cancel(true);
    }

    @Override
    protected void processLogic() {
        List<Fragment> mFragments = new ArrayList<>();
        TestAdapter testAdapter = new TestAdapter(getSupportFragmentManager());
        mFragments.add(new BuyFragment());
        mFragments.add(new MeFragment());
        testAdapter.addFrag(mFragments.get(0),"购物");
        testAdapter.addFrag(mFragments.get(1),"我的");
//        mVp.setAdapter(testAdapter);
    }


}
