package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.dto.EmailDto;
import br.edu.utfpr.ProjetoIDRAPI.model.ChangePassword;
import br.edu.utfpr.ProjetoIDRAPI.model.Email;
import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.ChangePasswordRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ChangePasswordService;
import br.edu.utfpr.ProjetoIDRAPI.service.EmailService;
import br.edu.utfpr.ProjetoIDRAPI.service.UserService;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService{
	@Autowired
    private UserService userService;
	
	@Autowired
    private EmailService emailService;
	
	private final ChangePasswordRepository changePasswordRepository;
	
	public ChangePasswordServiceImpl(ChangePasswordRepository changePasswordRepository) {
		this.changePasswordRepository = changePasswordRepository;
	}
	
	@Override
	public String sendCode(String email) {
		User user = userService.findByName(email);
		ChangePassword changePassword = new ChangePassword();
		changePassword.setRecuperationEmail(user.getUsername());
		changePassword.setRecuperationCode(getPasswordRecoveryCode(user.getId()));
		changePassword.setCodeSubmissionDate(new Date());
		changePassword.setUserDisplayName(user.getDisplayName());
		
		changePasswordRepository.save(changePassword);
		
		EmailDto emailDto = new EmailDto(changePassword.getUserDisplayName(),
				"danieli.marialefchak@gmail.com", changePassword.getRecuperationEmail(),
				"Teste Projeto IDR", "Teste de código de recuperação de senha. \n" +
		        "O codigo de recuperação de senha é: " + changePassword.getRecuperationCode());
		
		Email emailModel = new Email();
		BeanUtils.copyProperties(emailDto, emailModel);
		emailService.sendEmail(emailModel);
		
		return "Código enviado";
	}

	@Override
	public String getPasswordRecoveryCode(Long id) {
		DateFormat format = new SimpleDateFormat("HHmmssmm");
		return format.format(new Date().getTime())+id;
	}

	@Override
	public String changingUserPassword(ChangePassword change) {
		User userDb = userService.findByName(change.getRecuperationEmail());
		ChangePassword changePasswordDB = findByEmailAndCode(
										change.getRecuperationEmail(), 
										change.getRecuperationCode());
		
		if(userDb!=null) {
			if(changePasswordDB!=null) {
				Date difference = new Date(new Date().getTime()
								- changePasswordDB
								.getCodeSubmissionDate()
								.getTime());
				
				if(difference.getTime()/1000<600) {
					userDb.setPassword(change.getNewPassword());
					userService.save(userDb);
					Email emailModel = emailService.findByEmailTo(
							changePasswordDB.getRecuperationEmail());
					emailService.deleteById(emailModel.getId());
					changePasswordRepository.deleteById(changePasswordDB.getId());
					
					return "Senha alterada com sucesso";
				}else{
	                return "Tempo exprirado, solicite um novo código";
	            }
				
			}else {
				return "Código ou email não encontrado";
			}
		}else {
			return "Email não encontrado";
		}
	}

	@Override
	public ChangePassword findByEmailAndCode(String recuperationEmail, String recuperationCode) {
		return changePasswordRepository.findByRecuperationEmailAndRecuperationCode(recuperationEmail, recuperationCode);
	}

}
