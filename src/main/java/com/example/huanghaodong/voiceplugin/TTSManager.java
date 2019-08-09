package com.example.huanghaodong.voiceplugin;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Arturia
 * Date: 2018/12/6
 */
public class TTSManager {

    private final String ChineseNum[] = new String[]{"零", "一", "二", "三", "四", "五",
            "六", "七", "八", "九"};
    private final String ChineseUnit[] = new String[]{"个", "十", "百", "千", "万", "亿"};
    private final String Dot = "点";
    private final Map<String, Integer> mp3Map = new HashMap<String, Integer>() {{
        put("零", R.raw.ling);
        put("一", R.raw.yi);
        put("二", R.raw.er);
        put("三", R.raw.san);
        put("四", R.raw.si);
        put("五", R.raw.wu);
        put("六", R.raw.liu);
        put("七", R.raw.qi);
        put("八", R.raw.ba);
        put("九", R.raw.jiu);
        put("点", R.raw.dian);
        put("十", R.raw.shi);
        put("百", R.raw.bai);
        put("千", R.raw.qian);
        put("万", R.raw.wan);
    }};

    private Context context;
    private List<Integer> mp3List;
    private List<Integer> soundList;
    private Map<Integer, Integer> timeMap;
    private SoundPool soundPool;
    private int index = 0;
    private boolean isOrder = false;
    private TTSHandler handler = new TTSHandler(this);

    private TTSManager() {
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setLegacyStreamType(AudioManager.STREAM_RING);
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(builder.build())
                .build();
        mp3List = new ArrayList<>();
        soundList = new ArrayList<>();
        timeMap = new HashMap<>();
    }

    private static class Holder {
        private static final TTSManager manager = new TTSManager();
    }

    public static TTSManager get() {
        return Holder.manager;
    }

    public TTSManager setContext(Context context) {
        this.context = context;
        return this;
    }
    public void loadAddOrderSound(String payment, String money) {
        isOrder = true;
        resetSoundStatus();
        addCashSound(payment);
        addMoneySound(money);
    }

    private void addCashSound(String payment) {
        int mp3 = R.raw.pay;
        switch (payment) {
            case "alipay":
                mp3 = R.raw.alipay;
                break;
            case "wxpay":
                mp3 = R.raw.wxpay;
                break;
            case "fdpay":
                mp3 = R.raw.fubeipay;
                break;
        }
        mp3List.add(mp3);
    }

    public void loadMoneySound(String money) {
        if (!isOrder) {
            resetSoundStatus();
            addMoneySound(money);
        }
    }

    private void addMoneySound(String money) {
        mp3List.addAll(spell(money));
        mp3List.add(R.raw.yuan);
        for (int mp3 : mp3List) {
            setTimestamp(mp3);
            soundPool.load(context, mp3, 1);
        }
    }

    public void loadScanFailureSound() {
        resetSoundStatus();
        mp3List.add(R.raw.scan_failure);
        soundPool.load(context, R.raw.scan_failure, 1);
    }

    private void setTimestamp(int mp3) {
        if(mp3 == R.raw.ling){
            timeMap.put(mp3, 250);
        }else if(mp3 == R.raw.yi){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.er){
            timeMap.put(mp3, 288);
        }else if(mp3 == R.raw.san){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.si){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.wu){
            timeMap.put(mp3, 350);
        }else if(mp3 == R.raw.liu){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.qi){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.ba){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.jiu){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.dian){
            timeMap.put(mp3, 360);
        }else if(mp3 == R.raw.shi){
            timeMap.put(mp3, 360);
        }else if(mp3 == R.raw.bai){
            timeMap.put(mp3, 396);
        }else if(mp3 == R.raw.qian){
            timeMap.put(mp3, 324);
        }else if(mp3 == R.raw.wan){
            timeMap.put(mp3, 396);
        }else if(mp3 == R.raw.alipay){
            timeMap.put(mp3, 1000);
        }else if(mp3 == R.raw.wxpay){
            timeMap.put(mp3, 900);
        }else if(mp3 == R.raw.fubeipay){
            timeMap.put(mp3, 900);
        }
    }

    private void resetSoundStatus() {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundList.add(sampleId);
                if (mp3List.size() == soundList.size()) {
                    playSoundPool();
                }
            }
        });
