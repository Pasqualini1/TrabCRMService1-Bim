
package br.com.clinica.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CancelamentoConsultaDto {

    private Long consultaId;
    private String motivo;

    public CancelamentoConsultaDto(Long consultaId, String motivo) {
        this.consultaId = consultaId;
        this.motivo = motivo;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public String getMotivo() {
        return motivo;
    }
}
