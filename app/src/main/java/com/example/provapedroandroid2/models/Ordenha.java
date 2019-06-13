package com.example.provapedroandroid2.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.Date;

public class Ordenha extends SugarRecord implements Serializable {
    @Unique
    private int identificador;
    private MatrizLeiteira matrizLeiteira;
    private double qtLitros;
    private Date dtOrdenha;

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public MatrizLeiteira getMatrizLeiteira() {
        return matrizLeiteira;
    }

    public void setMatrizLeiteira(MatrizLeiteira matrizLeiteira) {
        this.matrizLeiteira = matrizLeiteira;
    }

    public double getQtLitros() {
        return qtLitros;
    }

    public void setQtLitros(double qtLitros) {
        this.qtLitros = qtLitros;
    }

    public Date getDtOrdenha() {
        return dtOrdenha;
    }

    public void setDtOrdenha(Date dtOrdenha) {
        this.dtOrdenha = dtOrdenha;
    }

    @Override
    public String toString() {
        return identificador + " - " + matrizLeiteira.getDescricao() + " - " + qtLitros;
    }
}