//        if (soundList.size() > 0) {
//            soundPool.stop(soundList.get(index));
//        }
        index = 0;
        mp3List.clear();
        soundList.clear();
        timeMap.clear();
    }

    private void playSoundPool() {
        handler.removeMessages(MSG_SOUND);
        if (soundList.size() > 0 && index < soundList.size()) {
            soundPool.play(soundList.get(index), 1, 1, 0, 0, 1);
            long timestamp = 300;
            if (index < mp3List.size() && timeMap.containsKey(mp3List.get(index))) {
                timestamp = timeMap.get(mp3List.get(index));
            }
            if (++index < soundList.size()) {
                handler.sendEmptyMessageDelayed(MSG_SOUND, timestamp);
            } else {
                index = 0;
                isOrder = false;
            }
        }
    }

    private List<Integer> spell(String price) {
        price = encode(price);

        String[] prices = price.split("");
        List<Integer> pricesSpell = new ArrayList<>();
        for (String item : prices) {
            if (TextUtils.isEmpty(item)) {
                continue;
            }
            int mp3 = mp3Map.get(item);
            pricesSpell.add(mp3);
        }
        return pricesSpell;
    }

    private String encode(String price) {

        String chinesePrice = "";

        String intPrice = getPriceResult(price).get("int");

        chinesePrice += encodeInt(intPrice);

        String decimalPrice = getPriceResult(price).get("decimal");

        chinesePrice += encodeDecimal(decimalPrice);

        return chinesePrice;
    }

    private static Map<String, String> getPriceResult(final String price) {

        int num = price.indexOf(".");

        if (num == -1) {
            return new HashMap<String, String>() {{
                put("int", Integer.toString(Integer.parseInt(price))); //去首位零
                put("decimal", "");
            }};
        }
        final String intPrice = price.substring(0, num);

        final String decimalPrice = price.substring(num).replace(".", "");

        return new HashMap<String, String>() {{
            put("int", Integer.toString(Integer.parseInt(intPrice)));
            put("decimal", decimalPrice);
        }};

    }

    private String encodeDecimal(String price) {

        price = clearZero(price, "0", "$");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < price.length(); i++) {
            int num = Integer.parseInt(String.valueOf(price.charAt(i)));
            sb.append(ChineseNum[num]);
        }

        String decimalPrice = sb.toString();

        if (!decimalPrice.equals("")) decimalPrice = Dot + decimalPrice;

        return decimalPrice;
    }

    private String encodeInt(String price) {

        price = getPriceResult(price).get("int");

        int length = price.length();

        StringBuilder sb = new StringBuilder();

        //一位数
        if (length == 1) return ChineseNum[Integer.parseInt(price)];
        //小于四位
        if (length <= 4) {
            for (int i = 0, n = length; i < length; i++) {
                --n;
                int num = Integer.parseInt(String.valueOf(price.charAt(i)));
                if (!(length == 2 && i == 0 && num == 1)) sb.append(ChineseNum[num]);
                if (num != 0 && n != 0) sb.append(ChineseUnit[n]);
            }
        } else { //大数递归
            int d = length / 4,
                    y = length % 4;
            while (y == 0 || ChineseUnit[3 + d].equals("")) {
                y += 4;
                d--;
            }
            sb.append(encodeInt(price.substring(0, y)));
            sb.append(ChineseUnit[3 + d]);
            if (price.substring(y - 1, y + 1).contains("0")) sb.append(ChineseNum[0]);
            sb.append(encodeInt(price.substring(y)));
        }
        String str = sb.toString();
        str = clearZero(str, "零", "");
        return str;
    }

    private static String clearZero(String str, String zero, String type) {

        if (type.equals("^")) {
            str = str.replaceAll("^" + zero + "+", "");
        }
        if (type.equals("") || type.equals("$")) {
            str = str.replaceAll(zero + "+$", "");
        }
        if (type.equals("")) {
            str = str.replaceAll(zero + "{2}/g", "");
        }
        return str;
    }

    private static final int MSG_SOUND = 100;

    private static class TTSHandler extends Handler {

        private WeakReference<TTSManager> weakReference;

        public TTSHandler(TTSManager ttsManager) {
            weakReference = new WeakReference<>(ttsManager);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SOUND:
                    weakReference.get().playSoundPool();
                    break;
            }
        }
    }
}
