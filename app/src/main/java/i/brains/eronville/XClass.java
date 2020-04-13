package i.brains.eronville;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XClass {
    private static final String buildAPI = "apple";
    private static final String api = "https://api.eronville.com/" + buildAPI + "/";
    private static final String cdn = "https://cdn.eronville.com/" + buildAPI + "/index.php";
    static String sharedPreferences = "spf";
    static String inducted = "intro";

    static String mail = "mail";
    static String surname = "surname";
    static String othername = "othername";
    static String line = "line";
    static String online = "online";
    static String outcast = "";

    static String name = "name";
    static String number = "number";
    static String gender = "gender";
    static String dob = "dob";
    static String avi = "avi";
    static String address = "addrs";
    static String notification = "notifd";


    static String agent = "agt";
    static String agent_line = "agtln";
    static String agent_whatsapp = "agtwh";





    static String apiAddApartment = api + "addapartment.php";
    static String apiRegister = api + "register.php";
    static String apiAgentRegister = api + "agentregister.php";
    static String apiApartments = api + "index.php";
    static String apiAccess = api + "access.php";
    static String apiAgentAccess = api + "agentaccess.php";
    static String apiSendMail = api + "bluemail.php";
    static String apiImages = api + "image.php";

    static void sendNotification(Context cx, String head, String body, Intent intent){
        NotificationManager notificationManager = (NotificationManager) cx.getSystemService(Context.NOTIFICATION_SERVICE);

        int requestCode = 0;
        int requestID = 1;
        String channelId = cx.getPackageName();
        String channelName = cx.getResources().getString(R.string.app_name);
        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT > 23) importance = NotificationManager.IMPORTANCE_HIGH;


        if (Build.VERSION.SDK_INT > 25) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(cx, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(head)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(cx);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(requestID, mBuilder.build()); //notificationID insttead of reuestID
    }




    static void addImageOnServer(String key, String value, String mail){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("key", key)
                .add("value", value)
                .add("mail", mail).build();
        Request request = new Request.Builder().post(body).url(XClass.api + "update.php").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    static void updateUser(String key, String value, String mail){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("key", key)
                .add("value", value)
                .add("mail", mail).build();
        Request request = new Request.Builder().post(body).url(XClass.api + "update.php").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    static void upload(final Activity face, final ProgressBar progress, final String file, final SimpleDraweeView avatar, final String name, final String type){
        if (ContextCompat.checkSelfPermission(face.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(face, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            progress.setVisibility(View.GONE);
        } else {
            class load extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... voids) {
                    int responseCode;
                    String network = "https://cdn.eronville.com/index.php?action=" + type + "&name=" + name;
                    HttpURLConnection conn;
                    DataOutputStream dos;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1024 * 1024;
                    File sourceFile = new File(file);

                    if (!sourceFile.isFile()) {
                        face.runOnUiThread(() -> progress.setVisibility(View.GONE));
                    } else {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(network);

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setUseCaches(false);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("asset", file);

                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"asset\"; filename=\"" + file + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            }

                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            responseCode = conn.getResponseCode();

                            if (responseCode == 200) {
                                face.runOnUiThread(() -> {
                                    try {
                                        String link = "https://cdn.eronville.com/avi/" + name + ".jpeg";
                                        avatar.setImageURI(link);
                                        progress.setVisibility(View.GONE);
                                    } catch (NullPointerException ignored){ }
                                });
                            }

                            fileInputStream.close();

                            dos.flush();
                            dos.close();
                        } catch (final IOException e) {
                            face.runOnUiThread(() -> {
                                Log.e("silvr", e.getMessage());
                                progress.setVisibility(View.GONE);
                            });
                        }
                    }
                    return null;
                }
            }
            new load().execute();
        }
    }
}
