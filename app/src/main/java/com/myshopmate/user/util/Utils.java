package com.myshopmate.user.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.myshopmate.user.ModelClass.Store;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static final int YESTERDAY = 0;
    public static final int FIRST_DAY_OF_WEEK = 1;
    public static final int LAST_DAY_OF_WEEK = 2;
    public static final int FIRST_DAY_OF_MONTH = 3;
    public static final int LAST_DAY_OF_MONTH = 4;

    public static final HashMap<String, Store> stores = new HashMap<>();

    // TODO Get String From Spinner
    public static String stringOf(Spinner spinner) {

        return spinner.getSelectedItem().toString();
    }

    // TODO Get String From CheckBox  1 or 0
    public static String stringOf(CheckBox checkBox) {

        return Integer.toString((checkBox.isChecked()) ? 1 : 0);
        //return (checkBox.isChecked()) ? "y" : "n";
    }

    // TODO Get String of image
    public static String stringOf(ImageView imageView) {
        Bitmap bitmap = null;
        boolean hasDrawable = imageView.getDrawable() != null;
        if (hasDrawable) {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return "";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }

    // TODO Get Bitmap From ImageView
    public static Bitmap getBitmap(ImageView imageView) {

        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    // TODO Get Bitmap from encoded image String
    public static Bitmap getBitmap(String encodedImageString) {

        byte[] decodedString = Base64.decode(encodedImageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


    }

    //TODO Get Encoded image String From Bitmap
    public static String getEncodedImageString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // TODO Get byte Array from image Bitmap
    public static byte[] getBitmapByteArray(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    // TODO Get file path from URI
    public static String getRealPathFromURI(Context ctx, Uri contentURI) {

        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = ctx.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
            cursor.close();
        }

        return thePath;
    }

    // TODO Save and open JPEG Image file
    public static void saveAndOpenJPEGFile(Context context, Bitmap bitmap, String dir, String name_with_ext) {

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/" + dir);
        myDir.mkdirs();
        // String fName = nameWithoutExtension + ".jpg";
        File file = new File(myDir, name_with_ext);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        openImageWithGallery(context, file);
    }

    // TODO Open Image file with Gallery
    public static void openImageWithGallery(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        context.startActivity(intent);

    }

    public static boolean PressedOnce = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void doubleBackExit(final Context context, View view) {


        if (PressedOnce) {
            //super.onBackPressed();
            ((Activity) context).finishAffinity();
            return;
        }

        PressedOnce = true;
        //Toast.makeText(context, "Press again to exit", Toast.LENGTH_LONG).show();
        Snackbar.make(view,"Press again to exit", BaseTransientBottomBar.LENGTH_LONG).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                PressedOnce = false;
            }
        }, 3000);

    }


    public static String getDay(int DAY, String format) {


        final Calendar calendar = Calendar.getInstance();
        switch (DAY) {
            case YESTERDAY:

                calendar.add(Calendar.DAY_OF_MONTH, -1);

                break;
            case FIRST_DAY_OF_WEEK:

                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                break;
            case LAST_DAY_OF_WEEK:

                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);

                break;
            case FIRST_DAY_OF_MONTH:

                calendar.set(Calendar.DAY_OF_MONTH, 1);

                break;
            case LAST_DAY_OF_MONTH:

                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                break;
        }


        return new SimpleDateFormat(format, Locale.US).format(calendar.getTime());

    }


    public static String getCurrentTime(String format) {

        return new SimpleDateFormat(format, Locale.US).format(Calendar.getInstance().getTime());
    }

    // TODO Date alert dialog box
    public static void setDate(Context context, final EditText editText) {

        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        editText.setText(str);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                        try {
                            Date date = df.parse(str);
                            editText.setText(df.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    // TODO Time alert dialog box
    public static void setTime(Context context, final EditText editText) {

        int Hour, Minute;

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        Hour = c.get(Calendar.HOUR_OF_DAY);
        Minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        editText.setText(hourOfDay + ":" + minute);
                    }
                }, Hour, Minute, false);
        timePickerDialog.show();

    }

    // TODO check Internet connection is available or not
    public static boolean isNetworkAvailable(Context context) {
        //todo  to use this you must need internet permission in manifest.xml file
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());

    }

    // TODO Enter or exit fullScreen
    public static void makeFullscreen(Context context, boolean isRequiredFullscreen) {

        if (isRequiredFullscreen) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {

            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    // TODO Display welcome screen
    public static void splashScreen(final Context context1, final Class<?> NextActivity, int TIME_IN_MILLIS) {

        ((Activity) context1).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        ((Activity) context1).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                context1.startActivity(new Intent(context1, NextActivity));
                ((Activity) context1).finish();
            }

        }, TIME_IN_MILLIS);


    }


    // TODO Close Application (single method)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void exitApp(Context context) {

        ((Activity) context).finishAffinity();
    }

    public static void restartApp(Activity ctx) {
        Intent i = ctx.getBaseContext().getPackageManager().getLaunchIntentForPackage(ctx.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    // TODO Share text content to other apps
    public static void shareText(Context context, String text_to_share) {
        String title = "Share with";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text_to_share);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    // TODO generate file name with current date and time
    public static String generateFileName(String prfx, String extension) {

        String str = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.US).format(Calendar.getInstance().getTime());
        return prfx + str + extension;

    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    public static void pickTime(Context context, final EditText tm) {
        final Calendar calendar = Calendar.getInstance();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        tm.setText(hourOfDay + ":" + minute);

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public static String getRandomString(final int sizeOfRandomString) {
        final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private File makeFile(String dir, String name) {

        File DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + dir);
        DIRECTORY.mkdirs();
        return new File(DIRECTORY, name);

    }
    // private static final String ALLOWED_CHARACTERS ="0123456789";

    private ArrayList<String> getDates(String from_date, String to_date, String sdf_pattern) {

        ArrayList<String> list = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat(sdf_pattern, Locale.getDefault());

        Date from = null;
        Date to = null;

        try {
            from = sdf.parse(from_date);
            to = sdf.parse(to_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(from);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(to);

        while (!cal1.after(cal2)) {

            String str = sdf.format(cal1.getTime());
            list.add(str);

            //add one date
            cal1.add(Calendar.DATE, 1);
        }
        return list;

    }

    private boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app.firstRun", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstrun", true)) {
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
            return true;
        } else {
            return false;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(view,0);
    }

    public static boolean backupDatabase(Context context, String database_name, String backup_dir) {

        String databasePath = context.getDatabasePath(database_name).getPath();

        File database_file = new File(databasePath);

        OutputStream outputStream = null;

        InputStream inputStream = null;

        if (database_file.exists()) {

            try {
                File backup_directory = new File(Environment.getExternalStorageDirectory() + File.separator + backup_dir);

                if (!backup_directory.exists())
                    backup_directory.mkdirs();


                outputStream = new FileOutputStream(backup_directory.getAbsolutePath() + "/" + database_name);

                inputStream = new FileInputStream(databasePath);

                byte[] buffer = new byte[1024];

                int length;

                while ((length = inputStream.read(buffer)) > 0) {

                    outputStream.write(buffer, 0, length);

                }

                outputStream.flush();

            } catch (Exception e) {

                return false;

            } finally {

                try {
                    if (outputStream != null) {

                        outputStream.close();

                    }
                    if (inputStream != null) {

                        inputStream.close();

                    }
                } catch (Exception e) {

                }
            }
        }
        return true;
    }

    public static boolean restoreDatabase(Context context, String database_name, File backup_file) {

        String databasePath = context.getDatabasePath(database_name).getPath();

        File database_file = new File(databasePath);

        OutputStream outputStream = null;

        InputStream inputStream = null;

        if (database_file.exists()) {

            try {

                outputStream = new FileOutputStream(databasePath);

                inputStream = new FileInputStream(backup_file);

                byte[] buffer = new byte[1024];

                int length;

                while ((length = inputStream.read(buffer)) > 0) {

                    outputStream.write(buffer, 0, length);

                }

                outputStream.flush();

            } catch (Exception e) {

                return false;

            } finally {

                try {

                    if (outputStream != null) {

                        outputStream.close();

                    }
                    if (inputStream != null) {

                        inputStream.close();

                    }

                } catch (Exception e) {

                }
            }
        }
        return true;
    }

    public static String convertDateString(String date_str, String from_format, String to_format) {
        // ymd = yyyy-MM-dd
        //dmy = dd-MM-yyyy

        @SuppressLint("SimpleDateFormat") SimpleDateFormat from = new SimpleDateFormat(from_format);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat to = new SimpleDateFormat(to_format);

        try {
            Date date = from.parse(date_str);

            return to.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date_str;
    }

    public static double calculateMapDistance(double lat1, double lng1,double lat2, double lng2) {
        int radius = 6371;// radius of earth in Km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return radius * c;
    }

    private int getWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthInPixels = displayMetrics.widthPixels;
        return convertToDp(widthInPixels);
    }

    private int getHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightInPixels = displayMetrics.heightPixels;
        return convertToDp(heightInPixels);
    }

    private int convertToDp(int pixels) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, displaymetrics);
    }

    public static String formatDateTimeString(String dateTimeStr, String dateTimeFormat, String convertTo) {
        String formattedDate = "";
        SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat, Locale.US);
        try { Date date = format.parse(dateTimeStr);
            if (date != null) {
                formattedDate = new SimpleDateFormat(convertTo, Locale.US).format(date);
            }
        } catch (ParseException ignored) {
            Log.d("DateTime","Error");
        }
        return formattedDate;
    }

    public static boolean isOnline(Activity context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}


/*spinner selection
 *
 * C1.setSelection(adapter.getPosition(c.getString(KEY_C1)));
 *
 */



/*if you want hide title bar in your activity just add this two lines in Base Application Theme in style.xml
 *
 *  <item name="windowActionBar">false</item>
 *  <item name="windowNoTitle">true</item>
 *
 */


// int i = Arrays.asList(getResources().getStringArray(R.array.a)).indexOf("Mercury");



/*
array list to array


List<String> stockList = new ArrayList<String>();
stockList.add("stock1");
stockList.add("stock2");

String[] stockArr = new String[stockList.size()];
stockArr = stockList.toArray(stockArr);

*/

