package com.massoudafrashteh.code.controller;

import com.massoudafrashteh.code.domain.User;
import com.massoudafrashteh.code.service.UserService;
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
import java.util.Collection;
import java.util.List;

@Component
@Path("/users")
@Api(value = "/users") // Enables Swagger Documentation
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User save(final User user) {
        return userService.save(user).get();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds an exist user with passed ID")
    public User findById(@PathParam("id") final long userId) {
        return userService.findById(userId).get();
    }

    @GET
    @ApiOperation(value = "Find all users", response = User.class)
    public List<User> findAll() {
        return userService.findAll().get();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Updates an exist user with passed user fields")
    public User update(@PathParam("id") final long id,
                       final User user) {
        return userService.update(id, user).get();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Deletes an exist user with ID")
    public void deleteById(@PathParam("id") final long userId) {
        userService.deleteById(userId);
    }

    @GET
    @Path("/search")
    @Produces( { MediaType.APPLICATION_JSON  } )
    public Collection< User > findPeople( @QueryParam("_s") String search ) {
        SearchCondition<User> filter = new FiqlParser<User>(User.class).parse(search);

        SearchConditionVisitor<User, TypedQuery<User>> jpa = new JPATypedQueryVisitor<User>(em, User.class);
        filter.accept(jpa);
        TypedQuery<User> query = jpa.getQuery();
        return query.getResultList();
    }
}

