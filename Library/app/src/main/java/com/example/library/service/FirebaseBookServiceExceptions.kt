package com.example.library.service

class CheckLibraryLikeFailedException:Exception("도서 좋아요 저장 확인 실패")
class UpdateLibraryStatusFailedException:Exception("도서 상태 수정 실패")
class GetLoanDueStatusFailedException:Exception("도서 대출일 상태 확인 실패")