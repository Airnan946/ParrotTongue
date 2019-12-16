package com.whn946.parrottongue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

public class SysToolUtil {

    public String help_str = "\t\t本软件（文转音）由章鱼小哥开发，隶属于章鱼哥之家工作室作品，软件仅供学习、研究使用。" +
            "\n\n● 试听音频：\n\t\t在文本框中输入文本点击播放即可。" +
            "\n\n● 调节音效：\n\t\t无音频播放时，滑动语调、语速进行调整。" +
            "\n\n● 切换引擎：\n\t\t点击切换引擎工具条进行选择。" +
            "\n\n● 下载引擎：\n\t\t在关于页面点击[下载更多引擎]进行选择下载。" +
            "\n\n● 问题反馈：\n\t\t在关于页面加群或直接与我联系进行反馈。";
    public String privacy_str = "\t\t本软件尊重并保护所有使用服务用户的个人隐私权。" +
            "为了给您提供更准确、更有个性化的服务，本软件会按照本隐私权政策的规定使用和披露您的个人信息。" +
            "但本软件将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，" +
            "在未征得您事先许可的情况下，本软件不会将这些信息对外披露或向第三方提供。本软件会不时更新本隐私权政策。" +
            "您在同意本软件服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于本软件服务使用协议不可分割的一部分。" +
            "\n\n本软件共申请三项权限：\n\t\t1：读取您的SD卡中的内容。\n\t\t2：修改或删除您的SD卡中的内容。\n\t\t3：网络访问" +
            "\n前两项权限仅用于导出mp3音频至本地储存空间，第三项仅用于检测更新。" +
            "\n\n\t\t除以上说明外，本软件并不含有任何收集用户隐私的地方。如今后需要会另行通知。";

    /**
     * 获取VersionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        PackageInfo pInfo = null;
        String version = "NotFound";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode + ""; //如果获取序列号则使用 pInfo.versionCode

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

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

}
