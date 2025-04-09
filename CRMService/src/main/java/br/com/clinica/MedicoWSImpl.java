package br.com.clinica;

import br.com.clinica.dto.AtualizacaoMedicoDto;
import br.com.clinica.dto.CadastroMedicoDto;
import br.com.clinica.dto.MedicoListagemDto;
import br.com.clinica.exceptions.BusinessException;
import br.com.clinica.interfaces.MedicoWS;
import br.com.clinica.services.MedicoService;
import jakarta.jws.WebService;

import java.sql.SQLException;
import java.util.List;

@WebService(endpointInterface = "br.com.clinica.interfaces.MedicoWS")
public class MedicoWSImpl implements MedicoWS {

    private final MedicoService service = new MedicoService();

    public MedicoWSImpl() throws SQLException {
    }

    @Override
    public void cadastrarMedico(CadastroMedicoDto dto) {
        try {
            service.cadastrar(dto);
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    @Override
    public List<MedicoListagemDto> listar() {
        return service.listar();
    }

    @Override
    public void atualizarMedico(String crm, AtualizacaoMedicoDto dto) {
        try {
            service.atualizar(crm, dto);
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    @Override
    public void excluirMedicoPorCrm(String crm) {
        try {
            service.excluir(crm);
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
        }
    }

    @Override
    public MedicoListagemDto buscarMedicoPorCrm(String crm) {
        try {
            return service.buscarPorCrm(crm);
        } catch (BusinessException e) {
            System.out.println("Erro de negócio: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            return null;
        }
    }
}
