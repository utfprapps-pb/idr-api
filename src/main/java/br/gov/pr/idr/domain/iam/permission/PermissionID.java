package br.gov.pr.idr.domain.iam.permission;

import br.gov.pr.idr.domain.shared.Identifier;

public record PermissionID(Long id) implements Identifier {


    public static PermissionID from(final Long id) {
        return new PermissionID(id);
    }

    //ID será gerado na camada de infraestrutura
    public static PermissionID unique() {
        return from(null);
    }
}
