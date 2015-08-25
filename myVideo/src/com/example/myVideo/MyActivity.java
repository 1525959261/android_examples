package com.example.myVideo;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.Button;

import java.io.File;
import java.util.Calendar;

public class MyActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "MyActivity";
    private SurfaceView mSurfaceview;
    private Button mBtnStartStop;
    private boolean mStartedFlg = false;
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private Camera c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��

        // ���ú�����ʾ
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // ѡ��֧�ְ�͸��ģʽ,����surfaceview��activity��ʹ�á�
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.main);

        mSurfaceview  = (SurfaceView)findViewById(R.id.surfaceview);
        mBtnStartStop = (Button)findViewById(R.id.btnStartStop);
        mBtnStartStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mStartedFlg) {
                    // Start
//                    if (mRecorder == null) {
//                        mRecorder = new MediaRecorder(); // Create MediaRecorder
//                    }
                    try {

                        // ��������ͷ����ʱ����ת90�� 2015/8/25 9:56
//                        Camera c = Camera.open();
//                        c.setDisplayOrientation(90);
//                        c.unlock();
//                        mRecorder.setCamera(c);
//
//                        // Set audio and video source and encoder
//                        // ��������Ҫ����setOutputFormat֮ǰ
//                        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//                        mRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//
//                        //�������������
////                        CamcorderProfile cProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF); // 504k
//                        CamcorderProfile cProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
//                        mRecorder.setProfile(cProfile);

//                        mRecorder.setVideoFrameRate(30); // ����֡��
//                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface()); // ������ʾ����ͼ

                        // Set output file path
                        String path = getSDPath();
                        if (path != null) {

                            File dir = new File(path + "/recordtest");
                            if (!dir.exists()) {
                                dir.mkdir();
                            }

                            path = dir + "/" + getDate() + ".3gp";
                            mRecorder.setOutputFile(path);
                            Log.d(TAG, "bf mRecorder.prepare()");
                            mRecorder.prepare();
                            Log.d(TAG, "af mRecorder.prepare()");
                            Log.d(TAG, "bf mRecorder.start()");
                            mRecorder.start();   // Recording is now started
                            Log.d(TAG, "af mRecorder.start()");
                            mStartedFlg = true;
                            mBtnStartStop.setText("Stop");
                            Log.d(TAG, "Start recording ...");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // stop
                    if (mStartedFlg) {
                        try {
                            Log.d(TAG, "Stop recording ...");
                            Log.d(TAG, "bf mRecorder.stop(");
                            mRecorder.stop();
                            Log.d(TAG, "af mRecorder.stop(");
                            mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
                            mBtnStartStop.setText("Start");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mStartedFlg = false; // Set button status flag
                }
            }

        });

        SurfaceHolder holder = mSurfaceview.getHolder();// ȡ��holder

        // ���ø������������Ļ�Զ��ر�
        mSurfaceview.getHolder().setKeepScreenOn(true);

        holder.addCallback(this); // holder����ص��ӿ�

        // setType�������ã�Ҫ������.����Surface����Ҫά���Լ��Ļ�����
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (mRecorder == null) {
            mRecorder = new MediaRecorder(); // Create MediaRecorder
        }

        // ��������ͷ����ʱ����ת90�� 2015/8/25 9:56
        if (c == null)
            c = Camera.open();

        c.setDisplayOrientation(90);
        c.unlock();
        mRecorder.setCamera(c);

        // Set audio and video source and encoder
        // ��������Ҫ����setOutputFormat֮ǰ
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        //�������������
//                        CamcorderProfile cProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF); // 504k
        CamcorderProfile cProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mRecorder.setProfile(cProfile);

        mRecorder.setVideoFrameRate(30); // ����֡��
    }

    /**
     * ��ȡϵͳʱ��
     * @return
     */
    public static String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);			// ��ȡ���
        int month = ca.get(Calendar.MONTH);			// ��ȡ�·�
        int day = ca.get(Calendar.DATE);			// ��ȡ��
        int minute = ca.get(Calendar.MINUTE);		// ��
        int hour = ca.get(Calendar.HOUR);			// Сʱ
        int second = ca.get(Calendar.SECOND);		// ��

        String date = "" + year + (month + 1 )+ day + hour + minute + second;
        Log.d(TAG, "date:" + date);

        return date;
    }

    /**
     * ��ȡSD path
     * @return
     */
    public String getSDPath(){
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
        if (sdCardExist)
        {
            return Environment.getExternalStorageDirectory().getPath();// ��ȡ��Ŀ¼
        }

        return null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int previewWidth,
                               int previewHeight) {
        // TODO Auto-generated method stub
        // ��holder�����holderΪ��ʼ��onCreate����ȡ�õ�holder����������mSurfaceHolder

//        final int width = 720;
//        final int height = 480;
//        if (width * previewHeight > height * previewWidth) {
//            final int surfaceViewWidth = previewWidth * height / previewHeight;
//            mSurfaceview.layout((int)((width - surfaceViewWidth)*0.5), 0, (int)((width + surfaceViewWidth)*0.5), height);
//        } else {
//            final int surfaceViewHeight = previewHeight * width / previewWidth;
//            mSurfaceview.layout(0, (int)((height - surfaceViewHeight)*0.5), width, (int)((height + surfaceViewHeight)*0.5));
//        }

        mSurfaceHolder = holder;
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface()); // ������ʾ����ͼ
        Log.d(TAG, "surfaceChanged 1");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // ��holder�����holderΪ��ʼ��onCreate����ȡ�õ�holder����������mSurfaceHolder
        mSurfaceHolder = holder;
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface()); // ������ʾ����ͼ
        Log.d(TAG, "surfaceChanged 2");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // surfaceDestroyed��ʱ��ͬʱ��������Ϊnull
        mSurfaceview = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;
            Log.d(TAG, "surfaceDestroyed release mRecorder");
        }
    }
}
