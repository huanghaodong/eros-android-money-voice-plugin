package com.example.huanghaodong.voiceplugin;


import android.widget.Toast;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.Map;

@WeexModule(name = "voice", lazyLoad = true)
public class VoicePlugin extends WXModule {

    @JSMethod(uiThread = true)
    public void play(Map<String, Object> params) {
        // new VoiceModule().setContext(mWXSDKInstance.getContext()).openAssetMusics();
        String money = (String) params.get("money");
        String payType = (String) params.get("payType");
        TTSManager.get().setContext(mWXSDKInstance.getContext()).loadAddOrderSound(payType, money);

    }
}
