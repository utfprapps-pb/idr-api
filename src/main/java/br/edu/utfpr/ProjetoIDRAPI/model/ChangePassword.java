package br.edu.utfpr.ProjetoIDRAPI.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
