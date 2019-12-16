package com.whn946.parrottongue;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class InternetUtil {

    /**
     * 网络连接检测
     *
     * @return
     */
    public static boolean isOnline() {
        try {
            String ip = "www.baidu.com";
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                return true;
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        } finally {
        }
        return false;
    }

    /**
     * 获取网络资源
     *
     * @param url
     * @return
     */
    public static String getDataRes(String url) {
        String res = null;
        try {
            res = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute().body().string();
        } catch (IOException e) {
            return "false";
        }
        return res;
    }



    /**
     * 检测更新
     * @param context
     * @throws IOException
     */
    public static String[] checkUpdate(final Context context) throws IOException {
        String[] res=null;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        // 创建一个Callable任务
        Callable getJsonData = new MyCallable(context);
        Future updRes = pool.submit(getJsonData);
        try {
            res= (String[]) updRes.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 关闭线程池
        pool.shutdown();
        return res;
    }
    @SuppressWarnings("unchecked")
    static class MyCallable implements Callable {
        String[] reStr = {"false", "", "", ""}; //状态[yes no false]，版本，更新日志，下载地址
        String response = null;
        Context context=null;
        MyCallable(Context context) {
            this.context=context;
        }
        public Object call() throws Exception {
            if (isOnline()) {
                try {
                    response = getDataRes("https://version.whn946.cn/wzy-version.json");
                    JSONObject json_v = new JSONObject(response);
                    int sys_v = Integer.valueOf(SysToolUtil.getVersionCode(context));
                    int ser_v = Integer.valueOf(json_v.getString("versionCode"));
                    if (ser_v > sys_v) {
                        reStr[0] = "yes";
                        reStr[1] = json_v.getString("versionName") + "";
                        reStr[2] = json_v.getString("changeLog") + "";
                        reStr[3] = json_v.getString("downloadPath") + "";
                    } else {
                        reStr[0] = "no";
                    }
                } catch ( JSONException e) {
                    e.printStackTrace();
                }
            } else {
                reStr[0] = "false";
            }
            return reStr;
        }
    }

    /**
     * 解析更新数据
     *
     * @param res
     * @throws IOException
     */
    public static void checkData(final String[] res,final boolean isBack,Context context) throws IOException {
        if (res == null || res[0].equals("false")) {
            if(!isBack){
                SysToolUtil.msgAlert("检测失败", "请检查网络连接是否可用！", "确认",context);
            }

        } else if (res[0].equals("no")) {
            if(!isBack){
                SysToolUtil.msgAlert("暂无更新", "有什么想法可以直接联系我哦！", "确认",context);
            }

        } else if (res[0].equals("yes")) {
            SysToolUtil.dwnAlert("检测到新版本！","版本：" + res[1] + "\n更新日志：" + res[2],"浏览器下载",res[3], context);
        }
    }


}
