package br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAttachmentDTO {
    private Long id;
    private byte[] attachment;
}
