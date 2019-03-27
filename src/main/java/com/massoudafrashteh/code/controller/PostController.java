package com.massoudafrashteh.code.controller;

import com.massoudafrashteh.code.domain.Post;
import com.massoudafrashteh.code.domain.Tag;
import com.massoudafrashteh.code.domain.User;
import com.massoudafrashteh.code.repository.PostRepository;
import com.massoudafrashteh.code.repository.TagRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchConditionVisitor;
import org.apache.cxf.jaxrs.ext.search.fiql.FiqlParser;
import org.apache.cxf.jaxrs.ext.search.jpa.JPATypedQueryVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Path("/posts")
@Api(value = "/posts") // Enables Swagger Documentation
@Produces(MediaType.APPLICATION_JSON)
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Post save(final Post post) {

        List<Tag> tags = new ArrayList<>();
        for (Tag tag : post.getTags()) {
            tags.add(tag);
        }

        post.setTags(tags);
        return postRepository.save(post);
    }


    @GET
    @ApiOperation(value = "Find all users", response = Post[].class)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @GET
    @Path("/search")
    public Collection< Post > findPeople( @QueryParam("_s") String search ) {
        SearchCondition<Post> filter = new FiqlParser<Post>(Post.class).parse(search);

        SearchConditionVisitor<Post, TypedQuery<Post>> jpa = new JPATypedQueryVisitor<Post>(em, Post.class);
        filter.accept(jpa);
        TypedQuery<Post> query = jpa.getQuery();
        return query.getResultList();
    }
}

