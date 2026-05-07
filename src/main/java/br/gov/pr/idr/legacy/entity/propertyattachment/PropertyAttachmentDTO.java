package br.gov.pr.idr.legacy.entity.propertyattachment;

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
