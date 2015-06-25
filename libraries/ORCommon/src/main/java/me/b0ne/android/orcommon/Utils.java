package me.b0ne.android.orcommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by b0ne on 14/01/06.
 */
public class Utils {

    /**
     * ファイル保存するディレクトリパスを取得する。
     * ディレクトリが存在していなければ作成される。
     *
     * @return
     */
    public static String getDirectoryPath(String dirName) {
        File baseDir = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir.getPath();
    }

    /**
     * ネットワークアクセス可能かどうか確認する
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return cm.getActiveNetworkInfo().isConnected();
        }
        return false;
    }

    /**
     * 文字列のMD5化
     *
     * @param string
     * @return String
     */
    public static String md5String(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 文字列のsha256化
     *
     * @param target
     * @return
     */
    public static String sha256String(String target) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(target.getBytes());
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                buf.append(String.format("%02x", digest[i]));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }

    /**
     * 正規表現ではなく文字列の置換を行う。大文字、小文字の区別をせずに置換する
     *
     * @param str
     * @param target
     * @param replacement
     * @return
     */
    public static String replaceAll(String str, String target, String replacement) {
        return Pattern.compile(Pattern.quote(target), Pattern.CASE_INSENSITIVE)
                .matcher(str).replaceAll(Matcher.quoteReplacement(replacement));
    }

    /**
     * URLの文字列であるかチェックする
     *
     * @param text
     * @return
     */
    public static boolean isMatchHttpUrl(String text) {
        String pattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(pattern);
        Matcher httpUrlMatcher = patt.matcher(text);
        return httpUrlMatcher.matches();
    }

    /**
     * ボタンをタップするタイミングにソフトキーボードを閉じる
     *
     * @param context
     * @param view
     */
    public static void cloaseKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 指定したURLをブラウザで開く
     *
     * @param activity
     * @param url
     */
    @Deprecated
    public static void gotoWeb(Activity activity, String url) {
        gotoWeb(activity.getApplicationContext(), url);
    }

    public static void gotoWeb(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * ディプレイサイズを見てタブレットかどうかチェックする
     *
     * @param activity
     * @return
     */
    public static boolean isTablet(Activity activity) {
        // 4.x より古いバージョンは除く
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return false;
        }

        Context context = activity.getApplicationContext();
        int orientation = context.getResources().getConfiguration().orientation;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);

        // 縦向きのとき
        if (orientation == Configuration.ORIENTATION_PORTRAIT
                && p.x > 1100) {
            return true;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE
                && p.y > 1100) {
            // 横向きのとき
            return true;
        }

        return false;
    }

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}