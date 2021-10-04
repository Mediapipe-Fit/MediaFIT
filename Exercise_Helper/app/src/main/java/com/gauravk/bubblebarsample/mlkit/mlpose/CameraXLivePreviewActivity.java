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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
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
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.MyGlobal;
import com.gauravk.bubblebarsample.mlkit.CameraXViewModel;
import com.gauravk.bubblebarsample.mlkit.GraphicOverlay;
import com.gauravk.bubblebarsample.mlkit.VisionImageProcessor;
import com.gauravk.bubblebarsample.mlkit.mlpose.posedetector.PoseDetectorProcessor;
import com.gauravk.bubblebarsample.mlkit.preference.PreferenceUtils;
import com.gauravk.bubblebarsample.mlkit.preference.SettingsActivity;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Live preview demo app for ML Kit APIs using CameraX. */
@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
public final class CameraXLivePreviewActivity extends AppCompatActivity
    implements OnRequestPermissionsResultCallback,
        OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener,
        DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener,
        CapabilityClient.OnCapabilityChangedListener {
  private static final String TAG = "CameraXLivePreview";
  private static final int PERMISSION_REQUESTS = 1;


  private static final String POSE_DETECTION = "Pose Detection";
  private static final String PUSH_UP = "팔굽혀펴기";
  private static final String KNEE_RAISE = "무릎올리기";
  private static final String SQUATS = "스쿼트";
  private static final String SITUP = "윗몸일으키기";
  private static final String BARBELL_CURL = "바벨컬";
  private static final String DEAD = "데드리프트";
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

  // Wear os
  private int rep = 0;
  private long rest_time = 0;
  public static final String START_ACTIVITY_PATH = "/start-activity";
  public static final String COUNT_PATH = "/count";
  public static final String Rest_PATH = "/rest";
  public static final String info_PATH = "/info";
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

    setContentView(R.layout.activity_vision_camerax_live_preview);


    previewView = findViewById(R.id.preview_view);
    if (previewView == null) {
      Log.d(TAG, "previewView is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    Spinner spinner = findViewById(R.id.spinner);
    List<String> options = new ArrayList<>();
    options.add(PUSH_UP);
    options.add(SQUATS);
    options.add(KNEE_RAISE);
    options.add(SITUP);
    options.add(BARBELL_CURL);
    options.add(DEAD);
    int select=0;
    String exer = MyGlobal.getInstance().getExercise();
    if(exer.compareTo(PUSH_UP)==0){
      select = 0;
    }
    else if(exer.compareTo(SQUATS)==0){
      select = 1;
    }
    else if(exer.compareTo(KNEE_RAISE)==0){
      select = 2;
    }
    else if(exer.compareTo(SITUP)==0){
      select = 3;
    }
    else if(exer.compareTo(BARBELL_CURL)==0){
      select = 4;
    }
    else if(exer.compareTo(DEAD)==0){
      select = 5;
    }


    // Creating adapter for spinner
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_style, options);
    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // attaching data adapter to spinner
    spinner.setAdapter(dataAdapter);
    spinner.setOnItemSelectedListener(this);
    spinner.setSelection(select);

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

  @Override
  public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent.getItemAtPosition(pos).toString();
    MyGlobal.getInstance().setNow_num();
    bindAnalysisUseCase();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }

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
    sendData_info(selectedModel);
    rep = 0;
    if (cameraProvider == null) {
      return;
    }
    if (analysisUseCase != null) {
      cameraProvider.unbind(analysisUseCase);
    }
    if (imageProcessor != null) {
      imageProcessor.stop();
    }
    //이부분이 실질적으로 detect하는부분임
    //옵션, Z축 저건 다 환경설정의 옵션
    try {
      switch (selectedModel) {
        case PUSH_UP:
          MyGlobal.getInstance().setExercise(PUSH_UP);
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/push_up.csv");
          break;
        case SQUATS:
          MyGlobal.getInstance().setExercise(SQUATS);
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/squat.csv");
          break;
        case KNEE_RAISE:
          MyGlobal.getInstance().setExercise(KNEE_RAISE);
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/kneel_up.csv");
          break;
        case SITUP:
          MyGlobal.getInstance().setExercise(SITUP);
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/situp.csv");
          break;
        case BARBELL_CURL:
          MyGlobal.getInstance().setExercise(BARBELL_CURL);
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/barbell_curl.csv");
          break;
        case DEAD:
          MyGlobal.getInstance().setExercise(DEAD);
          MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/dead.csv");
          break;
      }
      PoseDetectorOptionsBase poseDetectorOptions =
              PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
      boolean shouldShowInFrameLikelihood =
              PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
      boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
      boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
      //boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
      boolean runClassification = true;
      //이미지 프로세서 설정
      imageProcessor =
              new PoseDetectorProcessor(
                      this,
                      poseDetectorOptions,
                      shouldShowInFrameLikelihood,
                      visualizeZ,
                      rescaleZ,
                      runClassification,
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

            if(rep < MyGlobal.getInstance().getNow_num()){
              rep = (int)MyGlobal.getInstance().getNow_num();
              sendData_counts(rep);
            };
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
  @Override
  public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {

    LOGD(TAG, "onCapabilityChanged: " + capabilityInfo);
  }

  @Override
  public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

    LOGD(TAG, "onDataChanged: " + dataEventBuffer);
  }

  @Override
  public void onMessageReceived(@NonNull MessageEvent messageEvent) {
    LOGD(
            TAG,
            "onMessageReceived() A message from watch was received:"
                    + messageEvent.getRequestId()
                    + " "
                    + messageEvent.getPath());
  }

  /** As simple wrapper around Log.d */
  private static void LOGD(final String tag, String message) {
    if (Log.isLoggable(tag, Log.DEBUG)) {
      Log.d(tag, message);
    }
  }

  private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... args) {
      Collection<String> nodes = getNodes();
      for (String node : nodes) {
        sendStartActivityMessage(node);
      }
      return null;
    }
  }



  @WorkerThread
  private void sendStartActivityMessage(String node) {

    Task<Integer> sendMessageTask =
            Wearable.getMessageClient(this).sendMessage(node, START_ACTIVITY_PATH, new byte[0]);

    try {
      // Block on a task and get the result synchronously (because this is on a background
      // thread).
      Integer result = Tasks.await(sendMessageTask);
      LOGD(TAG, "Message sent: " + result);

    } catch (ExecutionException exception) {
      Log.e(TAG, "Task failed: " + exception);

    } catch (InterruptedException exception) {
      Log.e(TAG, "Interrupt occurred: " + exception);
    }
  }

  @WorkerThread
  private Collection<String> getNodes() {
    HashSet<String> results = new HashSet<>();

    Task<List<Node>> nodeListTask =
            Wearable.getNodeClient(this).getConnectedNodes();

    try {
      // Block on a task and get the result synchronously (because this is on a background
      // thread).
      List<Node> nodes = Tasks.await(nodeListTask);

      for (Node node : nodes) {
        results.add(node.getId());
      }

    } catch (ExecutionException exception) {
      Log.e(TAG, "Task failed: " + exception);

    } catch (InterruptedException exception) {
      Log.e(TAG, "Interrupt occurred: " + exception);
    }

    return results;
  }


  public void sendData_counts(int count) {
    Log.i("Send_info","send Counts");
    PutDataMapRequest dataMap = PutDataMapRequest.create(COUNT_PATH);
    dataMap.getDataMap().putString("path", COUNT_PATH);
    dataMap.getDataMap().putString("name",MyGlobal.getInstance().getExercise());
    dataMap.getDataMap().putString("counts", Integer.toString(count));
    PutDataRequest request = dataMap.asPutDataRequest();
    request.setUrgent();
    Wearable.getDataClient(this).putDataItem(request);

  }

  public void sendData_info(String exeName) {
    Log.i("Send_info",exeName);
    PutDataMapRequest dataMap = PutDataMapRequest.create(COUNT_PATH);
    dataMap.getDataMap().putString("path", info_PATH);
    dataMap.getDataMap().putString("1", exeName);
    dataMap.getDataMap().putString("2", "X");
    dataMap.getDataMap().putString("3", "X");
    dataMap.getDataMap().putString("4", "X");
    PutDataRequest request = dataMap.asPutDataRequest();
    request.setUrgent();

    Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
    dataItemTask
            .addOnSuccessListener(new OnSuccessListener<DataItem>() {
              @Override
              public void onSuccess(DataItem dataItem) {
                Log.d(TAG, "Sending message was successful: " + dataItem);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Sending message failed: " + e);
              }
            })
    ;

  }
  public void sendData_Rest(long Rest_time) {
    Log.i("Send_info","send Rest_time");
    PutDataMapRequest dataMap = PutDataMapRequest.create(COUNT_PATH);
    dataMap.getDataMap().putString("path", Rest_PATH);
    dataMap.getDataMap().putString("Rest_time", Integer.toString((int)Rest_time));
    PutDataRequest request = dataMap.asPutDataRequest();
    request.setUrgent();


    Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
    dataItemTask
            .addOnSuccessListener(new OnSuccessListener<DataItem>() {
              @Override
              public void onSuccess(DataItem dataItem) {
                Log.d(TAG, "Sending message was successful: " + dataItem);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Sending message failed: " + e);
              }
            })
    ;
  }
}
