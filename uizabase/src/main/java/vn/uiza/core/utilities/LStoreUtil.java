package vn.uiza.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import vn.uiza.utils.util.CloseUtils;
import vn.uiza.utils.util.SentryUtils;

public class LStoreUtil {
    private static String TAG = LStoreUtil.class.getSimpleName();
    private static final String SLASH = "/";
    private static String folderPath;

    public interface CallbackReadFile {
        void onFinish(String result);
    }

    public interface CallbackWriteFile {
        void onFinish(boolean isSuccess);
    }

    public static String getFolderPath(Context context) {
        String folderName = "UZ_Folder";
        if (isSdPresent()) {
            try {
                File sdPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + SLASH + folderName);
                if (!sdPath.exists()) {
                    sdPath.mkdirs();
                    folderPath = sdPath.getAbsolutePath();
                } else if (sdPath.exists()) {
                    folderPath = sdPath.getAbsolutePath();
                }
            } catch (Exception e) {
                LLog.d("TAG", "if getFolderPath: " + e.toString());
                SentryUtils.captureException(e);
            }
            folderPath = Environment.getExternalStorageDirectory().getPath() + SLASH + folderName + SLASH;
        } else {
            try {
                File cacheDir = new File(context.getCacheDir(), folderName + SLASH);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                    folderPath = cacheDir.getAbsolutePath();
                } else if (cacheDir.exists()) {
                    folderPath = cacheDir.getAbsolutePath();
                }
            } catch (Exception e) {
                LLog.d("TAG", "else getFolderPath: " + e.toString());
                SentryUtils.captureException(e);
            }
        }
        return folderPath;
    }

    public static boolean isSdPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static int checkSDCardFreeSize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        long megAvailable = bytesAvailable / (1024 * 1024);
        return (int) megAvailable;
    }

    /**
     * save string json to sdcard
     * ex: writeToFile("module.json", strJson);
     */
    public static boolean writeToFile(Activity activity, String folder, String fileName, String body) {
        boolean isComplete = true;
        FileOutputStream fos = null;
        try {
            String path = LStoreUtil.getFolderPath(activity);
            if (folder != null) {
                //path = path + "/" + folder;
                //path = path + folder;
                path = path + folder + SLASH;
            }
            LLog.d(TAG, "path: " + path);
            final File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    LLog.d(TAG, "could not create the directories");
                }
            }
            final File myFile = new File(dir, fileName);
            if (!myFile.exists()) {
                boolean isSuccess = myFile.createNewFile();
                LLog.d(TAG, "isSuccess: " + isSuccess);
            }
            fos = new FileOutputStream(myFile);
            fos.write(body.getBytes());
            fos.close();
        } catch (IOException e) {
            LLog.d(TAG, e.toString());
            isComplete = false;
            SentryUtils.captureException(e);
        } finally {
            CloseUtils.closeIO(fos);
        }
        return isComplete;
    }

    public static void writeToFile(final Activity activity, final String folder, final String fileName, final String body, final CallbackWriteFile callbackWriteFile) {
        new AsyncTask<Void, Void, Void>() {
            boolean isSuccess;

            @Override
            protected Void doInBackground(Void... params) {
                isSuccess = writeToFile(activity, folder, fileName, body);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callbackWriteFile != null) {
                    callbackWriteFile.onFinish(isSuccess);
                }
            }
        }.execute();
    }

    /**
     * read text file from folder
     */
    public static String readTxtFromFolder(Activity activity, String folderName, String fileName) {
        String path = LStoreUtil.getFolderPath(activity) + (folderName == null ? SLASH : (folderName + SLASH)) + fileName;
        LLog.d(TAG, "path: " + path);
        File txtFile = new File(path);
        StringBuilder text = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(txtFile));
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            LLog.d(TAG, "readTxtFromFolder===" + e.toString());
            SentryUtils.captureException(e);
        } finally {
            CloseUtils.closeIO(reader);
        }
        return text.toString();
    }

    public static void readTxtFromFolder(final Activity activity, final String folderName, final String fileName, final CallbackReadFile callbackReadFile) {
        new AsyncTask<Void, Void, Void>() {
            String result = "";

            @Override
            protected Void doInBackground(Void... params) {
                result = readTxtFromFolder(activity, folderName, fileName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callbackReadFile.onFinish(result);
            }
        }.execute();
    }

    public interface EventReadFromFolder {
        void onSuccess(String data);

        void onError();
    }

    /**
     * read text file from folder in background
     */
    public static void readTxtFromFolder(final Activity activity, final String folderName, final String fileName, final EventReadFromFolder eventReadFromFolder) {
        new AsyncTask<Void, Void, Void>() {
            private StringBuilder text = null;
            private boolean runTaskSuccess = true;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                String path = LStoreUtil.getFolderPath(activity) + (folderName == null ? SLASH :
                        (folderName + SLASH)) + fileName;
                LLog.d(TAG, "path: " + path);
                File txtFile = new File(path);
                text = new StringBuilder();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(txtFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text.append(line).append('\n');
                    }
                    reader.close();
                } catch (IOException e) {
                    runTaskSuccess = false;
                    LLog.d(TAG, "readTxtFromFolder===" + e.toString());
                    SentryUtils.captureException(e);
                } finally {
                    CloseUtils.closeIO(reader);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (runTaskSuccess) {
                    eventReadFromFolder.onSuccess(text.toString());
                } else {
                    eventReadFromFolder.onError();
                }
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    /**
     * read text file in raw folder
     */
    public static String readTxtFromRawFolder(Context context, int nameOfRawFile) {
        InputStream inputStream = context.getResources().openRawResource(nameOfRawFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (Exception e) {
            LLog.d(TAG, e.toString());
            SentryUtils.captureException(e);
        } finally {
            CloseUtils.closeIO(inputStream);
        }
        return byteArrayOutputStream.toString();
    }

    public interface CallbackReadFromRaw {
        void onFinish(String result);
    }

    public static void readTxtFromRawFolder(final Context context, final int nameOfRawFile, final CallbackReadFromRaw callbackReadFromRaw) {
        new AsyncTask<Void, Void, Void>() {
            String result;

            @Override
            protected Void doInBackground(Void... params) {
                result = readTxtFromRawFolder(context, nameOfRawFile);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callbackReadFromRaw.onFinish(result);
            }
        }.execute();
    }

    public static boolean saveHTMLCodeFromURLToSDCard(Context context, String link, String folderName, String fileName) {
        boolean state = false;
        InputStream is = null;
        BufferedReader br = null;
        try {
            URL url = new URL(link);
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();
            is.close();
            writeToFile((Activity) context, folderName, fileName, stringBuilder.toString());
            state = true;
        } catch (Exception e) {
            SentryUtils.captureException(e);
        } finally {
            CloseUtils.closeIO(br, is);
        }
        return state;
    }

    public static int getRandomNumber(int length) {
        Random r = new Random();
        return r.nextInt(length);
    }

    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}