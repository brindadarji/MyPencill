package com.example.mypencill;


import java.io.File;
import com.example.mypencill.R;
import com.example.mypencill.SampleActivity;
import com.mypencil.adapters.ImageAdapter;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SampleActivity extends ActionBarActivity {

	ImageAdapter myImageAdapter;
	 GridView gridview;
	 String pathalert;
	 Bitmap thumbBitmap;
	Cursor c;
	Bitmap[] bitmapArray;


	 final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		
	 private void UIClickEvents()
		{
		 
		 String ExternalStorageDirectoryPath = Environment
		          .getExternalStorageDirectory()
		          .getAbsolutePath();
		 
		 String targetPath = ExternalStorageDirectoryPath + "/MyPencil/";
	        
	        //Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
	        File targetDirector = new File(targetPath);
	        File[] files = targetDirector.listFiles();
	        if (!files.equals(null) || !(files.length <= 0)){
	            bitmapArray = new Bitmap[(files.length)];
	            for (int index = 0; (index <= files.length - 1); index++){
	                if (files[index] != null){
	                    bitmapArray[index] = BitmapFactory.decodeFile(files[index].toString());
	                }
	            }
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					
					final CharSequence[] options = { "View", "Cancel" };

					AlertDialog.Builder builder = new AlertDialog.Builder(
							SampleActivity.this);
					builder.setTitle("Choose");
					
					builder.setItems(options,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int item) {
									if (options[item].equals("View")) {
										
										        	
													AlertDialog.Builder thumbDialog = new AlertDialog.Builder(
															SampleActivity.this);
													thumbDialog
													.setPositiveButton(
															"Cancel",
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	dialog.dismiss();
																}
															});
											ImageView thumbView = new ImageView(
													SampleActivity.this);
											thumbView.setImageBitmap(bitmapArray[position]);
											thumbDialog.setView(thumbView);
											AlertDialog alert = thumbDialog.create();
											alert.show();
										}
							
									else if(options[item].equals("Cancel")) {
										dialog.dismiss();
									}
								}
							});
					
					builder.show();

				}
			});
	        }
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_sample);
			
			 gridview = (GridView) findViewById(R.id.gridView1); 
			 getImagesFromDevice();
			 UIClickEvents();
		    }
		
		
		@SuppressLint("NewApi")
		private void getImagesFromDevice()
		{
			String[] from = {MediaStore.MediaColumns.TITLE};
			int[] to = {android.R.id.text1};
			CursorLoader cursorLoader = new CursorLoader(
					this, 
					sourceUri, 
					null, 
					null, 
					null, 
					MediaStore.Audio.Media.TITLE);

			 Cursor cursor = cursorLoader.loadInBackground();
			 
			 myImageAdapter = new ImageAdapter(
						this, 
						android.R.layout.simple_list_item_1, 
						cursor, 
						from, 
						to, 
						CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			//myImageAdapter = new ImageAdapter(this,cursor);
	        gridview.setAdapter(myImageAdapter);
	       
	        String ExternalStorageDirectoryPath = Environment
	          .getExternalStorageDirectory()
	          .getAbsolutePath();
	        
	        String targetPath = ExternalStorageDirectoryPath + "/MyPencil/";
	        
	        //Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
	        File targetDirector = new File(targetPath);
	        
	        File[] files = targetDirector.listFiles();
	        for (File file : files){
	        	myImageAdapter.add(file.getAbsolutePath());
	        }
		}
		
		/*String getOriginalImagePathFromThumbID(int reterievedImageId)
		{
			String ExternalStorageDirectoryPath = Environment
			          .getExternalStorageDirectory()
			          .getAbsolutePath();
			 String targetPath = ExternalStorageDirectoryPath + "/MyPencil/";
		        
		        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
		        File targetDirector = new File(targetPath);
		        
		        File[] files = targetDirector.listFiles();
		        for (File file : files){
		        	
		        }
			   thumbBitmap = BitmapFactory.decodeFile(imagePath);
			       
				//Create a Dialog to display the thumbnail
				AlertDialog.Builder thumbDialog = new AlertDialog.Builder(SampleActivity.this);
				ImageView thumbView = new ImageView(SampleActivity.this);
				thumbView.setImageBitmap(thumbBitmap);
				thumbDialog
				.setPositiveButton(
						"Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				LinearLayout layout = new LinearLayout(SampleActivity.this);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.addView(thumbView);
				thumbDialog.setView(layout);
				thumbDialog.show();
			}
			return imagePath;
		}*/



		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.sample, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			
			return super.onOptionsItemSelected(item);
		}
	}

