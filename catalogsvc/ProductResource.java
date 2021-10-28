package com.oracle.fodlite.svc.catalogService;

import com.oracle.fodlite.svc.catalogService.database.Database;
import com.oracle.fodlite.svc.catalogService.model.Product;
import io.helidon.webserver.ServerRequest;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/product")
@RequestScoped
@Traced
public class ProductResource {

    @Inject
    Metrics metrics;

    @Inject
    Database database;

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private List<Product> allProducts= new ArrayList<>(this.database.getProducts().values());

    @Context
    private ServerRequest serverRequest;


    @GET
    @Timed(name = "fod_http_response_timer",tags = {"method=get"}, absolute = true,unit = MetricUnits.NANOSECONDS, reusable = true)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") long id ){



        try {
            Product result=null;
            for(Product p:allProducts){
                if(p.getId()==id){
                    result=p;
                    break;
                }
            }
            if(result==null) throw new NoResultException();
            JsonObjectBuilder jsonProduct = Json.createObjectBuilder()
                    .add("productId",result.getId())
                    .add("name", result.getName())
                    .add("imageUrl", result.getImageUrl())
                    .add("categoryId", result.getCategoryId())
                    .add("description", result.getDescription())
                    .add("featured", result.getFeatured())
                    .add("available" , result.getAvailable())
                    .add("numOfItems" , result.getNumOfItems())
                    .add("price" , result.getPrice());
            metrics.incrementStatusCodeCounter(200);
            return Response.status(Response.Status.OK).entity(jsonProduct.build().toString()).build();
        }catch (NoResultException e){
            metrics.incrementStatusCodeCounter(500);
            return Response.status(Response.Status.OK).entity("{}").build();
        }
        finally
        {
        }
    }


}
