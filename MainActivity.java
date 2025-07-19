package brunosm.com;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.view.ViewGroup;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText txtNombre, txtEdad, txtObservaciones;
    private RadioGroup radioGroupGenero;
    private Spinner spinnerCarrera, spinnerSemestre;
    private CheckBox checkMat, checkDev, checkEng, checkEst, checkBeca;
    private ListView listEstudiantes;
    private ArrayList<String> listaEstudiantes;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txt_nombre);
        txtEdad = findViewById(R.id.txt_edad);
        txtObservaciones = findViewById(R.id.txt_obsertvaciones);
        radioGroupGenero = findViewById(R.id.radio_group_genero);
        spinnerCarrera = findViewById(R.id.carrera);
        spinnerSemestre = findViewById(R.id.semestre);
        checkMat = findViewById(R.id.check_mat);
        checkDev = findViewById(R.id.check_dev);
        checkEng = findViewById(R.id.check_eng);
        checkEst = findViewById(R.id.check_est);
        checkBeca = findViewById(R.id.check_beca);
        listEstudiantes = findViewById(R.id.reg_estudiante);

        // Spinners de Carrera
        String[] carreras = {"Seleccione una carrera", "Ingeniería de Sistemas", "Medicina", "Derecho", "Administración", "Psicología"};
        ArrayAdapter<String> adapterCarreras = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carreras);
        adapterCarreras.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarrera.setAdapter(adapterCarreras);

        // Spinner de Semestre
        String[] semestres = {"Seleccione semestre", "1°", "2°", "3°", "4°", "5°", "6°", "7°", "8°", "9°", "10°"};
        ArrayAdapter<String> adapterSemestres = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semestres);
        adapterSemestres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemestre.setAdapter(adapterSemestres);


        listaEstudiantes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaEstudiantes);
        listEstudiantes.setAdapter(adapter);
    }

    public void registrarEstudiante(View view) {
        String nombre = txtNombre.getText().toString().trim();
        String edadStr = txtEdad.getText().toString().trim();
        int generoId = radioGroupGenero.getCheckedRadioButtonId();
        String carrera = spinnerCarrera.getSelectedItem().toString();
        String semestre = spinnerSemestre.getSelectedItem().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edadStr.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una edad", Toast.LENGTH_SHORT).show();
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edad < 16 || edad > 50) {
            Toast.makeText(this, "La ededa debe de ser entre 16-50", Toast.LENGTH_SHORT).show();
            return;
        }
        if (generoId == -1) {
            Toast.makeText(this, "Debe seleccionar un género", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerCarrera.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Debe seleccionar una carrera", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerSemestre.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Debe seleccionar un semestre", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton generoRadio = findViewById(generoId);
        String genero = generoRadio.getText().toString().substring(0, 1);
        String beca = checkBeca.isChecked() ? "Sí" : "No";

        ArrayList<String> intereses = new ArrayList<>();
        if (checkMat.isChecked()) intereses.add("Matemáticas");
        if (checkDev.isChecked()) intereses.add("Programación");
        if (checkEng.isChecked()) intereses.add("Inglés");
        if (checkEst.isChecked()) intereses.add("Estadística");

        String estudiante = nombre + " - " + edad + " años\n"
                + carrera + " - " + semestre + " semestre\n"
                + "Género: " + genero + " | Beca: " + beca + "\n"
                + "Intereses: " + (intereses.isEmpty() ? "Ninguno" : String.join(", ", intereses));

        listaEstudiantes.add(estudiante);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listEstudiantes);

        Toast.makeText(this, "Estudiante registrado con éxito", Toast.LENGTH_SHORT).show();

        limpiarCampos(null);
    }

    public void limpiarCampos(View view) {
        txtNombre.setText("");
        txtEdad.setText("");
        txtObservaciones.setText("");
        radioGroupGenero.clearCheck();
        spinnerCarrera.setSelection(0);
        spinnerSemestre.setSelection(0);
        checkMat.setChecked(false);
        checkDev.setChecked(false);
        checkEng.setChecked(false);
        checkEst.setChecked(false);
        checkBeca.setChecked(false);

        Toast.makeText(this, "Datos limpiados correctamente", Toast.LENGTH_SHORT).show();
    }

    // Ajusta la altura de la ListView (estuve 2 horas buscando como arreglarlo ya que solo se veia un registro)
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}