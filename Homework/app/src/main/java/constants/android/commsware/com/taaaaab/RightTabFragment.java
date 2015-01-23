package constants.android.commsware.com.taaaaab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yong on 15. 1. 23.
 */
public class RightTabFragment extends Fragment {
    public View view;
    ImageView imgView;
    private static final String TEMP_PHOTO_FILE = "temp.jpg"; // 임시 저장 파일
    final int REQ_CODE_Select_IMAGE = 100;
    Bitmap mSaveBm = null;
    String addr = "http://www.bloter.net/wp-content/uploads/2014/10/Github_student_Developer_pack_01.jpg";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Log.d(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        // 작성.
    }

    public void onPause() {
        super.onPause();
    }


    // Fragment 초기화 할 요소를 넣는다. UI inflate & Service & Timer 등
    // onStart()에 넣을 수 있으면 onStart()가 좋음, onCreateView()에 넣으면, 다 그려지기 전까지는 화면에 보이지 않을 수 있음.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_right_tab, container, false);
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();

        // 이미지 URI ImgView 에 보여주기
        imgView = (ImageView) getView().findViewById(R.id.imgView);

        Button bt = (Button) getView().findViewById(R.id.changeBtn);
        Button btn = (Button) getView().findViewById(R.id.saveBtn);

        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        // 서버에서 이미지를 가져옴
        new ImageDownloader().execute(addr);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 로직 처리
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*"); // 모든 이미지
                intent.putExtra("crop", "true"); // Crop 기능 활성화
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri()); // 임시 파일 생성
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // 포멧 방식

                startActivityForResult(intent, REQ_CODE_Select_IMAGE); // REQ_CODE_Select_IMAGE == requestCode
            } // end of onClick
        }); // end of Listener


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "download.jpg";
                new ImageDownloader().execute(addr, fileName);
            }// end of onClick
        }); // end on Listener
    }

    // 임시 저장 파일 경로를 반환
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    } // end of getTempUri

    private File getTempFile() {
        if (isSDCARDMOUNTED()) {
            File f = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE); // 외장메모리 경로
            try {
                f.createNewFile();
            } catch (IOException e) {
            } // end of try ~ catch
            return f;
        } else
            return null;
    } // end of getTempFile

    private boolean isSDCARDMOUNTED() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;

        return false;
    }// end of isSDCARDMOUNTED

    // 다시 액티비티로 복귀하였을때 이미지를 세팅

    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);

        switch (requestCode) {
            case REQ_CODE_Select_IMAGE:
                if (resultCode != 0) {
                    if (imageData != null) {
                        String filePath = Environment.getExternalStorageDirectory() + "/temp.jpg";

                        System.out.println("path" + filePath); // logcat으로 경로 확인
                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                        // temp.jpg 파일을 Bitmap으로 디코딩한다.

                        // 앨범에서 가져와 imageView에 넣기
                        imgView.setImageURI(imageData.getData());
                        //imgView.setImageBitmap(selectedImage);
                        // temp.jpg파일을 이미지뷰에 씌운다.
                    } // end of if
                } // end of if
                break;
        } // end of switch
    }// end of onActivityResult

    // 서버에서 다운로드 한 데이터를 파일로 저장
    boolean DownloadImage(String Url, String FileName) {
        URL imageurl;
        int Read;
        try {
            imageurl = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection(); // 서버 접속 클라이언트 객체 생성
            int len = conn.getContentLength();
            byte[] raster = new byte[len];
            InputStream is = conn.getInputStream(); // 입력스트림 구하기
            FileOutputStream fos = getActivity().openFileOutput(FileName, 0); // 파일 저장 스트림 생성

            for (; ; ) {
                Read = is.read(raster);
                if (Read <= 0) {
                    break;
                } // end of if
                fos.write(raster, 0, Read);
            } // end of for
            is.close();
            fos.close(); // 파일을 닫음
            conn.disconnect(); // 접속 해제
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } // end of try ~ catch
        return true;
    }// end of DownloadImage

    public boolean loadWebImage(String strUrl) {
        try {
            //스트림 데이터를 Bitmap에 저장
            InputStream is = new URL(strUrl).openStream();
            mSaveBm = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            Log.d("tag", "Image Stream Error");
            return false;
        } // end of try ~ catch
        return true;
    } // end of loadWebImage

    // 서버에서 이미지 다운로드를 수행하는 쓰레드
    private class ImageDownloader extends AsyncTask<String, String, String> {

        @Override // 쓰레드 주업무를 수행하는 함수
        protected String doInBackground(String... urls) {
            boolean result = false;

            if (urls.length == 1) {
                // 서버에서 다운로드 한 데이터를 파일로 저장
                result = loadWebImage(urls[0]);
            } else {
                // 서버에서 다운로드 한 데이터를 파일로 저장
                result = DownloadImage(urls[0], urls[1]);
                if (result) {
                    //파일을 로딩해서 Bitmap 객체로 생성
                    String sdRootPath = Environment.getDataDirectory().getAbsolutePath();
                    String filePath = sdRootPath + "/data/imgDownload/files/" + urls[1];
                    mSaveBm = BitmapFactory.decodeFile(filePath);
                } // end of if
            } // end of if

            if (result)
                return "True";
            return "";
        }// end of doInBackground

        // 쓰레드 업무가 끝났을 때 결과를 처리 하는 함수
        protected void onPostExecute(String result) {
            // 서버에서 다운받은 Bitmap 이미지를 ImageView에 표시
            imgView.setImageBitmap(mSaveBm);
        }// end of onPostExecute

    }// end of ImageDownloader
}
