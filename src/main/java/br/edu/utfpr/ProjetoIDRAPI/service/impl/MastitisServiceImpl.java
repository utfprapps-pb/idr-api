package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Mastitis;
import br.edu.utfpr.ProjetoIDRAPI.repository.MastitisRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.MastitisService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MastitisServiceImpl extends CrudServiceImpl<Mastitis, Long> implements MastitisService {

    private final MastitisRepository mastitisRepository;

    public MastitisServiceImpl(MastitisRepository mastitisRepository) {
        this.mastitisRepository = mastitisRepository;
    }

    @Override
    protected JpaRepository<Mastitis, Long> getRepository() {
        return this.mastitisRepository;
    }

}
