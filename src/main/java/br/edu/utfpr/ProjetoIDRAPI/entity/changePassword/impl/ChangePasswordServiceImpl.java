package br.edu.utfpr.ProjetoIDRAPI.entity.changePassword.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.entity.email.dto.EmailDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.changePassword.ChangePassword;
import br.edu.utfpr.ProjetoIDRAPI.entity.email.Email;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.changePassword.ChangePasswordRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.changePassword.ChangePasswordService;
import br.edu.utfpr.ProjetoIDRAPI.entity.email.EmailService;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserService;

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
				"Teste Projeto IDR", redirectButton(changePassword.getRecuperationCode()));
		
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
	public String redirectButton(String code) {
		return "<head>" +
	                "<p>Teste de código de recuperação de senha.</p>" +
	
	                "<style type=\"text/css\">" +
		                ".btn{" +
			                "background:white;" +
			                "color:#40A527;" +
			                "font-size:18px;" +
			                "border-color:#40A527;" +
			                "border-radius:12px;" +
			                "border-style:solid;" +
			                "text-align:center;" +
			                "width:200px;" +
			                "align-items:center;" +
			                "margin-top:25px;" +
			                "padding:5px;" +
		                "}" +
		
		                ".btn:hover{" +
			                "color:white;" +
			                "background:#40A527;" +
		                "}" +
	                "</style>" +
                "</head>" +

                "<body>" +
	                "<p>O codigo de recuperação de senha é: </p>" +
	                code + "<br>" + 
	                "<a href='#'>" +
		                "<button " +
		                "class=\"btn\">" +
		                	"Alterar senha" +
		                "</button>" +
	                "</a>" +
                "</body>";
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
