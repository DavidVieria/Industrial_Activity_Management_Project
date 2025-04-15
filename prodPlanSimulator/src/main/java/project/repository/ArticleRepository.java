package project.repository;

import project.domain.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {

    private List<Article> articles;

    public ArticleRepository(List<Article> articles) {
        this.articles = articles;
    }

    public ArticleRepository() {
        this.articles = new ArrayList<>();
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    public List<Article> getAllArticles() {
        return new ArrayList<>(articles);
    }

    public void clearArticles() {
        articles.clear();
    }

    public boolean isEmpty() {
        return articles.isEmpty();
    }

    public int getArticleCount() {
        return articles.size();
    }
}
