package com.linh.service;

import java.util.List;

import javax.transaction.Transactional;

import com.linh.entity.AuthProvider;
import com.linh.entity.UserEntity;

public interface InUserService {
	
      boolean isEmailExist(String email);
      
      @Transactional
      UserEntity save(UserEntity userEntity);
      UserEntity getLoggingInUsser();
      UserEntity findOneByEmail(String email);
      UserEntity findOneById(Integer id);
      void updateUser(String email, String name, AuthProvider authprovider);
      List<String> getEmails();
//      UserEntity findByEmails(String email);
      UserEntity findByToken(String token);
      void resetPassword(String token, String newPassword);
      void sendResetPasswordEmail(String email, String resetToken);
}
