package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.R;

public class GUIUtils {

    public static void avisoErro(Context contexto, int idTexto){
        avisoErro(contexto, contexto.getString(idTexto));
    }

    public static void avisoErro(Context contexto, String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle(R.string.aviso);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(msg);

        builder.setNeutralButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void confirmaAcao(Context contexto, String msg, DialogInterface.OnClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(msg);

        builder.setPositiveButton(R.string.sim, listener);
        builder.setNegativeButton(R.string.nao, listener);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String validaCampoTexto(Context contexto, EditText editText, int idMsgErro){

        String txt = editText.getText().toString();

        if (StringUtils.stringVazia(txt)){
            GUIUtils.avisoErro(contexto, idMsgErro);
            editText.setText(null);
            editText.requestFocus();
            return null;
        }else{
            return txt.trim();
        }
    }
}
