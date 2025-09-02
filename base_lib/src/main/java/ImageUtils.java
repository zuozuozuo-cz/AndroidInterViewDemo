
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;

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
