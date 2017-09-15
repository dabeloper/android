//package com.dabeloper.android.network;
//import android.Manifest;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.support.v4.app.ActivityCompat;
//import android.text.format.DateUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.widget.ImageView;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.ServerError;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//import java.io.BufferedOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.ConnectException;
//import java.net.HttpURLConnection;
//import java.net.NetworkInterface;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.logging.Handler;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.signature.StringSignature;
//
//
//
///**
// * Created by David Antonio Bolaños AKA DABEPOLER  on 10/02/2017.
// *
// *
// *
// */
//
//public class HandlerHttpRequest {
//
//    //    private Context mContext;
////    private static String URL = "http://192.168.1.74/api/";
//    private static String URL               = "https://app.parches.me/api/";
//    private static String TAG_LANGUAGE      = "language";
//    private static String TAG_CHECK_VERSION = "next_check";
//    private static String TAG_TOKEN         = "authentication_code";
//
//    private static String URI_PARCHE_IMG    = "image/parche/";
//    private static String URI_PROFILE_IMG   = "image/profile/";
//    private static String URI_OTHER_IMG     = "image/other/";
//    private static String TOKEN             = "";
//
//    private static String PATH          = Environment.getExternalStorageDirectory().getPath()+"/Parches/";
//    //    private static String PATH_MEDIA    = Environment.getExternalStorageDirectory().getPath()+"/Parches/Media";
////    private static String PATH_TEMP     = Environment.getExternalStorageDirectory().getPath()+"/Parches/Media/Temp/";
////    private static String PATH_IMAGES   = Environment.getExternalStorageDirectory().getPath()+"/Parches/Media/Images/";
////    private static String PATH_PROFILES = Environment.getExternalStorageDirectory().getPath()+"/Parches/Media/Profiles/";
////    private static String PATH_BG = Environment.getExternalStorageDirectory().getPath()+"/Parches/Media/Background/";
//    private static int TIMEOUT          = 60000; //timeout request
//    private static int One_Mega_Byte    = 1024 * 1024;
//    //    private int REQUEST_SIZE            = 2; //save the inSampleSize to resize succesfully the largests Bitmaps
//    private static String MAC;
//    private static HandlerHttpRequest mInstance;
//    private static Locale currentLocale =  new Locale( "es" , "ES" );
////    private ArrayList<String> imageCache;
////    private DisplayMetrics screenMetrics;
////    protected HashMap<String,Bitmap> imageCache = new HashMap<>();
//
//    private String
//            ERRMSG_server_down = "",
//            ERRMSG ="Error",
//            ERRMSG_empty ="Error",
//            ERRMSG_retry ="Error",
//            ERRMSG_no_change ="Error",
//            ERRMSG_enter_some ="Error",
//            ERRMSG_volley ="Error",
//            ERRMSG_401 ="Error",
//            ERRMSG_403 ="Error",
//            ERRMSG_404 ="Error",
//            ERRMSG_500 ="Error",
//            ERRMSG_try_again ="Error",
//            ERRMSG_notfound ="Error",
//            ERRMSG_unexpectederror ="Error",
//            ERRMSG_welcome_messages ="Error",
//            ERRMSG_missingform ="Error",
//            ERRMSG_missingformfield ="Error",
//            ERRMSG_validationerror ="Error",
//            ERRMSG_usernotfound ="Error",
//            ERRMSG_usernamerestrictedchars ="Error",
//            ERRMSG_usernametaken ="Error",
//            ERRMSG_missingusersecurity ="Error",
//            ERRMSG_badusersecurity ="Error",
//            ERRMSG_invalidpassword ="Error",
//            ERRMSG_wrongpassword ="Error",
//            ERRMSG_parchenotfound ="Error",
//            ERRMSG_messagenotfound ="Error",
//            ERRMSG_inscribefirst ="Error",
//            ERRMSG_privateparche ="Error",
//            ERRMSG_inactiveparche ="Error",
//            ERRMSG_parcheisactive ="Error",
//            ERRMSG_fullparche = "Error",
//            ERRMSG_owner ="Error",
//            ERRMSG_waitingforuser ="Error",
//            ERRMSG_messagehasreply ="Error";
//
//    private static String todaySignature = "";
//
//    public static void reloadLanguage(){
//        mInstance = null;
//    }
//
//    private HandlerHttpRequest(Context context) {
//        /* call the fill function only one time */
////        this.mContext = context;
//        fillErrorMessages(context);
////        verifyImagesDirectories();
//    }
//
//    public static HandlerHttpRequest getInstance( Context context ) {
//        if (mInstance == null) {
//            mInstance = new HandlerHttpRequest(context);
//            Calendar c = Calendar.getInstance();
//            todaySignature   = c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH);
//        }
//
//        /* set the actual context for the request */
//        VolleyAppController.getInstance(context);
//        return mInstance;
//    }
//
//
//    /********** START PHASE : CACHE ***********/
//
//    /*
//     * Fill the local array cache with the names of the cached images
//     *
//
//    private void verifyImagesDirectories(){
//        imageCache = new ArrayList<>();
//
//        File mainDirectory = new File( PATH );
//        File mediaDirectory = new File( PATH_MEDIA );
//        File directoryImages = new File( PATH_IMAGES );
//        File directoryBG = new File( PATH_BG );
//        File directoryProfiles = new File( PATH_PROFILES );
//        if( (mainDirectory.isDirectory() || mainDirectory.mkdirs()) &&
//                (mediaDirectory.isDirectory() || mediaDirectory.mkdirs()) &&
//                    (directoryImages.isDirectory() || directoryImages.mkdirs()) &&
//                    (directoryBG.isDirectory() || directoryBG.mkdirs()) &&
//                    (directoryProfiles.isDirectory() || directoryProfiles.mkdirs())
//                ){
//
////            File[] files = directory.listFiles();
////            Log.d("Files", "Size: "+ files.length);
////            for (int i = 0; i < files.length; i++)
////            {
////                imageCache.add( files[i].getName() );
////            }
//        }
//    }//end verifyImagesDirectories function
//
//
//    /**
//     * Create new image cache
//     *
//    private void addNewImageCache( final Bitmap imageBitmap , final String imageName ){
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                Looper.prepare();
//                File newFile;
//                FileOutputStream filecon;
//                try {
//                    newFile = new File( PATH_IMAGES+imageName+".jpg");
//                    filecon = new FileOutputStream( newFile );
//                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, filecon);
//                    filecon.flush();
//                    filecon.close();
//                    imageCache.add( imageName );
//
//                } catch (IOException e) {
//                    //e.printStackTrace();
//                }
//                newFile = null;
//                filecon = null;
//
////                try {
////                    theBitmap = Glide.
////                            with(SomeActivity.this).
////                            load("https://www.google.es/images/srpr/logo11w.png").
////                            asBitmap().
////                            into(-1,-1).
////                            get();
////                } catch (final ExecutionException e) {
////                    Log.e(TAG, e.getMessage());
////                } catch (final InterruptedException e) {
////                    Log.e(TAG, e.getMessage());
////                }
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void dummy) {
//                System.gc();
////                if (null != theBitmap) {
////                    // The full bitmap should be available here
////                    image.setImageBitmap(theBitmap);
////                    Log.d(TAG, "Image loaded");
////                }
//            }
//        }.execute();
//
//    }//end addNewImageCache function
//
//
//    /**
//     * Return the bitmap from a file cached
//     *
//     * @param REQUIRED_SIZE received the size to change the original image
//     *
//     *
//    private Bitmap getBitmapCached( int fileIdx , int REQUIRED_SIZE ){
//        try {
//            File imgFile = new File( PATH_IMAGES + imageCache.get(fileIdx) );
//
//            if(imgFile.exists()){
//
//                // Decode image size
//                BitmapFactory.Options o = new BitmapFactory.Options();
//                o.inJustDecodeBounds = true;
//                BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o);
//
//                // The new size we want to scale to
//                //final int REQUIRED_SIZE=70;
//                // Find the correct scale value. It should be the power of 2.
//                int scale = 1;
//                if( REQUIRED_SIZE > 0){
//                    while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
//                            o.outHeight / scale / 2 >= REQUIRED_SIZE) {
//                        scale *= 2;
//                    }
//                }
//
//                // Decode with inSampleSize
//                BitmapFactory.Options o2 = new BitmapFactory.Options();
//                o2.inSampleSize = scale;
//                return BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o2);
//            }
//        } catch (FileNotFoundException e) {
//            //e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    /************   END PHASE : CACHE **********/
//
//    /**
//     * http://stackoverflow.com/questions/31329733/how-to-get-the-missing-wifi-mac-address-on-android-m-preview/32948723#32948723
//     * Returns the MAC for the actual device */
//    public static String getMACDevice() {
//
//        //https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id
//        MAC = "02:00:00:00:00:00";
//
//        try {
//            String interfaceName = "wlan0";
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface anInterface : interfaces) {
//                if (!anInterface.getName().equalsIgnoreCase(interfaceName)){
//                    continue;
//                }
//
//                byte[] mac = anInterface.getHardwareAddress();
//                if (mac==null){
//                    MAC = "02:00:00:00:00:00";
//                    return MAC;
//                }
//
//                StringBuilder buf = new StringBuilder();
//                for (byte aMac : mac) {
//                    buf.append(String.format("%02X:", aMac));
//                }
//                if (buf.length()>0) {
//                    buf.deleteCharAt(buf.length() - 1);
//                }
//                MAC = buf.toString();
//                return MAC;
//            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//        }// for now eat exceptions
//
//
//        return MAC;
//    }//end function getMACDevice
//
//
//
//
//    /*
//    * Request GET for a JSON : The response should be json.
//    * */
//    public void GETJsonRequest( String Resource,
//                                Response.Listener<JSONObject> SuccessCallback,
//                                Response.ErrorListener ErrorCallback ,
//                                String TAG_ID){
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest( Request.Method.GET,
//                URL +Resource,
//                null,
//                SuccessCallback ,
//                ErrorCallback){
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("token", TOKEN);
//                return headers;
//            }};
//
//        // Adding request to request queue
//        VolleyAppController.getInstance().addToRequestQueue( jsonObjReq, TAG_ID );
//
//    }//end GETJsonRequest function
//
//
//    /*
//    * Request GET for a JSON skipping the cache: The response should be json.
//    * */
//    public void GETJsonRequestNoCache( String Resource,
//                                       Response.Listener<JSONObject> SuccessCallback,
//                                       Response.ErrorListener ErrorCallback ,
//                                       String TAG_ID){
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest( Request.Method.GET,
//                URL +Resource,
//                null,
//                SuccessCallback ,
//                ErrorCallback){
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("token", TOKEN);
//                return headers;
//            }};
//
//        jsonObjReq.setShouldCache(false);
//
//        VolleyAppController.getInstance().getRequestQueue().getCache().remove( URL+Resource );
//        VolleyAppController.getInstance().getRequestQueue().getCache().invalidate(URL+Resource, true);
//        // Adding request to request queue
//        VolleyAppController.getInstance().addToRequestQueue( jsonObjReq, TAG_ID );
//
//    }//end GETJsonRequest function
//
//
//
//    /**
//     * GET the unique resource id for GET/POST the parches images
//     * eg: /image/parche/
//     * */
//    public static String getParchesImagesURI(){
//        return URI_PARCHE_IMG;
//    }
//
//    /**
//     * GET the complete URL to GET the profile pic
//     * eg: http://parches.me/image/parche/username/
//     * */
//    public static String getParchesImagesURL(){
//        return URL + URI_PARCHE_IMG;
//    }
//
//    /**
//     * GET the unique resource id for GET/POST the profile pic
//     * eg: /image/profile/
//     * */
//    public static String getProfilePicURI(){
//        return URI_PROFILE_IMG;
//    }
//
//
//    /**
//     * GET the complete URL to GET the profile pic
//     * eg: http://parches.me/****
//     * */
//    public static String getProfilePicURL(){
//        return URL + URI_PROFILE_IMG;
//    }
//
//    /**
//     * GET the unique resource id for GET/POST the profile pic
//     * eg: pic_profile
//     * */
//    public static String getOtherImageURI(){
//        return URI_OTHER_IMG;
//    }
//
//
//    /**
//     * GET the complete URL to GET the profile pic
//     * eg: http://parches.me/****
//     * */
//    public static String getOtherImageURL(){
//        return URL + URI_OTHER_IMG;
//    }
//
//
//
////    private static void run( ) {
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////            }}, 0);
////        AsyncTask.execute(new Runnable() {
////            @Override
////            public void run() {
////            }
////        });
////        new Thread(new Runnable() {
////            public void run() {
////            }
////        }).start();
////    }//END run
//
//
//    /**
//     * load an image from server and set it in an ImageView and Cache it in Disk
//     * */
//    public void GETImage( final String url, final ImageView iv ){
//        Glide.with( MainActivity.newInstance() ).load( url )
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into( iv );
//    }//end GETImage function
//
//
//    /**
//     * load an image from server and set it in an ImageView
//     *
//     * NO CACHE BUT WITH THE DAY SIGNATURE
//     * */
//    public void GETImageNoCache( final String url, final ImageView iv ){
//
////        Glide.with( MainActivity.newInstance() ).load( url )
////                .diskCacheStrategy(DiskCacheStrategy.NONE)
////                .skipMemoryCache(true)
////                .into( iv );
//        Glide.with( MainActivity.newInstance() ).load( url )
//                .signature(new StringSignature(todaySignature))
//                .into( iv );
//    }//end GETImage function
//
//
//    /**
//     * load an image from server and set it in an ImageView and Cache it in Disk
//     * */
//    public void CANCELImageLoad( final ImageView iv ){
//        Glide.clear( iv );
//    }//end GETImage function
//
//
//    /**
//     * load an image from server and set it in an ImageView
//     *
//     * NO CACHE NO DAY SIGNATURE, DIRECTLY FROM SERVER
//     *
//     * * USED to get mainly the user pic BY MyInfroFragment
//     *
//     * */
//    public void GETImageNoCacheNoSignature( final String url, final ImageView iv ){
//
//        Glide.with( MainActivity.newInstance() ).load( url )
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into( iv );
//    }//end GETImage function
//
//
//
//    /**
//     * load an image from server and set it in an ImageView transform to circle
//     * RAM CACHE
//     * */
//    public void GETCircleImage( final String url, final ImageView iv ){
//        MainActivity ctx = MainActivity.newInstance();
//        Glide.with( ctx ).load( url )
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(ctx))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .placeholder(R.drawable.ic_user_not_found)
//                .into( iv );
//    }//end GETImage function
//
//
//
//    /**
//     * load an image from server and set it in an ImageView transform to circle
//     * NO CACHE STRATEGY, NO RAM CACHE
//     * */
//    public void GETCircleImageNoCache( final String url, final ImageView iv  ){
//        MainActivity ctx = MainActivity.newInstance();
//        Glide.with( ctx ).load( url )
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(ctx))
//                .signature(new StringSignature(todaySignature))
//                .placeholder(R.drawable.ic_user_not_found)
//                .into( iv );
//
//    }//end GETImage function
//
//
//
//    /**
//     * load an image from server and set it in an ImageView transform to circle
//     * NO CACHE STRATEGY, NO RAM CACHE
//     *
//     * * USED to get mainly the user pic BY User model
//     * */
//    public void GETCircleImageNoCacheNoSignature( final String url, final ImageView iv  ){
//        MainActivity ctx = MainActivity.newInstance();
//        Glide.with( ctx ).load( url )
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(ctx))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .placeholder(R.drawable.ic_user_not_found)
//                .into( iv );
//
//    }//end GETImage function
//
//
//
//    /**
//     * Upload an image
//     *
//     * if the image is larger than 1Mb resize, save in external storage and upload
//     *
//     *
//     * @param imagePath the absoluthe image path
//     * @param imageBitmap the bitmap for the image to upload
//     * @param image_index it is required for a parche image
//     * @param parche_nid it is required for a parche image
//     * @param TAG_ID the TAG to cancel the request if is required
//     *
//     *
//     * */
//    public void UploadImage(    final String Resource,
//                                final Response.Listener<String> SuccessCallback,
//                                final Response.ErrorListener ErrorCallback ,
//                                final String imagePath,
//                                final Bitmap imageBitmap,
//                                final int image_index,
//                                final int parche_nid,
//                                final String TAG_ID ){
//
//        final File imageFile = new File(imagePath);
//        if ( imageFile.isFile()) {
//            Thread thread = new Thread(new Runnable(){
//                @Override
//                public void run() {
//
//                    try {
//
////                        String lineEnd = "\r\n";
////                        String twoHyphens = "--";
//                        String boundary = "*****";
//                        String filename = imageFile.getAbsolutePath();
//                        int bytesRead, bytesAvailable, bufferSize;
//                        byte[] buffer;
//                        int maxBufferSize = One_Mega_Byte; // Max buffer 1Mb
//                        // open a URL connection to the Servlet
//                        URL url = new URL(URL + Resource);
//
//                        // Open a HTTP  connection to  the URL
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setDoInput(true); // Allow Inputs
//                        conn.setDoOutput(true); // Allow Outputs
//                        conn.setUseCaches(false); // Don't use a Cached Copy
//                        conn.setRequestMethod("POST");
//                        conn.setConnectTimeout(TIMEOUT); //set connection timeout to 60 seconds
//                        conn.setReadTimeout(TIMEOUT); //set read timeout to 60 seconds
//                        conn.setRequestProperty("Connection", "Keep-Alive");
//                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                        conn.setRequestProperty("token", TOKEN);
//                        //Required date for upload a Parche image
//                        conn.setRequestProperty("type", filename);
//                        conn.setRequestProperty("parche", ""+parche_nid);
//                        conn.setRequestProperty("image", ""+image_index);
//
//                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
////                        dos.writeBytes(twoHyphens + boundary + lineEnd);
////                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=" + fileName + "" + lineEnd);
////                        dos.writeBytes(lineEnd);
//
//
//                        //upload the original image without compress
//                        FileInputStream fileInputStream = new FileInputStream(imageFile);
//                        File fileCompress = null;
//
//                        //if the image size is larger than 1MB compress
//                        // Compress image, save the new compress image and upload
//                        if( imageFile.length() > One_Mega_Byte ){
//                            File parchesDirectory = new File(PATH);
//
//                            // have the object build the directory structure, if needed.
//                            if( parchesDirectory.isDirectory() || parchesDirectory.mkdirs() ){
//                                String name = "1";
//                                if ( parche_nid > -1 && image_index > -1 ){
//                                    name = parche_nid+"_"+image_index;
//                                }
//                                fileCompress = new File( PATH+name+".jpg");
//                                FileOutputStream filecon = new FileOutputStream(fileCompress);
//                                Bitmap finalBitmap = imageBitmap;
//                                if( finalBitmap == null ){
//                                    finalBitmap = BitmapFactory.decodeFile(imagePath);
//                                }
//
//                                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, filecon);
//                                filecon.close();
//                                fileInputStream = new FileInputStream(fileCompress);
//                            }//end directory check
//
//                        }//end compress image condition
//
//                        // create a buffer of  maximum size
//                        bytesAvailable = fileInputStream.available();
//
//                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                        buffer = new byte[bufferSize];
//
//                        // read file and write it into form...
//                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                        while (bytesRead > 0) {
//
//                            dos.write(buffer, 0, bufferSize);
//                            bytesAvailable = fileInputStream.available();
//                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                        }
//
//                        // send multipart form data necessary after file data...
//                        // but the NodeJS server do not need it, the Node server only
//                        // need a RawData (Stream data Bytes) to save the file in the server side
////                        dos.writeBytes(lineEnd);
////                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                        // Responses from the server (code and message)
//                        final int  serverResponseCode = conn.getResponseCode();
//                        final String serverResponseMessage = conn.getResponseMessage();
//
////                        System.out.println("uploadFile > HandlerHttpRequest > UploadImage > HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
//
//                        if (serverResponseCode == 200) {
//
//                            MainActivity.newInstance().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    SuccessCallback.onResponse( serverResponseMessage );
//                                }
//                            });
//                        }else{
//                            MainActivity.newInstance().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    ErrorCallback.onErrorResponse(null);
//                                }
//                            });
//                        }
//
//                        //close the streams //
//                        fileInputStream.close();
//                        dos.flush();
//                        dos.close();
//                        if( fileCompress != null ){
//                            if( !fileCompress.delete() ){
//                                //TODO :: fail deleting file
////                                fileCompress.deleteOnExit();
//                            }
//                        }
//
//                    } catch( ConnectException ce ){
//                        System.out.println( " UploadingImage/ UploadImage > ConnectException \n"+ ce.toString() );
//                        ce.printStackTrace();
//
//                        MainActivity.newInstance().runOnUiThread(new Runnable() {
//                            public void run() {
//                                ErrorCallback.onErrorResponse( new NoConnectionError() );
//                            }
//                        });
//                    } catch (FileNotFoundException fe){
//
//                        MainActivity.newInstance().runOnUiThread(new Runnable() {
//                            public void run() {
//                                MainActivity.newInstance().showToast(R.string.need_permission_storage);
//                                MainActivity.checkPermission("READ_EXTERNAL_STORAGE");
//                                ErrorCallback.onErrorResponse(null);
//                            }
//                        });
//
//                    }catch( Exception e) {
//                        System.out.println( " UploadingImage/ UploadImage > Exception \n"+ e.toString() );
//                        e.printStackTrace();
//
//                        MainActivity.newInstance().runOnUiThread(new Runnable() {
//                            public void run() {
//                                ErrorCallback.onErrorResponse(null);
//                            }
//                        });
//                    }//end try catch
//
//                }});
//
//            thread.start();
//
//        }//end isFile() condition
//    }//end UploadImage function
//
//
//
//
//    /**
//     * POST Request for the most commons resources : The response can be json, xml, html or plain text.
//     * */
//    public void POSTRequest( String Resource,
//                             Response.Listener<JSONObject> SuccessCallback,
//                             Response.ErrorListener ErrorCallback ,
//                             final JSONObject params,
//                             String TAG_ID){
//
//        VolleyFixJsonObjectRequestWithNull jsonObjReq = new VolleyFixJsonObjectRequestWithNull(   Request.Method.POST,
//                URL +Resource,
//                null,
//                SuccessCallback,
//                ErrorCallback) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("token", TOKEN);
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() {
//                try {
//                    //params.put("lang", Locale.getDefault().getLanguage() );
//                    String requestBody = params.toString();
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (Exception uee) {
//                    //VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }
//
//        };
//
//        //ADDING No timeout for the request, avoiding mae a POST twice
//        jsonObjReq.setRetryPolicy( new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        // Adding request to request queue
//        VolleyAppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ID);
//
//    }//end POSTRequest function
//
//
//
//
//    /**
//     * PUT Request for any String : The response can be json, xml, html or plain text.
//     * */
//    public void PUTRequest( String Resource,
//                            Response.Listener<JSONObject> SuccessCallback,
//                            Response.ErrorListener ErrorCallback ,
//                            final JSONObject params,
//                            String TAG_ID){
//
//        //avoid empty json data send
//        if( params == null || params.length()==0 ){
//            try {
//                JSONObject resp = new JSONObject();
//                resp.put("success",true);
//                resp.put("data",null);
//                resp.put("error_code","noChange");
//                SuccessCallback.onResponse( resp );
//            } catch (JSONException e) {
//                //e.printStackTrace();
//            }
//            return;
//        }
//
//
//        VolleyFixJsonObjectRequestWithNull jsonObjReq = new VolleyFixJsonObjectRequestWithNull(   Request.Method.PUT,
//                URL +Resource,
//                null,
//                SuccessCallback,
//                ErrorCallback) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("token", TOKEN);
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() {
//                try {
//                    //params.put("lang", Locale.getDefault().getLanguage() );
//                    String requestBody = params.toString();
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (Exception uee) {
//                    //VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }
//
//        };
//
//        // Adding request to request queue
//        VolleyAppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ID);
//
//    }//end PUTRequest function
//
//
//
//
//    /**
//     * DELETE Request for any String : The response can be json, xml, html or plain text.
//     * */
//    public void DELETERequest( String Resource,
//                               Response.Listener<JSONObject> SuccessCallback,
//                               Response.ErrorListener ErrorCallback ,
//                               final JSONObject params,
//                               String TAG_ID){
//
//        VolleyFixJsonObjectRequestWithNull jsonObjReq = new VolleyFixJsonObjectRequestWithNull(   Request.Method.DELETE,
//                URL +Resource,
//                null,
//                SuccessCallback,
//                ErrorCallback) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("token", TOKEN);
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() {
//                try {
//                    //params.put("lang", Locale.getDefault().getLanguage() );
//                    String requestBody = params.toString();
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (Exception uee) {
//                    //VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }
//
//        };
//
//        // Adding request to request queue
//        VolleyAppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ID);
//
//    }//end DELETEJsonRequest function
//
//
//
//
//
//    /** Get the error string from a volley error */
//    public String getVolleyErrorMsg(VolleyError error ){
//
//        if( error == null ){ return ""; }
//
//        String error_msg = "";
//        //VolleyLog.d(TAG, "Error: " + error.getMessage());
//        if (error instanceof TimeoutError || error instanceof NoConnectionError ) {
//            return null;
//            //TODO :: make the logic to wait internet connection
//            //error_msg = ERRMSG_server_down+ " " + ERRMSG_try_again;
//        } else if (error instanceof AuthFailureError) {
//            error_msg = ERRMSG_try_again;
//        } else if (error instanceof ServerError) {
//            error_msg = ERRMSG_unexpectederror + " " + ERRMSG_try_again;
//        } else if (error instanceof NetworkError) {
//            error_msg = ERRMSG_try_again;
//        } else if (error instanceof ParseError) {
//            error_msg = ERRMSG_unexpectederror + " " + ERRMSG_try_again;
//        }
//
//        NetworkResponse response = error.networkResponse;
////        System.out.println( "error.networkResponse :: isNull: "+(error.networkResponse==null) );
//        if( response != null ){
////            System.out.println( "error.networkResponse :: status code: " + error.networkResponse.statusCode );
//            if( response.statusCode< 400 ){
//                error_msg = ERRMSG_empty;
//            }else {
//
//                switch (response.statusCode) {
//                    case 401:
//                        error_msg = ERRMSG_401;
//                        break;
//                    case 403:
//                        error_msg = ERRMSG_403;
//                        break;
//                    case 404:
//                        error_msg = ERRMSG_404;
//                        break;
//                    case 500:
//                        error_msg = ERRMSG_500;
//                        break;
//                    default:
//                        error_msg = this.ERRMSG;
//                        break;
//                }
//            }
//
//            try {
//                // Deserialize data using what you want
//                JSONObject obj = new JSONObject(new String(response.data) );
////                System.out.println( "JSONObject: "+obj.getString("data") );
//            } catch (Exception e) {
//                //e.printStackTrace();
//            }
//        }
//
//        return error_msg;
//    }//end getVolleyErrorMsg function (Volley errors)
//
//
//
//
//
//    /*** Get the error string from a error_code server and replace "data" If that's the case  */
//    public String getErrorMsg(String error_code , String data ){
//
//        String error = this.getErrorMsg(error_code);
//
//        if( error.contains("ARG1") ){
//            if( data!=null ){
//                error = error.replace("ARG1",data);
//            }else{
//                error = error.replace("ARG1","").replace("\"\"","");
//            }
//        }
//
//        return error;
//    }//use the private method
//
//    /*** PRIVATE Get the error string from a error_code server */
//    private String getErrorMsg(String error_code ){
//
//        try {
//            switch ( error_code.toLowerCase() ) {
//                // Errores Generales
//
//                case "err_msg": return ERRMSG;
//
//                case "server_down": return ERRMSG_server_down;
//                case "err_msg_empty": return ERRMSG_empty;
//                case "err_msg_volley": return ERRMSG_volley;
//                case "err_msg_401": return ERRMSG_401;
//                case "err_msg_403": return ERRMSG_403;
//                case "err_msg_404": return ERRMSG_404;
//                case "err_msg_500": return ERRMSG_500;
//
//                case "retry": return ERRMSG_retry;
//                case "noChange": return ERRMSG_no_change;
//                case "enter_some": return ERRMSG_enter_some;
//                case "try_again": return ERRMSG_try_again;
//                case "notfound": return ERRMSG_notfound;
//                case "unexpectederror": return ERRMSG_unexpectederror;
//                case "welcome_messages": return ERRMSG_welcome_messages;
//                case "missingform": return ERRMSG_missingform;
//                case "missingformfield": return ERRMSG_missingformfield;
//                case "validationerror": return ERRMSG_validationerror;
//
//                // Errores de Usuarios
//
//                case "usernotfound": return ERRMSG_usernotfound;
//                case "usernamerestrictedchars": return ERRMSG_usernamerestrictedchars;
//                case "usernametaken": return ERRMSG_usernametaken;
//                case "missingusersecurity": return ERRMSG_missingusersecurity;
//                case "badusersecurity": return ERRMSG_badusersecurity;
//                case "invalidpassword": return ERRMSG_invalidpassword;
//                case "wrongpassword": return ERRMSG_wrongpassword;
//
//                // Errores de Parches
//
//                case "parchenotfound": return ERRMSG_parchenotfound;
//                case "messagenotfound": return ERRMSG_messagenotfound;
//                case "inscribefirst": return ERRMSG_inscribefirst;
//                case "privateparche": return ERRMSG_privateparche;
//                case "inactiveparche": return ERRMSG_inactiveparche;
//                case "parcheisactive": return ERRMSG_parcheisactive;
//                case "fullparche": return ERRMSG_fullparche;
//                case "owner": return ERRMSG_owner;
//                case "waitingforuser": return ERRMSG_waitingforuser;
//                case "messagehasreply": return ERRMSG_messagehasreply;
//
//                default:
//                    return this.ERRMSG;
//            }
//        } catch (Exception e) {
//            return this.ERRMSG_try_again;
//        }
//    }//end getVolleyErrorMsg function (server errors)
//
//    public void cancelRequest( String tag ){
//        VolleyAppController.getInstance().cancelPendingRequests(tag);
//    }
//
//    /* fill the error messages variables */
//    private void fillErrorMessages(Context context) {
//
//        if( ERRMSG_server_down.isEmpty()) {
//
//            //Errores Generales
//            ERRMSG_server_down  = context.getString(R.string.err_msg_serverdown);
//            ERRMSG              = context.getString(R.string.err_msg);
//            ERRMSG_empty        = context.getString(R.string.err_msg_empty);
//            ERRMSG_retry        = context.getString(R.string.retry);
//            ERRMSG_no_change    = context.getString(R.string.err_msg_no_change);
//            ERRMSG_enter_some   = context.getString(R.string.err_msg_enter_some);
//            ERRMSG_volley       = context.getString(R.string.err_msg_volley);
//            ERRMSG_401          = context.getString(R.string.err_msg_401);
//            ERRMSG_403          = context.getString(R.string.err_msg_403);
//            ERRMSG_404          = context.getString(R.string.err_msg_404);
//            ERRMSG_500          = context.getString(R.string.err_msg_500);
//
//            ERRMSG_try_again        = context.getString(R.string.err_msg_try_again);
//            ERRMSG_notfound         = context.getString(R.string.err_msg_notfound);
//            ERRMSG_unexpectederror  = context.getString(R.string.err_msg_unexpectederror);
//            ERRMSG_welcome_messages = context.getString(R.string.err_msg_welcome_messages);
//            ERRMSG_missingform      = context.getString(R.string.err_msg_missingform);
//            ERRMSG_missingformfield = context.getString(R.string.err_msg_missingformfield);
//            ERRMSG_validationerror  = context.getString(R.string.err_msg_validationerror);
//
//            //Errores de Usuarios
//            ERRMSG_usernotfound             = context.getString(R.string.err_msg_usernotfound);
//            ERRMSG_usernamerestrictedchars  = context.getString(R.string.err_msg_usernamerestrictedchars);
//            ERRMSG_usernametaken            = context.getString(R.string.err_msg_usernametaken);
//            ERRMSG_missingusersecurity      = context.getString(R.string.err_msg_missingusersecurity);
//            ERRMSG_badusersecurity          = context.getString(R.string.err_msg_badusersecurity);
//            ERRMSG_invalidpassword          = context.getString(R.string.err_msg_invalidpassword);
//            ERRMSG_wrongpassword            = context.getString(R.string.err_msg_wrongpassword);
//
//            //Errores de Parches
//            ERRMSG_parchenotfound   = context.getString(R.string.err_msg_parchenotfound);
//            ERRMSG_messagenotfound  = context.getString(R.string.err_msg_messagenotfound);
//            ERRMSG_inscribefirst    = context.getString(R.string.err_msg_inscribefirst);
//            ERRMSG_privateparche    = context.getString(R.string.err_msg_privateparche);
//            ERRMSG_inactiveparche   = context.getString(R.string.err_msg_inactiveparche);
//            ERRMSG_parcheisactive   = context.getString(R.string.err_msg_parcheisactive);
//            ERRMSG_fullparche       = context.getString(R.string.err_msg_fullparche);
//            ERRMSG_owner            = context.getString(R.string.err_msg_owner);
//            ERRMSG_waitingforuser   = context.getString(R.string.err_msg_waitingforuser);
//            ERRMSG_messagehasreply  = context.getString(R.string.err_msg_messagehasreply);
//        }
//
//    }//end fillErrorMessages
//
//
//
//    //clear SharedPreferences
//    public void saveToken( Context context, String tkn ){
////        VolleyAppController.getInstance().getRequestQueue().getCache().clear();
////System.out.println( " *********************************************************** " );
////System.out.println( "saveToken: " + tkn );
////System.out.println( " *********************************************************** " );
////if( tkn == null) MainActivity.newInstance().showSnackBar( "saveToken: " + tkn );
//        TOKEN = tkn;
////        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( context );
//        SharedPreferences settings = context.getSharedPreferences("token", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString( TAG_TOKEN , tkn);
//        editor.clear();
//        editor.commit();
//        editor.apply();
//    }
//
//
//    public String getToken( Context context ){
////        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences settings = context.getSharedPreferences("token", Context.MODE_PRIVATE);
//        TOKEN = settings.getString( TAG_TOKEN , null);
//
////        System.out.println( "getToken: " + TOKEN );
//        return TOKEN;
//    }
//
//
//
//    public void saveLanguage( Context context, String lang ){
//        SharedPreferences settings = context.getSharedPreferences(TAG_LANGUAGE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString( TAG_LANGUAGE , lang);
//        editor.clear();
//        editor.commit();
//        editor.apply();
//    }
//
//    public String getLanguage( Context context ){
//        SharedPreferences settings = context.getSharedPreferences(TAG_LANGUAGE, Context.MODE_PRIVATE);
//        String lang = settings.getString( TAG_LANGUAGE , "es");
//        if( lang.equals("en")) currentLocale =  Locale.ENGLISH;
//        return lang;
//    }
//
//    public static Locale getLanguageLocale(){
//        return currentLocale;
//    }
//
//    /**
//     * Save a time in millis to know when is the next check
//     * the time saved is NOW add 12 hours menas that the next check is in 12hours
//     * */
//    public boolean timeToCheckVersion( Context context ){
//        SharedPreferences settings = context.getSharedPreferences(TAG_CHECK_VERSION, Context.MODE_PRIVATE);
//        Long next_check = settings.getLong(TAG_CHECK_VERSION, -1);
//        boolean isTime = false;
//        Date now = new Date();
//        if( next_check == -1 ){
//            isTime = true;
//        }else if( now.getTime() > next_check ){
//            isTime = true;
//        }
//
//        if( isTime ){
//            now.setTime(now.getTime()+(12*60*60*1000));//NEXT Check in 12 hours
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putLong( TAG_CHECK_VERSION, now.getTime() );
//            editor.clear();
//            editor.commit();
//            editor.apply();
//        }
//
//        return isTime;
//    }//END timeToCheckVersion function
//
//    public String getError_msg_empty() {
//        return ERRMSG_empty;
//    }
//}
