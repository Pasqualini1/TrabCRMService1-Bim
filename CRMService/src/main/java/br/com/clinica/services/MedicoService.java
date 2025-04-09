package br.com.clinica.services;

import br.com.clinica.domain.Endereco;
import br.com.clinica.domain.Especialidade;
import br.com.clinica.domain.Medico;
import br.com.clinica.dto.AtualizacaoMedicoDto;
import br.com.clinica.dto.CadastroMedicoDto;
import br.com.clinica.dto.MedicoListagemDto;
import br.com.clinica.exceptions.BusinessException;
import br.com.clinica.repositories.MedicoRepository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class MedicoService {

    private final MedicoRepository repository;

    public MedicoService() throws SQLException {
        this.repository = new MedicoRepository();
    }

    public void cadastrar(CadastroMedicoDto dto) {
        if (isNullOrEmpty(dto.getNome()))
            throw new BusinessException("O nome é obrigatório.");
        if (isNullOrEmpty(dto.getEmail()))
            throw new BusinessException("O e-mail é obrigatório.");
        if (isNullOrEmpty(dto.getCrm()))
            throw new BusinessException("O CRM é obrigatório.");
        if (dto.getEspecialidade() == null)
            throw new BusinessException("A especialidade é obrigatória.");

        Endereco endereco = new Endereco(
                dto.getRua(),
                dto.getNumero(),
                dto.getComplemento(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getCep()
        );

        Medico medico = new Medico(
                dto.getNome(),
                dto.getEmail(),
                dto.getTelefone(),
                dto.getCrm(),
                Especialidade.valueOf(dto.getEspecialidade().toUpperCase()),
                endereco
        );

        repository.salvar(medico);
        System.out.println("[SOAP] Médico cadastrado com sucesso: "
                + medico.getNome() + " (CRM: " + medico.getCrm() + ")");
    }

    public List<MedicoListagemDto> listar() {
        List<MedicoListagemDto> lista = repository.listarTodos().stream()
                .filter(Medico::isAtivo)
                .map(m -> new MedicoListagemDto(
                        m.getNome(),
                        m.getEmail(),
                        m.getCrm(),
                        m.getEspecialidade().name()
                ))
                .sorted(Comparator.comparing(MedicoListagemDto::getNome,
                        String.CASE_INSENSITIVE_ORDER))
                .toList();

        System.out.println("[SOAP] Listagem de médicos realizada. Total: " + lista.size());
        return lista;
    }

    @WebMethod
    public void atualizar(
            @WebParam(name = "crm")String crm,
            AtualizacaoMedicoDto dto) {
        Medico medico = repository.buscarPorCrm(crm)
                .orElseThrow(() -> new BusinessException("Médico não encontrado."));

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            medico.setNome(dto.getNome());
        }

        if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
            medico.setTelefone(dto.getTelefone());
        }

        if (dto.getRua() != null) {
            medico.getEndereco().atualizar(
                    dto.getRua(),
                    dto.getNumero(),
                    dto.getComplemento(),
                    dto.getBairro(),
                    dto.getCidade(),
                    dto.getEstado(),
                    dto.getCep()
            );
        }

        repository.salvar(medico);
        System.out.println("[SOAP] Médico atualizado com sucesso: "
                + medico.getNome() + " (CRM: " + medico.getCrm() + ")");
    }

    @WebMethod
    public void excluir(
            @WebParam(name = "Crm") String crm) {
        Medico medico = repository.buscarPorCrm(crm)
                .orElseThrow(() -> new BusinessException("Médico não encontrado."));
        medico.desativar();
        repository.salvar(medico);
        System.out.println("[SOAP] Médico desativado: "
                + medico.getNome() + " (CRM: " + medico.getCrm() + ")");
    }

    private boolean isNullOrEmpty(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    @WebMethod
    public MedicoListagemDto buscarPorCrm(
            @WebParam(name = "Crm")String crm) {
        Medico medico = repository.buscarPorCrm(crm)
                .orElseThrow(() -> new BusinessException("Médico não encontrado."));

        System.out.println("[SOAP] Médico buscado por CRM: " + crm);

        return new MedicoListagemDto(
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getEspecialidade().name()
        );
    }
}
