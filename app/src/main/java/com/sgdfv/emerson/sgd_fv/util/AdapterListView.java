package com.sgdfv.emerson.sgd_fv.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgdfv.emerson.sgd_fv.ActivityEstoque;
import com.sgdfv.emerson.sgd_fv.R;
import com.sgdfv.emerson.sgd_fv.model.ItemListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emerson on 18/04/2017.
 */

public class AdapterListView extends BaseAdapter {
    //Itens de exibição / filtrados
    private List<ItemListView> itens_exibicao;
    //Essa lista contem todos os itens.
    private List<ItemListView> itens;
    //Utilizado no getView para carregar e construir um item.
    private LayoutInflater layoutInflater;

    public AdapterListView(Context context, List<ItemListView> itens) {
        this.itens = itens;
        this.itens_exibicao = itens;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens_exibicao.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itens_exibicao.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return itens_exibicao.get(arg0).getId();
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ItemHelper itemHelper = new ItemHelper();
        ItemListView objeto = itens_exibicao.get(arg0);

        if (arg1 == null) {
            arg1 = layoutInflater.inflate(R.layout.item_list, null);
            itemHelper.descricao = (TextView) arg1.findViewById(R.id.text);
            itemHelper.image = (ImageView) arg1.findViewById(R.id.imagemview);
            arg1.setTag(itemHelper);
        } else {
            itemHelper = (ItemHelper) arg1.getTag();
        }

        itemHelper.descricao.setText(objeto.getTexto());
        itemHelper.image.setImageResource(R.drawable.trolley);

        return arg1;
    }

    private class ItemHelper {
        ImageView image;
        TextView descricao;
    }

    /** Método responsável pelo filtro. Utilizaremos em um EditText
     *
     * @return
     */
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = itens.size();
                    results.values = itens;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<ItemListView> itens_filtrados = new ArrayList<ItemListView>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < itens.size(); i++) {
                        ItemListView data = itens.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getTexto().toLowerCase();

                        if (condicao.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            itens_filtrados.add(data);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = itens_filtrados.size();
                    results.values = itens_filtrados;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                itens_exibicao = (List<ItemListView>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
    }
}
