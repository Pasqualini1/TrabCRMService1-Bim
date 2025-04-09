package br.com.clinica;

import br.com.clinica.dto.AtualizacaoPacienteDto;
import br.com.clinica.dto.CadastroPacienteDto;
import br.com.clinica.dto.PacienteListagemDto;
import br.com.clinica.exceptions.BusinessException;
import br.com.clinica.interfaces.PacienteWS;
import br.com.clinica.services.PacienteService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "br.com.clinica.interfaces.PacienteWS")
public class PacienteWSImpl implements PacienteWS {

    private final PacienteService service = new PacienteService();

    @Override
    public void cadastrarPaciente(CadastroPacienteDto dto) {
        try {
            service.cadastrar(dto);
            System.out.println("Paciente cadastrado com sucesso!");
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    @Override
    public List<PacienteListagemDto> listarPacientes() {
        return service.listar();
    }

    @Override
    @WebMethod
    public void atualizarPaciente(AtualizacaoPacienteDto dto) {
        try {
            service.atualizar(dto);
            System.out.println("Paciente atualizado com sucesso!");
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    @Override
    @WebMethod
    public void excluirPaciente(Long id) {
        try {
            service.excluir(id);
            System.out.println("Paciente excluído com sucesso!");
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    @Override
    @WebMethod
    public PacienteListagemDto buscarPacientePorCpf(String cpf) {
        try {
            return service.buscarPorCpf(cpf);
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            return null;
        }
    }

}
