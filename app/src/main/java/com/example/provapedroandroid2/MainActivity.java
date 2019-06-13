package com.example.provapedroandroid2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.provapedroandroid2.models.MatrizLeiteira;
import com.example.provapedroandroid2.models.Ordenha;
import com.orm.SugarContext;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    private EditText etIdentificador, etQtLitros;
    private Spinner spMatriz;
    private TextView tvDtOrdenha, tvHrOrdenha;
    private Button btSalvar, btCancelar;
    private ListView lvOrdenha;
    private ArrayAdapter<Ordenha> ordenhaAdapter;
    private ArrayAdapter<MatrizLeiteira> matrizLeiteiraAdapter;
    private int day, month, year, hour, minute;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Date dtSelecionada;
    private Ordenha ordenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SugarContext.init(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        datePickerDialog = new DatePickerDialog(MainActivity.this,
                this, year, month, day);

        timePickerDialog = new TimePickerDialog(MainActivity.this,
                this, hour, minute, true);

        loadComponents();

    }

    private void loadComponents() {
        etIdentificador = findViewById(R.id.etIdentificador);
        etQtLitros = findViewById(R.id.etQtLitros);
        spMatriz = findViewById(R.id.spMatriz);
        tvDtOrdenha = findViewById(R.id.tvDtOrdenha);
        tvHrOrdenha = findViewById(R.id.tvHrOrdenha);
        btSalvar = findViewById(R.id.btSalvar);
        btCancelar = findViewById(R.id.btCancelar);
        lvOrdenha = findViewById(R.id.lvOrdenha);
        
        loadEvents();
        if (MatrizLeiteira.listAll(MatrizLeiteira.class).size() == 0)
            loadMatriz();
        setSpinnerMatriz();
        getLastId();
        loadList();
    }

    private void setSpinnerMatriz() {
        matrizLeiteiraAdapter = new ArrayAdapter<MatrizLeiteira>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item,
                MatrizLeiteira.listAll(MatrizLeiteira.class));
        spMatriz.setAdapter(matrizLeiteiraAdapter);
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
                ordenha = null;
            }
        });
        tvDtOrdenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        tvHrOrdenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
        lvOrdenha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ordenha = (Ordenha) lvOrdenha.getItemAtPosition(position);
                setFields();
                btSalvar.setText(R.string.lbAlterar);
            }
        });
        lvOrdenha.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Ordenha ordenhaDel = (Ordenha) ordenhaAdapter.getItem(position);
                AlertDialog.Builder alertConfirmacao = new AlertDialog.Builder(MainActivity.this);
                alertConfirmacao.setTitle("Confirmação Exclusão");
                alertConfirmacao.setMessage("Deseja Realmente excluir o Registro ???");
                alertConfirmacao.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ordenhaDel.delete();
                        loadList();
                        clearFields();
                        btSalvar.setText(R.string.lbSalvar);
                        ordenha = null;
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
        if (etQtLitros.getText().toString().trim().length() > 0
            && tvDtOrdenha.getText().toString().trim().length() > 0
            && tvHrOrdenha.getText().toString().trim().length() > 0) {
            if (ordenha != null){ // edição
                ordenha.setIdentificador(Integer.parseInt(etIdentificador.getText().toString().trim()));
                ordenha.setMatrizLeiteira((MatrizLeiteira) spMatriz.getSelectedItem());
                ordenha.setQtLitros(Double.parseDouble(etQtLitros.getText().toString().trim()));
                ordenha.setDtOrdenha(dtSelecionada);
                ordenha.update();
                ordenha = null;
                btSalvar.setText(R.string.lbSalvar);
            }else {
                ordenha = new Ordenha();
                ordenha.setIdentificador(Integer.parseInt(etIdentificador.getText().toString().trim()));
                ordenha.setMatrizLeiteira((MatrizLeiteira) spMatriz.getSelectedItem());
                ordenha.setQtLitros(Double.parseDouble(etQtLitros.getText().toString().trim()));
                ordenha.setDtOrdenha(dtSelecionada);
                ordenha.save();//Metodo responsável por salvar o registro
            }
            loadList();
        } else {
            Toast.makeText(getApplicationContext(), "Favor preencher todos os campos!", Toast.LENGTH_LONG).show();
        }
        clearFields();
    }

    private void clearFields() {
        getLastId();
        spMatriz.setSelection(0);
        etQtLitros.setText("");
        etQtLitros.setHint(R.string.lbQtLitros);
        tvDtOrdenha.setText("");
        tvDtOrdenha.setHint(R.string.lbDtOrdenha);
        tvHrOrdenha.setText("");
        tvHrOrdenha.setHint(R.string.lbHrOrdenha);
    }

    private void setFields() {
        etIdentificador.setText(String.valueOf(ordenha.getIdentificador()));
        spMatriz.setSelection(matrizLeiteiraAdapter.getPosition(ordenha.getMatrizLeiteira()));
        etQtLitros.setText(String.valueOf(ordenha.getQtLitros()));
        tvDtOrdenha.setText(ordenha.getDtOrdenha().getDay()+"/"+ordenha.getDtOrdenha().getMonth()+"/"+
                ordenha.getDtOrdenha().getYear());
        tvHrOrdenha.setText(ordenha.getDtOrdenha().getHours()+":"+ordenha.getDtOrdenha().getMinutes());
        day = ordenha.getDtOrdenha().getDay();
        month = ordenha.getDtOrdenha().getMonth();
        year = ordenha.getDtOrdenha().getYear();
    }

    private void loadMatriz() {
        MatrizLeiteira last = MatrizLeiteira.last(MatrizLeiteira.class);
        int codigo = last != null ? last.getIdentificador() + 1 : 1;

        MatrizLeiteira matrizLeiteira = new MatrizLeiteira(codigo, "Vaca " + codigo, 15, new Date());
        matrizLeiteira.save();

        last = MatrizLeiteira.last(MatrizLeiteira.class);
        codigo = last != null ? last.getIdentificador() + 1 : 1;
        matrizLeiteira = new MatrizLeiteira(codigo, "Vaca " + codigo, 18, new Date());
        matrizLeiteira.save();

        last = MatrizLeiteira.last(MatrizLeiteira.class);
        codigo = last != null ? last.getIdentificador() + 1 : 1;
        matrizLeiteira = new MatrizLeiteira(codigo, "Vaca " + codigo, 21, new Date());
        matrizLeiteira.save();

        last = MatrizLeiteira.last(MatrizLeiteira.class);
        codigo = last != null ? last.getIdentificador() + 1 : 1;
        matrizLeiteira = new MatrizLeiteira(codigo, "Vaca " + codigo, 24, new Date());
        matrizLeiteira.save();
    }

    private void loadList() {
        ordenhaAdapter =
                new ArrayAdapter<Ordenha>(MainActivity.this, //Contexto
                        R.layout.item_ordenha, //Layout
                        Ordenha.listAll(Ordenha.class)); //Lista de Objetos
        lvOrdenha.setAdapter(ordenhaAdapter);
    }

    private void getLastId() {
        Ordenha last = Ordenha.last(Ordenha.class);

        int codigo = last != null ? last.getIdentificador() + 1 : 1;
        etIdentificador.setText(String.valueOf(codigo));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_relatorio) {
            Intent intent = new Intent(MainActivity.this, RelatorioActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_matriz) {
            Intent intent = new Intent(MainActivity.this, MatrizActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        tvDtOrdenha.setText(day + "/" + (month + 1) + "/" + year);
        dtSelecionada = new Date();
        dtSelecionada.setYear(year);
        dtSelecionada.setMonth(month + 1);
        dtSelecionada.setDate(day);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        tvHrOrdenha.setText(hour + ":" + minute);
        dtSelecionada.setHours(hour);
        dtSelecionada.setMinutes(minute);
    }
}
