package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.shared.Identifier;

public record UserID(Long id) implements Identifier {

    public static UserID from(final Long id) {
        return new UserID(id);
    }

    //ID será gerado na camada de infraestrutura
    public static UserID unique(){
        return from(null);
    }
}
