package com.massoudafrashteh.code.controller;

import com.massoudafrashteh.code.domain.Person;
import com.massoudafrashteh.code.repository.PersonRepository;
import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchConditionVisitor;
import org.apache.cxf.jaxrs.ext.search.fiql.FiqlParser;
import org.apache.cxf.jaxrs.ext.search.jpa.JPATypedQueryVisitor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

@Path( "/people" )
@Produces(MediaType.APPLICATION_JSON)
public class PeopleRestService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PersonRepository personRepository;

    @Consumes( { MediaType.APPLICATION_JSON } )
    @POST
    public Person addPerson( Person person ) {
        return personRepository.save( person );
    }
    
    @GET
    @Path("/search")
    public Collection< Person > findPeople( @QueryParam("_s") String search ) {
        SearchCondition<Person> filter = new FiqlParser<Person>(Person.class).parse(search);

        SearchConditionVisitor<Person, TypedQuery<Person>> jpa = new JPATypedQueryVisitor<Person>(em, Person.class);
        filter.accept(jpa);
        TypedQuery<Person> query = jpa.getQuery();
        return query.getResultList();
    }
}
