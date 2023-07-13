## API Key는 제거된 버전 입니다.

- 디렉토리 구조
     
          src
          ├── main
          │   ├── java
          │   │   └── com
          │   │       └── example
          │   │           └── blogsearchapi
          │   │               ├── BlogSearchApiApplication.java
          │   │               ├── controller
          │   │               │   └── BlogSearchController.java
          │   │               ├── entity
          │   │               │   ├── Document.java
          │   │               │   ├── Item.java
          │   │               │   ├── Meta.java
          │   │               │   ├── PopularSearch.java
          │   │               │   ├── ResponseData.java
          │   │               │   └── ResponseDataNaver.java
          │   │               ├── repository
          │   │               │   └── PopularSearchRepository.java
          │   │               └── service
          │   │                   ├── BlogSearchService.java
          │   │                   ├── KakaoBlogSearchApi.java
          │   │                   ├── NaverBlogSearchApi.java
          │   │                   ├── PopularSearchService.java
          │   │                   └── SearchApi.java
          │   └── resources
          │       ├── application.properties
          │       ├── static
          │       └── templates
          └── test
              └── java
                  └── com
                      └── example
                          └── blogsearchapi
                              ├── BlogSearchApiControllerTests.java
                              ├── BlogSearchApiServiceTests.java
                              └── PopularSearchServiceTest.java

# 블로그 검색 서비스 API 명세

## 요약
이 API는 특정 키워드로 블로그를 검색하는 기능을 제공합니다.

## 요청
- URL: `localhost:8080/api/blog?keyword=test1&page=1&pageSize=10&sort=recency`
- 메서드: GET

### 쿼리 파라미터
- keyword (필수): 검색할 키워드
- page (선택적): 페이지 번호 (기본값: 1) 1~50
- pageSize (선택적): 페이지당 결과 수 (기본값: 10) 1~50
- sort (선택적): 결과 정렬 기준 (기본값: accuracy) accuracy,recency

## 응답

### 성공 응답
- 상태코드: 200 OK
- 내용:
```json
{
  "documents": [
    {
      "blogname": "개발합니다.",
      "contents": "5부터는 class나 method가 public일 필요 없음 (Junit4는 public이었어야함) * - 자바 리플렉션 사용. 굳이 public 사용할 필요 없음 */ class BasicTest { @<b>Test</b> void create<b>1</b>(){ Basic basic = new Basic(); assertNotNull(basic); System.out.println(&#34;create<b>1</b>&#34;); } @<b>Test</b> void create2(){ System.out.println...",
      "datetime": "2023-05-18T14:32:22.000+09:00",
      "thumbnail": "https://search4.kakaocdn.net/argon/130x130_85_c/4jrcxEfOFQ1",
      "title": "[Java <b>Test</b>] <b>1</b>. JUnit5 (<b>1</b>)",
      "url": "http://maybechrisk.tistory.com/101"
    },
    ...
  ],
  "meta": {
    "is_end": false,
    "pageable_count": 797,
    "total_count": 1270527,
    "status": "200 OK",
    "message": "성공",
    "source": "Kakao"
  }
}
```

### 응답 필드 설명
- documents: 검색된 블로그 목록
  - blogname: 블로그 이름
  - contents: 내용
  - datetime: 작성일시
  - thumbnail: 썸네일 이미지 URL
  - title: 제목
  - url: 블로그 URL
- meta: 검색 결과 메타 정보
  - is_end: 마지막 페이지 여부
  - pageable_count: 페이징 가능한 총 결과 수
  - total_count: 전체 검색 결과 수
  - status: 응답 상태
  - message: 응답 메시지
  - source: 검색소스(Kakao,Naver)

## 오류 응답
- meta: 검색 결과 메타 정보
  - status: 400 Bad Request...
  - 내용: 오류에 대한 설명이 포함된 응답 본문
