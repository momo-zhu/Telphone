package cn.zicoo.ir2teledemo.common;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.zicoo.ir2teledemo.R;
import cn.zicoo.ir2teledemo.model.CallRecord;


/**
 * Created by Admin on 2017/7/14.
 */

public class Util {
    /*判断是否含有中文*/
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /*判断是否为数字和字母组合*/
    public static boolean isLegalPassword(String str) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /*手机号是否合法*/
    public static boolean isLegal(String number) {
        Pattern p = Pattern.compile("^((13[0-9])|147|(15[0-9])|(17[0-9])|(18[0-9]))[0-9]{8}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

//
//    /*显示错误提示对话框*/
//    public static SweetAlertDialog showErrorDialog(Activity aty, String strMsg) {
//        SweetAlertDialog dialog = new SweetAlertDialog(aty, SweetAlertDialog.ERROR_TYPE)
//                .setTitleText(R.string.error)
//                .setContentText(failMessage(strMsg))
//                .setConfirmClickListener(null);
//        dialog.show();
//        return dialog;
//    }


    /**
     * 初始化输入监听事件
     */
//    public static TextWatcher getTextWatcher(final MaterialEditText compareView, final MaterialEditText compareView2, final TextView statusView) {
//        TextWatcher watcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                boolean enable = false;
//                if (compareView2 == null) {
//                    if (compareView.getText().toString().trim().length() != 0)
//                        enable = true;
//                } else {
//                    if (compareView.getText().toString().trim().length() != 0 && compareView2.getText().toString().trim().length() != 0)
//                        enable = true;
//                }
//                statusView.setEnabled(enable);
//            }
//        };
//        return watcher;
//    }

    /*悬浮窗监听*/
//    public static void setWindowListener(Activity aty, EditText editText) {
//        final TipWindow tipWindow = new TipWindow(aty).getTipPopupWindow(editText);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!tipWindow.isWindowShowing())
//                    tipWindow.showWindow();
//                tipWindow.setTipText(s.toString());
//            }
//        });
//    }

    /*对字符串进行加密*/
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }


    /*保留两位小数*/
    public static String saveTwoPoint(double amount) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(amount);
    }

