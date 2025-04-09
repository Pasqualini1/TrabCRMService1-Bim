package br.com.clinica.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    private LocalDateTime data;

    private boolean ativa = true;

    private String motivoCancelamento;

    public Consulta() {

    }

    public Consulta(Medico medico, Paciente paciente, LocalDateTime data) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
        this.ativa = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getData() {
        return data;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public boolean isCancelada() {
        return !ativa;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void cancelar(String motivo) {
        this.ativa = false;
        this.motivoCancelamento = motivo;
    }
}
