package br.com.clinica.dto;

import java.io.Serializable;

public class MedicoListagemDto implements Serializable {

    private String nome;
    private String email;
    private String crm;
    private String especialidade;

    public MedicoListagemDto() {
    }

    public MedicoListagemDto(String nome,
                             String email,
                             String crm,
                             String especialidade) {
        this.nome = nome;
        this.email = email;
        this.crm = crm;
        this.especialidade = especialidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}
