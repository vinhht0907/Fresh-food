package com.linh.service.imp;

import java.util.List;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linh.entity.AuthProvider;
import com.linh.entity.UserEntity;
import com.linh.respository.InUserRes;
import com.linh.service.InUserService;
import com.linh.utils.UserPrincipal;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements InUserService {
    
//	@Autowired
	private InUserRes user;
//	@Autowired
//	private InUserRes userRepository;
//	private PasswordEncoder passwordEncoder;
	private final JavaMailSender mailSender;
		
	@Override
	public boolean isEmailExist(String email) {
		// TODO Auto-generated method stub
		return user.findByEmail(email).isPresent();
	}

	@Override
	@Transactional
	public UserEntity save(UserEntity userEntity) {
		// TODO Auto-generated method stub
		return user.save(userEntity);
	}

	@Override
	public UserEntity getLoggingInUsser() {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //SecurityContextHolder là nơi lưu trữ thông tin về bối cảnh bảo mật hiện tại của ứng dụng
		//trong đó bao gồm chủ thể đang sử dụng ứng dụng
		//đối tượng Authentication đc dùng để thể hiện nhgx thông tin này
		//getContext trả về đối tượng là 1 thể hiện của interface SecurityContext 
		UserEntity userEntity= null;
		if (principal instanceof UserPrincipal) {
		 userEntity = ((UserPrincipal)principal).getUser();
		}
		return userEntity;
	}

	@Override
	public UserEntity findOneByEmail(String email) {
		// TODO Auto-generated method stub
		return user.findOneByEmail(email);
	}

	@Override
	public void updateUser(String email, String name, AuthProvider authprovider) {
		UserEntity userEntity = user.findOneByEmail(email);
		userEntity.setFullname(name);
		userEntity.setAuthProvider(authprovider);;
		user.save(userEntity);
	}

	@Override
	public List<String> getEmails() {
		// TODO Auto-generated method stub
		return user.getEmails();
	}

//	@Override
//	public UserEntity findByEmails(String email) {
//		return null;
//	}

	@Override
	public UserEntity findOneById(Integer id) {
		// TODO Auto-generated method stub
		return user.findOneById(id);
	}
////////
	@Override
	public UserEntity findByToken(String token){

		return user.findByToken(token);
	}
	@Override
	public void sendResetPasswordEmail(String email, String resetToken) {
		UserEntity u = user.findOneByEmail(email);

		if (u == null) {
			throw new RuntimeException("User not found");
		}

		u.setToken(resetToken);
		user.save(u);

		String resetUrl = "http://localhost:8080/freshfood/reset-password?token=" + resetToken;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply@example.com");
		message.setTo(email);
		message.setSubject("Reset Password");
		message.setText("To reset your password, please click the link below:\n\n" + resetUrl);
		mailSender.send(message);
	}

	@Override
	public void resetPassword(String resetToken, String newPassword) {
		UserEntity u = user.findByToken(resetToken);

		if (u == null) {
			throw new RuntimeException("Invalid reset token");
		}
//		BCrypt bCrypt = new BCrypt();
//		bCrypt.
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//		String encodedPassword = passwordEncoder.encode(newPassword);

		u.setPassword(bCryptPasswordEncoder.encode(newPassword));
		u.setToken(null);
		user.save(u);
	}
	//them mail
//	public User findByEmail(String email) {
//		return userRepository.findByEmail(email);
//	}
//
//	public User findByPasswordResetToken(String token) {
//		return userRepository.findByPasswordResetToken(token);
//	}
//
//	public void createPasswordResetTokenForUser(User user, String token) {
//		user.setPasswordResetToken(token);
//		user.setPasswordResetTokenExpirationDate(calculatePasswordResetTokenExpirationDate());
//		userRepository.save(user);
//	}
//
//	public void changeUserPassword(User user, String password) {
//		user.setPassword(bCryptPasswordEncoder.encode(password));
//		user.setPasswordResetToken(null);
//		user.setPasswordResetTokenExpirationDate(null);
//		userRepository.save(user);
//	}

}
