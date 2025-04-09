package br.com.clinica.repositories;

import br.com.clinica.domain.Consulta;
import br.com.clinica.domain.Especialidade;
import br.com.clinica.domain.Medico;
import br.com.clinica.domain.Paciente;
import br.com.clinica.infrastructure.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {

    private final Connection connection;

    public ConsultaRepository() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.connection.setAutoCommit(false);
    }

    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consultas (nome_medico, crm, especialidade, nome_paciente, cpf_paciente, data, ativa, motivo_cancelamento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, consulta.getMedico().getNome());
            ps.setString(2, consulta.getMedico().getCrm());
            ps.setString(3, consulta.getMedico().getEspecialidade().toString());
            ps.setString(4, consulta.getPaciente().getNome());
            ps.setString(5, consulta.getPaciente().getCpf());
            ps.setTimestamp(6, Timestamp.valueOf(consulta.getData()));
            ps.setBoolean(7, consulta.isAtiva());
            ps.setString(8, consulta.getMotivoCancelamento());

            ps.executeUpdate();
            connection.commit();

            System.out.println("[SOAP] Consulta salva com sucesso.");
        } catch (Exception e) {
            try {
                connection.rollback(); // rollback em caso de erro
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            throw new RuntimeException("Erro ao salvar consulta", e);
        }
    }

    public boolean cancelarConsulta(Long idConsulta, String motivo) {
        String sql = "UPDATE consultas SET ativa = false, motivo_cancelamento = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, motivo);
            ps.setLong(2, idConsulta);

            int linhasAfetadas = ps.executeUpdate();
            connection.commit();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            try {
                connection.rollback(); // rollback em caso de erro
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            throw new RuntimeException("Erro ao cancelar consulta", e);
        }
    }

    public boolean verificarConflito(String crm, LocalDateTime data) {
        String sql = "SELECT COUNT(*) FROM consultas WHERE crm = ? AND data = ? AND ativa = true";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, crm);
            ps.setTimestamp(2, Timestamp.valueOf(data));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar conflito de horário", e);
        }
    }
}
