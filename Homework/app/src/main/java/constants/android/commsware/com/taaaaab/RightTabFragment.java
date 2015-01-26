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
import android.provider.MediaStore;
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
    private static final String TEMP_PHOTO_FILE = "temp.jpg";       // 임시 저장파일 명

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_right_tab, container, false);
        // new httpreqtask().execute(addr);
        GetXMLTask task = new GetXMLTask();
        task.execute(new String[]{addr});
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Log.d(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        imgView = (ImageView) getView().findViewById(R.id.imgView);
    }

    // Fragment 초기화 할 요소를 넣는다. UI inflate & Service & Timer 등
    // onStart()에 넣을 수 있으면 onStart()가 좋음, onCreateView()에 넣으면, 다 그려지기 전까지는 화면에 보이지 않을 수 있음.
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 작성.

        Button svBtn = (Button) getView().findViewById(R.id.saveBtn);
        Button chBtn = (Button) getView().findViewById(R.id.changeBtn);

        svBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                //String fileName = "download.jpg";
                String addr = "http://postfiles6.naver.net/20141012_101/kcskiller_1413084768076txqYS_PNG/STALK_IO_%C3%A4%C6%C3_%BB%E7%C1%F8.png?type=w2";
                int idx = addr.lastIndexOf("/");
                String localimage = addr.substring(idx + 1);
                String path = Environment.getDataDirectory().getAbsolutePath();
                path += "/download/" + localimage;

                if (new File(path).exists() == false) {

                } else {
                    //new DownThread(addr, localimage).start();
                }*/
                downloadFile(addr);
                //new HttpReqTask().execute(addr, fileName);
            } // end of onClick
        }); // end of Listener

        chBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_GET_CONTENT,      // 또는 ACTION_PICK
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");              // 모든 이미지
                //intent.putExtra("crop", "true");        // Crop기능 활성화
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());     // 임시파일 생성
                intent.putExtra("outputFormat",         // 포맷방식
                        Bitmap.CompressFormat.JPEG.toString());

                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                // REQ_CODE_PICK_IMAGE == requestCode
            }// end of onClick
        }); // end on Listener
    }

    public void onPause() {
        super.onPause();
    }

    /**
     * 임시 저장 파일의 경로를 반환
     */
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    /**
     * 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환
     */
    private File getTempFile() {
        if (isSDCARDMOUNTED()) {
            File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리 경로
                    TEMP_PHOTO_FILE);
            try {
                f.createNewFile();      // 외장메모리에 temp.jpg 파일 생성
            } catch (IOException e) {
            }// end of try ~ catch

            return f;
        } else
            return null;
    } // end of getTempFile

    /**
     * SD카드가 마운트 되어 있는지 확인
     */
    private boolean isSDCARDMOUNTED() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    } // end of isSDCARDMOUNTED


    // 갤러리에서 이미지 가져와 imageView에 저장.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getBaseContext(), "resultCode : ", Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case REQ_CODE_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        //String filePath = Environment.getExternalStorageDirectory()
                        //        + "/temp.jpg";

                        //System.out.println("path" + filePath); // logCat으로 경로확인.

                        //Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                        // temp.jpg파일을 Bitmap으로 디코딩한다.

                        //ImageView _image = (ImageView) getView().findViewById(R.id.imgView);
                        //imgView.setImageBitmap(selectedImage);
                        imgView.setImageURI(data.getData());
                        // temp.jpg파일을 이미지뷰에 씌운다.
                    } // end of if
                } // end of if
                break;
        } // end of onActivityResult
    } //  end of onActivityResult

    // Uri 이미지 가져와 imageView에 저장.
    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }// end of for
            return map;
        } // end of doInBackground

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imgView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } // end of try ~ catch
            return bitmap;
        } // end of downloadImage

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoOutput(true);
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
/*
// 서버에서 이미지 다운로드를 수행하는 쓰레드
    private class HttpReqTask extends AsyncTask<String, String, String> {
        @Override // 쓰레드 주업무를 수행하는 함수
        protected String doInBackground(String... arg) {
            boolean result = false;
            if (arg.length == 1)
                // 서버에서 전달 받은 데이터를 Bitmap 이미지에 저장
                result = loadWebImage(arg[0]);
            else {
                // 서버에서 다운로드 한 데이터를 파일로 저장
                result = downloadFile(arg[0], arg[1]);
                if (result) {
                    // 파일을 로딩해서 Bitmap 객체로 생성
                    String sdRootPath = Environment.getDataDirectory().getAbsolutePath();
                    String filePath = sdRootPath + "/download/" + arg[1];
                    bitmap = BitmapFactory.decodeFile(filePath);
                }
            }

            if (result)
                return "True";
            return "";
        }

        // 쓰레드의 업무가 끝났을 때 결과를 처리하는 함수
        protected void onPostExecute(String result) {
            if (result.length() > 0)
                // 서버에서 다운받은 Bitmap 이미지를 ImageView 에 표시
                imgView.setImageBitmap(bitmap);
        }
    }

    // 서버에서 다운로드 한 데이터를 파일로 저장
    boolean downloadFile(String strUrl, String fileName) {
        try {
            URL url = new URL(strUrl);
            // 서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 입력 스트림을 구한다
            InputStream is = conn.getInputStream();
            // 파일 저장 스트림을 생성
            FileOutputStream fos = getActivity().openFileOutput(fileName, 0);

            // 입력 스트림을 파일로 저장
            byte[] buf = new byte[1024];
            int count;
            while ((count = is.read(buf)) > 0) {
                fos.write(buf, 0, count);
            }
            // 접속 해제
            conn.disconnect();
            // 파일을 닫는다
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag", "Image download error.");
            return false;
        }
        return true;
    }


    // 서버에서 전달 받은 데이터를 Bitmap 이미지에 저장
    public boolean loadWebImage(String strUrl) {
        try {
            // 스트림 데이터를 Bitmap 에 저장
            InputStream is = new URL(strUrl).openStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            Log.d("tag", "Image Stream error.");
            return false;
        }
        return true;
    }

    class DownThread extends Thread {
        String mAddr; // 다운받을 주소
        String mFile; // 저장할 경로를 저장

        DownThread(String addr, String filename) {
            mAddr = addr;
            mFile = filename;
        }

        public void run() {
            URL imageurl;
            int Read;
            try {
                imageurl = new URL(mAddr);
                HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                int len = conn.getContentLength();
                byte[] raster = new byte[len];
                InputStream is = conn.getInputStream();
                FileOutputStream fos = getActivity().openFileOutput(mFile, 0);

                while (true) {
                    Read = is.read(raster);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(raster, 0, Read);
                }

                is.close();
                fos.close();
                conn.disconnect();
            } catch (Exception e) {
                mFile = null;
            }
            Message message = mAfterDown.obtainMessage();
            message.obj = mFile;
            mAfterDown.sendMessage(message);
        }
    }

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                String path = Environment.getDataDirectory().getAbsolutePath();
                path += "/download/" + (String) msg.obj;
                //mImage.setImageBitmap(BitmapFactory.decodeFile(path));
            } else {
                //Toast.makeText(DownImage2.this, "File not found", 0).show();
            }
        }
    };*/
}
