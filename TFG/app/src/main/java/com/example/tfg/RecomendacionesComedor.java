package com.example.tfg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RecomendacionesComedor extends Fragment {
    private EditText editText;
    private TextView textView;

    public RecomendacionesComedor() {
        // Required empty public constructor
    }

    public static RecomendacionesComedor newInstance(String param1, String param2) {
        RecomendacionesComedor fragment = new RecomendacionesComedor();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_recomendaciones_comedor, container, false);
        editText=v.findViewById(R.id.inputpro);
        textView=v.findViewById(R.id.respuesta);
        Button sendButton = v.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });
        editText.setOnEditorActionListener((c, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                return true;
            }
            return false;
        });
        return v;

    }

}
