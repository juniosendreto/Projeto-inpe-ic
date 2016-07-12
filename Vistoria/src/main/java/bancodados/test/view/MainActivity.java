package bancodados.test.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import bancodados.test.R;
import bancodados.test.core.service.dao.GPSTracker;
import bancodados.test.model.Localizacao;


public class MainActivity extends AppCompatActivity {

    Localizacao localizacao;
    private MapView mMapView;
    private MapController mMapController;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("wait");
        //progressDialog.show();

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setBuiltInZoomControls(true);
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(13);
        GeoPoint gPt = new GeoPoint(51500000, -150000);
        mMapController.setCenter(gPt);

        final Button novaVistoria = (Button) findViewById(R.id.novaVistoriaB);
        final Button listarVistorias = (Button) findViewById(R.id.listarVistoriasB);
        final EditText latitudeET = (EditText) findViewById(R.id.latitudeET);
        final EditText longitudeET = (EditText)findViewById(R.id.longitudeET);
        final Button coodernadaGPSB = (Button) findViewById(R.id.coodernadaGPSB);
        localizacao =  new Localizacao();
        final Intent intentVistoria = new Intent(this, VistoriaActivity.class);
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        gpsTracker = new GPSTracker(getApplicationContext());

        novaVistoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    localizacao.setLatitude(Double.valueOf(latitudeET.getText().toString()));
                    localizacao.setLongitude(Double.valueOf(longitudeET.getText().toString()));
                    intentVistoria.putExtra("localizacao", (Localizacao) localizacao);
                    startActivity(intentVistoria);
                }catch (Exception e){
                    e.printStackTrace();
                    alert.setTitle("Alerta");
                    alert.setMessage("Você precisa digitar ou pegar coordenada para realizar vistoria");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });
        coodernadaGPSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker.getLocation();
                if(gpsTracker.podePegarLocalizacao()){
                    latitudeET.setText(String.valueOf(gpsTracker.getLatitude()));
                    longitudeET.setText(String.valueOf(gpsTracker.getLongitude()));
                }else{
                    alert.setTitle("Alerta");
                    alert.setMessage("Você não possui conexão com internet ou GPS não está ligado, digite manualmente");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });

        listarVistorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    chamarActivity(Class.forName("bancodados.test.view.ListViewVistoriaActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void chamarActivity(Class novaActivity) {
        Intent abrirActivity = new Intent(this, novaActivity);
        startActivity(abrirActivity);

    }

    public void onBackPressed(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alerta");
        alert.setMessage("Se você voltar, você terá que logar novamente!");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    chamarActivity(Class.forName("bancodados.test.view.LoginActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.action_update_usuario:
                intent = new Intent(this, UpdateUsuarioActivity.class);
                startActivity(intent);
                break;
            case R.id.action_criar_vistoria:
                intent = new Intent(this, VistoriaActivity.class);
                gpsTracker = new GPSTracker(getApplicationContext());
                localizacao.setLatitude(gpsTracker.getLatitude());
                localizacao.setLongitude(gpsTracker.getLongitude());
                intent.putExtra("localizacao", (Localizacao) localizacao);
                startActivity(intent);
                break;
            case R.id.action_listar_vistorias:
                intent = new Intent(this, ListViewVistoriaActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sair:
                onBackPressed();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

}
