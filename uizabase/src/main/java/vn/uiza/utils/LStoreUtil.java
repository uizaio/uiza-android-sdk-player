package vn.uiza.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;

import androidx.annotation.NonNull;

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

import timber.log.Timber;

public final class LStoreUtil {
    private static final String SLASH = "/";
    private static String folderPath;

    public interface CallbackReadFile {
        void onFinish(String result);
    }

    public interface CallbackWriteFile {
        void onFinish(boolean isSuccess);
    }

    public interface EventReadFromFolder {
        void onSuccess(String data);

        void onError();
    }

    private LStoreUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getFolderPath(@NonNull Context context) {
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
                Timber.e(e, "if getFolderPath:");
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
                Timber.e(e, "else getFolderPath:");
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
    public static boolean writeToFile(@NonNull Context context, String folder, String fileName, String body) {
        boolean isComplete = true;
        FileOutputStream fos = null;
        try {
            String path = LStoreUtil.getFolderPath(context);
            if (folder != null) {
                //path = path + "/" + folder;
                //path = path + folder;
                path = path + folder + SLASH;
            }
            Timber.d("path: %s", path);
            final File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Timber.d("could not create the directories");
                }
            }
            final File myFile = new File(dir, fileName);
            if (!myFile.exists()) {
                boolean isSuccess = myFile.createNewFile();
                Timber.d("isSuccess: %b", isSuccess);
            }
            fos = new FileOutputStream(myFile);
            fos.write(body.getBytes());
            fos.close();
        } catch (IOException e) {
            Timber.e(e);
            isComplete = false;
        } finally {
            AppUtils.closeIO(fos);
        }
        return isComplete;
    }

    public static void writeToFile(@NonNull Context context, final String folder, final String fileName, final String body, final CallbackWriteFile callbackWriteFile) {
        new AsyncTask<Void, Void, Void>() {
            boolean isSuccess;

            @Override
            protected Void doInBackground(Void... params) {
                isSuccess = writeToFile(context, folder, fileName, body);
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
    public static String readTxtFromFolder(@NonNull Context context, String folderName, String fileName) {
        String path = LStoreUtil.getFolderPath(context) + (folderName == null ? SLASH : (folderName + SLASH)) + fileName;
        Timber.d("path: %s", path);
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
            Timber.d(e, "readTxtFromFolder===");
        } finally {
            AppUtils.closeIO(reader);
        }
        return text.toString();
    }

    public static void readTxtFromFolder(@NonNull Context context, final String folderName, final String fileName, final CallbackReadFile callbackReadFile) {
        new AsyncTask<Void, Void, Void>() {
            String result = "";

            @Override
            protected Void doInBackground(Void... params) {
                result = readTxtFromFolder(context, folderName, fileName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callbackReadFile.onFinish(result);
            }
        }.execute();
    }


    /**
     * read text file from folder in background
     */
    public static void readTxtFromFolder(@NonNull Context context, final String folderName, final String fileName, final EventReadFromFolder eventReadFromFolder) {
        new AsyncTask<Void, Void, Void>() {
            private StringBuilder text = null;
            private boolean runTaskSuccess = true;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                String path = LStoreUtil.getFolderPath(context) + (folderName == null ? SLASH :
                        (folderName + SLASH)) + fileName;
                Timber.d("path: %s", path);
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
                    Timber.d(e, "readTxtFromFolder===");
                } finally {
                    AppUtils.closeIO(reader);
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
    public static String readTxtFromRawFolder(@NonNull Context context, int nameOfRawFile) {
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
            Timber.e(e);
        } finally {
            AppUtils.closeIO(inputStream);
        }
        return byteArrayOutputStream.toString();
    }

    public interface CallbackReadFromRaw {
        void onFinish(String result);
    }

    public static void readTxtFromRawFolder(@NonNull Context context, final int nameOfRawFile, final CallbackReadFromRaw callbackReadFromRaw) {
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

    public static boolean saveHTMLCodeFromURLToSDCard(@NonNull Context context, String link, String folderName, String fileName) {
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
            Timber.e(e);
        } finally {
            AppUtils.closeIO(br, is);
        }
        return state;
    }

    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}