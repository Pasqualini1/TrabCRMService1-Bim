package br.com.clinica.services;

import br.com.clinica.domain.Endereco;
import br.com.clinica.domain.Paciente;
import br.com.clinica.dto.AtualizacaoPacienteDto;
import br.com.clinica.dto.CadastroPacienteDto;
import br.com.clinica.dto.PacienteListagemDto;
import br.com.clinica.exceptions.BusinessException;
import br.com.clinica.repositories.PacienteRepository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;

import java.util.Comparator;
import java.util.List;

public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService() {
        this.repository = new PacienteRepository();
    }

    public void cadastrar(CadastroPacienteDto dto) {
        if (isNullOrEmpty(dto.getNome())) throw new BusinessException(
                "O nome do paciente é obrigatório.");
        if (isNullOrEmpty(dto.getEmail())) throw new BusinessException(
                "O e-mail do paciente é obrigatório.");
        if (isNullOrEmpty(dto.getTelefone())) throw new BusinessException(
                "O telefone do paciente é obrigatório.");
        if (isNullOrEmpty(dto.getCpf())) throw new BusinessException(
                "O CPF do paciente é obrigatório.");
        if (isNullOrEmpty(dto.getRua()) || isNullOrEmpty(dto.getBairro()) ||
                isNullOrEmpty(dto.getCidade()) ||
                isNullOrEmpty(dto.getEstado()) || isNullOrEmpty(dto.getCep())) {
            throw new BusinessException(
                    "Endereço incompleto. Todos os campos obrigatórios devem ser preenchidos.");
        }

        Endereco endereco = new Endereco(
                dto.getRua(),
                dto.getNumero(),
                dto.getComplemento(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getCep()
        );

        Paciente paciente = new Paciente(
                dto.getNome(),
                dto.getEmail(),
                dto.getTelefone(),
                dto.getCpf(),
                endereco
        );

        repository.salvar(paciente);
        System.out.println("[SOAP] Paciente cadastrado com sucesso: "
                + paciente.getNome() + " (CPF: " + paciente.getCpf() + ")");
    }

    public List<PacienteListagemDto> listar() {
        List<PacienteListagemDto> lista = repository.listarTodos().stream()
                .filter(Paciente::isAtivo)
                .map(p -> new PacienteListagemDto(p.getNome(),
                        p.getEmail(), p.getCpf()))
                .sorted(Comparator.comparing(PacienteListagemDto::getNome,
                        String.CASE_INSENSITIVE_ORDER))
                .toList();

        System.out.println("[SOAP] Listagem de pacientes realizada. Total: " + lista.size());
        return lista;
    }

    @WebMethod
    public void atualizar(AtualizacaoPacienteDto dto) {
        Paciente paciente = repository.buscarPorId(dto.getId())
                .orElseThrow(() -> new BusinessException("Paciente não encontrado."));

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            paciente.setNome(dto.getNome());
        }

        if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
            paciente.setTelefone(dto.getTelefone());
        }

        if (dto.getRua() != null) {
            paciente.getEndereco().atualizar(
                    dto.getRua(),
                    dto.getNumero(),
                    dto.getComplemento(),
                    dto.getBairro(),
                    dto.getCidade(),
                    dto.getEstado(),
                    dto.getCep()
            );
        }

        repository.salvar(paciente);
        System.out.println("[SOAP] Paciente atualizado com sucesso: "
                + paciente.getNome() + " (ID: " + dto.getId() + ")");
    }

    @WebMethod
    public void excluir(
            @WebParam(name = "Id")Long id) {
        Paciente paciente = repository.buscarPorId(id)
                .orElseThrow(() -> new BusinessException("Paciente não encontrado."));

        paciente.desativar();
        repository.salvar(paciente);
        System.out.println("[SOAP] Paciente desativado: "
                + paciente.getNome() + " (ID: " + paciente.getId() + ")");
    }

    private boolean isNullOrEmpty(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    @WebMethod
    public PacienteListagemDto buscarPorCpf(
            @WebParam(name = "Cpf")String cpf) {
        Paciente paciente = repository.buscarPorCpf(cpf)
                .orElseThrow(() -> new BusinessException("Paciente não encontrado."));

        System.out.println("[SOAP] Paciente encontrado: "
                + paciente.getNome() + " | CPF: " + paciente.getCpf());

        return new PacienteListagemDto(
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getCpf()
        );
    }

}
