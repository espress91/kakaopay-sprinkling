# DB 설계 
* ReceivedSprinkling: 뿌리기와 사용자를 이어주는 역할
    - 식별자 값
    - 해당 뿌리기 Id
    - 뿌리기 돈 받은 사용자 Id
    - 뿌리기를 통해 받은 금액
* Room : 대화방 관련 테이블
    - 식별자 값
    - 대화방 참여자 수
* Sprinkling: 뿌리기 관련 테이블  _(해당 테이블을 조회 하면 조회API 결과를 얻을 수 있음)_
    - 식별자 값
    - 뿌리기 고유 토큰 _(3자리 문자열)_
    - 대화방 식별자 값 _(Room_FK)_
    - 뿌리기 만든 사람 _(User_FK)_
    - 뿌린 금액
    - 분배할 인원 수 (뿌리기 받을 인원 수)
    - 뿌리기를 받는 사용자들 정보 _(ReceivedSprinkling_FK)_
    - 뿌리기 만든 날짜
    - 뿌리기 잔액 _(DTO에서 "뿌린 금액-뿌리기 잔액"을 하여 받기 완료된 금액을 구함)_
* User: 사용자 관련 테이블
    - 식별자 값
    - 사용자가 가지고 있는 돈
# 뿌리기 돈 분배 로직
    1. 모든 분배 인원에게 1원씩 나눠준다.(divideBudget 함수)
    2. 남은 나눠줄 금액에서 임의의 사람에게 임의의 금액을 나눠준다.(divide 함수)
    3. 잔액이 2원보다 작게 되면 잔액을 임의의 사람에게 모두 나눠준다.(divide 함수)
# API 명세 
* (서버 ip는 127.0.0.1, 서버 port는 9000으로 가정)
## 사용자 등록
#### Post url : http://127.0.0.1:9000/user
#### Post body : 
```json
{
    "budget" : 30000
}
```
#### Post response :
```json
{
    "success": true,
    "response": {
        "id": 1,
        "budget": 30000
    },
    "error": null
}
```
## 대화방 생성
#### Post url : http://127.0.0.1:9000/room
#### Post header : 
```bash
X-USER-ID: 대화방 생성할 사용자
X-ROOM-ID: 생성할 대화방 식별값
```
#### Post response :
```json
{
    "success": true,
    "response": {
        "id": 1,
        "participants": 1
    },
    "error": null
}
```
## 대화방 참여
#### Put url : http://127.0.0.1:9000/room
#### Put header : 
```bash
X-USER-ID: 대화방 참여할 사용자
X-ROOM-ID: 참여할 대화방 식별값
```
#### Put response :
```json
{
    "success": true,
    "response": {
        "id": 1,
        "participants": 2
    },
    "error": null
}
```
## 뿌리기 만들기
#### Post url : http://127.0.0.1:9000/sprinkling/create
#### Post header :
```bash 
X-USER-ID: 뿌리기 만드는 사용자
X-ROOM-ID: 뿌리기가 있을 대화방 식별값
```
#### Post Body :
```json
{
    "budget" : 10000, 
    "divide_num" : 3  
}
```
#### Post response :
```json
{
    "success": true,
    "response": {
        "id": 2,
        "token": "ndp", 
        "room": {   
            "id": 1,
            "participants": 3
        },
        "owner": { 
            "id": 1,
            "budget": 10000
        },
        "budget": 10000,   
        "receivedSprinklings": [    
            {
                "id": 4,
                "sprinklingId": 2,
                "user": null,  
                "budget": 63
            },
            {
                "id": 5,
                "sprinklingId": 2,
                "user": null,
                "budget": 762
            },
            {
                "id": 6,
                "sprinklingId": 2,
                "user": null,
                "budget": 9175
            }
        ],
        "balance": 0,  
        "divide_num": 3,
        "sprinkling_date": "2020-08-25 01:55"
    },
    "error": null
}
```                   
## 뿌리기 받기 
#### Post url : http://127.0.0.1:9000/sprinkling/receive
#### Post header :
```bash 
X-USER-ID: 뿌리기 받는 사용자
X-ROOM-ID: 뿌리기가 있는 대화방 식별값
```
#### Post Body :
```json
{
    "token" : "pvc" 
}
```   
#### Post response :
```json
{
    "success": true,
    "response": {
        "id": 1,
        "sprinklingId": 1, 
        "user": {   
            "id": 3,
            "budget": 33983
        },
        "budget": 3983  
    },
    "error": null
}
```    
## 뿌리기 조회 
#### Get url : http://127.0.0.1:9000/sprinkling/info
#### Get header :
```bash 
X-USER-ID: 뿌리기 만든 사용자
X-ROOM-ID: 뿌리기가 있는 대화방 식별값
```
#### Get Body :
```json
{
    "token" : "pvc"
}
```   
#### Get response :
```json
{
    "success": true,
    "response": {
        "id": 1,
        "token": "pvc",
        "room": {
            "id": 1,
            "participants": 3
        },
        "owner": {
            "id": 1,
            "budget": 0
        },
        "budget": 10000,
        "receivedSprinklings": [
            {
                "id": 1,
                "sprinklingId": 1,
                "user": {
                    "id": 3,
                    "budget": 33983
                },
                "budget": 3983
            },
            {
                "id": 2,
                "sprinklingId": 1,
                "user": null,
                "budget": 5184
            },
            {
                "id": 3,
                "sprinklingId": 1,
                "user": null,
                "budget": 833
            }
        ],
        "balance": 3983,
        "divide_num": 3,
        "sprinkling_date": "2020-08-25 08:57"
    },
    "error": null
}
```