package com.waperr.aalaundry.main.handle_fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waperr.aalaundry.main.MainActivity;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.main.handle_login.ForgotActivity;
import com.waperr.aalaundry.main.handle_login.LoginActivity;
import com.waperr.aalaundry.pojo.Profil;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private DatabaseHandler dataSource;
    public ArrayList<Profil> valuesProfil;

    private AlertDialog.Builder alert;
    private LinearLayout linHapusRiwayat, linLogout, linGantiPassword;
    private String nama, mail;
    private TextView tvNama, tvTelp, tvMail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        //((MainActivity) getActivity()).setTitle("Profil");

        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();


        linHapusRiwayat = (LinearLayout) rootView.findViewById(R.id.linLayRiwayatHapus);
        linGantiPassword = (LinearLayout) rootView.findViewById(R.id.linearLayoutChangePassword);
        linLogout = (LinearLayout) rootView.findViewById(R.id.linearLayoutLogout);
        tvNama = (TextView) rootView.findViewById(R.id.textViewFragmentProfileUsername);
        tvMail = (TextView) rootView.findViewById(R.id.textViewFragmentProfileEmail);

        for (Profil profil : valuesProfil){
            nama = profil.getUsername();
            mail = profil.getEmail();
        }
        tvNama.setText(nama);
        tvMail.setText(mail);


        linGantiPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gantiPassword();
                }
            });

            linLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataSource.hapusDbaseProfil();
                    dataSource.hapusDbaseRiwayat();
                    dataSource.hapusDbaseAlamat();
                    dataSource.hapusDbaseMitra();
                    dataSource.close();

                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("DATA",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("session",false);
                    editor.commit();


                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            linHapusRiwayat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hapusRiwayat();
                }
            });

            return rootView;
        }

    public void gantiPassword(){
        alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Peringatan!");
        alert.setMessage("Jika Anda tekan setuju maka akun Anda otomatis ter-logout terlebih dahulu dan riwayat transaksi akan hilang.");
        alert.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dataSource.hapusDbaseProfil();
                        dataSource.close();

                        SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("DATA",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("session",false);
                        editor.commit();


                        Intent intent = new Intent(getActivity(), ForgotActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        alert.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        alert.show();
    }

    public void hapusRiwayat (){
        dataSource = new DatabaseHandler(getActivity());
        alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Peringatan");
        alert.setMessage("Hapus semua riwayat transaksi?");
        alert.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dataSource.hapusDbaseRiwayat();
                        dataSource.hapusDbaseAlamat();
                        dataSource.hapusDbaseMitra();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Riwayat berhasil dihapus!", Toast.LENGTH_SHORT).show();
                    }
                });
        alert.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        alert.show();
    }

}
