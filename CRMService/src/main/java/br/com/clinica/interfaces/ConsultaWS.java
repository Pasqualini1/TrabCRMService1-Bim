package br.com.clinica.interfaces;

import br.com.clinica.dto.AgendamentoConsultaDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public interface ConsultaWS {

    @WebMethod
    void agendar(AgendamentoConsultaDto dto);

    @WebMethod
    void cancelarConsulta(
            @WebParam(name = "idConsulta") Long idConsulta,
            @WebParam(name = "motivoCancelamento") String motivoCancelamento);
}