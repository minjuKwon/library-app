package com.example.library.service

class SaveLibraryInfoFailedException:Exception("도서 정보 저장 실패")
class GetLibraryInfoFailedException:Exception("도서 정보 가져오기 실패")
class GetBookFailedException:Exception("책 정보 가져오기 실패")