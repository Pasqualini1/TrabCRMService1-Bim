package br.com.clinica.interfaces;

import br.com.clinica.dto.AtualizacaoMedicoDto;
import br.com.clinica.dto.CadastroMedicoDto;
import br.com.clinica.dto.MedicoListagemDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface MedicoWS {

    @WebMethod
    void cadastrarMedico(CadastroMedicoDto dto);

    @WebMethod
    List<MedicoListagemDto> listar();

    @WebMethod
    void atualizarMedico(
            @WebParam(name = "Crm")String crm, AtualizacaoMedicoDto dto);

    @WebMethod
    void excluirMedicoPorCrm(
            @WebParam(name = "Crm") String crm);

    @WebMethod
    MedicoListagemDto buscarMedicoPorCrm(
            @WebParam(name = "Crm")String crm);

}

