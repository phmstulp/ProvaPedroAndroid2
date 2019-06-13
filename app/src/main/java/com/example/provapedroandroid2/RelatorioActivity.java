package com.example.provapedroandroid2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.provapedroandroid2.adapters.OrdenhaAdapter;
import com.example.provapedroandroid2.models.Ordenha;

import java.util.List;

public class RelatorioActivity extends AppCompatActivity {
    
    private ListView lvOrdenhas;
    private OrdenhaAdapter ordenhaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadComponets();
        
    }

    private void loadComponets() {
        lvOrdenhas = findViewById(R.id.lvOrdenhas);
        
        loadList();
    }

    private void loadList() {
        List<Ordenha> ordenhaList = Ordenha.listAll(Ordenha.class);
        ordenhaAdapter = new OrdenhaAdapter(RelatorioActivity.this, ordenhaList);
        lvOrdenhas.setAdapter(ordenhaAdapter);
    }

}
