package br.edu.utfpr.ProjetoIDRAPI.entity.changepassword;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;

@RestController
@RequestMapping("changingPw")
public class ChangePasswordController {
	private final ChangePasswordService changePwService;
	
	private ModelMapper modelMapper;
	
	public ChangePasswordController(ChangePasswordService changePwService, 
									ModelMapper modelMapper) {
		
		this.changePwService = changePwService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping("sendCode")
	public ResponseEntity sendRecuperationCode(@RequestBody ChangePassword changePw) {
		return ResponseEntity.ok(new GenericResponse(
				changePwService.sendCode(changePw.
						getRecuperationEmail())));
	}
	
	@PostMapping("AlterPw")
	public ResponseEntity alterPassword(@RequestBody ChangePassword changePw) {
		return ResponseEntity.ok(new GenericResponse(
				changePwService.changingUserPassword(changePw)));
	}
}
