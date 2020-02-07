package com.whn946.parrottongue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;


public class SysToolUtil {

    /**
     * 普通消息Alert
     *
     * @param title
     * @param text
     * @param btn
     * @param context
     */
    public static void msgAlert(final String title, final String text, final String btn, final Context context) {
        new Thread() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(text).setPositiveButton(btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                });
            }
        }.start();
    }

    /**
     * 下载/打开网址Alert
     *
     * @param title
     * @param text
     * @param btn
     * @param context
     */
    public static void dwnAlert(final String title, final String text, final String btn, final String durl, final Context context) {
        new Thread() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(text)
                                .setNegativeButton(btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(durl)));
                                    }
                                })
                                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                });
            }
        }.start();
    }

    /**
     * 判断应用是否存在
     *
     * @param pkgName
     * @return
     */
    public boolean isAppExist(String pkgName, Context context) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }

        return info != null;
    }

    public void uninstallAPP(String packageName, Context context) {
        Uri packageURI = Uri.parse("package:".concat(packageName));
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 设定默认配置
     */
    public static void setDefConfig(Context context) {
        //记录初次启动
        SharedPreferences app_run = context.getSharedPreferences("app_run", Context.MODE_PRIVATE);
        SharedPreferences.Editor app_run_editor = app_run.edit();
        app_run_editor.putBoolean("runtime", true);
        app_run_editor.apply();
        //配置默认引擎语言
        SharedPreferences tts_lan = context.getSharedPreferences("tts_lan", Context.MODE_PRIVATE);
        SharedPreferences.Editor tts_lan_editor = tts_lan.edit();
        tts_lan_editor.putString("tts_lan", "CHINA");
        tts_lan_editor.apply();
        //配置排号模式默认数据
        SharedPreferences config_num_mode = context.getSharedPreferences("config_num_mode", Context.MODE_PRIVATE);
        SharedPreferences.Editor config_num_mode_editor = config_num_mode.edit();
        config_num_mode_editor.putString("number", "0001");
        config_num_mode_editor.putString("leftstr", "请");
        config_num_mode_editor.putString("rightstr", "号顾客取餐");
        config_num_mode_editor.apply();

    }

}
