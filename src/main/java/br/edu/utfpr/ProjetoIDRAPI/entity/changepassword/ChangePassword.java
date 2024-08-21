package br.edu.utfpr.ProjetoIDRAPI.entity.changepassword;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String recuperationCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date codeSubmissionDate;
    
    private String recuperationEmail;
    
    private String userDisplayName;
    
    private String newPassword;
}
