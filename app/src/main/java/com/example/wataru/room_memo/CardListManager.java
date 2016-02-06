package com.example.wataru.room_memo;

/**
 * Created by wataru on 2016/01/11.
 */
public class CardListManager {
    /**
     * カードリストを更新する
     */
//    public void refleshCardList() {
//
//        public Context mContext;
//        DatabaseManager dbManager = new DatabaseManager(mContext);
//        List<com.example.wataru.greendao.db.Object> objects = dbManager.listObjects();
//        Item[] data = new Item[objects.size()];
//        for (int i = 0; i < objects.size(); i++) {
//            long objectId = objects.get(i).getId();
//            List<ObjectImage> objectImages = dbManager.findObjectImagesByObjectId(objectId);
//
//            Bitmap bmp = null;
//            if (objectImages != null && objectImages.size() != 0) {
//                byte[] bytes = objectImages.get(0).getObjectImage();
//                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            } else {
//                bmp = getBitmapNoData();
//            }
//            data[i] = new Item(objects.get(i).getId(), objects.get(i).getObjectName(), bmp);
//        }
//        CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.card_item, data);
//        setListAdapter(adapter);
//    }
}
