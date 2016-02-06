package com.example.wataru.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.wataru.fragment.InsertFragment;
import com.example.wataru.room_memo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wataru on 2015/12/27.
 */
public class ObjectImagePagerAdapter extends PagerAdapter {
    private static final String TAG = "ObjectImagePagerAdapter";
    private Context mContext;
    private InsertFragment mFragment;
    private List<Bitmap> mBitmaps;
    private boolean hasResource;

    private final Object mLock = new Object();

    private boolean mNotifyOnChange = true;

//    public ObjectImagePagerAdapter(Context context, List<Bitmap> bitmaps) {
//        mContext = context;
//        mBitmaps = bitmaps;
//        if (mBitmaps.size() > 0) {
//            hasResource = true;
//        }
//    }

    public ObjectImagePagerAdapter(Context context, InsertFragment fragment, List<Bitmap> bitmaps) {
        mContext = context;
        mFragment = fragment;
        mBitmaps = bitmaps;
        if (mBitmaps.size() > 0) {
            hasResource = true;
        }
    }

    public List<Bitmap> getBitmaps() {
        return mBitmaps;
    }

    /**
     * URL におけるパラメータ部分を解析してディクショナリ化します。
     *
     * @param url URL。
     *
     * @return 解析結果。
     */
    private Map< String, String > parseUrlParameters( String url ) {
        Map< String, String > result = new HashMap< String, String >();
        int                   index = url.indexOf("?");
        if( index == -1 ) { return result; }

        String[] params = url.substring( index + 1 ).split("&");
        for( String param : params ) {
            String[] keyValuePair = param.split( "=" );
            if( keyValuePair.length >= 2 ) {
                try {
                    String value = URLDecoder.decode(keyValuePair[1], "utf-8");
                    result.put( keyValuePair[ 0 ], value );

                } catch( UnsupportedEncodingException e ) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 指定された URL が WebView からのコールバックであることを調べ、対応する処理を実行します。
     *
     * @param url URL。
     *
     * @return コールバックだった場合は true。
     */
    private boolean checkCallbackUrl( String url ) {
        final String CallbacScheme = "app-callback://map";
        if( !url.startsWith( CallbacScheme ) ) { return false; }

        Map< String, String > params = this.parseUrlParameters( url );
        String address   = params.get( "address" );
        String latitude  = params.get("lat");
        String longitude = params.get("lng");
        Log.d("HostWebView", String.format("address = %s, latitude = %s, longitude = %s", address, latitude, longitude));

        return true;
    }

    /**
     * マップの中央位置を指定された住所の位置へ移動させます。
     *
     * @param address 住所。ジオコーディングによって検索されます。
     */
    public void moveToMapCenter( String address ) {
        final String region = mContext.getString(R.string.google_map_rgion);
        final String script = "javascript:window.webViewCallbackSearchAddress('%s','%s');";
        this.mWebView.loadUrl(String.format(script, address, region));
    }


    private WebView mWebView;

    /**
     * WebView を初期化します。
     */
    @SuppressLint( "SetJavaScriptEnabled" )
    private View initWebView() {
        mWebView = new ChildPagerMapView(mContext);
        mWebView.setOnTouchListener(new WebView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return checkCallbackUrl(url);
            }
        });

        this.mWebView.loadUrl("file:///android_asset/html/map.html");
        return mWebView;
    }

    private ImageView mImageView;


    @Override
    public Object instantiateItem(View container, int position) {
        View v;
        if (position == 0) {
            v = initWebView();
        } else {
            ImageView iv = new ImageView(mContext);
            iv.setImageBitmap(mBitmaps.get(position - 1));
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mImageView = (ImageView)v;

                    return true;
                }
            });
            v = iv;
        }
        ((ViewPager)container).addView(v, 0);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
        ((ViewPager) collection).removeView((View) view);
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
        return mBitmaps.size() + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==(View)object;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
}
