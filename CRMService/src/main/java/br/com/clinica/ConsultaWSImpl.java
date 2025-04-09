package br.com.clinica;

import br.com.clinica.dto.AgendamentoConsultaDto;
import br.com.clinica.exceptions.BusinessException;
import br.com.clinica.interfaces.ConsultaWS;
import br.com.clinica.services.ConsultaService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(endpointInterface = "br.com.clinica.interfaces.ConsultaWS")
public class ConsultaWSImpl implements ConsultaWS {

    private final ConsultaService service = new ConsultaService();

    @Override
    public void agendar(AgendamentoConsultaDto dto) {
        try {
            service.agendarConsulta(dto);
            System.out.println("Consulta agendada com sucesso!");
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    @Override
    @WebMethod
    public void cancelarConsulta(
            @WebParam(name = "idConsulta")Long idConsulta,
            @WebParam(name = "motivoCancelamento")String motivo) {
        try {
            service.cancelar(idConsulta, motivo);
            System.out.println("Consulta cancelada com sucesso!");
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

}
