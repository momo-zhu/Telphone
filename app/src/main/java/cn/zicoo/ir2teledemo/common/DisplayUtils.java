package cn.zicoo.ir2teledemo.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.widget.EditText;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * emoji显示规则
 *
 * @author kymjs (http://www.kymjs.com/) on 6/8/15.
 */
public class DisplayUtils {


    private static final Map<String, String> MIME_TABLE = new HashMap<>();

    static {
        MIME_TABLE.put("323", "text/h323");
        MIME_TABLE.put("acx", "application/internet-property-stream");
        MIME_TABLE.put("ai", "application/postscript");
        MIME_TABLE.put("aif", "audio/x-aiff");
        MIME_TABLE.put("aifc", "audio/x-aiff");
        MIME_TABLE.put("aiff", "audio/x-aiff");
        MIME_TABLE.put("asf", "video/x-ms-asf");
        MIME_TABLE.put("asr", "video/x-ms-asf");
        MIME_TABLE.put("asx", "video/x-ms-asf");
        MIME_TABLE.put("au", "audio/basic");
        MIME_TABLE.put("apk", "application/vnd.android.package-archive");
        MIME_TABLE.put("avi", "video/x-msvideo");
        MIME_TABLE.put("axs", "application/olescript");
        MIME_TABLE.put("bas", "text/plain");
        MIME_TABLE.put("bcpio", "application/x-bcpio");
        MIME_TABLE.put("bin", "application/octet-stream");
        MIME_TABLE.put("bmp", "image/bmp");
        MIME_TABLE.put("c", "text/plain");
        MIME_TABLE.put("cat", "application/vnd.ms-pkiseccat");
        MIME_TABLE.put("cdf", "application/x-cdf");
        MIME_TABLE.put("cer", "application/x-x509-ca-cert");
        MIME_TABLE.put("class", "application/octet-stream");
        MIME_TABLE.put("clp", "application/x-msclip");
        MIME_TABLE.put("cmx", "image/x-cmx");
        MIME_TABLE.put("cod", "image/cis-cod");
        MIME_TABLE.put("cpio", "application/x-cpio");
        MIME_TABLE.put("crd", "application/x-mscardfile");
        MIME_TABLE.put("crl", "application/pkix-crl");
        MIME_TABLE.put("crt", "application/x-x509-ca-cert");
        MIME_TABLE.put("csh", "application/x-csh");
        MIME_TABLE.put("css", "text/css");
        MIME_TABLE.put("dcr", "application/x-director");
        MIME_TABLE.put("der", "application/x-x509-ca-cert");
        MIME_TABLE.put("dir", "application/x-director");
        MIME_TABLE.put("dll", "application/x-msdownload");
        MIME_TABLE.put("dms", "application/octet-stream");
        MIME_TABLE.put("doc", "application/msword");
        MIME_TABLE.put("dot", "application/msword");
        MIME_TABLE.put("dvi", "application/x-dvi");
        MIME_TABLE.put("dxr", "application/x-director");
        MIME_TABLE.put("eps", "application/postscript");
        MIME_TABLE.put("etx", "text/x-setext");
        MIME_TABLE.put("evy", "application/envoy");
        MIME_TABLE.put("exe", "application/octet-stream");
        MIME_TABLE.put("fif", "application/fractals");
        MIME_TABLE.put("flr", "x-world/x-vrml");
        MIME_TABLE.put("gif", "image/gif");
        MIME_TABLE.put("gtar", "application/x-gtar");
        MIME_TABLE.put("gz", "application/x-gzip");
        MIME_TABLE.put("h", "text/plain");
        MIME_TABLE.put("hdf", "application/x-hdf");
        MIME_TABLE.put("hlp", "application/winhlp");
        MIME_TABLE.put("hqx", "application/mac-binhex40");
        MIME_TABLE.put("hta", "application/hta");
        MIME_TABLE.put("htc", "text/x-component");
        MIME_TABLE.put("htm", "text/html");
        MIME_TABLE.put("html", "text/html");
        MIME_TABLE.put("htt", "text/webviewhtml");
        MIME_TABLE.put("ico", "image/x-icon");
        MIME_TABLE.put("ief", "image/ief");
        MIME_TABLE.put("iii", "application/x-iphone");
        MIME_TABLE.put("ins", "application/x-internet-signup");
        MIME_TABLE.put("isp", "application/x-internet-signup");
        MIME_TABLE.put("jfif", "image/pipeg");
        MIME_TABLE.put("jpe", "image/jpeg");
        MIME_TABLE.put("jpeg", "image/jpeg");
        MIME_TABLE.put("jpg", "image/jpeg");
        MIME_TABLE.put("js", "application/x-javascript");
        MIME_TABLE.put("latex", "application/x-latex");
        MIME_TABLE.put("lha", "application/octet-stream");
        MIME_TABLE.put("lsf", "video/x-la-asf");
        MIME_TABLE.put("lsx", "video/x-la-asf");
        MIME_TABLE.put("lzh", "application/octet-stream");
        MIME_TABLE.put("m13", "application/x-msmediaview");
        MIME_TABLE.put("m14", "application/x-msmediaview");
        MIME_TABLE.put("m3u", "audio/x-mpegurl");
        MIME_TABLE.put("man", "application/x-troff-man");
        MIME_TABLE.put("mdb", "application/x-msaccess");
        MIME_TABLE.put("me", "application/x-troff-me");
        MIME_TABLE.put("mht", "message/rfc822");
        MIME_TABLE.put("mhtml", "message/rfc822");
        MIME_TABLE.put("mid", "audio/mid");
        MIME_TABLE.put("mny", "application/x-msmoney");
        MIME_TABLE.put("mov", "video/quicktime");
        MIME_TABLE.put("movie", "video/x-sgi-movie");
        MIME_TABLE.put("mp2", "video/mpeg");
        MIME_TABLE.put("mp3", "audio/mpeg");
        MIME_TABLE.put("mpa", "video/mpeg");
        MIME_TABLE.put("mpe", "video/mpeg");
        MIME_TABLE.put("mpeg", "video/mpeg");
        MIME_TABLE.put("mpg", "video/mpeg");
        MIME_TABLE.put("mpp", "application/vnd.ms-project");
        MIME_TABLE.put("mpv2", "video/mpeg");
        MIME_TABLE.put("ms", "application/x-troff-ms");
        MIME_TABLE.put("mvb", "application/x-msmediaview");
        MIME_TABLE.put("nws", "message/rfc822");
        MIME_TABLE.put("oda", "application/oda");
        MIME_TABLE.put("p10", "application/pkcs10");
        MIME_TABLE.put("p12", "application/x-pkcs12");
        MIME_TABLE.put("p7b", "application/x-pkcs7-certificates");
        MIME_TABLE.put("p7c", "application/x-pkcs7-mime");
        MIME_TABLE.put("p7m", "application/x-pkcs7-mime");
        MIME_TABLE.put("p7r", "application/x-pkcs7-certreqresp");
        MIME_TABLE.put("p7s", "application/x-pkcs7-signature");
        MIME_TABLE.put("pbm", "image/x-portable-bitmap");
        MIME_TABLE.put("pdf", "application/pdf");
        MIME_TABLE.put("pfx", "application/x-pkcs12");
        MIME_TABLE.put("pgm", "image/x-portable-graymap");
        MIME_TABLE.put("pko", "application/ynd.ms-pkipko");
        MIME_TABLE.put("pma", "application/x-perfmon");
        MIME_TABLE.put("pmc", "application/x-perfmon");
        MIME_TABLE.put("pml", "application/x-perfmon");
        MIME_TABLE.put("pmr", "application/x-perfmon");
        MIME_TABLE.put("pmw", "application/x-perfmon");
        MIME_TABLE.put("pnm", "image/x-portable-anymap");
        MIME_TABLE.put("png", "image/png");
        MIME_TABLE.put("pot", "application/vnd.ms-powerpoint");
        MIME_TABLE.put("ppm", "image/x-portable-pixmap");
        MIME_TABLE.put("pps", "application/vnd.ms-powerpoint");
        MIME_TABLE.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TABLE.put("prf", "application/pics-rules");
        MIME_TABLE.put("ps", "application/postscript");
        MIME_TABLE.put("pub", "application/x-mspublisher");
        MIME_TABLE.put("qt", "video/quicktime");
        MIME_TABLE.put("ra", "audio/x-pn-realaudio");
        MIME_TABLE.put("rar", "application/x-rar-compressed");
        MIME_TABLE.put("ram", "audio/x-pn-realaudio");
        MIME_TABLE.put("ras", "image/x-cmu-raster");
        MIME_TABLE.put("rgb", "image/x-rgb");
        MIME_TABLE.put("rmi", "audio/mid");
        MIME_TABLE.put("roff", "application/x-troff");
        MIME_TABLE.put("rtf", "application/rtf");
        MIME_TABLE.put("rtx", "text/richtext");
        MIME_TABLE.put("scd", "application/x-msschedule");
        MIME_TABLE.put("sct", "text/scriptlet");
        MIME_TABLE.put("setpay", "application/set-payment-initiation");
        MIME_TABLE.put("setreg", "application/set-registration-initiation");
        MIME_TABLE.put("sh", "application/x-sh");
        MIME_TABLE.put("shar", "application/x-shar");
        MIME_TABLE.put("sit", "application/x-stuffit");
        MIME_TABLE.put("snd", "audio/basic");
        MIME_TABLE.put("spc", "application/x-pkcs7-certificates");
        MIME_TABLE.put("spl", "application/futuresplash");
        MIME_TABLE.put("src", "application/x-wais-source");
        MIME_TABLE.put("sst", "application/vnd.ms-pkicertstore");
        MIME_TABLE.put("stl", "application/vnd.ms-pkistl");
        MIME_TABLE.put("stm", "text/html");
        MIME_TABLE.put("svg", "image/svg+xml");
        MIME_TABLE.put("sv4cpio", "application/x-sv4cpio");
        MIME_TABLE.put("sv4crc", "application/x-sv4crc");
        MIME_TABLE.put("swf", "application/x-shockwave-flash");
        MIME_TABLE.put("t", "application/x-troff");
        MIME_TABLE.put("tar", "application/x-tar");
        MIME_TABLE.put("tcl", "application/x-tcl");
        MIME_TABLE.put("tex", "application/x-tex");
        MIME_TABLE.put("texi", "application/x-texinfo");
        MIME_TABLE.put("texinfo", "application/x-texinfo");
        MIME_TABLE.put("tgz", "application/x-compressed");
        MIME_TABLE.put("tif", "image/tiff");
        MIME_TABLE.put("tiff", "image/tiff");
        MIME_TABLE.put("tr", "application/x-troff");
        MIME_TABLE.put("trm", "application/x-msterminal");
        MIME_TABLE.put("tsv", "text/tab-separated-values");
        MIME_TABLE.put("txt", "text/plain");
        MIME_TABLE.put("log", "text/plain");
        MIME_TABLE.put("uls", "text/iuls");
        MIME_TABLE.put("ustar", "application/x-ustar");
        MIME_TABLE.put("vcf", "text/x-vcard");
        MIME_TABLE.put("vrml", "x-world/x-vrml");
        MIME_TABLE.put("wav", "audio/x-wav");
        MIME_TABLE.put("wcm", "application/vnd.ms-works");
        MIME_TABLE.put("wdb", "application/vnd.ms-works");
        MIME_TABLE.put("wks", "application/vnd.ms-works");
        MIME_TABLE.put("wmf", "application/x-msmetafile");
        MIME_TABLE.put("wps", "application/vnd.ms-works");
        MIME_TABLE.put("wri", "application/x-mswrite");
        MIME_TABLE.put("wrl", "x-world/x-vrml");
        MIME_TABLE.put("wrz", "x-world/x-vrml");
        MIME_TABLE.put("xaf", "x-world/x-vrml");
        MIME_TABLE.put("xbm", "image/x-xbitmap");
        MIME_TABLE.put("xla", "application/vnd.ms-excel");
        MIME_TABLE.put("xlc", "application/vnd.ms-excel");
        MIME_TABLE.put("xlm", "application/vnd.ms-excel");
        MIME_TABLE.put("xls", "application/vnd.ms-excel");
        MIME_TABLE.put("xlt", "application/vnd.ms-excel");
        MIME_TABLE.put("xlw", "application/vnd.ms-excel");
        MIME_TABLE.put("xof", "x-world/x-vrml");
        MIME_TABLE.put("xpm", "image/x-xpixmap");
        MIME_TABLE.put("xwd", "image/x-xwindowdump");
        MIME_TABLE.put("z", "application/x-compress");
        MIME_TABLE.put("zip", "application/zip");
        MIME_TABLE.put("7z", "application/x-7z-compressed");


    }




    public static void openWithOtherApp(Context cxt, File file) {
        Uri uri;
        String type = getMIMEType(file.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(cxt, cxt.getPackageName(),file);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, type);
        try {
            cxt.startActivity(intent);
        } catch (ActivityNotFoundException e) {
//            ViewInject.toast("没有找到应用打开该类型的文件");
        }
    }


    public static String getMIMEType(String path) {
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex < 0 || dotIndex == path.length()) {
            return "*/*";
        }
        /* 获取文件的后缀名*/
        String end = path.substring(dotIndex + 1).toLowerCase();
        String type = MIME_TABLE.get(end);
        if (type == null)
            type = "*/*";
        return type;
    }

    public static Drawable getIcon(Context cxt, String archiveFilePath) {
        PackageManager pm = cxt.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return pm.getApplicationIcon(appInfo);//得到图标信息
        }
        return null;
    }


    public static void backspace(EditText editText) {
        if (editText == null) {
            return;
        }
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
                0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }
}
