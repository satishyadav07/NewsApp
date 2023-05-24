package com.satish.newsapp.DataFetchListner;


import com.satish.newsapp.Models.Article;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse>{
    void onFetchData(List<Article> list, String message);
    void onError(String message);
}
