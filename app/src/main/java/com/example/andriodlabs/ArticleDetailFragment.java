package com.example.andriodlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class ArticleDetailFragment extends Fragment {

    private Article article;
    private FavouritesRepository repo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        repo = new FavouritesRepository(requireContext());

        Bundle b = getArguments();
        String title = b != null ? b.getString("title") : "";
        String link = b != null ? b.getString("link") : "";
        String desc = b != null ? b.getString("desc") : "";
        String date = b != null ? b.getString("date") : "";

        article = new Article(title, link, desc, date);

        TextView tvTitle = v.findViewById(R.id.tvTitle);
        TextView tvDesc = v.findViewById(R.id.tvDesc);
        TextView tvDate = v.findViewById(R.id.tvDate);
        TextView tvLink = v.findViewById(R.id.tvLink);

        Button btnOpen = v.findViewById(R.id.btnOpen);
        Button btnFav = v.findViewById(R.id.btnFavourite);

        tvTitle.setText(article.title);
        tvDesc.setText(article.description);
        tvDate.setText(article.pubDate);
        tvLink.setText(article.link);

        updateFavButton(btnFav);

        btnOpen.setOnClickListener(view -> {
            Intent i = new Intent(requireContext(), WebViewActivity.class);
            i.putExtra("url", article.link);
            startActivity(i);
        });

        btnFav.setOnClickListener(view -> {
            boolean isFav = repo.isFavourite(article.link);
            if (!isFav) {
                boolean ok = repo.add(article);
                Snackbar.make(v, ok ? R.string.saved : R.string.save_failed, Snackbar.LENGTH_SHORT).show();
            } else {
                boolean ok = repo.deleteByLink(article.link);
                Snackbar.make(v, ok ? R.string.deleted : R.string.delete_failed, Snackbar.LENGTH_SHORT).show();
            }
            updateFavButton(btnFav);
        });
    }

    private void updateFavButton(Button b) {
        boolean isFav = repo.isFavourite(article.link);
        b.setText(isFav ? R.string.delete_favourite : R.string.save_favourite);
    }
}
