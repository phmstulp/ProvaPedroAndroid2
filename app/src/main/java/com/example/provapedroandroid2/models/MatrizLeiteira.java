package com.example.provapedroandroid2.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class MatrizLeiteira extends SugarRecord implements Serializable {
    @Unique
    private int identificador;
    private String descricao;
    private int idade;
    private Date dtUltimoParto;

    public MatrizLeiteira() {
    }

    public MatrizLeiteira(int identificador, String descricao, int idade, Date dtUltimoParto) {
        this.identificador = identificador;
        this.descricao = descricao;
        this.idade = idade;
        this.dtUltimoParto = dtUltimoParto;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Date getDtUltimoParto() {
        return dtUltimoParto;
    }

    public void setDtUltimoParto(Date dtUltimoParto) {
        this.dtUltimoParto = dtUltimoParto;
    }

    @Override
    public String toString() {
        return identificador + " - " + descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatrizLeiteira)) return false;
        MatrizLeiteira matrizLeiteira = (MatrizLeiteira) o;
        return identificador == matrizLeiteira.identificador;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }
}
