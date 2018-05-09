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
 * Created by mxicoders on 23/8/17.
 */

public class EmotionGridAdapterDialog extends BaseAdapter {

    CommanClass cc;

    private Context context;
    private final String[] mobileValues;


    Holder holder;
    public int pos;

    private static LayoutInflater inflater=null;

    static final String[] MOBILE_OS = new String[] {
            "Tranqullidade", "Tedlo","Saudades", "Vergonha", "Orgulho", "Tristeza", "Amor", "Solidao","Esperanca",
            "Decepcao", "Alegria", "Confusao", "Entusiansmo", "Nojo","Ansiedade",
            "Preacupacao", "Raiva", "Desconfianca", "Medo", "Cupla"};

    String emotionName;

    boolean back = false;

    public EmotionGridAdapterDialog(Context context, String[] mobileValues) {

        this.context = context;
        this.mobileValues = mobileValues;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        cc = new CommanClass(context);
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
       public LinearLayout lnGrid;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {


        holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.todo_emotion_grid_item, null);

        pos = position;

        holder.lnGrid = (LinearLayout)rowView.findViewById(R.id.grid_click);

        holder.textView = (TextView) rowView.findViewById(R.id.text_name);

        holder.textView.setText(mobileValues[position]);


        holder.imageView = (ImageView) rowView.findViewById(R.id.Image);



        holder.lnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                emotionName = String.valueOf(position);

                emotionName = MOBILE_OS[position];


                for(int i=0;i<parent.getChildCount();i++){
                    View c = parent.getChildAt(i);
                    c.findViewById(R.id.grid_click).setBackgroundResource(R.drawable.emotion_back);

                }
                v.setBackgroundResource(R.drawable.emotion_back_click);

              //  v.setBackground(context.getResources().getDrawable(R.drawable.emotion_back_click));


                cc.savePrefString("emotion_name",emotionName);

               // Log.i("EmotionNameGrid",emotionName.toString());

            }
        });

        String mobile = mobileValues[position];

        if (mobile.equals("Tranqullidade")) {
            holder.imageView.setImageResource(R.drawable.tranqullidade);
        } else if (mobile.equals("Tedlo")) {
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
        else if (mobile.equals("Solidao")) {
            holder.imageView.setImageResource(R.drawable.solidao);
        }else if (mobile.equals("Esperanca")) {
            holder.imageView.setImageResource(R.drawable.esperanca);
        }else if (mobile.equals("Decepcao")) {
            holder.imageView.setImageResource(R.drawable.decepcao);
        }else if (mobile.equals("Alegria")) {
            holder.imageView.setImageResource(R.drawable.alegria);
        }else if (mobile.equals("Confusao")) {
            holder.imageView.setImageResource(R.drawable.confusao);
        }else if (mobile.equals("Entusiansmo")) {
            holder.imageView.setImageResource(R.drawable.entusiansmo);
        }else if (mobile.equals("Nojo")) {
            holder.imageView.setImageResource(R.drawable.nojo);
        }else if (mobile.equals("Ansiedade")) {
            holder.imageView.setImageResource(R.drawable.ansiedade);
        }else if (mobile.equals("Preacupacao")) {
            holder.imageView.setImageResource(R.drawable.preacupacao);
        }else if (mobile.equals("Raiva")) {
            holder.imageView.setImageResource(R.drawable.raiva);
        }else if (mobile.equals("Desconfianca")) {
            holder.imageView.setImageResource(R.drawable.desconfianca);
        }else if (mobile.equals("Medo")) {
            holder.imageView.setImageResource(R.drawable.medo);
        }else if (mobile.equals("Cupla")) {
            holder.imageView.setImageResource(R.drawable.cupla);
        }
        else {
            holder.imageView.setImageResource(R.drawable.angry);
        }


        /*LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);


            gridView = inflater.inflate(R.layout.todo_emotion_grid_item, null);

            lnGrid = (LinearLayout)gridView.findViewById(R.id.grid_click);

            textView = (TextView) gridView.findViewById(R.id.text_name);

            textView.setText(mobileValues[position]);


            imageView = (ImageView) gridView.findViewById(R.id.Image);

            String mobile = mobileValues[position];




            if (mobile.equals("Tranqullidade")) {
                imageView.setImageResource(R.drawable.tranqullidade);
            } else if (mobile.equals("Tedlo")) {
                imageView.setImageResource(R.drawable.tedlo);
            } else if (mobile.equals("Saudades")) {
                imageView.setImageResource(R.drawable.saudades);
            } else if (mobile.equals("Vergonha")) {
                imageView.setImageResource(R.drawable.vergonha);
            }else if (mobile.equals("Orgulho")) {
                imageView.setImageResource(R.drawable.orgulho);
            }else if (mobile.equals("Tristeza")) {
                imageView.setImageResource(R.drawable.tristeza);
            }
            else if (mobile.equals("Amor")) {
                imageView.setImageResource(R.drawable.amor);
            }
            else if (mobile.equals("Solidao")) {
                imageView.setImageResource(R.drawable.solidao);
            }else if (mobile.equals("Esperanca")) {
                imageView.setImageResource(R.drawable.esperanca);
            }else if (mobile.equals("Decepcao")) {
                imageView.setImageResource(R.drawable.decepcao);
            }else if (mobile.equals("Alegria")) {
                imageView.setImageResource(R.drawable.alegria);
            }else if (mobile.equals("Confusao")) {
                imageView.setImageResource(R.drawable.confusao);
            }else if (mobile.equals("Entusiansmo")) {
                imageView.setImageResource(R.drawable.entusiansmo);
            }else if (mobile.equals("Nojo")) {
                imageView.setImageResource(R.drawable.nojo);
            }else if (mobile.equals("Ansiedade")) {
                imageView.setImageResource(R.drawable.ansiedade);
            }else if (mobile.equals("Preacupacao")) {
                imageView.setImageResource(R.drawable.preacupacao);
            }else if (mobile.equals("Raiva")) {
                imageView.setImageResource(R.drawable.raiva);
            }else if (mobile.equals("Desconfianca")) {
                imageView.setImageResource(R.drawable.desconfianca);
            }else if (mobile.equals("Medo")) {
                imageView.setImageResource(R.drawable.medo);
            }else if (mobile.equals("Cupla")) {
                imageView.setImageResource(R.drawable.cupla);
            }
            else {
                imageView.setImageResource(R.drawable.angry);
            }

        } else {
            gridView = (View) convertView;
        }*/

        return rowView;
    }


}
