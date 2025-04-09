package br.com.clinica.interfaces;

import br.com.clinica.dto.AtualizacaoPacienteDto;
import br.com.clinica.dto.CadastroPacienteDto;
import br.com.clinica.dto.PacienteListagemDto;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface PacienteWS {

    @WebMethod
    void cadastrarPaciente(CadastroPacienteDto dto);

    @WebMethod
    List<PacienteListagemDto> listarPacientes();

    @WebMethod
    void atualizarPaciente(AtualizacaoPacienteDto dto);

    @WebMethod
    void excluirPaciente(
            @WebParam(name = "Id")Long id);

    @WebMethod
    PacienteListagemDto buscarPacientePorCpf(
            @WebParam(name = "Cpf")String cpf);
}
