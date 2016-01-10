package com.example.wataru.room_memo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by wataru on 2015/12/27.
 */
public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Bitmap> mBitmaps;
    private boolean hasResource;

    private final Object mLock = new Object();

    private boolean mNotifyOnChange = true;

    public MyPagerAdapter(Context context, List<Bitmap> bitmaps) {
        mContext = context;
        mBitmaps = bitmaps;
        if (mBitmaps.size() > 0) {
            hasResource = true;
        }
    }

    private Bitmap getBitmapNoData() {
        return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.no_data);
    }

    public List<Bitmap> getBitmaps() {
        return mBitmaps;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ImageView iv = new ImageView(mContext);
        if (hasResource) {
            iv.setImageBitmap(mBitmaps.get(position));
        } else {
            iv.setImageResource(R.mipmap.no_data);
        }
        ((ViewPager)container).addView(iv, 0);
        return iv;
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
        ((ViewPager) collection).removeView((ImageView) view);
    }


    /**
     * Adds the specified object at the end of the array.
     *
     * @param bitmap The object to add at the end of the array.
     */
    public void add(Bitmap bitmap) {
        synchronized (mLock) {
            if (hasResource) {
                mBitmaps.add(bitmap);
            } else {
                insert(bitmap, 0);
                hasResource = true;
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param bitmap The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(Bitmap bitmap, int index) {
        synchronized (mLock) {
            mBitmaps.add(index, bitmap);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param bitmap The object to remove.
     */
    public void remove(Bitmap bitmap) {
        synchronized (mLock) {
                mBitmaps.remove(bitmap);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mBitmaps.size() == 0) {
            return 1;
        }
        return mBitmaps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==(View)object;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
}
