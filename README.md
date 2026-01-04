# 프로젝트 소개
도서를 검색하고 대출, 반납, 예약할 수 있는 도서관 앱입니다. 회원 가입 및 로그인 기능으로 좋아요 목록과 대출 내역 확인 가능합니다.
API 호출 최소화를 위해 room을 이용한 데이터 캐시를 사용하였습니다. 기본 도서 정보는 Google Books API를 사용합니다.
# 개발기간
2025.04.02~2025.12.30(9개월)
# 개발환경
- Java 1.8
- Android Studio Ladybug
- Figma
- GitHub
# 기술 스택
- MVVM
- Coil
- Coroutine
- Navigation
- Proto Datastore, Preferences Datastore
- Room
- WorkManager
- Retrofit
- Firebase
- Hilt
- UI Test, Unit Test
# 분석 및 설계
- <p>Usecase Diagram
![Usecase Diagram](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_Usecase.png)</p>
- Sequence Diagram
  - 검색    
![Sequence Diagram_검색](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_Sequence_%EA%B2%80%EC%83%89.png)
  - 대출/반납/예약   
![Sequence Diagram_대출반납예약](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_Sequence_%EB%8C%80%EC%B6%9C_%EB%B0%98%EB%82%A9_%EC%98%88%EC%95%BD.png)
  - 좋아요   
![Sequence Diagram_좋아요](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_Sequence_%EC%A2%8B%EC%95%84%EC%9A%94%ED%91%9C%EC%8B%9C.png)
- <p>아키텍처
![아키텍처](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.png)</p>
- <p>IA
![IA](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_IA.png)</p>
- <p>DB
![DB](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_DB.png)</p>
- Class Diagram
  - bookshelf        
![Class Diagram_bookshelf](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_%ED%81%B4%EB%9E%98%EC%8A%A4_bookshelf.png)
  - user   
![Class Diagram_user](https://github.com/minjuKwon/library-app/blob/main/Docs/%EB%8F%84%EC%84%9C%EA%B4%80%EB%A6%AC_%ED%81%B4%EB%9E%98%EC%8A%A4_user.png)
- [UI](https://github.com/minjuKwon/library-app/blob/main/Docs/UI.pdf)
