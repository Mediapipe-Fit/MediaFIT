# Exercise Helper

## 📢 소개글 / Introduction 
```
운동 파트너가 되어 주는 APP 입니다.
운동을 하는 모습을 휴대폰 카메라에 담으면 사용자의 모습을 분석하여, 
횟수를 세어주고 자세를 교정하라는 알림을 보냅니다. 
또한, 자신의 루틴을 설정해 요일별로 계획을 세워 운동을 실시할 수 있습니다. 
추가적으로 Wearable Device인 Galexy Watch가 있다면 휴대폰을 들여다 보지 않고,
Watch의 화면으로 몇 개를 했는지, 세트간 휴식시간이 얼마나 남았는지를 알려줍니다. 
```
## 요구 사항 / Requirements
### 📱 어플리케이션 / For Application
```
[접근 권한 안내]
카메라 : 카메라 API 를 통해 카메라를 제어하고 사진 촬영을 합니다.
```

## 사용법 / How to Use
```
1. HOME 화면 

"운동 시작" 버튼 클릭 전, Routine 설정 화면에서 오늘의 요일에 해당하는 루틴을 만들어 주세요.
완료 후, "운동 시작" 버튼을 누르면 설정한 루틴이 실행됩니다. 
```
<p align="center">
  |제목 셀1|제목 셀2|
  |:---:|:---:|
  |<img src = "https://user-images.githubusercontent.com/38587274/136323645-d62be43f-f3e1-412e-865d-a8f4c8cce102.jpg" alt="루틴설정 후" title="루틴설정 후"|내용 2|
</p>

<p align="center">
  <img src = "https://user-images.githubusercontent.com/38587274/136323655-46ed2fd8-3856-48b4-a2bf-f1616ef6a2f7.jpg" alt="루틴설정 전" title="루틴설정 전" style="centerme"><img src = "https://user-images.githubusercontent.com/38587274/136323645-d62be43f-f3e1-412e-865d-a8f4c8cce102.jpg" alt="루틴설정 후" title="루틴설정 후" style="centerme">
</p>

```
2. Calendar (기능 구현 X)
자신의 일별 운동 완료여부를 표시합니다. 
```

<p align="center" style="margin:20px">
  <img src = "https://user-images.githubusercontent.com/38587274/136324126-5fd60d8a-0d8e-4230-bfd2-51b70d324c7a.jpg" alt="캘린더">
</p>

```
3. Routine 설정
요일별로 자신의 루틴을 설정할 수 있습니다. 추가, 수정, 삭제가 가능하며, 
오늘의 루틴을 수정시 Home화면에 업데이트 됩니다. 
```

<p align="center">
  <img src = "https://user-images.githubusercontent.com/38587274/136322823-bbd3e16b-baf2-4df6-a39a-8a6ca304f6ea.jpg" alt="루틴설정 전"><img src = "https://user-images.githubusercontent.com/38587274/136322831-bef19256-bb1f-46ab-9530-5350bb212370.jpg" alt="루틴설정 중"><img src = "https://user-images.githubusercontent.com/38587274/136322849-60e6cffd-bdc4-47a1-b9db-1edd094cc7e4.jpg" alt="루틴설정 후">
</p>

```
4. 운동 목록
현재 App을 통해 사용가능한 운동을 체험할 수 있습니다.
체험하고 싶은 운동의 이미지를 클릭시 카메라 화면으로 넘어갑니다.
해당 기능 사용 후 Routine설정에서 자신만의 루틴을 설정하는 것을 권장합니다.
```
<p align="center">
  <img src = "https://user-images.githubusercontent.com/38587274/136323620-0c8a03f7-b371-44c5-810d-b71cdf93e2ec.jpg" alt="운동목록">
</p>

```
5. 가이드 
앱의 사용법과 apk 파일을 이용해 watch에 설치하는 방법이 나와있습니다.
```
<p align="center">
  <img src = "https://user-images.githubusercontent.com/38587274/136323613-ebf1c62e-d1ba-4a73-94d0-2f4a17bb1319.jpg" alt="가이드">
</p>

img[src$="centerme"] {
  display:block;
  margin: 30;
}
