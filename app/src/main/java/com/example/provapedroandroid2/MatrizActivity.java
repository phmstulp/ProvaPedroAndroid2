package com.example.provapedroandroid2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.provapedroandroid2.models.MatrizLeiteira;

import java.util.Calendar;
import java.util.Date;

public class MatrizActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText etIdentificadorMatriz, etDescricao, etIdade;
    private TextView tvDtParto;
    private Button btSalvar, btCancelar;
    private ListView lvMatriz;
    private ArrayAdapter<MatrizLeiteira> matrizAdapter;
    private int day, month, year;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private Date dtSelecionada;
    private MatrizLeiteira matrizLeiteira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matriz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(MatrizActivity.this,
                this, year, month, day);

        loadComponents();

    }

    private void loadComponents() {
        etIdentificadorMatriz = findViewById(R.id.etIdentificadorMatriz);
        etDescricao = findViewById(R.id.etDescricao);
        etIdade = findViewById(R.id.etIdade);
        tvDtParto = findViewById(R.id.tvDtParto);
        btSalvar = findViewById(R.id.btSalvar);
        btCancelar = findViewById(R.id.btCancelar);
        lvMatriz = findViewById(R.id.lvMatriz);

        loadEvents();
        getLastId();
        loadList();
    }

    private void loadEvents() {
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                btSalvar.setText(R.string.lbSalvar);
                matrizLeiteira = null;
            }
        });
        tvDtParto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        lvMatriz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matrizLeiteira = (MatrizLeiteira) lvMatriz.getItemAtPosition(position);
                setFields();
                btSalvar.setText(R.string.lbAlterar);
            }
        });
        lvMatriz.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final MatrizLeiteira matrizDel = (MatrizLeiteira) matrizAdapter.getItem(position);
                AlertDialog.Builder alertConfirmacao = new AlertDialog.Builder(MatrizActivity.this);
                alertConfirmacao.setTitle("Confirmação Exclusão");
                alertConfirmacao.setMessage("Deseja Realmente excluir o Registro ???");
                alertConfirmacao.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        matrizDel.delete();
                        loadList();
                        clearFields();
                        btSalvar.setText(R.string.lbSalvar);
                        matrizLeiteira = null;
                    }
                });

                alertConfirmacao.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertConfirmacao.show();

                return true;
            }
        });
    }

    private void save() {
        if (etDescricao.getText().toString().trim().length() > 0
                && etIdade.getText().toString().trim().length() > 0
                && tvDtParto.getText().toString().trim().length() > 0) {
            if (matrizLeiteira != null){ // edição
                matrizLeiteira.setIdentificador(Integer.parseInt(etIdentificadorMatriz.getText().toString().trim()));
                matrizLeiteira.setDescricao(etDescricao.getText().toString().trim());
                matrizLeiteira.setIdade(Integer.parseInt(etIdade.getText().toString().trim()));
                matrizLeiteira.setDtUltimoParto(dtSelecionada);
                matrizLeiteira.update();
                matrizLeiteira = null;
                btSalvar.setText(R.string.lbSalvar);
            }else {
                matrizLeiteira = new MatrizLeiteira();
                matrizLeiteira.setIdentificador(Integer.parseInt(etIdentificadorMatriz.getText().toString().trim()));
                matrizLeiteira.setDescricao(etDescricao.getText().toString().trim());
                matrizLeiteira.setIdade(Integer.parseInt(etIdade.getText().toString().trim()));
                matrizLeiteira.setDtUltimoParto(dtSelecionada);
                matrizLeiteira.save();//Metodo responsável por salvar o registro
            }
            loadList();
        } else {
            Toast.makeText(getApplicationContext(), "Favor preencher todos os campos!", Toast.LENGTH_LONG).show();
        }
        clearFields();
    }

    private void clearFields() {
        getLastId();
        etDescricao.setText("");
        etDescricao.setHint(R.string.lbDescricao);
        etIdade.setText("");
        etIdade.setHint(R.string.lbIdade);
        tvDtParto.setText("");
        tvDtParto.setHint(R.string.lbDtParto);
    }

    private void setFields() {
        etIdentificadorMatriz.setText(String.valueOf(matrizLeiteira.getIdentificador()));
        etDescricao.setText(matrizLeiteira.getDescricao());
        etIdade.setText(String.valueOf(matrizLeiteira.getIdade()));
        tvDtParto.setText(matrizLeiteira.getDtUltimoParto().getDay()+"/"+matrizLeiteira.getDtUltimoParto().getMonth()+"/"+
                matrizLeiteira.getDtUltimoParto().getYear());
        day = matrizLeiteira.getDtUltimoParto().getDay();
        month = matrizLeiteira.getDtUltimoParto().getMonth();
        year = matrizLeiteira.getDtUltimoParto().getYear();
    }

    private void loadList() {
        matrizAdapter =
                new ArrayAdapter<MatrizLeiteira>(MatrizActivity.this, //Contexto
                        R.layout.item_ordenha, //Layout
                        MatrizLeiteira.listAll(MatrizLeiteira.class)); //Lista de Objetos
        lvMatriz.setAdapter(matrizAdapter);
    }

    private void getLastId() {
        MatrizLeiteira last = MatrizLeiteira.last(MatrizLeiteira.class);

        int codigo = last != null ? last.getIdentificador() + 1 : 1;
        etIdentificadorMatriz.setText(String.valueOf(codigo));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        tvDtParto.setText(day + "/" + (month + 1) + "/" + year);
        dtSelecionada = new Date();
        dtSelecionada.setYear(year);
        dtSelecionada.setMonth(month + 1);
        dtSelecionada.setDate(day);
    }
}
