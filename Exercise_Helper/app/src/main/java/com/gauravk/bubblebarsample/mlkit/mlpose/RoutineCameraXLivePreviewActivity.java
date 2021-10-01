/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gauravk.bubblebarsample.mlkit.mlpose;

import static com.gauravk.bubblebarsample.cfg.MyGlobal.today_hangle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;

import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.MyGlobal;
import com.gauravk.bubblebarsample.mlkit.CameraXViewModel;
import com.gauravk.bubblebarsample.mlkit.GraphicOverlay;
import com.gauravk.bubblebarsample.mlkit.VisionImageProcessor;
import com.gauravk.bubblebarsample.mlkit.mlpose.posedetector.PoseDetectorProcessor;
import com.gauravk.bubblebarsample.mlkit.preference.PreferenceUtils;
import com.gauravk.bubblebarsample.mlkit.preference.SettingsActivity;
import com.google.android.gms.common.annotation.KeepName;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Live preview demo app for ML Kit APIs using CameraX. */
@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
public final class RoutineCameraXLivePreviewActivity extends AppCompatActivity
    implements OnRequestPermissionsResultCallback,
        CompoundButton.OnCheckedChangeListener {
  private static final String TAG = "CameraXLivePreview";
  private static final int PERMISSION_REQUESTS = 1;


  private static final String POSE_DETECTION = "Pose Detection";
  private static final String PUSH_UP = "PUSH UP";
  private static final String KNEE_RAISE = "KNEE RAISE";
  private static final String SQUATS = "SQUATS";
  private static final String SITUP = "SITUP";
  private static final String BARBELL_CURL = "BARBELL_CURL";
  private static final String DEAD = "DEAD";
  private static final String STATE_SELECTED_MODEL = "selected_model";

  private PreviewView previewView;
  private GraphicOverlay graphicOverlay;

  @Nullable private ProcessCameraProvider cameraProvider;
  @Nullable private Preview previewUseCase;
  @Nullable private ImageAnalysis analysisUseCase;
  @Nullable private VisionImageProcessor imageProcessor;
  private boolean needUpdateGraphicOverlayImageSourceInfo;

  private String selectedModel = POSE_DETECTION;
  private int lensFacing = CameraSelector.LENS_FACING_FRONT;
  private CameraSelector cameraSelector;

  //for routine
  private Date currentTime ;
  private SimpleDateFormat weekdayFormat;
  private String weekDay;
  private QueryClass databaseQueryClass;
  private List<Routine> routineList;
  private Routine temp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    //권한 얻는 함수
    if (!allPermissionsGranted()) {
      getRuntimePermissions();
    }
    if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
      Toast.makeText(
              getApplicationContext(),
              "CameraX is only supported on SDK version >=21. Current SDK version is "
                  + VERSION.SDK_INT,
              Toast.LENGTH_LONG)
          .show();
      return;
    }
    if (savedInstanceState != null) {
      selectedModel = savedInstanceState.getString(STATE_SELECTED_MODEL, POSE_DETECTION);
    }
    cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
    setContentView(R.layout.routine_camera);
    previewView = findViewById(R.id.preview_view);
    if (previewView == null) {
      Log.d(TAG, "previewView is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    //Setting Myglobal routinList
    MyGlobal.getInstance().setMode(true);

    databaseQueryClass = new QueryClass(this);
    routineList = new ArrayList<>();
    currentTime = Calendar.getInstance().getTime();
    routineList.addAll(databaseQueryClass.getDaysRoutine(today_hangle()));
    MyGlobal.getInstance().initRoutine(routineList);
    if(routineList.isEmpty()){

      //this.finish();
    }
    temp = routineList.get(0);
    Logger.d(temp);
    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);
    new ViewModelProvider(this, AndroidViewModelFactory.getInstance(getApplication()))
        .get(CameraXViewModel.class)
        .getProcessCameraProvider()
        .observe(
            this,
            provider -> {
              cameraProvider = provider;
              if (allPermissionsGranted()) {
                bindAllCameraUseCases();
              }
            });

    ImageView settingsButton = findViewById(R.id.settings_button_routine);
    settingsButton.setOnClickListener(
        v -> {
          Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
          intent.putExtra(
              SettingsActivity.EXTRA_LAUNCH_SOURCE,
              SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW);
          startActivity(intent);
        });

    if (!allPermissionsGranted()) {
      getRuntimePermissions();
    }
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle bundle) {
    super.onSaveInstanceState(bundle);
    bundle.putString(STATE_SELECTED_MODEL, selectedModel);
  }
  //해당 부분에서 camera mode select가 된다. 동기화가 되어있구만



  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (cameraProvider == null) {
      return;
    }
    int newLensFacing =
        lensFacing == CameraSelector.LENS_FACING_FRONT
            ? CameraSelector.LENS_FACING_BACK
            : CameraSelector.LENS_FACING_FRONT;
    CameraSelector newCameraSelector =
        new CameraSelector.Builder().requireLensFacing(newLensFacing).build();
    try {
      if (cameraProvider.hasCamera(newCameraSelector)) {
        Log.d(TAG, "Set facing to " + newLensFacing);
        lensFacing = newLensFacing;
        cameraSelector = newCameraSelector;
        bindAllCameraUseCases();
        return;
      }
    } catch (CameraInfoUnavailableException e) {
      // Falls through
    }
    Toast.makeText(
            getApplicationContext(),
            "This device does not have lens with facing: " + newLensFacing,
            Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onResume() {
    super.onResume();
    bindAllCameraUseCases();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (imageProcessor != null) {
      imageProcessor.stop();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (imageProcessor != null) {
      imageProcessor.stop();
    }
  }

  private void bindAllCameraUseCases() {
    if (cameraProvider != null) {
      // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
      cameraProvider.unbindAll();
      bindPreviewUseCase();
      bindAnalysisUseCase();
    }
  }

  private void bindPreviewUseCase() {
    if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
      return;
    }
    if (cameraProvider == null) {
      return;
    }
    if (previewUseCase != null) {
      cameraProvider.unbind(previewUseCase);
    }

    Preview.Builder builder = new Preview.Builder();
    Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing);
    if (targetResolution != null) {
      builder.setTargetResolution(targetResolution);
    }
    previewUseCase = builder.build();
    previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());
    cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, previewUseCase);
  }


  //private에서 바꿈
  private void bindAnalysisUseCase() {
    System.out.println("bindAnalysisUseCase()");
    if (cameraProvider == null) {
      return;
    }
    if (analysisUseCase != null) {
      cameraProvider.unbind(analysisUseCase);
    }
    if (imageProcessor != null) {
      imageProcessor.stop();
    }

    selectedModel = MyGlobal.getInstance().getExercise();
    if(selectedModel.compareTo("스쿼트")==0){selectedModel = SQUATS;}
    else if (selectedModel.compareTo("무릎올리기")==0){selectedModel = KNEE_RAISE;}
    else if (selectedModel.compareTo("팔굽혀펴기")==0){selectedModel = PUSH_UP;}
    else if (selectedModel.compareTo("윗몸일으키기")==0){selectedModel = SITUP;}
    else if (selectedModel.compareTo("턱걸이")==0){selectedModel = BARBELL_CURL;}
    else if (selectedModel.compareTo("크런치")==0){selectedModel = DEAD;}


    //이부분이 실질적으로 detect하는부분임
    //옵션, Z축 저건 다 환경설정의 옵션
    try {
      switch (selectedModel) {
        case PUSH_UP:
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/push_up.csv");
          break;
        case SQUATS:
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/squat.csv");
          break;
        case KNEE_RAISE:
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/kneel_up.csv");
          break;
        case SITUP:
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/situp.csv");
          break;
        case BARBELL_CURL:
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/barbell_curl.csv");
          break;
        case DEAD:
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/dead.csv");
          break;
      }
      System.out.println("selected:" + selectedModel);
      PoseDetectorOptionsBase poseDetectorOptions =
              PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
      //이미지 프로세서 설정
      imageProcessor =
              new PoseDetectorProcessor(
                      this,
                      poseDetectorOptions,
                      true,
                      true,
                      true,
                      true,
                      true);
    } catch (Exception e) {
      Log.e(TAG, "Can not create image processor: " + selectedModel, e);
      Toast.makeText(
              getApplicationContext(),
              "Can not create image processor: " + e.getLocalizedMessage(),
              Toast.LENGTH_LONG)
              .show();
      return;
    }
    ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
    Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing);
    if (targetResolution != null) {
      builder.setTargetResolution(targetResolution);
    }
    analysisUseCase = builder.build();
    needUpdateGraphicOverlayImageSourceInfo = true;
    //이놈이 계속 실행이 된다
    analysisUseCase.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            ContextCompat.getMainExecutor(this),
            imageProxy -> {
              if (needUpdateGraphicOverlayImageSourceInfo) {
                boolean isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT;
                int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                  graphicOverlay.setImageSourceInfo(
                          imageProxy.getWidth(), imageProxy.getHeight(), isImageFlipped);
                } else {
                  graphicOverlay.setImageSourceInfo(
                          imageProxy.getHeight(), imageProxy.getWidth(), isImageFlipped);
                }
                needUpdateGraphicOverlayImageSourceInfo = false;
              }
              //이부분이 카메라 실행시키는 부분
              //반복되는것도 이부분이넹
              try {
                //processImageProxy() -> requestDetectImage() -> processImageProxy() -> detectInImage() -> getPoseResult() -> 문자열 생성
                imageProcessor.processImageProxy(imageProxy, graphicOverlay);

                //세트,개수 확인;
                if(MyGlobal.getInstance().getNum() == MyGlobal.getInstance().getNow_num()){
                  MyGlobal.getInstance().do1set();
                  temp.setCounts(MyGlobal.getInstance().getnow_set());
                  databaseQueryClass.updateRoutineInfo(temp);

                  if(MyGlobal.getInstance().getnow_set()== MyGlobal.getInstance().getSET()){
                    //운동이 아예 끝난 상황이면
                    temp = MyGlobal.getInstance().getNow_routine();
                    temp.Complete();
                    databaseQueryClass.updateRoutineInfo(temp); //끝났다고 표시를 해주고

                    if(!MyGlobal.getInstance().Done()){   //여기서 세트수를 올려주고 다음 세트를 셋팅합니다
                      MyGlobal.getInstance().setFinish(true);
                      Handler mHandler = new Handler();
                      mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          MyGlobal.getInstance().setFinish(false);
                          finish();
                        }
                      },10000);
                      //finish();     //끝냄
                    }
                    else{       //현재운동은 끝났으나 다음운동이 있으면
                      Handler mHandler = new Handler();
                      mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          MyGlobal.getInstance().setRest_time(false);
                          bindAnalysisUseCase();
                        }
                      },6000);          //요만큼 쉬어준 다음에 실행을 함
                      MyGlobal.getInstance().setRest_time(true);  //휴식시간인것을 세팅해줌
                    }
                  }
                  else{     //현재운동이 끝났지만 다음운동이 있음
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                        MyGlobal.getInstance().setRest_time(false);
                        bindAnalysisUseCase();
                      }
                    },(int)(MyGlobal.getInstance().getREST())*1000);
                    MyGlobal.getInstance().setRest_time(true);
                  }
                }
              } catch (MlKitException e) {
                Log.e(TAG, "Failed to process image. Error: " + e.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
              }
            });
    //이부분 제거하면 detector 사라지네
    //analysisusecase에서 imageprocessor하고 graphic씌우고 여기서 그걸 업로드 해주나 봄
    //cameraselector가 뭐지
    cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, analysisUseCase);
    //imageProcessor.stop();
    //finish();
  }






  //아래에는 다 권한 관련 함수들
  private String[] getRequiredPermissions() {
    try {
      PackageInfo info =
          this.getPackageManager()
              .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
      String[] ps = info.requestedPermissions;
      if (ps != null && ps.length > 0) {
        return ps;
      } else {
        return new String[0];
      }
    } catch (Exception e) {
      return new String[0];
    }
  }

  private boolean allPermissionsGranted() {
    for (String permission : getRequiredPermissions()) {
      if (!isPermissionGranted(this, permission)) {
        return false;
      }
    }
    return true;
  }

  private void getRuntimePermissions() {
    List<String> allNeededPermissions = new ArrayList<>();
    for (String permission : getRequiredPermissions()) {
      if (!isPermissionGranted(this, permission)) {
        allNeededPermissions.add(permission);
      }
    }

    if (!allNeededPermissions.isEmpty()) {
      ActivityCompat.requestPermissions(
          this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    Log.i(TAG, "Permission granted!");
    if (allPermissionsGranted()) {
      bindAllCameraUseCases();
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private static boolean isPermissionGranted(Context context, String permission) {
    if (ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED) {
      Log.i(TAG, "Permission granted: " + permission);
      return true;
    }
    Log.i(TAG, "Permission NOT granted: " + permission);
    return false;
  }
}
