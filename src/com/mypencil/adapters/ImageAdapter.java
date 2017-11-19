package com.mypencil.adapters;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mypencill.R;
import com.example.mypencill.SampleActivity;
import com.mypencil.utils.ImageGetter;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends SimpleCursorAdapter {
    
    private Context mContext;
    private Cursor cursor;
    
    final Uri thumbUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	final String thumb_DATA = MediaStore.Images.Thumbnails.DATA;
	final String thumb_IMAGE_ID = MediaStore.Images.Thumbnails._ID;
	
	
    ArrayList<String> itemList = new ArrayList<String>();
    String path;
    Bitmap bm;
    public ImageAdapter(Context c,int layout, Cursor cur, String[] from,
			int[] to, int flags) {
    	//super();
    	super(c, layout, cur, from, to, flags);
    	
     mContext = c; 
     cursor = cur;
    }
    
    public void add(String path){
     itemList.add(path); 
    }

 @Override
 public int getCount() {
  return itemList.size();
 }

 @Override
 public Object getItem(int position) {
  // TODO Auto-generated method stub
  return position;
 }

 @Override
 public long getItemId(int position) {
  // TODO Auto-generated method stub
  return position;
 }

 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
	  
	  ViewHolder viewHolder = null;
		View row = convertView;
		
		if(row==null)
		{
			viewHolder = new ViewHolder();
			
			LayoutInflater inflater=((SampleActivity)mContext).getLayoutInflater();
			row=inflater.inflate(R.layout.item_image_grid, parent, false);	
			
			viewHolder.imageView = (ImageView)row.findViewById(R.id.imageView1);

			row.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) row.getTag();
		}
			
		
        path=itemList.get(position);
        
        cursor.moveToPosition(position);
		
		int myID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
		
		String[] thumbColumns = {thumb_DATA, thumb_IMAGE_ID};
		CursorLoader thumbCursorLoader = new CursorLoader(
				mContext, 
        		thumbUri, 
				thumbColumns, 
				thumb_IMAGE_ID + "=" + myID, 
				null, 
				null);
		Cursor thumbCursor = thumbCursorLoader.loadInBackground();
		
        if(thumbCursor.moveToFirst()){
			int thCulumnIndex = thumbCursor.getColumnIndex(thumb_DATA);
			String thumbPath = thumbCursor.getString(thCulumnIndex);
			
			if(viewHolder.imageView.getTag() != null) {
			    ((ImageGetter) viewHolder.imageView.getTag()).cancel(true);
			}
        }
		Picasso.with(mContext).load(new File(path)).centerCrop().resize(100, 100).into(viewHolder.imageView);
		return row;
 }

 public class ViewHolder
 {
 	public ImageView imageView;
 }

}
   

