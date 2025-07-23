package com.example.library.service

class SignUpFailedException:Exception("회원가입 실패")
class SaveUserInfoFailedException:Exception("사용자 정보 저장 실패")
class SignInFailedException:Exception("로그인 실패")
class SignOutFailedException:Exception("로그아웃 실패")
class ReAuthenticateFailedException:Exception("사용자 인증 실패")
class UnRegisterFailedException:Exception("회원가입 실패")
class DeleteUserInfoFailedException:Exception("사용자 정보 삭제 실패")
class UpdateUserInfoFailedException:Exception("사용자 정보 수정 실패")
class UpdatePasswordFailedException:Exception("비밀번호 수정 실패")
class SaveSessionFailedException:Exception("세션 저장 실패")
class VerificationFailedException:Exception("사용자 인증 실패")
class ResetPasswordFAiledException:Exception("비밀번호 수정 실패")