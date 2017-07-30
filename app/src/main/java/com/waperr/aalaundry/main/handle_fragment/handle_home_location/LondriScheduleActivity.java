package com.waperr.aalaundry.main.handle_fragment.handle_home_location;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.pojo.Cart;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LondriScheduleActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private Spinner spinner;
    private Button button;
    private EditText etTanggal;

    private ArrayList<Cart> foodCart = new ArrayList<Cart>();

    private String idMitra, namaMitra, alamatMitra, teleponMitra,  namaUser,
            namaAlamatUser, detilDeskripsiUser, teleponUser, alamatUser, latitudeUser,
            longitudeUser, hargaPilih, jenisPilih, layananPilih, londri;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int Year, Month, Day, Hour, Minute;
    private String outputTanggalSekarang, outputTanggalPrediksi, waktuJemput;
    private SimpleDateFormat sdfDate, sdfTime;

    private String[] bankDuration ={"Select Time","08.00 - 09.00 WIB","09.00 - 10.00 WIB","10.00 - 11.00 WIB",
                                    "11.00 - 12.00 WIB","12.00 - 13.00 WIB","13.00 - 14.00 WIB","14.00 - 15.00 WIB",
                                    "15.00 - 16.00 WIB","16.00 - 17.00 WIB","17.00 - 18.00 WIB","18.00 - 19.00 WIB",
                                    "19.00 - 20.00 WIB","20.00 - 21.00 WIB"};
    private String[] bankPosition = {"Select Time","08:00:00","09:00:00","10:00:00","11:00:00","12:00:00",
                                    "13:00:00","14:00:00","15:00:00","16:00:00","17:00:00","18:00:00","19:00:00","20:00:00"};
    private String durasiPilih, total, person, invoice, totalBerat, totalHarga, catatan;
    private double latitudeMitra, longitudeMitra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_londri_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        spinner = (Spinner) findViewById(R.id.spinnerDurasi);
        button = (Button) findViewById(R.id.buttonLondriScheduleNext);
        etTanggal = (EditText) findViewById(R.id.editTextCreateConferenceDate);

        //Mengambil kalender dan waktu saat ini
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        sdfDate = new SimpleDateFormat("dd-MM-yyyy");
        sdfTime = new SimpleDateFormat("HH:mm:ss");

        Intent intent = getIntent();
        idMitra = intent.getStringExtra("idMitra");
        namaMitra= intent.getStringExtra("namaMitra");
        alamatMitra= intent.getStringExtra("alamatMitra");
        teleponMitra= intent.getStringExtra("teleponMitra");
        namaUser= intent.getStringExtra("namaUser");
        detilDeskripsiUser = intent.getStringExtra("detilDeskripsiUser");
        teleponUser = intent.getStringExtra("teleponUser");
        alamatUser = intent.getStringExtra("alamatUser");
        latitudeUser = intent.getStringExtra("latitudeUser");
        longitudeUser = intent.getStringExtra("longitudeUser");
        latitudeMitra = intent.getDoubleExtra("latitudeMitra",0);
        longitudeMitra = intent.getDoubleExtra("longitudeMitra",0);
        hargaPilih = intent.getStringExtra("hargaPilih");
        jenisPilih = intent.getStringExtra("jenisPilih");
        layananPilih = intent.getStringExtra("layananPilih");
        totalBerat = intent.getStringExtra("totalBerat");
        totalHarga = intent.getStringExtra("totalHarga");
        catatan = intent.getStringExtra("catatan");
        londri = intent.getStringExtra("londri");
        foodCart = (ArrayList<Cart>)getIntent().getExtras().getSerializable("order");

        Log.d("DETIL",""+detilDeskripsiUser);

        //Untuk set kalender ke hari ini
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(LondriScheduleActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#F89F1E"));
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.adapter_spinner, bankDuration);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                durasiPilih = bankDuration[position];
                if(position != 0) {
                    Toast.makeText(LondriScheduleActivity.this, "Anda Memilih "+bankDuration[position], Toast.LENGTH_SHORT).show();
                    waktuJemput = bankPosition[position];
                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tanggalJemput = etTanggal.getText().toString();
                if(tanggalJemput.isEmpty()||waktuJemput.equals("Select Time")){
                    Toast.makeText(LondriScheduleActivity.this, "Silahkan isi dengan benar!", Toast.LENGTH_SHORT).show();
                }else{
                    if(londri.equals("ByWeight")){
                        Intent intent = new Intent(LondriScheduleActivity.this, LondriConfirmationActivity.class);
                        intent.putExtra("idMitra",idMitra);
                        intent.putExtra("namaMitra",namaMitra);
                        intent.putExtra("alamatMitra",alamatMitra);
                        intent.putExtra("teleponMitra",teleponMitra);
                        intent.putExtra("namaUser",namaUser);
                        intent.putExtra("alamatUser",alamatUser);
                        intent.putExtra("detilDeskripsiUser",detilDeskripsiUser);
                        intent.putExtra("teleponUser",teleponUser);
                        intent.putExtra("latitudeUser",latitudeUser);
                        intent.putExtra("longitudeUser",longitudeUser);
                        intent.putExtra("latitudeMitra",latitudeMitra);
                        intent.putExtra("longitudeMitra",longitudeMitra);
                        intent.putExtra("jenisPilih",jenisPilih);
                        intent.putExtra("hargaPilih",hargaPilih);
                        intent.putExtra("layananPilih",layananPilih);
                        intent.putExtra("totalBerat",totalBerat);
                        intent.putExtra("totalHarga",totalHarga);
                        intent.putExtra("londri",londri);
                        intent.putExtra("catatan",catatan);
                        intent.putExtra("waktuJemput",waktuJemput);
                        intent.putExtra("tanggalJemput",outputTanggalSekarang);
                        intent.putExtra("tanggalAntar",outputTanggalPrediksi);
                        intent.putExtra("catatan",catatan);
                        startActivity(intent);
                    }else if(londri.equals("ByItem")){
                        Intent intent = new Intent(LondriScheduleActivity.this, LondriConfirmationActivity.class);
                        intent.putExtra("idMitra",idMitra);
                        intent.putExtra("namaMitra",namaMitra);
                        intent.putExtra("alamatMitra",alamatMitra);
                        intent.putExtra("teleponMitra",teleponMitra);
                        intent.putExtra("namaUser",namaUser);
                        intent.putExtra("alamatUser",alamatUser);
                        intent.putExtra("detilDeskripsiUser",detilDeskripsiUser);
                        intent.putExtra("teleponUser",teleponUser);
                        intent.putExtra("latitudeUser",latitudeUser);
                        intent.putExtra("longitudeUser",longitudeUser);
                        intent.putExtra("latitudeMitra",latitudeMitra);
                        intent.putExtra("longitudeMitra",longitudeMitra);
                        intent.putExtra("jenisPilih",jenisPilih);
                        intent.putExtra("hargaPilih",hargaPilih);
                        intent.putExtra("layananPilih",layananPilih);
                        intent.putExtra("totalBerat",totalBerat);
                        intent.putExtra("totalHarga",totalHarga);
                        intent.putExtra("londri",londri);
                        intent.putExtra("catatan",catatan);
                        intent.putExtra("waktuJemput",waktuJemput);
                        intent.putExtra("tanggalJemput",outputTanggalSekarang);
                        intent.putExtra("tanggalAntar",outputTanggalPrediksi);
                        intent.putExtra("catatan","Tidak ada catatan tambahan");
                        intent.putExtra("order",foodCart);
                        startActivity(intent);
                    }
                }

            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int Years, int Months, int Days) {
        calendar.set(Years, Months, Days);
        Year = Years;
        Month = Months;
        Day = Days;
        outputTanggalSekarang = sdfDate.format(calendar.getTime());
        try{
            Date date = new Date();
            String output = new SimpleDateFormat("dd-MM-yyyy").format(date);
            Date dateNow = new SimpleDateFormat("dd-MM-yyyy").parse(output);
            Date dateCalendar=new SimpleDateFormat("dd-MM-yyyy").parse(outputTanggalSekarang);

            Log.d("Tanggal",""+dateNow+", "+dateCalendar);

            if(dateCalendar.compareTo(dateNow)>0){
                System.out.println("Date1 is after Date2");
                etTanggal.setText(""+outputTanggalSekarang);
            }else if(dateCalendar.compareTo(dateNow)<0){
                System.out.println("Date1 is before Date2");
                etTanggal.setText("");
                Toast.makeText(this, "The date has already passed!", Toast.LENGTH_SHORT).show();
            }else{
                System.out.println("Date1 is equal to Date2");
                etTanggal.setText(""+outputTanggalSekarang);
            }


            if(layananPilih.equals("3")){
                calendar.add(Calendar.DATE, 0);
            }else if(layananPilih.equals("2")){
                calendar.add(Calendar.DATE, 1);
            }else if(layananPilih.equals("1")){
                calendar.add(Calendar.DATE, 3);
            }
            outputTanggalPrediksi = sdfDate.format(calendar.getTime());

        }catch (Exception e){

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
