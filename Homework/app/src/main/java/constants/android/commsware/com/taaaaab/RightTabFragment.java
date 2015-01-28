package constants.android.commsware.com.taaaaab;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by yong on 15. 1. 23.
 */
public class RightTabFragment extends Fragment {
    public View view;
    ImageView imgView;
    String addr = "http://cfile25.uf.tistory.com/original/19481F414D5F83DC2AEAC5";
    final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_right_tab, container, false);
        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[]{addr});
        Button svBtn = (Button) view.findViewById(R.id.saveBtn);
        Button chBtn = (Button) view.findViewById(R.id.changeBtn);
        svBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(addr);
            } // end of onClick
        }); // end of Listener

        chBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_GET_CONTENT,      // OR ACTION_PICK
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");              // All image
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }// end of onClick
        }); // end on Listener
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgView = (ImageView) getView().findViewById(R.id.imgView);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() { super.onResume(); }

    public void onPause() {
        super.onPause();
    }

    // before & after executed onStart() , must executed before onResume()
    // save to imageView from image in Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        imgView.setImageURI(data.getData());
                    } // end of if
                } // end of if
                break;
        } // end of onActivityResult
    } //  end of onActivityResult

    // Save to imageView from Uri
    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }// end of for
            return map;
        } // end of doInBackground

        // Sets the Bitmap returned by doInBackground, Update UI
        @Override
        protected void onPostExecute(Bitmap result) {
            imgView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options(); // decode option
            bmOptions.inSampleSize = 1; // size

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions); // InputStream , Bitmap
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } // end of try ~ catch
            return bitmap;
        } // end of downloadImage

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null; //
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoOutput(true); // URLConnection available
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                } // end of if
            } catch (Exception ex) {
                ex.printStackTrace();
            } // end of try ~ catch
            return stream;
        } // end of getHttpConnection
    } // end of GetXMLTask

    // used to File manager for Uri Image Download
    public void downloadFile(String url) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/download");
        if (!direct.exists()) {
            direct.mkdir();
        } // end of if
        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Sample")
                .setDescription("Something useful. No, Really")
                .setDestinationInExternalPublicDir("/download", "fileName.jpg");
        mgr.enqueue(request);
    } // end of downloadFile

}
