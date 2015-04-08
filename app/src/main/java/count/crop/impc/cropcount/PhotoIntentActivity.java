package count.crop.impc.cropcount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PhotoIntentActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO_B = 1;
	private static final int ACTION_PICK_FROM_GALLERY = 2;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

	private ImageView mImageView;
	private Bitmap mImageBitmap;

    private ImageView mImageafterEffect;
    private TextView mPercentage;

	private String mCurrentPhotoPath = "";

    private Context context=null;


	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	
	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}

    //how to get real screensize in run time?
    private int targetW = 700;
    private int targetH = 700;


	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */

        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {

                        // Ensure you call it only once :
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            mImageView.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                        } else {
                            mImageView.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                        }

                        // Here you can get the size :)
                        targetW = mImageView.getWidth();
                        targetH = mImageView.getHeight();
                        Log.v("target Dim Inside here", mImageView.getWidth()+" "+ mImageView.getHeight());
                    }
                    });


		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
        Log.v("targetDim", targetH+" "+ targetW);
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		

        //set the  image
        mImageBitmap = bitmap;

        //setAfterEffectImage(bitmap);


	}

    private void setAfterEffectImage(Bitmap bitmap){
        /* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);

        Bitmap bitmapAF = doColorFilter(bitmap, 0, 100, 0);
        mImageafterEffect.setImageBitmap(bitmapAF);
        mImageafterEffect.setVisibility(View.VISIBLE);
    }

	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_B:
			File f = null;

			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;

			}

            startActivityForResult(takePictureIntent, actionCode);
			break;
            case ACTION_PICK_FROM_GALLERY:


            break;

		default:
			break;
		} // switch


	}


	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			//setPic();
            new GetImages().execute();
			galleryAddPic();

			mCurrentPhotoPath = null;
		}

	}


	Button.OnClickListener mTakePicOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
		}
	};



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageafterEffect = (ImageView) findViewById(R.id.imageView2);
        mPercentage = (TextView) findViewById(R.id.greenPercentage);

        context = this;
		mImageBitmap = null;


		Button picBtn = (Button) findViewById(R.id.btnIntend);
		setBtnListenerOrDisable( 
				picBtn, 
				mTakePicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
		);

		//Button picSBtn = (Button) findViewById(R.id.btnIntendS);

        ((Button) findViewById(R.id.btnIntendS))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");

                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), ACTION_PICK_FROM_GALLERY);
                    }
                });


		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK) {
				handleBigCameraPhoto();
			}
			break;
		} // ACTION_TAKE_PHOTO_B

		case ACTION_PICK_FROM_GALLERY: {

			if (resultCode == RESULT_OK && data != null) {

                Uri selectedImageUri = data.getData();
                mCurrentPhotoPath = getPath(selectedImageUri);
                Log.v("image path-------->", mCurrentPhotoPath);

                //setPic();
                //use it in a seperate thread
                new GetImages().execute();
                //setAfterEffectImage(mImageBitmap);

            }



			break;
		} // ACTION_PICK_FROM_GALLERY


		} // switch
	}



    private String getPath(Uri uri) {

        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this,uri);

            // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            realPath = RealPathUtil.getRealPathFromURI_API11to18(this, uri);

            // SDK > 19 (Android 4.4)
        else
            realPath = RealPathUtil.getRealPathFromURI_API19(this, uri);

        return realPath;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);

		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);

		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
						ImageView.VISIBLE : ImageView.INVISIBLE
		);

	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void setBtnListenerOrDisable( 
			Button btn, 
			Button.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);        	
		} else {
			btn.setText( 
				getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}



    /**
     * process the image and give green filter
     *
     * */
    private Bitmap doColorFilter(Bitmap src, double red, double green, double blue){

        int greenCount = 0;
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                //count green
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel));
                G = (int)(Color.green(pixel));
                B = (int)(Color.blue(pixel));

                if(G > 40 && G-10 > R && G-10 > B){
                    greenCount++;

                    R=0;
                    G=255;
                    B=0;

                }else{
                    R=0;
                    G=0;
                    B=0;
                }

                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));

            }
        }

        //display count;
        double greenPercentage = (double)greenCount/(double)(width*height) *100;
        DecimalFormat df = new DecimalFormat("#.00");
        mPercentage.setText("The percentage of Green in the picture is: " + df.format(greenPercentage) +"%");


        // return final image
        return bmOut;

    }


    /**
     * An async task that loads the decompose the image and load it
     * into the imageview after done.
     *
    * */

    public class GetImages extends AsyncTask<Void, Void, Void>
    {
        public ProgressDialog progDialog=null;

        protected void onPreExecute()
        {
            progDialog=ProgressDialog.show(context, "", "Loading",true);
        }
        @Override
        protected Void doInBackground(Void... params)
        {

              setPic();

            return null;
        }

        protected void onPostExecute(Void result)
        {
            setAfterEffectImage(mImageBitmap);
            if(progDialog.isShowing())
            {
                progDialog.dismiss();
            }


        }
    }


}