package course;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    public Document findByPermalink(String permalink) {

        // todo  XXX
        Document post;

        post = postsCollection.find(new BasicDBObject("permalink", permalink)).first();

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // todo,  XXX
        // Return a list of Documents, each one a post from the posts collection
        //List<Document> posts = null;

        List<Document> posts = new ArrayList<Document>();

        List<Document> list = postsCollection.find().sort(new Document().append("date", -1)).limit(limit).into(new ArrayList<Document>());

        for (Document document : list) {
            posts.add(document);
        }

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();
        permalink = permalink+ (new Date()).getTime();


        Document post = new Document();

        Date now = new Date();
        post.append("author", username).append("title", title)
                .append("body", body).append("permalink", permalink)
                .append("tags", tags).append("date", now);

            postsCollection.insertOne(post);

        return permalink;
    }


    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        BasicDBObject comment = new BasicDBObject().append("author", name).append("body", body);
        if (email != null) {
            comment.append("email", email);
        }

        postsCollection.updateOne(new Document("permalink", permalink), new Document("$push", new BasicDBObject("comments", comment)));

    }
}
