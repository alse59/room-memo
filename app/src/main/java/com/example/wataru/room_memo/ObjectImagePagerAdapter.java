package com.example.wataru.room_memo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wataru on 2015/12/27.
 */
public class ObjectImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Bitmap> mObjectImages;
    private boolean hasNoData;

    public ObjectImagePagerAdapter(Context context, List<Bitmap> objectImages) {
        mContext = context;
        mObjectImages = objectImages;
        if (mObjectImages == null || mObjectImages.size() == 0) {
            Bitmap bitmap = getBitmapNoData();
            mObjectImages = new ArrayList<>();
            mObjectImages.add(bitmap);
            hasNoData = true;
        }

    }
    public void add(Bitmap bitmap) {
        if (hasNoData) {
            mObjectImages.set(0, bitmap);
            hasNoData = false;
        } else {
            mObjectImages.add(bitmap);
        }

    }
    public void set(List<Bitmap> bitmaps) {
        mObjectImages = bitmaps;
    }

    public List<Bitmap> getBitmaps() {
        return mObjectImages;
    }



    private Bitmap getBitmapNoData() {
        return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.no_data);
    }
    /**
     * ページを破棄する。
     * postion番目のViweを削除するために利用
     * @param collection: 削除するViewのコンテナ
     * @param position : インスタンス削除位置
     * @param view   : instantiateItemメソッドで返却したオブジェクト
     */
    @Override
    public void destroyItem(View collection, int position, Object view) {
        //ViewPagerに登録していたTextViewを削除する
        ((ViewPager)collection).removeView((View) view);
    }

    @Override
    public int getCount() {
        return mObjectImages.size();
    }



    /**
     * ページを生成する
     * position番目のViewを生成し返却するために利用
     * @param collection: 表示するViewのコンテナ
     * @param position : インスタンス生成位置
     * @return ページを格納しているコンテナを返却すること。サンプルのようにViewである必要は無い。
     */
    @Override
    public Object instantiateItem(View collection, int position) {
        ImageView iv = new ImageView(mContext);
        Bitmap objectImage = mObjectImages.get(position);
        iv.setImageBitmap(objectImage);

        ((ViewPager)collection).addView(iv, 0);

        return iv;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((View)object);
    }
}
