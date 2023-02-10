package dawidborkowski.dawidborkowski;

public class Book {
    protected Integer id;
    protected String title;
    protected String author;
    protected String rating;

    public Book(Integer id, String title, String author, String rating)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
