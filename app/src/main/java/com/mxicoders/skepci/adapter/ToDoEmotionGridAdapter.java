package com.mxicoders.skepci.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;

/**
 * Created by mxicoders on 25/7/17.
 */

public class ToDoEmotionGridAdapter extends BaseAdapter {

    CommanClass cc;

    private Context context;
    private final String[] mobileValues;

    String emotionName;
    private static LayoutInflater inflater=null;

    static final String[] MOBILE_OS = new String[] {
            "Tranqullidade", "Tedlo","Saudades", "Vergonha", "Orgulho", "Tristeza", "Amor", "Solidao","Esperanca",
            "Decepcao", "Alegria", "Confusao", "Entusiansmo", "Nojo","Ansiedade",
            "Preacupacao", "Raiva", "Desconfianca", "Medo", "Cupla"};

    static final String[] MOBILE_OS_PRIMARY = new String[] {
            "Alegria", "Amor","Tristeza", "Medo", "Nojo"};

    static final String[] MOBILE_OS_PRIMARY_SECONDARY = new String[] {
            "Tranquilidade", "Saudades","Orgulho","Esperança","Entusiasmado","Entediado","Vergonha","Solidão","Decepção","Confusão","Preocupação","Desconfiança","Culpa","Ansiedade","Raiva","Desespero"};


    public int selectedRow = -1;

    int clicked = 0;
    int clicked_two = 1;

    Holder holder;

    public ToDoEmotionGridAdapter(Context context, String[] mobileValues) {

        cc = new CommanClass(context);
        this.context = context;
        this.mobileValues = mobileValues;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView textView;
        ImageView imageView;
        LinearLayout lnGrid;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.todo_emotion_grid_item, null);

        holder.lnGrid = (LinearLayout)rowView.findViewById(R.id.grid_click);

        holder.textView = (TextView) rowView.findViewById(R.id.text_name);

        holder.textView.setText(mobileValues[position]);


        holder.imageView = (ImageView) rowView.findViewById(R.id.Image);


        /*pending......................................................*/

        holder.lnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int i=0;i< parent.getChildCount();i++){

                    View c = parent.getChildAt(i);

                    c.findViewById(R.id.grid_click).setBackgroundResource(R.drawable.emotion_back);

                }

                v.setBackgroundResource(R.drawable.emotion_back_click);

                emotionName = String.valueOf(position);

                if (cc.loadPrefString("emotion_level").equals("Primário")){

                    emotionName = MOBILE_OS_PRIMARY[position];

                    cc.savePrefString("emotion_level","");

                }else {

                    emotionName = MOBILE_OS_PRIMARY_SECONDARY[position];

                    cc.savePrefString("emotion_level","");
                }



                cc.savePrefString("emotion_name2",emotionName);

                // Log.i("EmotionNameGrid",emotionName.toString());

            }
        });

        String mobile = mobileValues[position];

        if (mobile.equals("Tranquilidade")) {
            holder.imageView.setImageResource(R.drawable.tranqullidade);
        } else if (mobile.equals("Entediado")) {
            holder.imageView.setImageResource(R.drawable.tedlo);
        } else if (mobile.equals("Saudades")) {
            holder.imageView.setImageResource(R.drawable.saudades);
        } else if (mobile.equals("Vergonha")) {
            holder.imageView.setImageResource(R.drawable.vergonha);
        }else if (mobile.equals("Orgulho")) {
            holder.imageView.setImageResource(R.drawable.orgulho);
        }else if (mobile.equals("Tristeza")) {
            holder.imageView.setImageResource(R.drawable.tristeza);
        }
        else if (mobile.equals("Amor")) {
            holder.imageView.setImageResource(R.drawable.amor);
        }
        else if (mobile.equals("Solidão")) {
            holder.imageView.setImageResource(R.drawable.solidao);
        }else if (mobile.equals("Esperança")) {
            holder.imageView.setImageResource(R.drawable.esperanca);
        }else if (mobile.equals("Decepção")) {
            holder.imageView.setImageResource(R.drawable.decepcao);
        }else if (mobile.equals("Alegria")) {
            holder.imageView.setImageResource(R.drawable.alegria);
        }else if (mobile.equals("Confusão")) {
            holder.imageView.setImageResource(R.drawable.confusao);
        }else if (mobile.equals("Entusiasmado")) {
            holder.imageView.setImageResource(R.drawable.entusiansmo);
        }else if (mobile.equals("Nojo")) {
            holder.imageView.setImageResource(R.drawable.nojo);
        }else if (mobile.equals("Ansiedade")) {
            holder.imageView.setImageResource(R.drawable.ansiedade);
        }else if (mobile.equals("Preocupação")) {
            holder.imageView.setImageResource(R.drawable.preacupacao);
        }else if (mobile.equals("Raiva")) {
            holder.imageView.setImageResource(R.drawable.raiva);
        }else if (mobile.equals("Desconfiança")) {
            holder.imageView.setImageResource(R.drawable.desconfianca);
        }else if (mobile.equals("Medo")) {
            holder.imageView.setImageResource(R.drawable.medo);
        }else if (mobile.equals("Culpa")) {
            holder.imageView.setImageResource(R.drawable.cupla);
        }else if (mobile.equals("Desespero")) {
            holder.imageView.setImageResource(R.drawable.cupla);
        }
        else {
            holder.imageView.setImageResource(R.drawable.angry);
        }


        return rowView;
    }

}
