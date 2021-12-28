package com.longluo.android.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longluo.android.ui.activity.DemoActivity;

import com.longluo.android.R;

import io.github.longluo.baseframework.BaseFragment;
import io.github.longluo.baseframework.interfaces.Layout;
import io.github.longluo.baseframework.interfaces.OnClick;
import io.github.longluo.baseframework.util.JumpParameter;
import io.github.longluo.baseframework.util.OnJumpResponseListener;

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.fragment_introduction)
/**
 * 此处泛型是用于约束绑定目标 Activity 的，是可选操作，
 * 如果你指定了目标绑定目标 Activity，则使用“me.”关键词可直接调用该 Activity 中的 public 成员或方法
 */
public class IntroductionFragment extends BaseFragment<DemoActivity> {

    private TextView linkHome;
    private FloatingActionButton btnFab;

    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        linkHome = findViewById(R.id.link_home);
        btnFab = findViewById(R.id.btn_fab);
    }

    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas() {
        linkHome.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        linkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/longluo/Android_Dev_Quick");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.btn_fab)
    //你也可以使用 @OnClick 注解直接绑定点击事件
    public void startTest() {
        jump(1, new JumpParameter().put("tip", "开始尝试功能！"), new OnJumpResponseListener() {
            @Override
            public void OnResponse(JumpParameter jumpParameter) {
                toast("你刚刚使用了：" + jumpParameter.getString("function"));
            }
        });
    }

    @Override
    /**
     * 进入 Fragment 时调用此方法，isSwitchFragment 标记说明了是否为从别的 Fragment 切换至此 Fragment 的，
     * 若为 false，则有可能是从后台切换至前台触发
     */
    public void onShow(boolean isSwitchFragment) {
        log("IntroductionFragment: onShow");
        super.onShow(isSwitchFragment);
    }

    @Override
    public void onHide() {
        log("IntroductionFragment: onHide");
        super.onHide();
    }
}