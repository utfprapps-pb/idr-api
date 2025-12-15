package br.edu.utfpr.ProjetoIDRAPI.entity.email;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity @Audited
public class Email{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	private String ownerRef;
	private String emailFrom;
	private String emailTo;
	private String subject;
	@Column(columnDefinition = "TEXT")
	private String text;
	private LocalDateTime sendDateEmail;
	private String statusEmail;
}
