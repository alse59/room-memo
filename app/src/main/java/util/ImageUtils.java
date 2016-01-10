package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.example.wataru.greendao.db.ObjectImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wataru on 2015/12/28.
 */
public class ImageUtils {
    private static final int MAX_IMAGE_SIZE = 100;
    public static final byte[] INIT_BYTES = {0};
    private ImageUtils() {}

    public static Bitmap toBitmap(ImageView view) {
        if (view == null) return null;

        BitmapDrawable bitmapDrawable = (BitmapDrawable)view.getDrawable();
        if (bitmapDrawable != null) {
            return bitmapDrawable.getBitmap();
        }
        return null;

    }
    public static List<Bitmap> toArrayBitmap(List<ObjectImage> objectImages) {
        if (objectImages == null) return null;
        List<Bitmap> bitmaps = new ArrayList<>();
        for (ObjectImage objectImage : objectImages) {
            byte[] imageBytes = objectImage.getObjectImage();
            bitmaps.add(toBitmap(imageBytes));
        }
        return bitmaps;
    }

    public static Bitmap toBitmap(byte[] bytes) {
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    public static byte[] toBites(ImageView view) {
        Bitmap bitmap = toBitmap(view);
        if (bitmap == null) return INIT_BYTES;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, MAX_IMAGE_SIZE, baos);
        return baos.toByteArray();
    }

    public static byte[] toBites(Bitmap bitmap) {
        if (bitmap == null) return INIT_BYTES;
        List<Byte> list = new ArrayList<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }
}
