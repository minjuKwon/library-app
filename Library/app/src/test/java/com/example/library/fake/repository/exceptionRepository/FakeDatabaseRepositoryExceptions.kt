package com.example.library.fake.repository.exceptionRepository

class FakeLikeCheckFailedException:Exception("LikeCheckFailingRepository 테스트 실패")
class FakeGetLoanHistoryFailedException:Exception("GetLoanHistoryFailingRepository 테스트 실패")
class FakeReservationByUserFailedException:Exception("ReservationByUserFailingRepository 테스트 실패")
class FakeReservationByBookFailedException:Exception("ReservationByBookFailingRepository 테스트 실패")
