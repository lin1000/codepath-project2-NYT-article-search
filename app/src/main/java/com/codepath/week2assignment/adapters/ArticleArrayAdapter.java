package com.codepath.week2assignment.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.week2assignment.R;
import com.codepath.week2assignment.model.NYTArticle;

import java.util.ArrayList;

/**
 * Created by lin1000 on 2017/2/24.
 */

public class ArticleArrayAdapter extends ArrayAdapter<NYTArticle> {

    public ArticleArrayAdapter(Context context, ArrayList<NYTArticle> nytArticles){
        super(context, android.R.layout.simple_list_item_1, nytArticles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NYTArticle article = getItem(position);

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result,parent,false);

        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadLine());

        if(article.getNytMultimedia() != null) {
            String thumbnail = article.getNytMultimedia().getUrl();
            Glide.with(getContext()).load(thumbnail).into(imageView);
        }


        return convertView;
    }
}
