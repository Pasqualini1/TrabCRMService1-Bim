package br.com.clinica.services;

import br.com.clinica.domain.Consulta;
import br.com.clinica.domain.Medico;
import br.com.clinica.domain.Paciente;
import br.com.clinica.dto.AgendamentoConsultaDto;
import br.com.clinica.exceptions.BusinessException;
import br.com.clinica.repositories.ConsultaRepository;
import br.com.clinica.repositories.MedicoRepository;
import br.com.clinica.repositories.PacienteRepository;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsultaService {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ConsultaService() {
        try {
            this.medicoRepository = new MedicoRepository();
            this.pacienteRepository = new PacienteRepository();
            this.consultaRepository = new ConsultaRepository();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }

    public void agendarConsulta(AgendamentoConsultaDto dto) {
        LocalDateTime data;
        try {
            data = LocalDateTime.parse(dto.getData(), formatter);
        } catch (Exception e) {
            throw new BusinessException("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm");
        }

        if (data.getHour() < 7 || data.getHour() > 18) {
            throw new BusinessException("Consultas devem ser agendadas entre 07:00 e 18:00.");
        }

        if (data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new BusinessException("Consultas não podem ser agendadas aos domingos.");
        }

        if (Duration.between(LocalDateTime.now(), data).toMinutes() < 30) {
            throw new BusinessException("Consultas devem ser agendadas com pelo menos 30 minutos de antecedência.");
        }

        Paciente paciente = pacienteRepository.buscarPorCpf(dto.getCpfPaciente())
                .orElseThrow(() -> new BusinessException("Paciente não encontrado."));

        if (!paciente.isAtivo()) {
            throw new BusinessException("Paciente inativo.");
        }

        Medico medico;
        if (dto.getCrm() != null) {
            medico = medicoRepository.buscarPorCrm(dto.getCrm())
                    .orElseThrow(() -> new BusinessException("Médico não encontrado com o CRM informado."));
        } else {
            if (dto.getEspecialidade() == null) {
                throw new BusinessException("Especialidade obrigatória se não for informado o médico.");
            }

            medico = medicoRepository.buscarPorEspecialidadeELivre(dto.getEspecialidade(), data)
                    .orElseThrow(() -> new BusinessException("Nenhum médico disponível nesta data para a especialidade informada."));
        }

        if (!medico.isAtivo()) {
            throw new BusinessException("Médico inativo.");
        }

        boolean conflito = consultaRepository.verificarConflito(medico.getCrm(), data);
        if (conflito) {
            throw new BusinessException("Médico já possui consulta marcada neste horário.");
        }

        Consulta consulta = new Consulta(medico, paciente, data);
        consultaRepository.salvar(consulta);

        System.out.println("[SOAP] Consulta agendada com sucesso:");
        System.out.println("       Paciente: " + paciente.getNome());
        System.out.println("       Médico: " + medico.getNome() + " (CRM: " + medico.getCrm() + ")");
        System.out.println("       Especialidade: " + medico.getEspecialidade());
        System.out.println("       Data e hora: " + data.format(formatter));
    }

    public void cancelar(Long idConsulta, String motivo) {
        try {
            boolean atualizado = consultaRepository.cancelarConsulta(idConsulta, motivo);
            if (!atualizado) {
                throw new BusinessException("Consulta não encontrada.");
            }

            System.out.println("[SOAP] Consulta cancelada com sucesso (ID: " + idConsulta + ")");
            System.out.println("       Motivo: " + motivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cancelar consulta: " + e.getMessage(), e);
        }
    }

}
