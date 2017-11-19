package com.example.mypencill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.impl.entity.LaxContentLengthStrategy;

import com.example.mypencill.MainActivity;
import com.example.mypencill.MainActivity.MyView;
import com.example.mypencill.R;
import com.example.mypencill.HSVColorPickerDialog;
import com.example.mypencill.HSVColorPickerDialog.OnColorSelectedListener;
import com.example.mypencill.SampleActivity;

import android.R.color;
import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Telephony.Sms.Conversations;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements HSVColorPickerDialog.OnColorSelectedListener{

	    private Paint mPaint;
	    private ImageView imgpencil,imgeraser,imgcolor,imgsave,imgHandle;
	    private LinearLayout LPencil,LEraser,LColor,LSave,LContent;
	    private MaskFilter  mEmboss;
	    private MaskFilter  mBlur;
	    private SlidingDrawer SlidingDrawer;
	    Button b;
	    Dialog dialog;
	    static MyView mv;
	    File f;
	    private int Col=-16711165; 
	    private int seekvalue=0,seekvalErase=0;
	    private String fname,picturepath;
	 
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
	    mv= new MyView(this);
	    mv.setDrawingCacheEnabled(true);
	    ll.addView(mv);
	    
	    mPaint = new Paint();
	    mPaint.setAntiAlias(true);
	    mPaint.setDither(true);
	    mPaint.setColor(Col);
	    mPaint.setStyle(Paint.Style.STROKE);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(20);

	    mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
	                                   0.4f, 6, 3.5f);
	    LContent=(LinearLayout) findViewById(R.id.content);
	    imgHandle=(ImageView) findViewById(R.id.handle);
	    SlidingDrawer=(android.widget.SlidingDrawer) findViewById(R.id.slidingDrawer1);
	    SlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				
				imgHandle.setImageResource(R.drawable.cir_down);
			}
		});
	    
	    SlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				imgHandle.setImageResource(R.drawable.cir_up);
			}
		});
	    	
	    
	    LPencil=(LinearLayout) findViewById(R.id.LPencil);
	    LEraser=(LinearLayout) findViewById(R.id.LEraser);
	    LColor=(LinearLayout) findViewById(R.id.LColor);
	    LSave=(LinearLayout) findViewById(R.id.LSave);
	    
	    LPencil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				LPencil.setBackgroundColor(Color.parseColor("#ACAC83"));
				LEraser.setBackgroundColor(Color.parseColor("#000000"));
				LColor.setBackgroundColor(Color.parseColor("#000000"));
				LSave.setBackgroundColor(Color.parseColor("#000000"));
				
				AlertDialog.Builder pencilalert = new AlertDialog.Builder(MainActivity.this);
		        pencilalert.setTitle("size of pencil");
		        final SeekBar seek = new SeekBar(MainActivity.this);
		        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                LinearLayout.LayoutParams.FILL_PARENT,
		                LinearLayout.LayoutParams.FILL_PARENT);
		        seek.setLayoutParams(lp);
		        pencilalert.setView(seek);
		        seek.setProgress(seekvalue);
		        pencilalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {

		            seekvalue= seek.getProgress();
		            mv.DrawCanvas();
		            mPaint = new Paint();
					mPaint.setAntiAlias(true);
					mPaint.setDither(true);
					mPaint.setColor(Col);
					mPaint.setStyle(Paint.Style.STROKE);
					mPaint.setStrokeJoin(Paint.Join.ROUND);
					mPaint.setStrokeCap(Paint.Cap.ROUND);
					mPaint.setStrokeWidth(seekvalue);
					mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
					                           0.4f, 6, 3.5f);
					mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
		            }
		        });
		        pencilalert.show();  
				
			}
		});
	    
	    LEraser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				LPencil.setBackgroundColor(Color.parseColor("#000000"));
				LEraser.setBackgroundColor(Color.parseColor("#ACAC83"));
				LColor.setBackgroundColor(Color.parseColor("#000000"));
				LSave.setBackgroundColor(Color.parseColor("#000000"));
				
				//mPaint.setXfermode(null);
				//mPaint.setAlpha(0xFF);
				
				AlertDialog.Builder erasealert = new AlertDialog.Builder(MainActivity.this);
		        erasealert.setTitle("Size of eraser");
		        final SeekBar seek = new SeekBar(MainActivity.this);
		        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                LinearLayout.LayoutParams.FILL_PARENT,
		                LinearLayout.LayoutParams.FILL_PARENT);
		        seek.setLayoutParams(lp);
		        erasealert.setView(seek);
		        seek.setProgress(seekvalErase);
		        erasealert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	
		        seekvalErase= seek.getProgress();
		        mPaint.setStrokeWidth(seekvalErase);
		        mv.ClearCanvas();
		        //mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		        
				//mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
				//mPaint.setAlpha(0x80);
		            }
		        });
		        erasealert.show();
			}
		});
	    
	    LColor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LPencil.setBackgroundColor(Color.parseColor("#000000"));
				LEraser.setBackgroundColor(Color.parseColor("#000000"));
				LColor.setBackgroundColor(Color.parseColor("#ACAC83"));
				LSave.setBackgroundColor(Color.parseColor("#000000"));
				mv.DrawCanvas();
				mPaint.setXfermode(null);
				mPaint.setAlpha(0xFF);
				mPaint.setStrokeWidth(seekvalue);
				HSVColorPickerDialog cpd = new HSVColorPickerDialog(MainActivity.this, 0xFF4488CC, new OnColorSelectedListener() {
				 @Override
				    public void colorSelected(Integer color) {
				        mPaint.setColor(color);
				        Col=color;
				    }
				});
				cpd.setTitle( "Pick a color" );
				cpd.show();	
			}
		});
	    
	    LSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LPencil.setBackgroundColor(Color.parseColor("#000000"));
				LEraser.setBackgroundColor(Color.parseColor("#000000"));
				LColor.setBackgroundColor(Color.parseColor("#000000"));
				LSave.setBackgroundColor(Color.parseColor("#ACAC83"));
				
				AlertDialog.Builder editalert = new AlertDialog.Builder(MainActivity.this);
		        editalert.setTitle("Please Enter the name with which you want to Save");
		        final EditText input = new EditText(MainActivity.this);
		        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                LinearLayout.LayoutParams.FILL_PARENT,
		                LinearLayout.LayoutParams.FILL_PARENT);
		        input.setLayoutParams(lp);
		        editalert.setView(input);
		        editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {

		            String name= input.getText().toString();
		            Bitmap bitmap = mv.getDrawingCache();
		            
		           //String path = Environment.getExternalStorageDirectory().toString();
		           String path = Environment.getExternalStorageDirectory().toString();
					 File myDir=new File(path + "/MyPencil");
					 myDir.mkdir();
					
					 File file = new File (myDir,name + ".png");
					 boolean success = false;

					    // Encode the file as a PNG image.
					    FileOutputStream outStream;
					    try {

					        outStream = new FileOutputStream(file);
					        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
					        /* 100 to keep full quality of the image */

					        outStream.flush();
					        outStream.close();
					        success = true;
					    } catch (FileNotFoundException e) {
					        e.printStackTrace();
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					    
					    if (success) {
					        Toast.makeText(getApplicationContext(), "Image Saved Successfully",
					                Toast.LENGTH_LONG).show();
					    } else {
					        Toast.makeText(getApplicationContext(),
					                "Error during image saving", Toast.LENGTH_LONG).show();
					    }
		           
		            }
		        });

		        editalert.show();  
			}
		});
	
	    
	}
	
	
	
	public class MyView extends View {

	    private static final float MINP = 0.25f;
	    private static final float MAXP = 0.75f;
    
	    private Bitmap  mBitmap;
	    private Canvas  mCanvas;
	    private Path    mPath;
	    private Paint   mBitmapPaint;
	    Context mcontext;
	    private int width,height;

	    public MyView(Context c) {
	        super(c);
	        mcontext=c;
	        mPath = new Path();
	        mBitmapPaint = new Paint();
	        mBitmapPaint.setColor(Color.RED);
	    }
	    
	    public void clear(){
	    	mBitmap.eraseColor(Color.TRANSPARENT);
	        mPath.reset();
	        mv.invalidate();
	        mv.setBackgroundColor(Color.TRANSPARENT);
	    }
	    
	   
	    @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        super.onSizeChanged(w, h, oldw, oldh);
	        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	        mCanvas = new Canvas(mBitmap);
	    }
	    
	   

	    @Override
	    protected void onDraw(Canvas canvas) {
	    	
	       //canvas.drawColor(0xFFAAAAAA);
	        Display display = ( (MainActivity) mcontext).getWindowManager().getDefaultDisplay();  
	        float w = display.getWidth(); 
	        float h = display.getHeight();
	        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
	        canvas.drawLine(0, 0, w, 0,mBitmapPaint);
	        canvas.drawLine(0, 0, 0, h,mBitmapPaint);
	        canvas.drawLine(w,h,w,0,mBitmapPaint);
	        canvas.drawLine(w, h, 0,h , mBitmapPaint);
	        canvas.drawPath(mPath, mPaint);
	    	
	    }

	    private float mX, mY;
	    private static final float TOUCH_TOLERANCE = 4;

	    private void touch_start(float x, float y) {
	        mPath.reset();
	        mPath.moveTo(x, y);
	        mX = x;
	        mY = y;
	    }
	    private void touch_move(float x, float y) {
	        float dx = Math.abs(x - mX);
	        float dy = Math.abs(y - mY);
	        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
	            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
	            mX = x;
	            mY = y;
	        }
	    }
	    
	    public boolean clear_canvas=false;
	    public boolean draw_canvas=false;
	    public void ClearCanvas()
	    {
	    	clear_canvas=true;
	    	draw_canvas=false;
	    }
	    
	    public void DrawCanvas()
	    {
	    	draw_canvas=true;
	    	clear_canvas=false;
	    }
	    
	    private void touch_up() {
	    	if(clear_canvas)
	    	{
	    			mPath.lineTo(mX, mY);
		 	        // commit the path to our offscreen
		 	        mCanvas.drawPath(mPath, mPaint);
		 	        mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		 	        // kill this so we don't double draw
		 	        mPath.reset();
	    	}
	    	else if(draw_canvas)
	    	{
	    		mPath.lineTo(mX, mY);
	 	        // commit the path to our offscreen
	 	        mCanvas.drawPath(mPath, mPaint);
	 	        mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SCREEN));
	 	        // kill this so we don't double draw
	 	        mPath.reset();
	    	}
	       
	    }
	    
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        float x = event.getX();
	        float y = event.getY();

	        switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	                touch_start(x, y);
	                invalidate();
	                break;
	            case MotionEvent.ACTION_MOVE:
	                touch_move(x, y);
	                invalidate();
	                break;
	            case MotionEvent.ACTION_UP:
	                touch_up();
	                invalidate();
	                break;
	        }
	        return true;
	    }
	}
	    
	

	private void selectImage() {
		final AlertDialog builder = new AlertDialog.Builder(MainActivity.this).create();
		builder.setTitle("Select Background image!");
		
		 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		 layoutParams.setMargins(60,20,20,20);
		 
		 LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		 layoutParams1.setMargins(40,20,20,20);
		
		 LinearLayout main=new LinearLayout(this);
		 
		 LinearLayout layoutcam    = new LinearLayout(this);
		 ImageView imgcam		   = new ImageView(this);
		 TextView txtcam	       = new TextView(this);
		 
		 LinearLayout layoutgallery      = new LinearLayout(this);
		 ImageView imggallery			 = new ImageView(this);
		 TextView txtgallery             = new TextView(this);
		 
		 LinearLayout layoutcolor       = new LinearLayout(this);
		 ImageView imgcolor			 = new ImageView(this);
		 TextView txtcolor             = new TextView(this);
		 
		 LinearLayout layoutcancel       = new LinearLayout(this);
		 ImageView imgcancel			 = new ImageView(this);
		 TextView txtcancel              = new TextView(this);
		
		 
		 main.setOrientation(LinearLayout.VERTICAL);
		 
		 layoutcam.setOrientation(LinearLayout.HORIZONTAL);
		 imgcam.setImageResource(R.drawable.camreaa);
		 txtcam.setText("Take Photo");
		 layoutcam.addView(imgcam);
		 layoutcam.addView(txtcam,layoutParams1);
		 
		 layoutgallery.setOrientation(LinearLayout.HORIZONTAL);
		 imggallery.setImageResource(R.drawable.photo);
		 txtgallery.setText("Choose from Gallery");
		 layoutgallery.addView(imggallery);
		 layoutgallery.addView(txtgallery,layoutParams1);
		 
		 layoutcancel.setOrientation(LinearLayout.HORIZONTAL);
		 imgcancel.setImageResource(R.drawable.cancell);
		 txtcancel.setText("Cancel");
		 layoutcancel.addView(imgcancel);
		 layoutcancel.addView(txtcancel,layoutParams1);
		 
		 layoutcolor.setOrientation(LinearLayout.HORIZONTAL);
		 imgcolor.setImageResource(R.drawable.color_48);
		 txtcolor.setText("Background Color");
		 layoutcolor.addView(imgcolor);
		 layoutcolor.addView(txtcolor,layoutParams1);
		 
		 main.addView(layoutcam,layoutParams);
		 main.addView(layoutgallery,layoutParams);
		 main.addView(layoutcolor,layoutParams);
		 main.addView(layoutcancel,layoutParams);

		 builder.setView(main);
		 layoutcam.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				String path = Environment.getExternalStorageDirectory().toString();
				File myDir=new File(path + "/CAMERA");
				myDir.mkdir();
				Random generator = new Random();
				int n = 10000;
				n = generator.nextInt(n);
				fname = "Image-"+ n +".jpg";
				File file = new File (myDir, fname);
				//Toast.makeText(getApplicationContext(), ""+file.getAbsolutePath(), 1).show();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, 1);
				builder.dismiss();
			}
		});
		 
		layoutgallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 2);
				builder.dismiss();

			}
		});
		
		layoutcolor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//new ColorPickerDialogBack(MainActivity.this,MainActivity.this,mPaint.getColor()).show();
				//mv.setBackgroundColor(colorBackk);
				HSVColorPickerDialog cpd = new HSVColorPickerDialog(MainActivity.this, 0xFF4488CC, new OnColorSelectedListener() {
				    @Override
				    public void colorSelected(Integer color) {
				    	mv.setBackgroundColor(color);
				    }
				});
				cpd.setTitle( "Pick a color" );
				cpd.show();	
				builder.dismiss();
			}
		});
		
		layoutcancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		 builder.show();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				File file = new File(Environment.getExternalStorageDirectory().toString()+"/CAMERA/" + fname);
				try {
					Bitmap bitmap;
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
					
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				   bitmapOptions);
				Drawable d = new BitmapDrawable(getResources(),bitmap);
				mv.setBackgroundDrawable(d);
				}
				catch(Exception e){
					//e.getMessage();
				}
				
			} else if (requestCode == 2) {

				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getContentResolver().query(selectedImage, filePath,
						null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				picturepath = c.getString(columnIndex);
				c.close();
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				Bitmap bitmap = (BitmapFactory.decodeFile(picturepath));
				Drawable d = new BitmapDrawable(getResources(),bitmap);
				mv.setBackgroundDrawable(d);
			
			}
		}
	}



	private static final int BACKGROUND = Menu.FIRST;
	private static final int ALL_DRAWING = Menu.FIRST + 1;
	private static final int SHARE_VIA = Menu.FIRST + 2;
	private static final int NEW_CANVAS = Menu.FIRST + 3;
	private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(0, BACKGROUND, 0, "Set Background").setShortcut('3', 'c');
	    menu.add(0, ALL_DRAWING, 0, "View All drawing").setShortcut('4', 's');
	    menu.add(0, SHARE_VIA, 0, "Share").setShortcut('5', 'z');
	    menu.add(0, NEW_CANVAS, 0, "New Drawing").setShortcut('6', 'z');
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		mPaint.setXfermode(null);
		mPaint.setAlpha(0xFF);

		switch (item.getItemId()) {
		case BACKGROUND:
			selectImage();
			return true;
		case ALL_DRAWING:
			Intent i=new Intent(MainActivity.this,SampleActivity.class);
			startActivity(i);
			return true;
		case SHARE_VIA:
			Uri bmpUri = getLocalBitmapUri(mv);
			Intent shareIntent = new Intent();
		    shareIntent.setAction(Intent.ACTION_SEND);
		    shareIntent.setType("image/*");
		    shareIntent.putExtra(Intent.EXTRA_STREAM,bmpUri);
		    startActivity(Intent.createChooser(shareIntent, "Share with"));
		    return true;
		 case NEW_CANVAS:
			 	mv.clear();
			 	 mPaint.setColor(Color.BLACK);
			 	mPaint.setStrokeWidth(20);
			   return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public Uri getLocalBitmapUri(View myview) {
		Uri bmpUri = null;
		Bitmap bitmap =mv.getDrawingCache();
		 
		 String path = Environment.getExternalStorageDirectory().toString();
		 File myDir=new File(path + "/Shared Images");
		 myDir.mkdir();
		 Random generator = new Random();
		 int n = 10000;
		 n = generator.nextInt(n);
		 String fname = "Image-"+ n +".jpg";
		 File file = new File (myDir, fname);
		 boolean success = false;

		    // Encode the file as a PNG image.
		    FileOutputStream outStream;
		    try {

		        outStream = new FileOutputStream(file);
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
		        /* 100 to keep full quality of the image */

		        outStream.flush();
		        outStream.close();
		        success = true;
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		 bmpUri=Uri.fromFile(file);
	    return bmpUri;
	}


	@Override
	public void colorSelected(Integer color) {
		// TODO Auto-generated method stub
		
	}
}
