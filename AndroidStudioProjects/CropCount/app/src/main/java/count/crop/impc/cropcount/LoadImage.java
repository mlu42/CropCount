//package count.crop.impc.cropcount;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.Gravity;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//public class LoadImage extends Activity
//{
//    Activity activity=null;
//    Context context=null;
//
//    Button header_left_btn=null;
//    Button header_right_btn=null;
//    TextView header_text=null;
//    TableLayout image_table=null;
//
//    ArrayList<String> image_list=new ArrayList<String>();
//    ArrayList<Drawable> image_drawable=new ArrayList<Drawable>();
//    String path="";
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        setContentView(R.layout.main);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.header);
//
//        activity=LoadImage.this;
//        context=LoadImage.this;
//
//        header_left_btn=(Button)findViewById(R.id.header_left_btn);
//        header_right_btn=(Button)findViewById(R.id.header_right_btn);
//        header_text=(TextView)findViewById(R.id.header_text);
//        image_table=(TableLayout)findViewById(R.id.image_table);
//
//        header_text.setText("Image Table");
//        header_left_btn.setText("Select");
//        header_right_btn.setText("Clear");
//        registerForContextMenu(header_left_btn);
//
//        header_left_btn.setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                openContextMenu(header_left_btn);
//            }
//        });
//
//        header_right_btn.setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                image_list.clear();
//                image_drawable.clear();
//                deletePhotos();
//                updateImageTable();
//            }
//        });
//    }
//
//    public void deletePhotos()
//    {
//        String folder=Environment.getExternalStorageDirectory() +"/LoadImg";
//        File f=new File(folder);
//        if(f.isDirectory())
//        {
//            File[] files=f.listFiles();
//            Log.v("Load Image", "Total Files To Delete=====>>>>>"+files.length);
//            for(int i=0;i<files.length;i++)
//            {
//                String fpath=folder+File.separator+files[i].getName().toString().trim();
//                System.out.println("File Full Path======>>>"+fpath);
//                File nf=new File(fpath);
//                if(nf.exists())
//                {
//                    nf.delete();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
//    {
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Post Image");
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.camer_menu, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case R.id.take_photo:
//                //Toast.makeText(context, "Selected Take Photo", Toast.LENGTH_SHORT).show();
//                takePhoto();
//                break;
//
//            case R.id.choose_gallery:
//                //Toast.makeText(context, "Selected Gallery", Toast.LENGTH_SHORT).show();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
//
//                break;
//
//            case R.id.share_cancel:
//                closeContextMenu();
//                break;
//            default:
//                return super.onContextItemSelected(item);
//        }
//        return true;
//    }
//
//    public void takePhoto()
//    {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg");
//
//        if(!folder.exists())
//        {
//            folder.mkdir();
//        }
//        final Calendar c = Calendar.getInstance();
//        String new_Date= c.get(Calendar.DAY_OF_MONTH)+"-"+((c.get(Calendar.MONTH))+1)   +"-"+c.get(Calendar.YEAR) +" " + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE)+ "-"+ c.get(Calendar.SECOND);
//        path=String.format(Environment.getExternalStorageDirectory() +"/LoadImg/%s.png","LoadImg("+new_Date+")");
//        File photo = new File(path);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
//        startActivityForResult(intent, 2);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==1)
//        {
//            Uri photoUri = data.getData();
//            if (photoUri != null)
//            {
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String filePath = cursor.getString(columnIndex);
//                cursor.close();
//                Log.v("Load Image", "Gallery File Path=====>>>"+filePath);
//                image_list.add(filePath);
//                Log.v("Load Image", "Image List Size=====>>>"+image_list.size());
//
//                //updateImageTable();
//                new GetImages().execute();
//            }
//        }
//
//        if(requestCode==2)
//        {
//            Log.v("Load Image", "Camera File Path=====>>>"+path);
//            image_list.add(path);
//            Log.v("Load Image", "Image List Size=====>>>"+image_list.size());
//            //updateImageTable();
//            new GetImages().execute();
//        }
//    }
//
//    public void updateImageTable()
//    {
//        image_table.removeAllViews();
//
//        if(image_drawable.size() > 0)
//        {
//            for(int i=0; i<image_drawable.size(); i++)
//            {
//                TableRow tableRow=new TableRow(this);
//                tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
//                tableRow.setPadding(5, 5, 5, 5);
//                for(int j=0; j<1; j++)
//                {
//                    ImageView image=new ImageView(this);
//                    image.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//
//                    /*Bitmap bitmap = BitmapFactory.decodeFile(image_list.get(i).toString().trim());
//                    bitmap = Bitmap.createScaledBitmap(bitmap,500, 500, true);
//                    Drawable d=loadImagefromurl(bitmap);*/
//                    image.setBackgroundDrawable(image_drawable.get(i));
//
//                    tableRow.addView(image, 200, 200);
//                }
//                image_table.addView(tableRow);
//            }
//        }
//    }
//
//    public Drawable loadImagefromurl(Bitmap icon)
//    {
//        Drawable d=new BitmapDrawable(icon);
//        return d;
//    }
//
//    public class GetImages extends AsyncTask<Void, Void, Void>
//    {
//        public ProgressDialog progDialog=null;
//
//        protected void onPreExecute()
//        {
//            progDialog=ProgressDialog.show(context, "", "Loading...",true);
//        }
//        @Override
//        protected Void doInBackground(Void... params)
//        {
//            image_drawable.clear();
//            for(int i=0; i<image_list.size(); i++)
//            {
//                Bitmap bitmap = BitmapFactory.decodeFile(image_list.get(i).toString().trim());
//                bitmap = Bitmap.createScaledBitmap(bitmap,500, 500, true);
//                Drawable d=loadImagefromurl(bitmap);
//
//                image_drawable.add(d);
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Void result)
//        {
//            if(progDialog.isShowing())
//            {
//                progDialog.dismiss();
//            }
//            updateImageTable();
//        }
//    }
//}
//