    // Base64加密
    public static String encodeBase64(String str) {
        if (str == null)
            return null;
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    //Base64解密
    public static String decodeBase64(String str) {
        if (str == null)
            return null;
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    /*计算两个日期之间相差的天数*/
    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return Math.abs(day2 - day1);

    }

    public static boolean isBelong(Date nowTemp, String begin, String end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTemp);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(nowTemp));
            beginTime = df.parse(begin);
            endTime = df.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = belongCalendar(now, beginTime, endTime);
        return flag;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        if (nowTime == null || beginTime == null || endTime == null) return false;
        return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() <= endTime.getTime();
    }

    /*判断是否是金额*/
    public static double getDigit(String digitStr) {
        double digit = -1;
        try {
            digit = Double.parseDouble(digitStr);
        } catch (Exception e) {
            return digit;
        }
        return digit;
    }


    /*
     计算 GridView 高度
    */
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columnNum) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i += columnNum) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + gridView.getVerticalSpacing();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    /*计算单个控件高度*/
    public static int signalHeight(List<View> views) {
        int totalHeight = 0;
        if (views == null)
            totalHeight = -1;
        else {
            for (int i = 0; i < views.size(); i++) {
                View item = views.get(i);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight();
            }
        }
        return totalHeight;
    }

    /*计算单个控件宽度*/
    public static int signalWidth(List<View> views) {
        int totalWidth = 0;
        if (views == null)
            totalWidth = -1;
        else {
            for (int i = 0; i < views.size(); i++) {
                View item = views.get(i);
                item.measure(0, 0);
                totalWidth += item.getMeasuredWidth();
            }
        }
        return totalWidth;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*携带E的浮点型*/
    public static String double2string(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(amount);
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return 0 (相等) 1 (version1 > version2)
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }


    /*请求权限*/
    public static void requestPermission(Activity aty, String permission) {
        ActivityCompat.requestPermissions(aty, new String[]{permission},
                Config.REQUEST_PERMISSION);
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    private static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    private static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212,
            3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925,
            5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    private static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    private static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {
            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                //判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
                if ((ch > 'A' && ch < 'Z') || (ch > 'a' && ch < 'z')) {
                    buffer.append(String.valueOf(ch).toUpperCase());
                } else {
                    buffer.append("#");
                }
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell).toUpperCase());
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }


    public static String handleHtml(String input) {
        StringBuilder s = new StringBuilder(input.replaceAll("<.*?>|<.*?/.*?>", ""));
        while (s.length() > 0 && s.charAt(s.length() - 1) == '\n')
            s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

//    public static void showSuccess(View view) {
//        Snackbar.make(view, R.string.success_operation, Snackbar.LENGTH_SHORT).show();
//    }
//
//    public static void showMessage(View view, String message) {
//        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
//    }


    /*
    设置Listview高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int totalHeight = 0;
        if (listAdapter != null) {

            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            totalHeight += listView.getDividerHeight() * (listAdapter.getCount() - 1);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    /*
    GridView
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int columns = gridView.getNumColumns();
        for (int i = 0; i < listAdapter.getCount(); i += columns) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + gridView.getVerticalSpacing();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }


    public static boolean isEventInView(MotionEvent event, View view) {
        if (view != null) {
            int[] leftTop = {0, 0};
            view.getLocationOnScreen(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            return event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom;
        }
        return false;
    }



    /*
       文件选择
     */

    public static void showFileChooser(Activity acy, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            acy.startActivityForResult(Intent.createChooser(intent, "选择文件"), requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
//            ViewInject.toast("未发现文件管理器");
        }
    }

    /*
    clickspan
     */
//    public static ClickableSpan getDownLoadClickSpan(final Activity acy, final String storeFilePath, final String fileName, final long fileSize, final String url) {
//        return new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                downloadFile(acy, url, storeFilePath, fileSize, fileName);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setColor(Color.rgb(13, 140, 209));
//                ds.setUnderlineText(false);
//            }
//        };
//    }

    //    //简体转成繁体
//    public static String s2t(String changeText) {
//        try {
//            JChineseConvertor jChineseConvertor = JChineseConvertor
//                    .getInstance();
//            changeText = jChineseConvertor.s2t(changeText);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return changeText;
//    }
//
//    //繁体转成简体
//    public static String t2s(String changeText) {
//        Log.e("xj", "t2s: changeText--" + changeText);
//        try {
//            JChineseConvertor jChineseConvertor = JChineseConvertor
//                    .getInstance();
//            changeText = jChineseConvertor.t2s(changeText);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return changeText;
//    }


//    public static void downloadFile(final Activity acy, final String url, final String storeFilePath, final long fileSize, final String fileName) {
//        final File file = new File(storeFilePath);
//        if (file.exists() && file.length() == fileSize) {
//            new SweetAlertDialog(acy, SweetAlertDialog.SUCCESS_TYPE).
//                    setTitleText("已下载").
//                    setContentText("路径:" + storeFilePath).
//                    showCancelButton(true).
//                    setCancelText("取消").
//                    setConfirmText("查看").
//                    setCancelClickListener(null).
//                    setConfirmClickListener(sweetAlertDialog -> {
//                        sweetAlertDialog.dismissWithAnimation();
//                        DisplayUtils.openWithOtherApp(acy, new File(storeFilePath));
//                    }).show();
//            return;
//        }
//        final KJHttp kjHttp = new KJHttp();
//        View view = View.inflate(acy, R.layout.download, null);
//        final ProgressBar progressBar = ViewFindUtils.find(view, R.id.progressBar);
//        final SweetAlertDialog dialog = new SweetAlertDialog(acy, SweetAlertDialog.CUSTOM_TYPE).setCustomView(view)
//                .setTitleText(fileName).setContentText("0B").setCancelClickListener(
//                        sweetAlertDialog -> {
//                            sweetAlertDialog.dismissWithAnimation();
//                            if(file.exists())
//                                file.delete();
//                            kjHttp.getDownloadController(storeFilePath, url).removeTask();
//                        }).showCancelButton(true).showConfirmButton(false);
////        Map<String, String> headers = new HashMap<>();
////        headers.put("Authorization", app.getAuth(Config.NOMAL_AUTH));
//        kjHttp.download(storeFilePath, url, new HttpCallBack() {
//            @Override
//            public void onPreStart() {
//                progressBar.setMax(100);
//                progressBar.setProgress(0);
//                dialog.show();
//            }
//
//            @Override
//            public void onSuccess(Map<String, String> headers, byte[] t) {
//                dialog.setTitleText("下载完成").
//                        setContentText("路径:" + storeFilePath).
//                        setConfirmText("查看").
//                        setCancelClickListener(null).
//                        setConfirmClickListener(sweetAlertDialog -> {
//                            dialog.dismissWithAnimation();
//                            DisplayUtils.openWithOtherApp(acy, new File(storeFilePath));
//                        }).changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
//                        .showCancelButton(true);
//
////                ViewInject.toast("下载完成,路径:" + storeFilePath);
//            }
//
//
//            @Override
//            public void onFailure(int errorNo, final String strMsg) {
//                acy.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.setTitleText("下载出错!")
//                                .setContentText(strMsg)
//                                .setCancelClickListener(null)
//                                .setConfirmClickListener(null)
//                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
//                    }
//                });
//            }
//
//
//            @Override
//            public void onLoading(final long count, final long current) {
//                acy.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.setContentText(getFileLength(current));
//                        progressBar.setProgress((int) (current * 100 / count));
//                    }
//                });
//            }
//        });
//    }

    /*
    图片压缩
     */

//    public static File scalImage(Uri fileUri) {
//        String path = fileUri.getPath();
//        File outputFile = new File(path);
//        long fileSize = outputFile.length();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        int height = options.outHeight;
//        int width = options.outWidth;
//        if (height == -1) return null;
//
//        double scale = Math.sqrt((float) fileSize / Config.fileMaxSize);
//        options.outHeight = (int) (height / scale);
//        options.outWidth = (int) (width / scale);
//        options.inSampleSize = (int) (scale + 0.5);
//        options.inJustDecodeBounds = false;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//        if (fileSize >= Config.fileMaxSize) {
//            outputFile = new File(createImageFile().getPath());
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(outputFile);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (!bitmap.isRecycled()) {
//                bitmap.recycle();
//            } else {
//                File tempFile = outputFile;
//                outputFile = new File(createImageFile().getPath());
//                FileUtils.copyFile(tempFile, outputFile);
//            }
//
//        }
//        return outputFile;
//
//    }

    /*
    图片压缩
     */

    public static boolean isImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return options.outHeight != -1;

    }

    /**
     * 通知媒体库更新文件
     *
     * @param context
     * @param filePath 文件全路径
     */
    public void scanFile(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }

    public static Uri createImageFile() {
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }


    public static String getFileLength(long length) {
        if (length < 1024) return length + "B";
        if (length < 1024 * 1024) return String.format(Locale.CHINA, "%.1fKB", length / 1024f);
        if (length < 1024 * 1024 * 1024)
            return String.format(Locale.CHINA, "%.1fMB", length / (1024 * 1024f));
        return String.format(Locale.CHINA, "%.1fGB", length / (1024 * 1024 * 1024f));
    }

    public static String getDistance(int distance) {
        if (distance < 1000) return distance + "m";
        return String.format(Locale.CHINA, "%.1fKm", distance / 1000f);
    }

    public static String url2name(String url) {
        int index = url.lastIndexOf('/');
        return index > -1 ? url.substring(index + 1) : url;
    }

    public static String getWebViewData(String content) {
        return "<html><head lang='zh-CN'><meta charset='UTF-8'><title></title><style>" +
                "body{padding:0px;font-size:16px;font-style:normal;font-weight:normal;" +
                "font-variant:normal;color:#666666;text-decoration:none;line-height:1.5;" +
                "margin:6px;}.fullimg{width:100%;-moz-border-radius:5px;border-radius:5px;}" +
                "</style></head><body>" + content + "</body></html>";
    }

    public static Date translate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new Date(calendar.getTimeInMillis() + calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET));
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 10000) {       //10秒内按钮无效
            return true;
        }
        lastClickTime = time;
        return false;
    }


    //分享文字
    public static void shareText(Context context, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享"));
    }

    //分享单张图片
    public static void shareSingleImage(Context context, String url) {
        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

//    /**
//     * 演示调用ShareSDK执行分享
//     *
//     * @param context
//     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
//     * @param showContentEdit 是否显示编辑页
//     */
//    public static void showShare(Context context, String platformToShare, boolean showContentEdit, ShareModel model) {
//        OnekeyShare oks = new OnekeyShare();
//        oks.setSilent(!showContentEdit);
//        if (platformToShare != null) {
//            oks.setPlatform(platformToShare);
//        }
//        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
//        oks.setTheme(OnekeyShareTheme.CLASSIC);
//        // 令编辑页面显示为Dialog模式
//        oks.setDialogMode();
//        // 在自动授权时可以禁用SSO方式
//        oks.disableSSOWhenAuthorize();
//        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
//        oks.setTitle(model.getTitle());
////        oks.setTitleUrl("http://mob.com");
//        oks.setText(model.getText());
//        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
//        oks.setImageUrl(model.getImageUrl());
//        oks.setUrl(model.getUrl()); //微信不绕过审核分享链接
//        //oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
//        oks.setComment(model.getComment()); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
//        oks.setSite(model.getSite());  //QZone分享完之后返回应用时提示框上显示的名称
//        oks.setSiteUrl(model.getSiteUrl());//QZone分享参数
//        oks.setVenueName(model.getVenueName());
//        oks.setVenueDescription(model.getVenueDescription());
//        // 将快捷分享的操作结果将通过OneKeyShareCallback回调
//        //oks.setCallback(new OneKeyShareCallback());
//        // 去自定义不同平台的字段内容
//        //oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
//        // 在九宫格设置自定义的图标
//        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//        String label = "分享";
//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        };
//        oks.setCustomerLogo(logo, label, listener);
//
//        // 为EditPage设置一个背景的View
//        //oks.setEditPageBackground(getPage());
//        // 隐藏九宫格中的新浪微博
//        // oks.addHiddenPlatform(SinaWeibo.NAME);
//
//        // String[] AVATARS = {
//        // 		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
//        // 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
//        // 		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
//        // 		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
//        // 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
//        // 		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
//        // oks.setImageArray(AVATARS);              //腾讯微博和twitter用此方法分享多张图片，其他平台不可以
//
//        // 启动分享
//        oks.show(context);
//    }

//    public static void drawCertificate(Context cxt){
//        Bitmap bmp= BitmapFactory.decodeResource(cxt.getResources(), R.drawable.ic_avater);
//        Canvas canvas = new Canvas();
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);                              //设置画笔的抗锯齿
//        paint.setColor(Color.WHITE);                           //设置画笔的颜色
//        paint.setStyle(Paint.Style.FILL);                      //设置画出的图形填充的类型,fill为内部填充,stroke为只有边框,内容不填充
//    }


    /*获得*/
    public static float getBaseLable(float maxData) {
        float base = 0;
        String maxStr = String.valueOf(maxData);
        if (maxStr.contains(".")) {
            String subStr = maxStr.substring(0, maxStr.indexOf("."));
            if (subStr.length() != 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < subStr.length(); i++) {
                    if (i == 0) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                }
                int temp = Integer.parseInt(sb.toString());
                if (temp < maxData) {
                    if (temp * 5 < maxData) {
                        base = temp * 5;
                    } else {
                        base = temp;
                    }
                } else {
                    base = temp;
                }
            } else {
                if (subStr.equals("1")) {
                    base = (float) 0.5;
                } else {
                    String subStr2 = maxStr.substring(maxStr.indexOf(".") + 1);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < subStr2.length(); i++) {
                        if (i == 0) {
                            sb.append("0.");
                        } else {
                            sb.append(0);
                        }
                    }
                    sb.append(1);
                    float temp = Float.valueOf(sb.toString());
                    if (temp < maxData) {
                        if (temp * 5 < maxData) {
                            base = temp * 5;
                        } else {
                            base = temp;
                        }
                    } else {
                        base = temp;
                    }

                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < maxStr.length(); i++) {
                if (i == 0) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }
            }
            int temp = Integer.parseInt(sb.toString());
            if (temp < maxData) {
                if (temp * 5 < maxData) {
                    base = temp * 5;
                } else {
                    base = temp;
                }
            } else {
                base = temp;
            }
        }
        return base;
    }


    /*权限检查*/
    public static boolean checkPermission(Activity aty, String... permissions) {
        List<String> ps = new ArrayList<>();
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(aty, p) != PackageManager.PERMISSION_GRANTED) {
                ps.add(p);
            }
        }
        if (!ps.isEmpty()) {
            String[] array = ps.toArray(new String[]{});
            ActivityCompat.requestPermissions(aty, array, Config.REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    /*处理权限检查结果*/
    public static void dealPermissionRequest(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == Config.REQUEST_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissions.length; i++)
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = activity.shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            switch (permissions[i]) {
                                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                    showDialogTipUserGoToAppSettting(activity, activity.getString(R.string.no_storage_permission),
                                            activity.getString(R.string.please_open_storage_permission), Config.REQUEST_PERMISSION_STORAGE);
                                    break;
                                case Manifest.permission.CAMERA:
                                    showDialogTipUserGoToAppSettting(activity, activity.getString(R.string.no_camera_permission),
                                            activity.getString(R.string.please_open_camera_permission), Config.REQUEST_PERMISSION_CAMERA);
                                    break;
                                case Manifest.permission.ACCESS_FINE_LOCATION:
                                    showDialogTipUserGoToAppSettting(activity, activity.getString(R.string.no_location_permission),
                                            activity.getString(R.string.please_open_location_permission), Config.REQUEST_PERMISSION_LOCATION);
                                    break;
                                default:
                                    showDialogTipUserGoToAppSettting(activity, activity.getString(R.string.no_permission),
                                            activity.getString(R.string.please_open_permission), Config.REQUEST_PERMISSION);
                                    break;
                            }
                        } else
                            Toast.makeText(activity, activity.getString(R.string.can_not_use_this_operation), Toast.LENGTH_SHORT).show();

//                        ViewInject.toast(activity.getString(R.string.can_not_use_this_operation));
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.permission_get_successful), Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }
    // 提示用户去应用设置界面手动开启权限

    public static void showDialogTipUserGoToAppSettting(final Activity activity, String title, String message, final int requestCode) {
        AlertDialog.Builder requestDialog = new AlertDialog.Builder(activity);
        requestDialog.setTitle(title);
        requestDialog.setMessage(message);
        requestDialog.setPositiveButton(activity.getString(R.string.open_now), (dialog, which) -> goToAppSetting(activity, requestCode));
        requestDialog.setNegativeButton(activity.getString(R.string.cancel), null);
        requestDialog.setCancelable(false);
        requestDialog.show();
    }

    public static String getDateDiff(Date postDate) {
        if (DateUtils.isSameDay(postDate, new Date()))
            return cn.zicoo.ir2teledemo.common.DateUtils.formatDate(postDate, "HH:mm:ss");
        return cn.zicoo.ir2teledemo.common.DateUtils.formatDate(postDate, "MM-dd HH:mm");
    }

    // 跳转到当前应用的设置界面
    private static void goToAppSetting(Activity activity, int requsetCode) {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);

        activity.startActivityForResult(intent, requsetCode);
    }

    //处理权限获取结果
    public static void dealPermissionOpen(Activity activity, String subject, String message, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = -1;
            switch (requestCode) {
                case Config.REQUEST_PERMISSION_STORAGE:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    break;
                case Config.REQUEST_PERMISSION_CAMERA:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
                    break;
                case Config.REQUEST_PERMISSION_AUDIO:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
                    break;
                case Config.REQUEST_PERMISSION_LOCATION:
                    i = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
                    break;
            }
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 提示用户应该去应用设置界面手动开启权限
                showDialogTipUserGoToAppSettting(activity, subject, message, requestCode);
            } else {
//                if (requestDialog != null && requestDialog.isShowing()) {
//                    requestDialog.dismiss();
//                }
                Toast.makeText(activity, activity.getString(R.string.permission_get_successful), Toast.LENGTH_SHORT).show();
            }
        }

    }


    public static Bitmap getFirstFrameFromVideo(String url) {
        //创建MediaMetadataRetriever对象
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//设置资源位置
//绑定资源
        mmr.setDataSource(url, new HashMap<String, String>());
//获取第一帧图像的bitmap对象
        return mmr.getFrameAtTime(1);
//加载到ImageView控件上
    }

    public static int[] wiFiIp(Context inContext) {
        WifiManager mWifiManager = (WifiManager) inContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();

        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            return new int[]{ipAddress & 0xFF, 0xFF & ipAddress >> 8, 0xFF & ipAddress >> 16, 0xFF & ipAddress >> 24};
        } else {
            return null;
        }
    }

    public static List<CallRecord> callRecords(Context context, int pageNo, int pageSize) {
        // 1.获得ContentResolver
        ContentResolver resolver = context.getContentResolver();
        String[] projections = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.DURATION};
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         */
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                projections, null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        // 3.通过Cursor获得数据
        List<CallRecord> list = new ArrayList<>();
        if (cursor.getCount() <= (pageNo - 1) * pageSize) return list;
        cursor.moveToPosition((pageNo - 1) * pageSize);
        int i = 0;
        while (cursor.moveToNext() && i++ < 20) {
            String number = cursor.getString(0);
            long dateLong = cursor.getLong(1);
            int type = cursor.getInt(2);
            int duration = cursor.getInt(3);
            CallRecord record = new CallRecord();
            record.Number = number;
            record.Date = new Date(dateLong);
            record.Type = type;
            record.Duration = duration;
            list.add(record);
        }
        return list;
    }

}

