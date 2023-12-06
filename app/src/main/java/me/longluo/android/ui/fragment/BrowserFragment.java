package me.longluo.android.ui.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import me.longluo.android.R;
import me.longluo.android.action.StatusAction;
import me.longluo.android.aop.CheckNet;
import me.longluo.android.aop.Log;
import me.longluo.android.app.AppActivity;
import me.longluo.android.app.AppFragment;
import me.longluo.android.ui.activity.BrowserActivity;
import me.longluo.android.widget.BrowserView;
import me.longluo.android.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * 浏览器 Fragment
 */
public final class BrowserFragment extends AppFragment<AppActivity>
        implements StatusAction, OnRefreshListener {

    private static final String INTENT_KEY_IN_URL = "url";

    @Log
    public static BrowserFragment newInstance(String url) {
        BrowserFragment fragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY_IN_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mRefreshLayout;
    private BrowserView mBrowserView;

    @Override
    protected int getLayoutId() {
        return R.layout.browser_fragment;
    }

    @Override
    protected void initView() {
        mStatusLayout = findViewById(R.id.hl_browser_hint);
        mRefreshLayout = findViewById(R.id.sl_browser_refresh);
        mBrowserView = findViewById(R.id.wv_browser_view);

        // 设置 WebView 生命周期回调
        mBrowserView.setLifecycleOwner(this);
        // 设置网页刷新监听
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        mBrowserView.setBrowserViewClient(new AppBrowserViewClient());
        mBrowserView.setBrowserChromeClient(new BrowserView.BrowserChromeClient(mBrowserView));
        mBrowserView.loadUrl(getString(INTENT_KEY_IN_URL));
        showLoading();
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private void reload() {
        mBrowserView.reload();
    }

    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reload();
    }

    private class AppBrowserViewClient extends BrowserView.BrowserViewClient {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post(() -> showError(listener -> reload()));
        }

        /**
         * 开始加载网页
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            mRefreshLayout.finishRefresh();
            showComplete();
        }

        /**
         * 跳转到其他链接
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            String scheme = Uri.parse(url).getScheme();
            if (scheme == null) {
                return true;
            }
            switch (scheme.toLowerCase()) {
                // 如果这是跳链接操作
                case "http":
                case "https":
                    BrowserActivity.start(getAttachActivity(), url);
                    break;
                default:
                    break;
            }
            // 已经处理该链接请求
            return true;
        }
    }
}