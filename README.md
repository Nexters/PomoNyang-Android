# 모하냥

<img src="https://github.com/user-attachments/assets/923c0812-ff6e-4887-80a9-750bb008c621" width="120" height="120" align="left"/> 

> 고양이와 함께하는 집중 시간, 모하냥!

[![download](https://img.shields.io/badge/playstore-download-brightgreen?style=social&logo=googleplay&logoColor=FFB019)](https://play.google.com/store/apps/details?id=com.pomonyang.mohanyang)

<br>

## ⏰ 고양이와 함께하는 집중 시간, 모하냥!

모하냥은 귀여운 고양이 친구들과 함께하는 뽀모도로 타이머 앱이에요.

- 까칠한 삼색냥, 긍정적인 까만냥, 응원하는 치즈냥 중 나에게 가장 맞는 친구를 골라보세요.
- 집중 시간을 다 채우면 고양이와 하이파이브! 함께하는 즐거운 순간이 기다리고 있어요.
- 집중 모드에서 다른 앱을 실행하면 30초마다 방해 알림을 보내 더 효율적인 집중이 가능해요.

귀여운 고양이들이 당신의 집중력을 한층 더 높여줄 거예요!

<br>

![mohanyang-full](https://github.com/user-attachments/assets/84037ddf-e40f-438c-ab03-883326b53d2e)

<br>

## 💌 궁금한 점이 있으신가요?

[카카오톡 채널 문의하기](https://pf.kakao.com/_FvuAn)
<br>
[의견 남기기](https://docs.google.com/forms/d/e/1FAIpQLSdoFxWJ7TFTU0-HKZEeqmDxz5ZprYtRz08FwrzNgDWnkNaOeA/viewform?usp=send_form)

<br>

## 👷‍♂️ Contributors

|                    [이지훈](https://github.com/lee-ji-hoon)                     |                       [김효민](https://github.com/HyomK)                        
|:----------------------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| <img width="150" src="https://avatars.githubusercontent.com/u/53300830?v=4"> | <img width="150" src="https://avatars.githubusercontent.com/u/78139690?v=4"> |

<br>

## ⚒️ Tech Stack

| Tech Stack   |                                            |
|:-------------|:-------------------------------------------|
| Architecture | MVI,  Multi-Module                         |
| Compose      | Navigation, Animation etc                  |
| DI           | Hilt                                       |
| Network      | Retrofit2, OkHttp3                         |
| Asynchronous | Coroutine, Flow , Channel                  |
| Jetpack      | Room, DataStore, ViewModel                 |
| CI           | Github Actions                             |
| CD           | Github Actions, Firebase App Distritubtion |
| ETC          | Rive, Lottie, Firebase Cloud Messaging     |

<br>

## 💻System Architecture

### Dependencies Graph

<img width="400" src="https://github.com/user-attachments/assets/625a202a-3406-4c52-8c76-84bf99a93638">

### Module

```
🗃️app
 ┣ 📂di
 ┣ 📂navigation
 ┣ 📂notification
 ┣ 📂util
🗃️build-logic
 ┣ 📂convention
🗃️data
 ┣ 📂local
 ┣ 📂remote
 ┣ 📂repository
 ┣ 📂util
🗃️domain
 ┣ 📂di
 ┣ 📂usecase
 ┣ 📂model
🗃️presentation
 ┣ 📂base
 ┣ 📂component
 ┣ 📂designsystem
 ┣ 📂model
 ┣ 📂screen
 ┣ 📂theme
 ┣ 📂util
```
