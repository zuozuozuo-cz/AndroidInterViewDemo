
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {

    /**
     * 分享图片
     * @param context 上下文
     * @param imagePath 图片路径（绝对路径）
     */
    public static void shareImage(Context context, String imagePath) {
        if (imagePath == null) return;

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) return;

        Uri uri = getUriFromFile(context, imageFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    /**
     * 打开系统图片查看器展示图片
     * @param context 上下文
     * @param imagePath 图片路径（绝对路径）
     */
    public static void openImage(Context context, String imagePath) {
        if (imagePath == null) return;

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) return;

        Uri uri = getUriFromFile(context, imageFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }

    public static Uri saveImageToAlbum(Context context, String srcPath, String pictureName) {
//        if (TextUtils.isEmpty(srcPath)) return null;

        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            Log.e("SaveImage", "源文件不存在: " + srcPath);
            return null;
        }

        ContentResolver resolver = context.getContentResolver();
        Uri imageUri = null;
        OutputStream out = null;
        InputStream in = null;

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, pictureName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // 建议写具体类型

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+，存到系统相册 Pictures/MyApp 目录下
                values.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/MyApp");
            }

            // 1. 插入 MediaStore
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (imageUri == null) {
                Log.e("SaveImage", "MediaStore insert 失败，返回 null");
                return null;
            }

            // 2. 文件拷贝
            in = new FileInputStream(srcFile);
            out = resolver.openOutputStream(imageUri);

            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();

            Log.i("SaveImage", "保存成功: " + imageUri);
            return imageUri;

        } catch (Exception e) {
            e.printStackTrace();
            if (imageUri != null) {
                // 写失败就删除空记录
                resolver.delete(imageUri, null, null);
            }
            return null;
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException ignore) {}
        }
    }



    /**
     * 兼容 Android 7.0+ FileProvider 获取 Uri
     */
    private static Uri getUriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 这里的 "com.example.fileprovider" 要和 AndroidManifest.xml 里的 authority 保持一致
            return FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    file
            );
        } else {
            return Uri.fromFile(file);
        }
    }
}
