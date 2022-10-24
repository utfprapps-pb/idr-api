package br.edu.utfpr.ProjetoIDRAPI.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegion implements Serializable {

    @Id
    @JoinColumn(name = "User_id")
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "Region_id")
    @ManyToOne
    private Region region;
}
